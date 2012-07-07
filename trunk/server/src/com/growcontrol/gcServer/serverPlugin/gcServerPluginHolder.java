package com.growcontrol.gcServer.serverPlugin;

import com.growcontrol.gcServer.commands.gcCommandsHolder;

public class gcServerPluginHolder {

	// plugin instance
	public final gcServerPlugin plugin;
	public boolean enabled = false;

	// plugin name
	public static final String DEFAULT_PLUGIN_NAME = "Untitled gcPlugin";
	public String pluginName = DEFAULT_PLUGIN_NAME;
	// plugin class path
	public String className = null;
	// plugin data folder
//	private File dataFolder = null;
	// commands
	public final gcCommandsHolder commands = new gcCommandsHolder();

	// outputs
//@SuppressWarnings("unused")
//	private List<Integer> registeredOutputs = new ArrayList<Integer>();
//@SuppressWarnings("unused")
//	private List<Integer> registeredInputs = new ArrayList<Integer>();


	// new plugin instance
	public gcServerPluginHolder(String className, Class<?> pluginClass)
	throws InstantiationException, IllegalAccessException {
		this.className = className;
		plugin = (gcServerPlugin) pluginClass.newInstance();
		plugin.setHolder(this);
	}


	public void doEnable() {
		try {
			plugin.onEnable();
			enabled = true;
		} catch(Exception e) {
			plugin.getLogger().exception(e);
		}
	}
	public void doDisable() {
		enabled = false;
		try {
			plugin.onDisable();
		} catch(Exception e) {
			plugin.getLogger().exception(e);
		}
	}
	public boolean isEnabled() {
		return enabled;
	}


}
