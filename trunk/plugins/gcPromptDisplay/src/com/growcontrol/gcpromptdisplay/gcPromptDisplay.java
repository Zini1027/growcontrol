package com.growcontrol.gcpromptdisplay;

import java.util.HashMap;

import com.growcontrol.gcServer.commands.gcCommand;
import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.serverPlugin.gcServerPlugin;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerCommand;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerOutput;
import com.growcontrol.gcpromptdisplay.PromptPin.PinMode;

public class gcPromptDisplay extends gcServerPlugin implements gcServerPluginListenerCommand, gcServerPluginListenerOutput {
	private static final String PLUGIN_NAME = "gcPromptDisplay";
	// logger
	public static gcLogger log = getLogger(PLUGIN_NAME);

	// pin data
	public HashMap<Integer, PromptPin> outputPins = new HashMap<Integer, PromptPin>();


	@Override
	public void onEnable() {
		// register plugin name
		registerPlugin(PLUGIN_NAME);
		// register commands
		registerCommand("promptdisplay")
			.addAlias("prompt");
		// register listeners
		registerListenerCommand(this);
		registerListenerOutput(this);
		// load configs

		outputPins.put(0, new PromptPin(PinMode.io));
		outputPins.put(1, new PromptPin(PinMode.pwm));
		UpdatePrompt();
	}


	@Override
	public void onDisable() {
		// reset prompt to default
		gcLogger.setPrompt(null);
	}


	@Override
	public boolean onCommand(gcCommand command, String[] args) {
		return false;
	}


	@Override
	public boolean onOutput(String[] args) {
		if(args.length < 2) return false;
		if(!args[0].equalsIgnoreCase("gcPromptDisplay")) return false;
		int pinNum = Integer.valueOf(args[1]);
		PromptPin pin;
		if(outputPins.containsKey(pinNum)) {
			pin = outputPins.get(pinNum);
		} else {
			pin = new PromptPin(PinMode.io);
			outputPins.put(pinNum, pin);
		}
		pin.setState(args[2]);
//for(String arg : args)
//log.warning(arg);
		UpdatePrompt();
		return true;
	}


	public void UpdatePrompt() {
		String prompt = "";
		for(PromptPin pin : outputPins.values()) {
			if(!prompt.isEmpty()) prompt += " | ";
			prompt += PromptPin.toString(pin.pinMode, pin.pinState);
		}
		if(prompt.isEmpty())
			gcLogger.setPrompt(">");
		else
			gcLogger.setPrompt(prompt+" >");
	}


}
