package com.growcontrol.gcClient;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcClient.ConnectState.gcConnectState;
import com.growcontrol.gcClient.clientPlugin.gcClientPluginManager;
import com.gcCommon.pxnUtils;
import com.gcCommon.pxnParser.pxnParser;
import com.gcCommon.pxnSocket.pxnSocketClient;


public class gcClient {
	public static final String version = "3.0.3";
//	public static final String defaultPrompt = ">";

//	private boolean stopping = false;

//	// logger
//	private final gcLogger log;
//	private Thread consoleInputThread = null;

	// client plugin manager
	private final gcClientPluginManager pluginManager = new gcClientPluginManager();

//	// config files
//	private ClientConfig config = null;
//	protected String configsPath = null;

//	// clock
//	private pxnClock clock = null;

	// client socket
	private pxnSocketClient socket = null;
	// client connection state
	private final gcConnectState state = new gcConnectState();

	// zones
	private List<String> zones = new ArrayList<String>();

	// plugins
	private List<String> plugins = new ArrayList<String>();

//	// frame handlers (windows)
//	private LoginHandler loginWindow = null;
//	private DashboardHandler dashboardWindow = null;


	// wait for connection state change
	public gcClient() {
//		log = Main.getLogger();
	}


	// init client gui
	public void Start() {
		// single instance lock
		pxnUtils.lockInstance("gcClient.lock");
//		if(!Main.isConsoleEnabled()) {
//			System.out.println("Console input is disabled due to noconsole command argument.");
//		} else {
//			AnsiConsole.systemInstall();
//			ASCIIHeader();
//		}
//		log.printRaw("[[ Starting GC Client ]]");
//		log.info("GrowControl "+version+" Client is starting..");
		pxnUtils.addLibraryPath("lib");

//		// load configs
//		config = new ClientConfig(configsPath);
//		if(config==null || config.config==null) {
//			log.severe("Failed to load config.yml");
//			System.exit(1);
//		}

//		// set log level
//		String logLevel = config.getLogLevel();
//		if(logLevel != null && !logLevel.isEmpty()) {
//			if(Main.isConsoleEnabled())
//				gcLogger.setLevel("console", logLevel);
//			gcLogger.setLevel("file",    logLevel);
//		}

		// command listener
		pluginManager.registerCommandListener(new ClientCommands());
//		// start console input thread
//		if(Main.isConsoleEnabled()) {
//			consoleInputThread = new Thread() {
//				@Override
//				public void run() {
//					StartConsole();
//				}
//			};
//			consoleInputThread.start();
//		}

//		// query time server
//		if(clock == null)
//			clock = pxnUtils.getClock();

//TODO: remove this - will load from server
//		// zones
//		if(zones == null) zones = new ArrayList<String>();
//		synchronized(zones) {
//			config.getZones(zones);
//			log.info("Loaded [ "+Integer.toString(zones.size())+" ] zones.");
//		}

//TODO: remove this - probably don't need it
//		// load scheduler (paused)
//		scheduler = gcSchedulerManager.getScheduler("gcClient");
//		// load ticker
//		ticker = new gcTicker();

//TODO: remove this - probably don't need it
//		// start schedulers
//		log.info("Starting schedulers..");
//		gcSchedulerManager.StartAll();

		// load plugins
		try {
			pluginManager.LoadPlugins();
			pluginManager.EnablePlugins();
		} catch (Exception e) {
//			log.exception(e);
//			Shutdown();
//			return;
e.printStackTrace();
System.exit(1);
		}

		// show connect window
		state.setStateClosed();
//		// connect to server
//		conn = new connection("192.168.3.3", 1142);
//		conn.sendPacket(clientPacket.sendHELLO(version, "lorenzo", "pass"));

//		log.printRaw("[[ GC Client Running! ]]");
	}


//	public void Shutdown() {
//	}
//	public boolean isStopping() {
//		return stopping;
//	}


//	// get main logger
//	public static gcLogger getLogger() {
//		return Main.getLogger();
//	}


	// get plugin manager
	public gcClientPluginManager getPluginManager() {
		return pluginManager;
	}


	// get client socket
	public pxnSocketClient getSocket() {
		return socket;
	}
	public void setSocket(pxnSocketClient socket) {
		if(socket == null) throw new NullPointerException("socket can't be null!");
		if(this.socket != null) {
			this.socket.close();
			this.socket = null;
		}
		this.socket = socket;
	}


	// get connect state manager
	public gcConnectState getConnectState() {
		return state;
	}


	public void addZone(pxnParser line) {
		zones.add(line.getRest());
	}
	public void addPlugin(pxnParser line) {
		plugins.add(line.getRest());
	}


}
