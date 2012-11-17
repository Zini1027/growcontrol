package com.growcontrol.gcServer.serverPlugin;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEvent;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEvent.EventPriority;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEventCommand;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerListener;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerListener.ListenerType;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerListenerHolder;

public class gcServerPluginManager {

	protected static HashMap<String, gcServerPluginHolder> plugins = new HashMap<String, gcServerPluginHolder>();
	protected String pluginsPath = "plugins";

	// listeners
	protected final gcServerListenerHolder listenersCommand = new gcServerListenerHolder(ListenerType.COMMAND);
//	protected static HashMap<String, gcServerPluginListenerCommand>	listenersCommand	= new HashMap<String, gcServerPluginListenerCommand>();
//	protected static HashMap<String, gcServerPluginListenerTick>	listenersTick		= new HashMap<String, gcServerPluginListenerTick>();
//	protected static HashMap<String, gcServerPluginListenerOutput>	listenersOutput		= new HashMap<String, gcServerPluginListenerOutput>();
//	protected static HashMap<String, gcServerPluginListenerInput>	listenersInput		= new HashMap<String, gcServerPluginListenerInput>();
//	protected static HashMap<String, gcServerPluginListenerDevice>	listenersDevice		= new HashMap<String, gcServerPluginListenerDevice>();


	// load plugins
	public void LoadPlugins() {
		// file filter .jar
		final class fileFilterJar implements FileFilter {
			public boolean accept(File pathname) {
				return pathname.toString().endsWith(".jar");
			}
		}

		try {
			// get files list from /plugins
			File dir = new File(pluginsPath);
			if(!dir.isDirectory()) {
				gcServer.log.severe("Plugins folder '"+pluginsPath+"' folder not found!");
				return;
			}
			File[] files = dir.listFiles(new fileFilterJar());
			if(files == null) {
				gcServer.log.severe("Failed to get plugins list for '"+pluginsPath+"' !");
				return;
			}
			// loop .jar files
			for(File f : files) {
				String serverMain = "";
				try {

					// load plugin.yml from jar
					pluginYML yml = new pluginYML(f, "plugin.yml");
					// get server main class
					serverMain = yml.getServerMain();
					if(serverMain == null || serverMain.isEmpty()) {
						gcServer.log.severe("plugin.yml file not found for plugin: "+f.toString());
						continue;
					}
					if(serverMain.endsWith(".class"))
						serverMain = serverMain.substring(0, serverMain.length()-6);
					// find class
					boolean onEnable = false;
					boolean onDisable = false;
					Class<?> clss = getClass(f, serverMain);
					for(Method m : getClass(f, serverMain).getMethods()) {
						String methodName = m.getName();
						if(methodName.equals("onEnable"))       onEnable  = true;
						else if(methodName.equals("onDisable")) onDisable = true;
						if(onEnable && onDisable) break;
					}
					// onEnable() or onDisable() is missing
					if(!onEnable) {
						gcServer.log.severe("onEnable() function not found in plugin: "+f.toString());
						continue;
					}
					if(!onDisable) {
						gcServer.log.severe("onDisable() function not found in plugin: "+f.toString());
						continue;
					}
					// found plugin
					gcServer.log.debug("Loading server plugin: "+serverMain);
					plugins.put(serverMain, new gcServerPluginHolder(serverMain, clss));

				} catch(Exception e) {
					gcServer.log.severe("Failed to load server plugin: "+serverMain);
					gcServer.log.exception(e);
				}
			}
		} catch(Exception e){
			gcServer.log.exception(e);
			return;
		}

		// enable plugins
		int pluginsLoaded = 0;
		for(Map.Entry<String, gcServerPluginHolder> pluginEntry : plugins.entrySet()) {
			gcServerPluginHolder plugin = pluginEntry.getValue();
			if(plugin == null) continue;
			try {
				plugin.doEnable();
				pluginsLoaded++;
			} catch(Exception e) {
				gcServer.log.severe("Failed to enable server plugin!");
				gcServer.log.exception(e);
			}
		}

		gcServer.log.info("Loaded [ "+Integer.toString(pluginsLoaded)+" ] server plugins");
		if(plugins.size() != pluginsLoaded)
			gcServer.log.warning("Failed to load [ "+Integer.toString(plugins.size()-pluginsLoaded)+" ] server plugins!");
	}
//TODO: add UnloadPlugin(String className) to unregister listeners
	public void UnloadPlugins() {
		for(gcServerPluginHolder plugin : plugins.values()) {
			gcServerPlugin.getLogger(plugin.pluginName).info("Stopping plugin..");
			plugin.enabled = false;
			plugin.doDisable();
		}
		plugins.clear();
	}


	// register listeners
	public void registerListener(ListenerType type, gcServerListener listener) {
		registerListener(null, type, listener);
	}
	public void registerListener(String className, ListenerType type, gcServerListener listener) {
		if(type.equals(ListenerType.COMMAND))
			listenersCommand.registerListener(listener);
	}
//TODO: how to unregister listeners?
	public void unregisterListeners() {
	}


	// run event listeners
	public boolean triggerEventCommand(String commandStr, String[] args) {
		if(commandStr == null) throw new NullPointerException();
		if(args       == null) throw new NullPointerException();
		return triggerEvent( new gcServerEventCommand(commandStr, args) );
	}
	public boolean triggerEvent(gcServerEvent event) {
		if(event == null) throw new NullPointerException();
		// highest
		triggerEventPriority(event, EventPriority.HIGHEST);
		// high
		triggerEventPriority(event, EventPriority.HIGH);
		// normal
		triggerEventPriority(event, EventPriority.NORMAL);
		// low
		triggerEventPriority(event, EventPriority.LOW);
		// lowest
		triggerEventPriority(event, EventPriority.LOWEST);
		return event.isHandled();
	}
	private boolean triggerEventPriority(gcServerEvent event, EventPriority priority) {
		if(event == null) throw new NullPointerException();
		// command listeners
		if(event instanceof gcServerEventCommand)
			return listenersCommand.triggerEvent(event, priority);
		// somethingelse listeners
//		if(event instanceof gcServerEventSomethingelse)
//			return listenersSomethingelse.triggerEvent(event, priority);
		return event.isHandled();
	}




//		for(Map.Entry<String, gcServerPluginListenerCommand> entry : listenersCommand.entrySet()) {
//			// get plugin
//			gcServerPluginHolder plugin = getPluginByClassName(entry.getKey());
//			if(plugin == null) continue;
//			// has command
//			gcCommand command = plugin.commands.getCommandOrAlias(commandStr);
//			if(command == null) continue;
//			// get listener
//			gcServerPluginListenerCommand listener = entry.getValue();
//			if(listener == null) continue;
//			// run listener
//			if(listener.onCommand(command, args)) return true;
//		}
//		return false;


//	public void doTick() {
//		for(Map.Entry<String, gcServerPluginListenerTick> entry : listenersTick.entrySet()) {
//			// validate plugin
//			if(getPluginByClassName(entry.getKey()) == null) continue;
//			// get listener
//			gcServerPluginListenerTick listener = entry.getValue();
//			if(listener == null) continue;
//			// run listener
//			listener.onTick();
//		}
//	}
//	public boolean doOutput(String[] args) {
//		if(args == null) throw new NullPointerException();
//		if(args.length < 1) return false;
//		for(Map.Entry<String, gcServerPluginListenerOutput> entry : listenersOutput.entrySet()) {
//			// get plugin
//			gcServerPluginHolder plugin = getPluginByClassName(entry.getKey());
//			if(plugin == null) continue;
////TODO: should outputs be registered?
////			// is output registered
////			plugin.isOutputRegistered()
//			// get listener
//			gcServerPluginListenerOutput listener = entry.getValue();
//			if(listener == null) continue;
//			// run listener
//			if(listener.onOutput(args)) return true;
//		}
//		return false;
//	}
//	public boolean doInput(String[] args) {
//TODO:
//		return false;
//	}


	// list class names
	public static List<String> getClassNames(String jarName) throws IOException {
		if(jarName == null) throw new NullPointerException();
		ArrayList<String> classes = new ArrayList<String>(10);
		JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
		JarEntry jarEntry;
		while(true) {
			jarEntry = jarFile.getNextJarEntry();
			if(jarEntry == null) break;
			if(jarEntry.getName().endsWith(".class"))
				classes.add(jarEntry.getName().replaceAll("/", "\\."));
		}
		return classes;
	}
	// get class by name
	public static Class<?> getClass(File file, String className) throws Exception {
		if(file == null) throw new NullPointerException();
		if(className == null) throw new NullPointerException();
		addURL(file.toURI().toURL());
		URL url = new File("jar:file://"+file.getAbsolutePath()+"!/").toURI().toURL();
		URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
		try {
			Class<?> clss = classLoader.loadClass(className);
			return clss;
		} catch (Exception e) {
			gcServer.log.severe("Failed to load plugin class: "+className);
			gcServer.log.exception(e);
		}
		return null;
	}
	private static final Class<?>[] parameters = new Class[]{URL.class};
	private static void addURL(URL u) throws IOException {
		if(u == null) throw new NullPointerException();
		URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		URL urls[] = sysLoader.getURLs();
		// skip if already loaded
		for (int i = 0; i < urls.length; i++)
			if (urls[i].toString().equalsIgnoreCase(u.toString())) return;
		// add classpath
		try {
			Class<?> sysclass = URLClassLoader.class;
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysLoader, new Object[]{u});
		} catch (Throwable e) {
			gcServer.log.exception(e);
			throw new IOException("Error, could not add URL to system classloader");
		}
	}


	// list plugins
	public static void listPlugins() {
		String msg = "Plugins ("+Integer.toString(plugins.size())+"):  ";
		boolean firstPlugin = true;
		for(gcServerPluginHolder plugin : plugins.values()) {
			if(firstPlugin) firstPlugin = false;
			else msg += ", ";
			msg += plugin.pluginName;
		}
		gcServer.log.info(msg);
	}


	// plugins path
	public void setPath(String path) {
		if(path == null) throw new NullPointerException();
		pluginsPath = path;
	}
	public String getPath() {
		return pluginsPath;
	}


	// get plugin by class name
	public static gcServerPluginHolder getPluginByClassName(String className) {
		if(className == null) throw new NullPointerException();
		if(className.isEmpty()) return null;
		if(!plugins.containsKey(className)) return null;
		gcServerPluginHolder plugin = plugins.get(className);
		return plugin;
	}


}
