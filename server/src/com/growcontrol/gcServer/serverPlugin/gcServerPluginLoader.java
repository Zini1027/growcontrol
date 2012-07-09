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
import com.growcontrol.gcServer.commands.gcCommand;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerCommand;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerDevice;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerInput;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerOutput;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerTick;

public class gcServerPluginLoader {

	protected static HashMap<String, gcServerPluginHolder> plugins = new HashMap<String, gcServerPluginHolder>();
	private static final Class<?>[] parameters = new Class[]{URL.class};

	// listeners
	protected static HashMap<String, gcServerPluginListenerCommand>	listenersCommand	= new HashMap<String, gcServerPluginListenerCommand>();
	protected static HashMap<String, gcServerPluginListenerTick>	listenersTick		= new HashMap<String, gcServerPluginListenerTick>();
	protected static HashMap<String, gcServerPluginListenerOutput>	listenersOutput		= new HashMap<String, gcServerPluginListenerOutput>();
//	protected static HashMap<String, gcServerPluginListenerInput>	listenersInput		= new HashMap<String, gcServerPluginListenerInput>();
	protected static HashMap<String, gcServerPluginListenerDevice>	listenersDevice		= new HashMap<String, gcServerPluginListenerDevice>();


	// load plugins
	public void LoadPlugins() {
		// file filter .jar
		class jarFilter implements FileFilter {
			public boolean accept(File pathname) {
				return pathname.toString().endsWith(".jar");
			}
		}
		try {
			// get files list from /plugins
			File dir = new File("plugins");
			if(!dir.isDirectory()) {
				gcServer.log.severe("\\plugin folder not found!");
				return;
			}
			File[] files = dir.listFiles(new jarFilter());
			if(files == null) {
				gcServer.log.severe("Failed to get files list for \\plugins !");
				return;
			}
			// loop .jar files
			for(File f : files) {
				String name = "";
				try {
					List<String> classNames = getClassNames(f.getAbsolutePath());
					// loop class files in jar
					for(String className : classNames) {
						if(!className.endsWith(".class")) continue;
						name = className.substring(0,className.length()-6);
						Class<?> clss = getClass(f, name);
						Method[] methods = getClass(f, name).getMethods();
						boolean onEnable = false;
						boolean onDisable = false;
						// loop methods in class
						for(Method m : methods) {
							String methodName = m.getName();
							if(methodName.equals("onEnable"))		onEnable = true;
							else if(methodName.equals("onDisable"))	onDisable = true;
							if(onEnable && onDisable) break;
						}
						// try next class
						if(!onEnable || !onDisable) continue;
						// found plugin
						gcServer.log.debug("Loading server plugin: "+name);
						plugins.put(name, new gcServerPluginHolder(name, clss));
						break;
					}
				} catch(Exception e) {
					gcServer.log.severe("Failed to load server plugin: "+name);
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
		gcServer.log.info("Loaded "+Integer.toString(pluginsLoaded)+" server plugins");
		if(plugins.size() != pluginsLoaded)
			gcServer.log.warning("Failed to load "+Integer.toString(plugins.size()-pluginsLoaded)+" server plugins!");
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


	// jar loading functions
	private static List<String> getClassNames(String jarName) throws IOException {
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
	private static Class<?> getClass(File file, String name) throws Exception {
		addURL(file.toURI().toURL());
		URLClassLoader clazzLoader;
		Class<?> clazz;
		URL url = new File("jar:file://" + file.getAbsolutePath() + "!/").toURI().toURL();
		clazzLoader = new URLClassLoader(new URL[]{url});
		clazz = clazzLoader.loadClass(name);
		return clazz;
	}
	private static void addURL(URL u) throws IOException {
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
			if(firstPlugin)
				firstPlugin = false;
			else
				msg += ", ";
			msg += plugin.pluginName;
		}
		gcServer.log.info(msg);
	}


	// get plugin by class name
	public static gcServerPluginHolder getPluginByClassName(String className) {
		if(className==null || className.isEmpty()) return null;
		if(!plugins.containsKey(className)) return null;
		gcServerPluginHolder plugin = plugins.get(className);
		return plugin;
	}


//TODO: how to unregister listeners?
	// register listeners
	public static void registerListenerCommand(String className, gcServerPluginListenerCommand listener) {
		if(listener == null) return;
		listenersCommand.put(className, listener);
	}
	public static void registerListenerTick(String className, gcServerPluginListenerTick listener) {
		if(listener == null) return;
		listenersTick.put(className, listener);
	}
	public static void registerListenerOutput(String className, gcServerPluginListenerOutput listener) {
		if(listener == null) return;
		listenersOutput.put(className, listener);
	}
	public static void registerListenerInput(String className, gcServerPluginListenerInput listener) {
//TODO:
	}
	public static void registerListenerDevice(String className, gcServerPluginListenerDevice listener) {
		if(listener == null) return;
		listenersDevice.put(className, listener);
	}


	// run listeners
	public static boolean doCommand(String commandStr, String[] args) {
		for(Map.Entry<String, gcServerPluginListenerCommand> entry : listenersCommand.entrySet()) {
			// get plugin
			gcServerPluginHolder plugin = getPluginByClassName(entry.getKey());
			if(plugin == null) continue;
			// has command
			gcCommand command = plugin.commands.getCommandOrAlias(commandStr);
			if(command == null) continue;
			// get listener
			gcServerPluginListenerCommand listener = entry.getValue();
			if(listener == null) continue;
			// run listener
			if(listener.onCommand(command, args)) return true;
		}
		return false;
	}
	public static void doTick() {
		for(Map.Entry<String, gcServerPluginListenerTick> entry : listenersTick.entrySet()) {
			// validate plugin
			if(getPluginByClassName(entry.getKey()) == null) continue;
			// get listener
			gcServerPluginListenerTick listener = entry.getValue();
			if(listener == null) continue;
			// run listener
			listener.onTick();
		}
	}
	public static boolean doOutput(String[] args) {
		if(args.length < 1) return false;
		for(Map.Entry<String, gcServerPluginListenerOutput> entry : listenersOutput.entrySet()) {
			// get plugin
			gcServerPluginHolder plugin = getPluginByClassName(entry.getKey());
			if(plugin == null) continue;
//TODO: should outputs be registered?
//			// is output registered
//			plugin.isOutputRegistered()
			// get listener
			gcServerPluginListenerOutput listener = entry.getValue();
			if(listener == null) continue;
			// run listener
			if(listener.onOutput(args)) return true;
		}
		return false;
	}
	public static boolean doInput(String[] args) {
//TODO:
		return false;
	}


}
