package com.poixson.pxnPlugin;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.poixson.pxnLogger.pxnLogger;


public class pxnPluginManager {

	// plugin instances
	protected HashMap<String, pxnPlugin> plugins = new HashMap<String, pxnPlugin>();

	// plugin manager variables
	protected String pluginsPath       = "plugins/";
	protected String pluginYmlFileName = "plugin.yml";
//	protected String mainClassYmlName  = "main";


	public pxnPluginManager() {
	}
	public pxnPluginManager(String pluginsPath) {
		this.setPath(pluginsPath);
	}
	public pxnPluginManager(String pluginsPath, String pluginYmlFileName) {
		if(pluginsPath != null && !pluginsPath.isEmpty())
			setPath(pluginsPath);
		if(pluginYmlFileName != null && !pluginYmlFileName.isEmpty())
			this.setYmlFileName(pluginYmlFileName);
//		if(mainClassYmlName != null && !mainClassYmlName.isEmpty())
//			this.setMainClassYmlName(mainClassYmlName);
	}


	// load plugins dir
	public void LoadPlugins() throws Exception {
		File dir = new File(pluginsPath);
		if(!dir.isDirectory())
			dir.mkdirs();
		if(!dir.isDirectory())
			throw new FileNotFoundException(pluginsPath+" (Plugins folder not found!)");
		// get files list from /plugins
		File[] files = dir.listFiles(new fileFilterJar());
		if(files == null)
			throw new IOException(pluginsPath+" (Failed to get plugins list!)");
		// loop .jar files
		int successful = 0;
		int failed     = 0;
		for(File f : files) {
			try {
				LoadPlugin(f);
				successful++;
			} catch(Exception e) {
				// non-interrupted exception
				pxnLogger.log().exception(f.toString()+" (Failed to load plugin)", e);
				failed++;
			}
		}
		pxnLogger.log().info("Loaded [ "+Integer.toString(plugins.size())+" ] plugins.");
		if(failed > 0) pxnLogger.log().warning("Failed to load [ "+Integer.toString(failed)+" ] plugins!");
	}
	// file filter .jar
	protected final class fileFilterJar implements FileFilter {
		@Override
		public boolean accept(File pathname) {
			return pathname.toString().endsWith(".jar");
		}
	}


	// load plugin jar
	public void LoadPlugin(File f) throws Exception {
		// load plugin.yml from jar
		pxnPluginYML yml = getPluginYML(f);
		// get server main class
		String mainClassValue = yml.getMainClassValue();
		if(mainClassValue == null || mainClassValue.isEmpty()) {
			// non-interrupted exception
			pxnLogger.log().exception(new FileNotFoundException(f.toString()+" : plugin.yml (File not found in jar!)"));
			return;
		}
		// find classes
		Class<pxnPlugin> clss = getClassWithMethods(f, mainClassValue,
			Arrays.asList("onEnable", "onDisable") );
		if(clss == null) {
			pxnLogger.log().severe(f.toString()+" : "+mainClassValue+" (Plugin main class not found with required methods!)");
			return;
		}
		// found plugin
		pxnLogger.log().debug("Loading plugin: "+mainClassValue);
		pxnPlugin plugin = clss.newInstance();
		plugin.setPluginManager(this);
		plugins.put(mainClassValue, plugin);
	}
	// get plugin.yml
	protected pxnPluginYML getPluginYML(File f) {
		return new pxnPluginYML(f, this.pluginYmlFileName);
	}


	// enable plugins
	public void EnablePlugins() {
		int successful = 0;
		int failed     = 0;
		for(pxnPlugin plugin : plugins.values()) {
			if(!plugin.isEnabled()) {
				try {
					EnablePlugin(plugin);
					successful++;
				} catch (Exception e) {
					pxnLogger.log().exception(e);
					failed++;
				}
			}
		}
		if(successful > 0) pxnLogger.log().info("Successfully enabled [ "+Integer.toString(successful)+" ] plugins.");
		if(failed     > 0) pxnLogger.log().warning("Failed to enable [ "+Integer.toString(failed)+" ] plugins!");
	}
	public void EnablePlugin(String pluginName) throws Exception {
		if(plugins.containsKey(pluginName))
			EnablePlugin(plugins.get(pluginName));
	}
	public void EnablePlugin(pxnPlugin plugin) throws Exception {
		plugin.getLogger().info("Starting plugin..");
		plugin.onEnable();
		plugin.enabled = true;
	}


	// unload plugins
	public void UnloadPlugins() {
		DisablePlugins();
		for(Entry<String, pxnPlugin> entry : plugins.entrySet())
			plugins.put(entry.getKey(), null);
		plugins.clear();
	}
	public void DisablePlugins() {
		int successful = 0;
		int failed     = 0;
		for(pxnPlugin plugin : plugins.values()) {
			try {
				DisablePlugin(plugin);
				successful++;
			} catch (Exception e) {
				pxnLogger.log().exception(e);
				failed++;
			}
		}

		if(successful > 0) pxnLogger.log().info("Successfully unloaded [ "+Integer.toString(successful)+" ] plugins.");
		if(failed     > 0) pxnLogger.log().warning("Failed to unload [ "+Integer.toString(failed)+" ] plugins!");
	}
	public void DisablePlugin(String pluginName) {
		if(plugins.containsKey(pluginName))
			DisablePlugin(plugins.get(pluginName));
	}
	public void DisablePlugin(pxnPlugin plugin) {
//TODO: add UnloadPlugin(String className) to unregister listeners
		plugin.getLogger().info("Stopping plugin..");
		plugin.enabled = false;
		plugin.onDisable();
	}


	// plugin manager variables

	// plugins path
	public void setPath(String path) {
		if(path == null)   throw new NullPointerException("path can't be null");
		if(path.isEmpty()) throw new IllegalArgumentException("path can't be empty!");
		pluginsPath = path;
	}
	public String getPath() {
		return pluginsPath;
	}
	// plugin.yml or alternate file name
	public void setYmlFileName(String fileName) {
		if(fileName == null)   throw new NullPointerException("fileName can't be null");
		if(fileName.isEmpty()) throw new IllegalArgumentException("fileName can't be empty!");
		this.pluginYmlFileName = fileName;
	}
//	public String getYmlFileName() {
//		return pluginYmlFileName;
//	}
//	// main class variable in yml
//	public void setMainClassYmlName(String mainClassYmlName) {
//		if(mainClassYmlName == null)   throw new NullPointerException("mainClassYmlName can't be null");
//		if(mainClassYmlName.isEmpty()) throw new IllegalArgumentException("mainClassYmlName can't be empty!");
//		this.mainClassYmlName = mainClassYmlName;
//	}
//	public String getMainClassYmlName() {
//		return mainClassYmlName;
//	}


	// load class (with required methods)
	public static Class<pxnPlugin> getClassWithMethods(File f, String mainClass, List<String> methodsRequired) throws Exception {
		if(f == null) throw new NullPointerException("file can't be null!");
		if(mainClass == null) throw new NullPointerException("mainClass can't be null!");
		if(methodsRequired == null) throw new NullPointerException("methodsRequired can't be null!");
		Class<?> clss = getClass(f, mainClass);
		if(methodsRequired.isEmpty())
			return castPluginClass(clss);
		List<String> methodsTmp = new ArrayList<String>(methodsRequired);
		for(Method m : getClass(f, mainClass).getMethods() ) {
			String methodName = m.getName();
			if(methodsTmp.contains(methodName))
				methodsTmp.remove(methodName);
			if(methodsTmp.isEmpty())
				break;
		}
		if(methodsTmp.isEmpty())
			return castPluginClass(clss);
		// required methods not all found
		return null;
	}
	@SuppressWarnings("unchecked")
	protected static Class<pxnPlugin> castPluginClass(Class<?> clss) {
		return (Class<pxnPlugin>) clss;
	}


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
	// get class by name
	public static Class<?> getClass(File file, String className) throws Exception {
		if(file      == null) throw new NullPointerException("file cannot be null");
		if(className == null) throw new NullPointerException("className cannot be null");
		addURL(file.toURI().toURL());
		URL url = new File("jar:file://"+file.getAbsolutePath()+"!/").toURI().toURL();
		URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
		try {
			Class<?> clss = classLoader.loadClass(className);
			return clss;
		} catch (Exception e) {
			pxnLogger.log().severe("Failed to load plugin class: "+className);
			pxnLogger.log().exception(e);
		}
		return null;
	}
	private static final Class<?>[] parameters = new Class[]{URL.class};
	private static void addURL(URL url) throws IOException {
		if(url == null) throw new NullPointerException("url cannot be null");
		URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		URL urls[] = sysLoader.getURLs();
		// skip if already loaded
		for (int i = 0; i < urls.length; i++)
			if (urls[i].toString().equalsIgnoreCase(url.toString())) return;
		// add classpath
		try {
			Class<?> sysclass = URLClassLoader.class;
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysLoader, new Object[]{url});
		} catch (Throwable e) {
			pxnLogger.log().exception(e);
			throw new IOException("Error, could not add URL to system classloader");
		}
	}


//TODO:
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


}
