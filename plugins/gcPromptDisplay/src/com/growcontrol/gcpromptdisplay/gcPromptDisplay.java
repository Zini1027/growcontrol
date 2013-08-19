package com.growcontrol.gcpromptdisplay;

import com.growcontrol.gcServer.serverPlugin.gcServerPlugin;


public class gcPromptDisplay extends gcServerPlugin {

	// commands listener
	private Commands commands = new Commands();

//	// pin data
//	private static HashMap<Integer, PromptPin> outputPins = new HashMap<Integer, PromptPin>();


	// load/unload plugin
	@Override
	public void onEnable() {
		// register listeners
		if(commands == null)
			commands = new Commands();
		register(commands);
//		registerListenerOutput(this);
		// load configs
		Config.get("plugins/"+getName()+"/");
		if(!Config.isLoaded()) {
			getLogger().severe("Failed to load "+Config.CONFIG_FILE);
			return;
		}
//		UpdatePrompt();
	}
	@Override
	public void onDisable() {
//		try {
//			// reset prompt to default
//			getLogger().setPrompt(null);
//		} catch (IOException e) {
//			getLogger().exception(e);
//		}
	}


//	// load config.yml
//	private static void LoadConfig() {
//		pxnConfig config = pxnConfig.loadFile("plugins/gcPromptDisplay", "config.yml");
//		if(config == null) {
//			log.severe("Failed to load config.yml");
//			return;
//		}
//		@SuppressWarnings("unchecked")
//		List<Object> displays = (List<Object>) config.get("Prompt Displays");
//		if(displays == null) {
//			log.severe("Failed to load Prompt Displays from config!");
//			return;
//		}
//		for(Object obj : displays) {
//			try {
//				@SuppressWarnings("unchecked")
//				HashMap<String, Object> display = (HashMap<String, Object>) obj;
//				if(display == null) {
//					log.severe("Failed to parse config!");
//					continue;
//				}
//				int id = (Integer) display.get("Id");
//				String type = (String) display.get("Type");
//				PinMode mode = PromptPin.fromString(type);
//				if(mode == null) {
//					log.warning("Invalid pin mode! "+type);
//					mode = PinMode.DISABLED;
//				}
//				// add pin
//				outputPins.put(id, new PromptPin(mode));
//			} catch(Exception ignore) {
//				log.severe("Failed to parse config!");
//			}
//		}
//	}


//	@Override
//	public boolean onOutput(String[] args) {
//		if(args.length < 2) return false;
//		if(!args[0].equalsIgnoreCase("gcPromptDisplay")) return false;
//		int pinNum = Integer.valueOf(args[1]);
//		PromptPin pin;
//		if(outputPins.containsKey(pinNum)) {
//			pin = outputPins.get(pinNum);
//		} else {
//			pin = new PromptPin(PinMode.IO);
//			outputPins.put(pinNum, pin);
//		}
//		pin.setState(args[2]);
//		UpdatePrompt();
//		return true;
//	}


//	public void UpdatePrompt() {
//		String prompt = "";
//		for(PromptPin pin : outputPins.values()) {
//			if(!prompt.isEmpty()) prompt += " | ";
//			prompt += PromptPin.toString(pin.pinMode, pin.pinState);
//		}
//		try {
//			if(prompt.isEmpty())
//				getLogger().setPrompt(">");
//			else
//				getLogger().setPrompt(prompt+" >");
//		} catch (IOException e) {
//			log.exception(e);
//		}
//	}


}
