package com.growcontrol.gcpromptdisplay;

import java.util.HashMap;
import java.util.List;

import com.growcontrol.gcServer.config.gcConfig;
import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.serverPlugin.gcServerPlugin;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerOutput;
import com.growcontrol.gcpromptdisplay.PromptPin.PinMode;

public class gcPromptDisplay extends gcServerPlugin implements gcServerPluginListenerOutput {
	private static final String PLUGIN_NAME = "gcPromptDisplay";
	// logger
	public static gcLogger log = getLogger(PLUGIN_NAME);
	// commands listener
	private CommandsListener commandsListener = new CommandsListener();

	// pin data
	private static HashMap<Integer, PromptPin> outputPins = new HashMap<Integer, PromptPin>();


	@Override
	public void onEnable() {
		// register plugin name
		registerPlugin(PLUGIN_NAME);
		// register commands
		registerCommand("promptdisplay")
			.addAlias("prompt");
		// register listeners
		commandsListener = new CommandsListener();
		registerListenerCommand(commandsListener);
		registerListenerOutput(this);
		// load configs
		LoadConfig();
		UpdatePrompt();
	}


	@Override
	public void onDisable() {
		// reset prompt to default
		gcLogger.setPrompt(null);
	}


	// load config.yml
	private static void LoadConfig() {
		gcConfig config = gcConfig.loadFile("plugins/gcPromptDisplay", "config.yml");
		if(config == null) {
			log.severe("Failed to load config.yml");
			return;
		}
		@SuppressWarnings("unchecked")
		List<Object> displays = (List<Object>) config.get("Prompt Displays");
		if(displays == null) {
			log.severe("Failed to load Prompt Displays from config!");
			return;
		}
		for(Object obj : displays) {
			try {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> display = (HashMap<String, Object>) obj;
				if(display == null) {
					log.severe("Failed to parse config!");
					continue;
				}
				int id = (Integer) display.get("Id");
				String type = (String) display.get("Type");
				PinMode mode = PromptPin.fromString(type);
				if(mode == null) {
					log.warning("Invalid pin mode! "+type);
					mode = PinMode.DISABLED;
				}
				// add pin
				outputPins.put(id, new PromptPin(mode));
			} catch(Exception ignore) {
				log.severe("Failed to parse config!");
			}
		}
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
			pin = new PromptPin(PinMode.IO);
			outputPins.put(pinNum, pin);
		}
		pin.setState(args[2]);
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
