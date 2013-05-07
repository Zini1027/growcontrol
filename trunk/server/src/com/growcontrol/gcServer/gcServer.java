package com.growcontrol.gcServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.scheduler.gcSchedulerManager;
import com.growcontrol.gcServer.scheduler.gcTicker;
import com.growcontrol.gcServer.serverPlugin.gcServerPluginManager;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEventCommand;
import com.growcontrol.gcServer.socketServer.gcSocketProcessor;
import com.gcCommon.pxnUtils;
import com.gcCommon.pxnClock.pxnClock;
import com.gcCommon.pxnSocket.pxnSocketProcessorFactory;
import com.gcCommon.pxnSocket.pxnSocketServer;


public class gcServer {
	public static final String version = "3.0.4";
	public static final String defaultPrompt = ">";

	// runtime args
	protected boolean consoleEnabled = true;
	protected boolean forceDebug = false;
	private final long startTime;
	private boolean stopping = false;

	// logger
	private final gcLogger log;
	private Thread consoleInputThread = null;

	// server plugin manager
	private final gcServerPluginManager pluginManager = new gcServerPluginManager();
//	public final gcServerDeviceLoader deviceLoader = new gcServerDeviceLoader();

	// config files
	private ServerConfig config = null;
	protected String configsPath = null;

	// server scheduler
	private gcSchedulerManager scheduler = null;
	private gcTicker ticker = null;

	// clock
	private pxnClock clock = null;

	// server socket pool
	private pxnSocketServer socket = null;

	// zones
	private List<String> zones = null;


	// server instance
	public gcServer() {
		log = Main.getLogger();
		startTime = pxnUtils.getCurrentMillis();
	}


	// init server
	public void Start() {
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
		clock = pxnClock.getClock(true);

		// load configs
		config = new ServerConfig(configsPath);
		if(config==null || config.config==null) {
			log.severe("Failed to load config.yml");
			System.exit(1);
		}

		// set log level
		if(!forceDebug && consoleEnabled) {
			String logLevel = config.getLogLevel();
			if(logLevel != null && !logLevel.isEmpty()) {
				log.info("Set log level: "+logLevel.toString());
				gcLogger.setLevel("console", logLevel);
			}
		}
//		gcLogger.setLevel("file",    logLevel);

		// command listener
		pluginManager.registerCommandListener(new ServerCommands());
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
			config.getZones(zones);
			log.info("Loaded [ "+Integer.toString(zones.size())+" ] zones.");
		}

		// load scheduler (paused)
		scheduler = gcSchedulerManager.getScheduler("gcServer");
		// load ticker
		ticker = new gcTicker();

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
//		socket = new pxnSocketServer(config.getListenPort(), new gcSocketProcessorFactory() );
		socket = new pxnSocketServer(config.getListenPort(), new pxnSocketProcessorFactory(){
			@Override
			public gcSocketProcessor newProcessor() {
				return new gcSocketProcessor();
			}
		});

		// start schedulers
		log.info("Starting schedulers..");
		gcSchedulerManager.StartAll();

//TODO: remove this
//log.severe("Listing Com Ports:");
//for(Map.Entry<String, String> entry : Serial.listPorts().entrySet())
//log.severe(entry.getKey()+" - "+entry.getValue());
		log.printRaw("[[ GC Server Running! ]]");
	}


	// stop server
	public void Shutdown() {
		// queue shutdown
		Main.getMainThread().addQueue(new Thread() {
			@Override
			public void run() {
				doShutdown();
			}
		});
	}
	private void doShutdown() {
//TODO: display total time running
		stopping = true;
		log.printRaw("[[ Stopping GC Server ]]");
		log.warning("Stopping GC Server..");
		// close socket listener
		socket.stop();
		// pause scheduler
		gcSchedulerManager.StopAll();
		// shutdown event
//TODO: trigger shutdown event here
		// sleep
		log.debug("Waiting 200ms..");
		pxnUtils.Sleep(200L);
		// close sockets
		socket.forceCloseAll();
		// plugins
		pluginManager.UnloadPlugins();
		// end schedulers
		gcSchedulerManager.ShutdownAll();
		// sleep
		log.debug("Waiting 200ms..");
		pxnUtils.Sleep(200L);
		// loggers
		consoleInputThread.interrupt(); // doesn't do much of anything
		AnsiConsole.systemUninstall();

// display threads still running
log.severe("Threads still running:");
Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
for(Thread t : threadSet)
	log.printRaw(t.getName());

		// queue exit
		Main.getMainThread().exit();
	}
	// reload server
	public void Reload() {
		Thread reloadThread = new Thread() {
			@Override
			public void run() {
//TODO:
			}
		};
		reloadThread.start();
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
				String line = gcLogger.readLine();
				if(line == null) break;
				if(line.isEmpty()) continue;
				processCommand(line);
			} catch(Exception e) {
				log.exception(e);
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
//		String commandStr;
//		String[] args;
//		// get args list
//		if(line.contains(" ")) {
//			int index = line.indexOf(" ");
//			commandStr = line.substring(0, index);
//			List<String> argsList = new ArrayList<String>();
//			for(String arg : line.substring(index+1).split(" "))
//				if(!arg.isEmpty())
//					argsList.add(arg);
//			args = (String[]) argsList.toArray(new String[argsList.size()]);
//			argsList = null;
//		} else {
//			commandStr = new String(line);
//			args = new String[0];
//		}
		// trigger event
		if(pluginManager.triggerEvent(new gcServerEventCommand(line)))
			return;
		// command not found
//		for(String arg : args) commandStr += " "+arg;
//		log.warning("Command not processed: "+commandStr);
		log.warning("Unknown command: "+line);
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
		return pxnUtils.getCurrentMillis() - getStartTime();
	}
	public String getUptimeString() {
		return Long.toString(getUptime());
	}


	// get main logger
	public gcLogger getLogger() {
		return log;
	}
	// get plugin manager
	public gcServerPluginManager getPluginManager() {
		return pluginManager;
	}


	// schedulers
	public gcSchedulerManager getScheduler() {
		return scheduler;
	}
	public gcTicker getTicker() {
		return ticker;
	}
	public pxnClock getClock() {
		return clock;
	}


	// get zones
	public List<String> getZones() {
		return zones;
	}


}
