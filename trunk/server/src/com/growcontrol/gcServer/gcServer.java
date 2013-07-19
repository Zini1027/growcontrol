package com.growcontrol.gcServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.pxnApp;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnScheduler.pxnScheduler;
import com.growcontrol.gcCommon.pxnScheduler.pxnSchedulerTask;
import com.growcontrol.gcCommon.pxnScheduler.pxnTicker;
import com.growcontrol.gcCommon.pxnScheduler.pxnTriggers.triggerInterval;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketProcessorFactory;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketServer;
import com.growcontrol.gcServer.serverPlugin.gcServerPluginManager;
import com.growcontrol.gcServer.socketServer.gcSocketProcessor;


public class gcServer extends pxnApp {
	public static final String appName = "gcServer";
	public static final String version = "3.0.5";
	public static final String defaultPrompt = ">";

	protected static gcServer server = null;
	private static ServerListeners listeners;

	// server plugin manager
	private final gcServerPluginManager pluginManager = new gcServerPluginManager();
//	public final gcServerDeviceLoader deviceLoader = new gcServerDeviceLoader();

	// server socket pool
	private pxnSocketServer socket = null;

	// zones
	private List<String> zones = new ArrayList<String>();


	// server instance
	public gcServer() {
	}
	public static gcServer get() {
		return server;
	}


	// init server
	@Override
	public void Start() {
		super.Start();
		pxnLogger log = pxnLogger.get();
if(!consoleEnabled) {
System.out.println("Console input is disabled due to noconsole command argument.");
//TODO: currently no way to stop the server with no console input
System.exit(0);
}

		log.info("GrowControl "+version+" Server is starting..");
		// load configs
		ServerConfig config = ServerConfig.get(configsPath);
		if(config==null || config.config==null) {
			log.severe("Failed to load config.yml, exiting..");
			System.exit(1);
			return;
		}
		// set log level
		setLogLevel(config.LogLevel());
		// init listeners
		listeners = new ServerListeners();
		// start console input thread
		StartConsole();

		// load zones
		synchronized(this.zones) {
			config.PopulateZones(this.zones);
			log.info("Loaded [ "+Integer.toString(this.zones.size())+" ] zones.");
		}

		// load scheduler
		pxnScheduler.get(getAppName()).start();
		// load ticker
		pxnTicker.get();

		// load plugins
		try {
			pluginManager.LoadPlugins();
			pluginManager.EnablePlugins();
		} catch (Exception e) {
			log.exception(e);
			Shutdown();
			return;
		}

//		// load devices
//		deviceLoader.LoadDevices(Arrays.asList(new String[] {"Lamp"}));

		// start socket listener
		try {
			socket = new pxnSocketServer(config.ListenPort(), new pxnSocketProcessorFactory() {
				@Override
				public gcSocketProcessor newProcessor() {
					return new gcSocketProcessor();
				}
			});
		} catch (IOException e) {
			log.exception(e);
		}

//		// start schedulers
//		log.info("Starting schedulers..");
//		gcSchedulerManager.StartAll();

//TODO: remove this
//log.severe("Listing Com Ports:");
//for(Map.Entry<String, String> entry : Serial.listPorts().entrySet())
//log.severe(entry.getKey()+" - "+entry.getValue());
		log.printRaw("[[ GC Server Running! ]]");


//TODO: remove temp scheduled task
// new task (multi-threaded / repeat)
pxnSchedulerTask task = new pxnSchedulerTask(true, true) {
	@Override
	public void run() {
		System.out.println("333333333 tick");
	}
	@Override
	public String getTaskName() {
		return "333tickname";
	}
};
task.addTrigger(new triggerInterval("3s"));
pxnScheduler.get("gcServer").newTask(task);

//System.out.println("next run: "+task.UntilNextTrigger().get(TimeU.MS));


	}


	@Override
	protected void doShutdown(int step) {
		switch(step) {
		// first step (announce)
		case 10:
			pxnLogger.get().info("Stopping GC Server..");
			break;
		case 9:
			// close socket listener
			socket.stop();
			// pause scheduler
			pxnScheduler.PauseAll();
			break;
		case 8:
			break;
		case 7:
			break;
		case 6:
			break;
		case 5:
			// end schedulers
			pxnScheduler.ShutdownAll();
			break;
		case 4:
			// unload plugins
			pluginManager.UnloadPlugins();
			break;
		case 3:
			// close sockets
			socket.forceCloseAll();
			break;
		case 2:
			break;
		// last step
		case 1:
//TODO: display total time running
			break;
		}
	}
//	// reload server
//	public void Reload() {
//		pxnThreadQueue.addToMain("Reload", new Runnable() {
//			@Override
//			public void run() {
////TODO:reload
//			}
//		});
//	}


	// process command
	@Override
	public void processCommand(String line) {
		if(line == null) throw new NullPointerException("line cannot be null");
		line = line.trim();
		if(line.isEmpty()) return;
		// trigger event
		if(!listeners.triggerCommand(line)) {
			// command not found
			pxnLogger.get().warning("Unknown command: "+line);
		}
	}


	@Override
	public String getAppName() {
		return appName;
	}
	@Override
	public String getVersion() {
		return version;
	}


	public static ServerListeners getListeners() {
		return listeners;
	}


	// get plugin manager
	public gcServerPluginManager getPluginManager() {
		return pluginManager;
	}


	// get zones
	public List<String> getZones() {
		synchronized(zones) {
			return new ArrayList<String>(zones);
		}
	}


}
