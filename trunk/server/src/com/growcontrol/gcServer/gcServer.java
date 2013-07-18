package com.growcontrol.gcServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnClock.pxnClock;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnScheduler.pxnScheduler;
import com.growcontrol.gcCommon.pxnScheduler.pxnSchedulerTask;
import com.growcontrol.gcCommon.pxnScheduler.pxnTriggers.triggerInterval;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketProcessorFactory;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketServer;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;
import com.growcontrol.gcServer.scheduler.gcTicker;
import com.growcontrol.gcServer.serverPlugin.gcServerPluginManager;
import com.growcontrol.gcServer.socketServer.gcSocketProcessor;


public class gcServer {
	public static final String version = "3.0.5";
	public static final String defaultPrompt = ">";

	protected static gcServer server = null;
	private static ServerListeners listeners;
	private Thread consoleInputThread = null;

	// runtime args
	protected boolean consoleEnabled = true;
	protected boolean forceDebug = false;
	private long startTime = -1;
	private boolean stopping = false;
	protected String configsPath = null;

	// server plugin manager
	private final gcServerPluginManager pluginManager = new gcServerPluginManager();
//	public final gcServerDeviceLoader deviceLoader = new gcServerDeviceLoader();

	// server socket pool
	private pxnSocketServer socket = null;

	// zones
	private List<String> zones = null;


	// server instance
	public gcServer() {
	}
	public static gcServer get() {
		return server;
	}


	// init server
	public void Start() {
		pxnLogger log = pxnLogger.get();
//		if(noconsole)
//			gcLogger.setLevel("console", pxnLevel.LEVEL.WARNING);
		// single instance lock
		pxnUtils.lockInstance("gcServer.lock");
		if(!consoleEnabled) {
			System.out.println("Console input is disabled due to noconsole command argument.");
//TODO: currently no way to stop the server with no console input
System.exit(0);
		}
		log.printRaw("[[ Starting GC Server ]]");
		log.info("GrowControl "+version+" Server is starting..");
		pxnUtils.addLibraryPath("lib");

		// query time server
		pxnClock clock = pxnClock.getBlocking();
		startTime = clock.Millis();
System.out.println(startTime);

		// load configs
		ServerConfig config = ServerConfig.get(configsPath);
		if(config==null || config.config==null) {
			log.severe("Failed to load config.yml, exiting..");
			System.exit(1);
			return;
		}

		// set log level
		if(!forceDebug && consoleEnabled) {
			String logLevel = config.LogLevel();
			if(logLevel != null && !logLevel.isEmpty()) {
				log.info("Set log level: "+logLevel.toString());
				pxnLogger.setLevel("console", logLevel);
			}
		}
//		gcLogger.setLevel("file",    logLevel);

		// init listeners
		listeners = new ServerListeners();

		// start console input thread
		if(consoleEnabled) {
			consoleInputThread = new Thread("ConsoleInput") {
				@Override
				public void run() {
					StartConsole();
				}
			};
			consoleInputThread.start();
		}

		// zones
		if(zones == null) zones = new ArrayList<String>();
		synchronized(zones) {
			config.ZonesList(zones);
			log.info("Loaded [ "+Integer.toString(zones.size())+" ] zones.");
		}

		// load scheduler
		pxnScheduler.get("gcServer").start();
		// load ticker
		gcTicker.get();

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


	// stop server
	public void Shutdown() {
		// queue shutdown
		pxnThreadQueue.addToMain("BeginShutdown", new Runnable() {
			@Override
			public void run() {
				doShutdown();
			}
		});
	}
	private void doShutdown() {
		pxnLogger log = pxnLogger.get();
		stopping = true;
		log.printRaw("[[ Stopping GC Server ]]");
		log.info("Stopping GC Server..");
		// sleep
		log.info("Waiting for things to finish..");
		final int inter = 50;
		final int maxWait = 1000;
		for(int ms=maxWait; ms>0; ms-=inter) {
			pxnUtils.Sleep(inter);
			if((ms % 500)==0)
				log.debug("Waiting "+ms+"ms..");
			switch(ms) {
			// first
			case maxWait:
				// close socket listener
				socket.stop();
				// pause scheduler
				pxnScheduler.PauseAll();
				break;
			// 500ms
			case 500:
				// end schedulers
				pxnScheduler.ShutdownAll();
				break;
			// 450ms
			case 450:
				break;
			// 400ms
			case 400:
				break;
			// 350ms
			case 350:
				// unload plugins
				pluginManager.UnloadPlugins();
				break;
			// 300ms
			case 300:
				break;
			// 250ms
			case 250:
				// close sockets
				socket.forceCloseAll();
				// stop console
				consoleInputThread.interrupt(); // doesn't do much of anything
				AnsiConsole.systemUninstall();
				break;
			// 200ms
			case 200:
				break;
			// last
			case inter:
//TODO: display total time running
				break;
			default:
				break;
			}
		}
		// stop process
//		getMainThread().Shutdown();
		pxnThreadQueue.getMain().Exit();
	}
	// reload server
	public void Reload() {
		pxnThreadQueue.addToMain("Reload", new Runnable() {
			@Override
			public void run() {
//TODO:reload
			}
		});
	}


	// console input loop
	private void StartConsole() {
		if(!consoleEnabled) return;
		//TODO: password login
		// If we input the special word then we will mask
		// the next line.
		//if ((trigger != null) && (line.compareTo(trigger) == 0))
//			line = reader.readLine("password> ", mask);
		//if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) break;
		while(!stopping) {
			if(consoleInputThread.isInterrupted()) break;
			try {
				// wait for commands
				String line = pxnLogger.readLine();
				if(line == null) break;
				if(line.isEmpty()) continue;
				processCommand(line);
			} catch(Exception e) {
				pxnLogger.get().exception(e);
				break;
			}
		}
		System.out.println();
		System.out.println();
	}
	// process command
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


	public static ServerListeners getListeners() {
		return listeners;
	}


	// is server stopping
	public boolean isStopping() {
		return this.stopping;
	}
	// uptime
	public long getStartTime() {
		return this.startTime;
	}
	public long getUptime() {
		return pxnClock.get().Millis() - getStartTime();
	}
	public String getUptimeString() {
		return Long.toString(getUptime());
	}


	// get plugin manager
	public gcServerPluginManager getPluginManager() {
		return pluginManager;
	}


	// get zones
	public List<String> getZones() {
		return zones;
	}


}
