package com.growcontrol.gcServer.serverPlugin;


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


	// new plugin instance
	public gcServerPluginHolder(String className, Class<?> pluginClass)
	throws InstantiationException, IllegalAccessException {
		if(className   == null) throw new NullPointerException();
		if(pluginClass == null) throw new NullPointerException();
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
