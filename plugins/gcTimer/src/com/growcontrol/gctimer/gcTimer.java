package com.growcontrol.gctimer;

import com.growcontrol.gcServer.serverPlugin.gcServerPlugin;
import com.growcontrol.gctimer.config.TimerDAO;
import com.growcontrol.gctimer.listeners.CommandsListener;


public class gcTimer extends gcServerPlugin {

	private static gcTimer instance = null;

	// commands listeners
	private CommandsListener commandsListener = new CommandsListener();
//	private DeviceListener deviceListener = new DeviceListener();

	// timer types
	public static enum Type {CLOCK, TICKER, SEQUENCER};
	// timer instances
//	private static HashMap<String, deviceTimer> timersMap = new HashMap<String, deviceTimer>();


	public static gcTimer get() {
		return instance;
	}
	public gcTimer() {
		super();
		if(instance == null)
			instance = this;
	}


	// load/unload plugin
	@Override
	public void onEnable() {
		// register listeners
		register(commandsListener);
//		registerListenerTick(this);
//		registerListenerDevice(deviceListener);
		// load configs
		Config.get();
		if(!Config.isLoaded()) {
			getLogger().severe("Failed to load timers.yml");
			return;
		}

		// load timers
		LoadTimers();

		// register timer devices
//		for(String line : ) {
//		}
	}
	@Override
	public void onDisable() {
		UnloadTimers();
		getLogger().info("gcTimer disabled!");
	}


	// load timers
	private void LoadTimers() {
		for(TimerDAO timer : Config.Timers()) {
			getLogger().config("Timer: "+timer.name);
		}
	}
	private void UnloadTimers() {
		//TODO:
	}


	// timer type
	public static gcTimer.Type ParseTimerType(String value) {
		if(value == null) return null;
		switch(value.toUpperCase()) {
		case "TICKER":
			return gcTimer.Type.TICKER;
		case "CLOCK":
			return gcTimer.Type.CLOCK;
		case "SEQUENCER":
			return gcTimer.Type.SEQUENCER;
		}
		return null;
	}
	public static String TimerTypeToString(gcTimer.Type type) {
		if(type == null) return null;
		switch(type) {
		case TICKER:
			return "TICKER";
		case CLOCK:
			return "CLOCK";
		case SEQUENCER:
			return "SEQUENCER";
		}
		return null;
	}


//		// load output commands
//		List<String> outputCommands = config.getStringList("Outputs");
//		timer.addOutputCommands(outputCommands);
//		// run mode (cycle)
//		RunMode runMode = gcServerDevice.RunModeFromString(cycle);
//		// start the timer!
//		((gcServerDevice) timer).StartDevice(runMode);
//	}


//	// create new timer
//	public deviceTimer newTimer(String name, String title, String type) {
//		return null;
//		return newTimer(name, title, timerTypeFromString(type));
//	}
//	public deviceTimer newTimer(String name, String title, TimerType type) {
//		if(name==null || title==null || type==null) return null;
//		deviceTimer timer = null;
//		synchronized(timersMap) {
//			if(timersMap.containsKey(name)) {
//				getLogger().warning("A timer named \""+name+"\" already exists!");
//				return null;
//			}
//			if(type.equals(TimerType.CLOCK))
//				timer = new timerClock(name, title);
//			else if(type.equals(TimerType.TICKER))
//				timer = new timerTicker(name, title);
//			else if(type.equals(TimerType.SEQUENCER))
//				timer = new timerSequencer(name, title);
//			else				getLogger().severe("Unknown timer type: "+type.toString());
//			if(timer == null)	getLogger().severe("Unable to create new timer!");
//			// add to hash map
//			timersMap.put(name, timer);
//		}
//		return timer;
//	}


//	// tick all timer devices
//	@Override
//	public void onTick() {
//		for(deviceTimer timer : timersMap.values())
//			if(timer != null)
//				timer.onTick();
//	}


}
