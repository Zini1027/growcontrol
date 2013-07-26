package com.growcontrol.gcCommon.pxnPlugin;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.xeustechnologies.jcl.JarClassLoader;

import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public class pxnPluginManager {

	protected static pxnPluginManager manager = null;
	protected final String pluginsPath;

//	// plugin manager variables
//	protected String pluginsPath       = "plugins/";
//	protected String pluginYmlFileName = "plugin.yml";
//	protected final String mainClassFieldName_Server = "Server Main";
//	protected final String mainClassFieldName_Client = "Client Main";

//	// plugin instance holder
//	protected HashMap<String, pxnPlugin> plugins = new HashMap<String, pxnPlugin>();
//	protected List<String> pluginsListOnly = new ArrayList<String>();


	public static pxnPluginManager get() {
		return get(null);
	}
	public static synchronized pxnPluginManager get(String pluginsPath) {
		if(manager == null)
			if(pluginsPath != null && !pluginsPath.isEmpty())
				manager = new pxnPluginManager(pluginsPath);
		return manager;
	}
	protected pxnPluginManager(String pluginsPath) {
		if(pluginsPath == null || pluginsPath.isEmpty())
			this.pluginsPath = "plugins/";
		else
			this.pluginsPath = pluginsPath;
	}
//	public pxnPluginManager(String pluginsPath, String pluginYMLFileName) {
//		if(pluginsPath != null && !pluginsPath.isEmpty())
//			setPath(pluginsPath);
//		if(pluginYMLFileName != null && !pluginYMLFileName.isEmpty())
//			this.setPluginYMLFileName(pluginYMLFileName);
//	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}


//	// plugin.yml entries for main class
//	protected abstract String getMainClassFieldName();
//	protected abstract String getMainClassFieldName_ListOnly();


//	protected volatile String pluginsPath = null;
//	protected final JarClassLoader classLoader;


//	protected pxnPluginManager(String pluginsPath) {
//		classLoader = new JarClassLoader();
//		classLoader.add(pluginsPath);
//		this.pluginsPath = pluginsPath;
//	}


	// plugins (info and instance)
	protected HashMap<String, PluginHolder> plugins = new HashMap<String, PluginHolder>();
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


	// load plugins dir
	public synchronized void LoadPluginsDir() {
		LoadPluginsDir(new String[] {null});
	}
	public synchronized void LoadPluginsDir(String mainClassFieldName) {
		LoadPluginsDir(new String[] {mainClassFieldName});
	}
	public synchronized void LoadPluginsDir(String[] mainClassFieldNames) {
		if(mainClassFieldNames == null || mainClassFieldNames.length == 0 ||
				(mainClassFieldNames.length == 1 && mainClassFieldNames[0] == null))
			mainClassFieldNames = new String[] {"Main Class"};
		synchronized(this.plugins) {
//			String path = getPath();
			String path = this.pluginsPath;
			if(path == null || path.isEmpty())
				path = "plugins/";
			File dir = new File(path);
			if(!dir.isDirectory()) dir.mkdirs();
			if(!dir.isDirectory()) {
				pxnLogger.get().exception(new FileNotFoundException("plugins folder not found! "+path));
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
				pxnLogger.get().exception(new IOException("Failed to get plugins list! "+path));
				return;
			}
			// loop .jar files
			int successful = 0;
			int failed = 0;
			for(File file : files) {
				try {
					// load plugin.yml from jar
					pxnPluginYML yml = new pxnPluginYML(file, "plugin.yml");
					if(!yml.hasLoaded()) {
						// non-interrupted exception
						pxnLogger.get().debug("plugin.yml not found in jar: "+file.toString());
						//log.exception(new FileNotFoundException("File plugin.yml not found in jar! "+file.toString()));
						continue;
					}
					String pluginName = yml.getPluginName();
					// plugin already loaded
					if(this.plugins.containsKey(pluginName)) {
						pxnLogger.get().warning("Duplicate plugin already loaded: "+pluginName);
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
					this.plugins.put(pluginName, holder);
					successful++;
				} catch(Exception e) {
					// non-interrupted exception
					pxnLogger.get().exception("Failed to load plugin! "+file.toString(), e);
					failed++;
				}
			}
			pxnLogger.get().info("Found [ "+Integer.toString(successful)+" ] plugins.");
			if(failed > 0) pxnLogger.get().warning("Failed to preload [ "+Integer.toString(failed)+" ] plugins!");
		}
	}


	// init plugins
	public void InitPlugins() {
		InitPlugins(null);
	}
	public void InitPlugins(String mainClassFieldName) {
		if(mainClassFieldName == null || mainClassFieldName.isEmpty())
			mainClassFieldName = "Main Class";
		synchronized(this.plugins){ 
			int successful = 0;
			int failed = 0;
			for(PluginHolder holder : this.plugins.values()) {
				if(!holder.file.isFile()) {
					pxnLogger.get().warning("jar file not found: "+holder.file);
					failed++;
					continue;
				}
				// find classes
				String mainClass = holder.mainClasses.get(mainClassFieldName);
				if(mainClass == null || mainClass.isEmpty())
					continue;
//				Class clss = null;
//				try {
//					clss = getClassWithMethods(holder.file, mainClass,
//						new String[] {"onEnable", "onDisable"}
//					);
//					clss = classLoader.loadClass(mainClass);
//				} catch(Exception e) {
//					pxnLogger.get().warning("Failed to load plugin "+holder.pluginName);
//					pxnLogger.get().exception(e);
//					failed++;
//					continue;
//				}
//				if(clss == null) {
//					pxnLogger.get().warning("Required pxnPlugin class not found [ "+mainClass+" ] "+holder.file.toString());
//					failed++;
//					continue;
//				}
				// new plugin instance
				pxnLogger.get().info("Loading plugin "+holder.pluginName+" "+holder.version);
				try {
//					holder.plugin = (pxnPlugin) clss.newInstance();
					JarClassLoader classLoader = new JarClassLoader();
					classLoader.add(holder.file.toString());
					holder.plugin = (pxnPlugin) classLoader.loadClass(mainClass).newInstance();
					holder.plugin.setPluginManager(this);
					holder.plugin.setPluginYML(holder.yml);
				} catch(Exception e) {
					pxnLogger.get().warning("Failed to load plugin: "+holder.pluginName);
					pxnLogger.get().exception(e);
					failed++;
					continue;
				}
			}
			if(successful > 0) pxnLogger.get().info("Inited [ "+Integer.toString(successful)+" ] plugins.");
			if(failed     > 0) pxnLogger.get().warning("Failed to init [ "+Integer.toString(failed)+" ] plugins!");
		}
	}


	// enable plugins
	public void EnablePlugins() {
		pxnLogger log = pxnLogger.get();
		int successful = 0;
		int failed = 0;
		synchronized(this.plugins) {
			for(PluginHolder holder : this.plugins.values()) {
				if(holder.plugin == null) continue;
				// plugin already enabled
				if(holder.plugin.isEnabled()) continue;
				try {
					// enable plugin
					holder.plugin.doEnable();
					// getPluginName matches plugin.yml
					if(!holder.pluginName.equals(holder.plugin.getPluginName())) {
						log.warning("Plugin name doesn't match plugin.yml! "+holder.pluginName+" | "+holder.plugin.getPluginName());
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
		if(successful > 0) log.info("Successfully enabled [ "+Integer.toString(successful)+" ] plugins.");
		if(failed     > 0) log.warning("Failed to enable [ "+Integer.toString(failed)+" ] plugins!");
	}


	// disable plugins
	public void DisablePlugins() {
		pxnLogger log = pxnLogger.get();
		int successful = 0;
		int failed     = 0;
		synchronized(this.plugins) {
			for(PluginHolder holder : this.plugins.values()) {
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
		if(successful > 0) log.info("Successfully unloaded [ "+Integer.toString(successful)+" ] plugins.");
		if(failed     > 0) log.warning("Failed to unload [ "+Integer.toString(failed)+" ] plugins!");
	}


	// unload plugins
	public void UnloadPlugins() {
		DisablePlugins();
		synchronized(this.plugins) {
			for(Entry<String, PluginHolder> entry : this.plugins.entrySet())
				this.plugins.put(entry.getKey(), null);
			this.plugins.clear();
		}
	}


//	// plugins path
//	public void setPath(String path) {
//		pluginsPath = path;
//	}
//	public String getPath() {
//		if(pluginsPath == null || pluginsPath.isEmpty())
//			return "plugins/";
//		return pluginsPath;
//	}


//	// load class (with required methods)
//	public static pxnPluginInterface getClassWithMethods(File f, String mainClass, String[] methodsRequired) throws Exception {
//		if(f               == null) throw new NullPointerException("file cannot be null!");
//		if(mainClass       == null) throw new NullPointerException("mainClass cannot be null!");
//		if(methodsRequired == null) throw new NullPointerException("methodsRequired cannot be null!");
//		// get class
//
//JarClassLoader jcl = new JarClassLoader();
//jcl;
//
//		Class clss = getClass(f, mainClass);
//		// no required methods
//		if(methodsRequired.length == 0)
//{
//System.out.println("sdfgfdh-----------------");
//			return castPluginClass(clss);
//}
//		// check for required methods
//		List<String> methods = new ArrayList<String>();
//		for(Method m : clss.getMethods())
//			methods.add(m.getName());
//		for(String required : methodsRequired)
//			if(!methods.contains(required))
//{
//System.out.println("NOT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//				return null;
//}
//System.out.println("methods found!!!!!!!!");
//		return castPluginClass(clss);
//	}
//	@SuppressWarnings("unchecked")
//	private static pxnPluginInterface castPluginClass(Class clss) {
//if(clss == null){System.out.println("cast NULL!!!!!!!!!!");return null;}
//		if(clss == null) return null;
//		// class extends
//		if(clss.isAssignableFrom(pxnPlugin.class))
//{
//System.out.println("FOUND!!!!!!!!");
//			return (Class<pxnPlugin>) clss;
//}
//		try {
//			return (pxnPluginInterface) clss;
//		} catch (Exception ignore) {}
//		return null;
//		// check interfaces
//System.out.println(clss.getInterfaces().length);
//		for(Class<?> iface : clss.getSuperclass().getInterfaces()) {
//System.out.println(pxnPlugin.class.getName()+" - "+iface.getName());
//			if(iface.equals(pxnPlugin.class))
//				return (Class<pxnPlugin>) clss;
//		}
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
//	public static Class getClass(File file, String className) throws Exception {
//		if(file      == null) throw new NullPointerException("file cannot be null");
//		if(className == null) throw new NullPointerException("className cannot be null");
//		addURL(file.toURI().toURL());
//		URL url = new File("jar:file://"+file.getAbsolutePath()+"!/").toURI().toURL();
//		URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
//		Class clss = null;
//		try {
//			clss = classLoader.loadClass(className);
//		} catch (Exception e) {
//			pxnLogger.get().severe("Failed to load plugin class: "+className);
//			pxnLogger.get().exception(e);
//		}
//		if(classLoader != null) classLoader.close();
//		return clss;
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
//			pxnLogger.get().exception(e);
//			throw new IOException("Error, could not add URL to system classloader");
//		}
//	}


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
