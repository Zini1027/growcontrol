package com.poixson.pxnPlugin;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.poixson.pxnLogger.pxnLogger;

public abstract class pxnPluginManager {

	protected pxnLogger log;

	// plugin instances
	protected static HashMap<String, pxnPlugin> plugins = new HashMap<String, pxnPlugin>();
	protected String pluginsPath = "plugins";


	public pxnPluginManager() {
		this(pxnLogger.getLogger("pxnPluginManager"));
	}
	public pxnPluginManager(pxnLogger log) {
		this.log = log;
	}


	// load plugins
	public void LoadPlugins() throws Exception {
		File dir = new File(pluginsPath);
		if(!dir.isDirectory())
			throw new FileNotFoundException(pluginsPath+" (Plugins folder not found!)");
		// get files list from /plugins
		File[] files = dir.listFiles(new fileFilterJar());
		if(files == null)
			throw new IOException(pluginsPath+" (Failed to get plugins list!)");
		// loop .jar files
		for(File f : files) {
			String mainClassName = "";
			try {
				// get server main class
				mainClassName = getMainClassName(f);
				if(mainClassName == null || mainClassName.isEmpty()) {
					// non-interrupted exception
					log.exception(new FileNotFoundException(f.toString()+":plugin.yml (File not found in jar!)"));
					continue;
				}
				// trim .class from end
				if(mainClassName.endsWith(".class"))
					mainClassName = mainClassName.substring(0, mainClassName.length()-6);
				// find classes
//TODO: move this to its own static function
//				boolean onEnable = false;
//				boolean onDisable = false;
//				Class<?> clss = getClass(f, serverMain);
//				for(Method m : getClass(f, serverMain).getMethods()) {
//					String methodName = m.getName();
//					if(methodName.equals("onEnable"))       onEnable  = true;
//					else if(methodName.equals("onDisable")) onDisable = true;
//					if(onEnable && onDisable) break;
//				}
//				// onEnable() or onDisable() is missing
//				if(!onEnable) {
//					gcServer.log.severe("onEnable() function not found in plugin: "+f.toString());
//					continue;
//				}
//				if(!onDisable) {
//					gcServer.log.severe("onDisable() function not found in plugin: "+f.toString());
//					continue;
//				}

			} catch(Exception e) {
				// non-interrupted exception
				log.exception(mainClassName+" (Failed to load server plugin)", e);
				continue;
			}
		}


	}
	// file filter .jar
	protected final class fileFilterJar implements FileFilter {
		@Override
		public boolean accept(File pathname) {
			return pathname.toString().endsWith(".jar");
		}
	}
//					// found plugin
//					gcServer.log.debug("Loading server plugin: "+serverMain);
//					plugins.put(serverMain, new gcServerPluginHolder(serverMain, clss));



//			}

//		// enable plugins
//		int pluginsLoaded = 0;
//		for(Map.Entry<String, gcServerPluginHolder> pluginEntry : plugins.entrySet()) {
//			gcServerPluginHolder plugin = pluginEntry.getValue();
//			if(plugin == null) continue;
//			try {
//				plugin.getLogger().info("Starting plugin..");
//				plugin.doEnable();
//				pluginsLoaded++;
//			} catch(Exception e) {
//				gcServer.log.severe("Failed to enable server plugin!");
//				gcServer.log.exception(e);
//			}
//		}
//
//		gcServer.log.info("Loaded [ "+Integer.toString(pluginsLoaded)+" ] server plugins");
//		if(plugins.size() != pluginsLoaded)
//			gcServer.log.warning("Failed to load [ "+Integer.toString(plugins.size()-pluginsLoaded)+" ] server plugins!");
//	}


	public void UnloadPlugins() {
		for(Entry<String, pxnPlugin> entry : plugins.entrySet()) {
			pxnPlugin plugin = entry.getValue();
			plugin.getLogger().info("Stopping plugin..");
			plugin.enabled = false;
			plugin.onDisable();
		}
		plugins.clear();
	}
//TODO: add UnloadPlugin(String className) to unregister listeners
	public void UnloadPlugin(String pluginName) {
	}


	// plugins path
	public void setPath(String path) {
		if(path == null) throw new NullPointerException("path cannot be null");
		pluginsPath = path;
	}
	public String getPath() {
		return pluginsPath;
	}


	// load plugin.yml from jar
	protected static String getYmlFileName() {
		return "plugin.yml";
	}
	protected static String getMainClassName(File jarFile) {
		return getMainClassName(jarFile, getYmlFileName());
	}
	protected static String getMainClassName(File jarFile, String fileName) {
		// load plugin.yml from jar
		pxnPluginYML yml = new pxnPluginYML(jarFile, fileName);
		// get server main class
		return yml.getServerMain();
	}







//	// trigger event by priority
//	protected abstract boolean triggerEventPriority(pxnEvent event, EventPriority priority);
















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


//	// list class names
//	public static List<String> getClassNames(String jarName) throws IOException {
//		if(jarName == null) throw new NullPointerException("jarName cannot be null");
//		ArrayList<String> classes = new ArrayList<String>(10);
//		JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
//		JarEntry jarEntry;
//		while(true) {
//			jarEntry = jarFile.getNextJarEntry();
//			if(jarEntry == null) break;
//			if(jarEntry.getName().endsWith(".class"))
//				classes.add(jarEntry.getName().replaceAll("/", "\\."));
//		}
//		return classes;
//	}
//	// get class by name
//	public static Class<?> getClass(File file, String className) throws Exception {
//		if(file      == null) throw new NullPointerException("file cannot be null");
//		if(className == null) throw new NullPointerException("className cannot be null");
//		addURL(file.toURI().toURL());
//		URL url = new File("jar:file://"+file.getAbsolutePath()+"!/").toURI().toURL();
//		URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
//		try {
//			Class<?> clss = classLoader.loadClass(className);
//			return clss;
//		} catch (Exception e) {
//			gcServer.log.severe("Failed to load plugin class: "+className);
//			gcServer.log.exception(e);
//		}
//		return null;
//	}
//	private static final Class<?>[] parameters = new Class[]{URL.class};
//	private static void addURL(URL url) throws IOException {
//		if(url == null) throw new NullPointerException("url cannot be null");
//		URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
//		URL urls[] = sysLoader.getURLs();
//		// skip if already loaded
//		for (int i = 0; i < urls.length; i++)
//			if (urls[i].toString().equalsIgnoreCase(url.toString())) return;
//		// add classpath
//		try {
//			Class<?> sysclass = URLClassLoader.class;
//			Method method = sysclass.getDeclaredMethod("addURL", parameters);
//			method.setAccessible(true);
//			method.invoke(sysLoader, new Object[]{url});
//		} catch (Throwable e) {
//			gcServer.log.exception(e);
//			throw new IOException("Error, could not add URL to system classloader");
//		}
//	}


//	// list plugins
//	public static void listPlugins() {
//		String msg = "Plugins ("+Integer.toString(plugins.size())+"):  ";
//		boolean firstPlugin = true;
//		for(gcServerPluginHolder plugin : plugins.values()) {
//			if(firstPlugin) firstPlugin = false;
//			else msg += ", ";
//			msg += plugin.pluginName;
//		}
//		gcServer.log.info(msg);
//	}




//	// get plugin by class name
//	public static gcServerPluginHolder getPluginByClassName(String className) {
//		if(className == null)   throw new NullPointerException("className cannot be null");
//		if(className.isEmpty()) throw new NullPointerException("className cannot be empty");
//		if(!plugins.containsKey(className)) return null;
//		gcServerPluginHolder plugin = plugins.get(className);
//		return plugin;
//	}























}
