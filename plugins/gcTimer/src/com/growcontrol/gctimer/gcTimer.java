package com.growcontrol.gctimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.growcontrol.gcServer.devices.gcServerDevice;
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
	public static gcLogger log = getLogger(PLUGIN_NAME);

	// listeners
	private static CommandsListener commandsListener = new CommandsListener();
//	private static DeviceListener deviceListener = new DeviceListener();

	// timer types
	public static enum TimerType{CLOCK, TICKER, SEQUENCER};
	// timer instances
	private static List<deviceTimer> timers = new ArrayList<deviceTimer>();


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
//		registerListenerTick(deviceListener);

		LoadTimers();
	}


	@Override
	public void onDisable() {
		log.info("gcTimer disabled!");
	}


	// load timers
	private static void LoadTimers() {
		deviceTimer timer = newTimer("ticker", "test timer", Arrays.asList("setZ 3 %value%"));
//		timer.addSpan();
		timers.add(timer);
		((gcServerDevice) timer).StartDevice(gcServerDevice.RunMode.AlwaysRun);
	}


	// create new timer
	public static deviceTimer newTimer(String type, String deviceName, List<String> outputCommands) {
		return newTimer(timerTypeFromString(type), deviceName, outputCommands);
	}
	public static deviceTimer newTimer(TimerType type, String deviceName, List<String> outputCommands) {
		deviceTimer timer = null;
		synchronized(timers) {
			if(type.equals(TimerType.CLOCK)) {
				timer = new timerClock(deviceName);
				((timerClock) timer).addOutputCommands(outputCommands);
			} else if(type.equals(TimerType.TICKER)) {
				timer = new timerTicker(deviceName);
				((timerTicker) timer).addOutputCommands(outputCommands);
			} else if(type.equals(TimerType.SEQUENCER)) {
				timer = new timerSequencer(deviceName);
				((timerSequencer) timer).addOutputCommands(outputCommands);
			} else
				gcTimer.log.warning("Unknown timer type: "+type.toString());
			if(timer == null)
				gcTimer.log.warning("Unable to create new timer!");
		}
		return timer;
	}


	public static TimerType timerTypeFromString(String type) {
		if(type.equalsIgnoreCase("clock"))
			return TimerType.CLOCK;
		else if(type.equalsIgnoreCase("ticker") || type.equalsIgnoreCase("tick"))
			return TimerType.TICKER;
		else if(type.equalsIgnoreCase("sequencer") || type.equalsIgnoreCase("seq"))
			return TimerType.SEQUENCER;
		return null;
	}


	// tick timer devices
	@Override
	public void onTick() {
		for(deviceTimer timer : timers)
			if(timer != null)
				timer.onTick();
	}




}
