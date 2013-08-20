package com.growcontrol.gcCommon.pxnPlugin;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.xeustechnologies.jcl.JarClassLoader;

import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public class pxnPluginManager {

	protected static volatile pxnPluginManager manager = null;
	protected static final Object lock = new Object();
	protected final String pluginsPath;

	protected HashMap<String, PluginHolder> plugins = new HashMap<String, PluginHolder>();


	public static pxnPluginManager get() {
		return get(null);
	}
	public static pxnPluginManager get(String pluginsPath) {
		if(manager == null) {
			synchronized(lock) {
				if(manager == null)
					if(pluginsPath != null && !pluginsPath.isEmpty())
						manager = new pxnPluginManager(pluginsPath);
			}
		}
		return manager;
	}
	protected pxnPluginManager(String pluginsPath) {
		if(pluginsPath == null || pluginsPath.isEmpty())
			this.pluginsPath = "plugins/";
		else
			this.pluginsPath = pluginsPath;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}


	// plugins (info and instance)
	protected class PluginHolder {
		public final String pluginName;
		public pxnPlugin plugin = null;
		public HashMap<String, String> mainClasses = new HashMap<String, String>();
		public String version   = null;
		public File file        = null;
		public pxnPluginYML yml = null;
		public PluginHolder(String pluginName) {
			this.pluginName = pluginName;
		}
	}


	// load jars from dir
	public void LoadPluginsDir() {
		LoadPluginsDir(new String[] {null});
	}
	public void LoadPluginsDir(String mainClassFieldName) {
		LoadPluginsDir(new String[] {mainClassFieldName});
	}
	public void LoadPluginsDir(String[] mainClassFieldNames) {
		if(mainClassFieldNames == null || mainClassFieldNames.length == 0 ||
				(mainClassFieldNames.length == 1 && mainClassFieldNames[0] == null))
			mainClassFieldNames = new String[] {"Main Class"};
		synchronized(plugins) {
			String path = this.pluginsPath;
			if(path == null || path.isEmpty())
				path = "plugins/";
			File dir = new File(path);
			if(!dir.isDirectory()) dir.mkdirs();
			if(!dir.isDirectory()) {
				pxnLog.get().exception(new FileNotFoundException("plugins folder not found! "+path));
				return;
			}
			// get files list from /plugins
			File[] files = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathName) {
					return pathName.toString().endsWith(".jar");
				}
			});
			if(files == null) {
				pxnLog.get().exception(new IOException("Failed to get plugins list! "+path));
				return;
			}
			// loop .jar files
			int successful = 0;
			int failed = 0;
			for(File file : files) {
				try {
					// load plugin.yml from jar
					pxnPluginYML yml = new pxnPluginYML(file, "plugin.yml");
					if(!yml.isLoaded()) {
						throw new FileNotFoundException("File plugin.yml not found in jar! "+file.toString());
					}
					String pluginName = yml.getPluginName();
					// plugin already loaded
					if(plugins.containsKey(pluginName)) {
						pxnLog.get().warning("Duplicate plugin already loaded: "+pluginName);
						failed++;
						continue;
					}
					PluginHolder holder = new PluginHolder(pluginName);
					holder.version = yml.getPluginVersion();
					holder.file = file;
					holder.yml = yml;
					// main class paths
					for(String fieldName : mainClassFieldNames) {
						String value = yml.getMainClass(fieldName);
						if(value == null || value.isEmpty())
							continue;
						holder.mainClasses.put(fieldName, value);
					}
					plugins.put(pluginName, holder);
					successful++;
				} catch(Exception e) {
					pxnLog.get().exception("Failed to load plugin! "+file.toString(), e);
					failed++;
				}
			}
			pxnLog.get().info("Found [ "+Integer.toString(successful)+" ] plugins");
			if(failed > 0) pxnLog.get().warning("Failed to preload [ "+Integer.toString(failed)+" ] plugins!");
		}
	}


	// init plugin instances
	public void InitPlugins() {
		InitPlugins(null);
	}
	public void InitPlugins(String mainClassFieldName) {
		if(mainClassFieldName == null || mainClassFieldName.isEmpty())
			mainClassFieldName = "Main Class";
		synchronized(plugins){
			pxnLogger log = pxnLog.get();
			int successful = 0;
			int failed = 0;
			for(PluginHolder holder : plugins.values()) {
				if(!holder.file.isFile()) {
					log.warning("jar file not found: "+holder.file);
					failed++;
					continue;
				}
				// find classes
				String mainClass = holder.mainClasses.get(mainClassFieldName);
				if(mainClass == null || mainClass.isEmpty())
					continue;
				// new plugin instance
				log.info("Loading plugin "+holder.pluginName+" "+holder.version);
				try {
					JarClassLoader classLoader = new JarClassLoader();
					classLoader.add(holder.file.toString());
					holder.plugin = (pxnPlugin) classLoader.loadClass(mainClass).newInstance();
					holder.plugin.setPluginManager(this);
					holder.plugin.setPluginYML(holder.yml);
					holder.plugin.Init();
				} catch(Exception e) {
					log.warning("Failed to load plugin: "+holder.pluginName);
					log.exception(e);
					failed++;
					continue;
				}
			}
			if(successful > 0) log.info("Inited [ "+Integer.toString(successful)+" ] plugins");
			if(failed     > 0) log.warning("Failed to init [ "+Integer.toString(failed)+" ] plugins!");
		}
	}


	// enable plugins
	public void EnablePlugins() {
		pxnLogger log = pxnLog.get();
		int successful = 0;
		int failed = 0;
		synchronized(plugins) {
			for(PluginHolder holder : plugins.values()) {
				if(holder.plugin == null) continue;
				// plugin already enabled
				if(holder.plugin.isEnabled()) continue;
				try {
					// enable plugin
					holder.plugin.doEnable();
					// getPluginName matches plugin.yml
					if(!holder.pluginName.equals(holder.plugin.getName())) {
						log.warning("Plugin name doesn't match plugin.yml! "+holder.pluginName+" | "+holder.plugin.getName());
						failed++;
					}
					successful++;
				} catch (Exception e) {
					log.exception(e);
					failed++;
					continue;
				}
			}
		}
		if(successful > 0) log.info("Successfully enabled [ "+Integer.toString(successful)+" ] plugins");
		if(failed     > 0) log.warning("Failed to enable [ "+Integer.toString(failed)+" ] plugins!");
	}


	// disable plugins
	public void DisablePlugins() {
		pxnLogger log = pxnLog.get();
		int successful = 0;
		int failed     = 0;
		synchronized(plugins) {
			for(PluginHolder holder : plugins.values()) {
				if(holder.plugin == null) continue;
				// plugin already disabled
				if(!holder.plugin.isEnabled()) continue;
				try {
					// disable plugin
					holder.plugin.doDisable();
					successful++;
				} catch (Exception e) {
					log.exception(e);
					failed++;
				}
			}
		}
		if(successful > 0) log.info("Successfully unloaded [ "+Integer.toString(successful)+" ] plugins");
		if(failed     > 0) log.warning("Failed to unload [ "+Integer.toString(failed)+" ] plugins!");
	}


	// unload plugins
	public void UnloadPlugins() {
		DisablePlugins();
		synchronized(plugins) {
			for(Entry<String, PluginHolder> entry : plugins.entrySet())
				plugins.put(entry.getKey(), null);
			plugins.clear();
		}
	}


}
