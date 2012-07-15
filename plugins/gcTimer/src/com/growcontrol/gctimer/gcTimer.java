package com.growcontrol.gctimer;

import java.util.HashMap;
import java.util.List;

import com.growcontrol.gcServer.config.gcConfig;
import com.growcontrol.gcServer.devices.gcServerDevice;
import com.growcontrol.gcServer.devices.gcServerDevice.RunMode;
import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.serverPlugin.gcServerPlugin;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerTick;
import com.growcontrol.gctimer.listeners.CommandsListener;
import com.growcontrol.gctimer.timers.deviceTimer;
import com.growcontrol.gctimer.timers.timerClock;
import com.growcontrol.gctimer.timers.timerSequencer;
import com.growcontrol.gctimer.timers.timerTicker;

public class gcTimer extends gcServerPlugin implements gcServerPluginListenerTick {
	private static final String PLUGIN_NAME = "gcTimer";
	// logger
	public static gcLogger log = getLogger(PLUGIN_NAME);

	// listeners
	private static CommandsListener commandsListener = new CommandsListener();
//	private static DeviceListener deviceListener = new DeviceListener();

	// timer types
	public static enum TimerType{CLOCK, TICKER, SEQUENCER};
	// timer instances
	private static HashMap<String, deviceTimer> timersMap = new HashMap<String, deviceTimer>();


	@Override
	public void onEnable() {
		// register plugin name
		registerPlugin(PLUGIN_NAME);
		// register commands
		registerCommand("timer");
		// register listeners
		registerListenerCommand(commandsListener);
		registerListenerTick(this);
//		registerListenerDevice(deviceListener);
		// load timers from configs
		LoadConfig();
	}


	@Override
	public void onDisable() {
		log.info("gcTimer disabled!");
	}


	// load timers
	private static void LoadConfig() {
		gcConfig config = gcConfig.loadFile("plugins/gcTimer", "config.yml");
		if(config == null) {
			gcTimer.log.severe("Failed to load config.yml");
			return;
		}
		List<String> timerConfigs = config.getStringList("timers");
		if(timerConfigs == null) {
			gcTimer.log.severe("Failed to load timers from config.yml");
			return;
		}
		// load timer configs
		for(String timer : timerConfigs)
			loadTimerConfig(timer);
	}
	private static void loadTimerConfig(String configFile) {
		if(!configFile.endsWith(".yml")) configFile += ".yml";
		gcConfig config = gcConfig.loadFile("plugins/gcTimer/timers", configFile);
		if(config == null) {
			gcTimer.log.severe("Failed to load "+configFile);
			return;
		}
		String name = configFile.substring(0, configFile.lastIndexOf("."));
		String title = config.getString("Title");
		String type = config.getString("Type");
		String cycle = config.getString("Cycle");
		// load new timer
		deviceTimer timer = newTimer(name, title, type);
		if(timer == null) {
			log.severe("Failed to load timer! "+configFile);
			return;
		}
		// timer duration
		String durationStr = config.getString("Duration");
		if(durationStr == null) durationStr = Integer.toString(config.getInt("Duration"));
		timer.setDuration(durationStr);
		// load ticker spans
		if(timer.getTimerType().equals(TimerType.TICKER)) {
			try {
				for(String line : config.getStringList("Spans")) {
					if(line.isEmpty()) continue;
					if(!line.contains("-")) {
						gcTimer.log.warning("Timer spans line ignored: "+line);
						continue;
					}
					long onTick = -1;
					long offTick = -1;
					try {
						onTick = Long.valueOf( line.substring(0, line.indexOf("-")).trim() );
					} catch(Exception ignore) {}
					try {
						offTick = Long.valueOf( line.substring(line.indexOf("-")+1).trim() );
					} catch(Exception ignore) {}
					// add ticker span
					((timerTicker) timer).addSpan(onTick, offTick);
				}
			} catch(Exception ignore) {}
		}
		// load output commands
		List<String> outputCommands = config.getStringList("Outputs");
		timer.addOutputCommands(outputCommands);
		// run mode (cycle)
		RunMode runMode = gcServerDevice.RunModeFromString(cycle);
		// start the timer!
		((gcServerDevice) timer).StartDevice(runMode);
	}


	// create new timer
	public static deviceTimer newTimer(String name, String title, String type) {
		return newTimer(name, title, timerTypeFromString(type));
	}
	public static deviceTimer newTimer(String name, String title, TimerType type) {
		if(name==null || title==null || type==null) return null;
		deviceTimer timer = null;
		synchronized(timersMap) {
			if(timersMap.containsKey(name)) {
				log.warning("A timer named \""+name+"\" already exists!");
				return null;
			}
			if(type.equals(TimerType.CLOCK))
				timer = new timerClock(name, title);
			else if(type.equals(TimerType.TICKER))
				timer = new timerTicker(name, title);
			else if(type.equals(TimerType.SEQUENCER))
				timer = new timerSequencer(name, title);
			else				gcTimer.log.severe("Unknown timer type: "+type.toString());
			if(timer == null)	gcTimer.log.severe("Unable to create new timer!");
			// add to hash map
			timersMap.put(name, timer);
		}
		return timer;
	}


	// enum from string
	public static TimerType timerTypeFromString(String type) {
		if(type == null) return null;
		if(type.equalsIgnoreCase("clock"))
			return TimerType.CLOCK;
		else if(type.equalsIgnoreCase("ticker") || type.equalsIgnoreCase("tick"))
			return TimerType.TICKER;
		else if(type.equalsIgnoreCase("sequencer") || type.equalsIgnoreCase("seq"))
			return TimerType.SEQUENCER;
		return null;
	}


	// tick all timer devices
	@Override
	public void onTick() {
		for(deviceTimer timer : timersMap.values())
			if(timer != null)
				timer.onTick();
	}


}
