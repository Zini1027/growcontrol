package com.growcontrol.gcServer.serverPlugin;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.pxnPlugin.pxnPluginManager;


public class gcServerPluginManager extends pxnPluginManager {

	// main class field names for plugin.yml
	protected final String mainClass_Server = "Server Main";
	protected final String mainClass_Client = "Client Main";

	// listeners
//	protected final gcServerListenerGroup commandListenerGroup = new gcServerListenerGroup(ListenerType.COMMAND);
//	protected static HashMap<String, gcServerPluginListenerCommand>	listenersCommand	= new HashMap<String, gcServerPluginListenerCommand>();
//	protected static HashMap<String, gcServerPluginListenerTick>	listenersTick		= new HashMap<String, gcServerPluginListenerTick>();
//	protected static HashMap<String, gcServerPluginListenerOutput>	listenersOutput		= new HashMap<String, gcServerPluginListenerOutput>();
//	protected static HashMap<String, gcServerPluginListenerInput>	listenersInput		= new HashMap<String, gcServerPluginListenerInput>();
//	protected static HashMap<String, gcServerPluginListenerDevice>	listenersDevice		= new HashMap<String, gcServerPluginListenerDevice>();



	@Override
	public void LoadPluginsDir() {
		LoadPluginsDir(new String[] {
			mainClass_Server,
			mainClass_Client
		});
	}
	@Override
	public void InitPlugins() {
		InitPlugins(mainClass_Server);
	}


	// get client plugins
	// 0 | plugin name
	// 1 | version
	// 2 | filename
	public List<String[]> getClientPlugins() {
		List<String[]> clientPlugins = new ArrayList<String[]>();
		synchronized(this.plugins) {
			for(PluginHolder holder : this.plugins.values()) {
				String mainClass = holder.mainClasses.get(mainClass_Client);
				if(mainClass == null || mainClass.isEmpty())
					continue;
				// add to client plugin list
				clientPlugins.add(new String[] {
					holder.pluginName,
					holder.version,
					holder.file.toString()
				});
			}
		}
		if(clientPlugins.size() == 0)
			return null;
		return clientPlugins;
	}


//	// plugin.yml entries for main class
//	@Override
//	protected String getMainClassFieldName() {
//		return "Server Main";
//	}
//	@Override
//	protected String getMainClassFieldName_ListOnly(){
//		return "Client Main";
//	}


//	public gcServerPluginManager() {
//		super();
//	}
//	public gcServerPluginManager(String pluginsPath) {
//		super(pluginsPath);
//	}
//	public gcServerPluginManager(String pluginsPath, String pluginYmlFileName, String mainClassYmlName) {
//		super(pluginsPath, pluginYmlFileName, mainClassYmlName);
//	}


//	// get plugin.yml
//	@Override
//	protected pxnPluginYML getPluginYML(File f) {
//		return new gcPluginYML(f, this.pluginYmlFileName);
//	}


//	// register listeners
//	public void registerCommandListener(gcServerListenerCommand listener) {
//		commandListenerGroup.register(listener);
//	}
////	public gcServerListenerGroup getCommandListenerGroup() {
////		return commandListenerGroup;
////	}
//	public boolean triggerEvent(gcServerEventCommand event) {
//		Main.getLogger().debug("Triggering event: "+event.getLine().getFirst());
//		for(EventPriority priority : EventPriority.values()) {
//			if(commandListenerGroup.triggerEvent(event, priority))
//				event.setHandled();
//		}
//		return event.isHandled();
//	}


//	// register listeners
//	public void registerListener(ListenerType type, gcServerListener listener) {
//		registerListener(null, type, listener);
//	}
//	public void registerListener(String className, ListenerType type, gcServerListener listener) {
//		if(type.equals(ListenerType.COMMAND))
//			listenersCommand.registerListener(listener);
//	}
//TODO: how to unregister listeners?
//	public void unregisterListeners() {
//	}


//	// run command event
//	public boolean triggerEventCommand(String commandStr, String[] args) {
//		if(commandStr == null) throw new NullPointerException("commandStr cannot be null");
//		if(args       == null) throw new NullPointerException("args cannot be null");
//		gcServerEventCommand event = new gcServerEventCommand(commandStr, args);
//		return triggerEvent(event);
//	}


//	// run event listeners
//	public boolean triggerEvent(gcServerEvent event) {
//		if(event == null) throw new NullPointerException("event cannot be null");
//		// highest
//		if(triggerEventPriority(event, EventPriority.HIGHEST)) event.setHandled();
//		// high
//		if(triggerEventPriority(event, EventPriority.HIGH))    event.setHandled();
//		// normal
//		if(triggerEventPriority(event, EventPriority.NORMAL))  event.setHandled();
//		// low
//		if(triggerEventPriority(event, EventPriority.LOW))     event.setHandled();
//		// lowest
//		if(triggerEventPriority(event, EventPriority.LOWEST))  event.setHandled();
//		return event.isHandled();
//	}
//	protected boolean triggerEventPriority(pxnEvent event, EventPriority priority) {
//		if(event instanceof gcServerEvent)
//			return 
//		return false;
//	}
//	private boolean triggerEventPriority(gcServerEvent event, EventPriority priority) {
//		if(event == null) throw new NullPointerException("event cannot be null");
//		// command listeners
//		if(event instanceof gcServerEventCommand)
//			if(commandListenerGroup.triggerEvent(event, priority))
//				event.setHandled();
////		// somethingelse listeners
////		if(event instanceof gcServerEventSomethingelse)
////			return listenersSomethingelse.triggerEvent(event, priority);
//		return event.isHandled();
//	}


}
