package com.growcontrol.gcClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcClient.socketClient.gcSocketProcessor;
import com.growcontrol.gcClient.socketClient.sendClientPackets;
import com.growcontrol.gcCommon.pxnApp;
import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnScheduler.pxnScheduler;
import com.growcontrol.gcCommon.pxnScheduler.pxnTicker;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketClient;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


public class gcClient extends pxnApp {
	public static final String appName = "gcClient";
	public static final String version = "3.0.5";
	public static final String defaultPrompt = "";

	protected static gcClient client = null;
	private static ClientListeners listeners;

//	// client plugin manager
//	private final gcClientPluginManager pluginManager = new gcClientPluginManager();

	// client socket
	private pxnSocketClient socket = null;
//	// client connection state
//	private final gcConnectState state = new gcConnectState();

	// zones
	private List<String> zones = new ArrayList<String>();

//	// plugins
//	private List<String> plugins = new ArrayList<String>();

//	// frame handlers (windows)
//	private LoginHandler loginWindow = null;
//	private DashboardHandler dashboardWindow = null;


	public gcClient() {
	}
	public static gcClient get() {
		return client;
	}


	public void Connect(String host, int port, String user, String pass) {
		pxnThreadQueue.addToMain("connecting",
			new doConnect(host, port, user, pass));
	}
	private class doConnect implements Runnable {
		public pxnSocketClient socket = null;
		private final String host;
		private final int    port;
@SuppressWarnings("unused")
		private final String user;
@SuppressWarnings("unused")
		private final String pass;
		public doConnect(String host, int port, String user, String pass) {
			if(host == null || host.isEmpty())
				host = "127.0.0.1";
			if(port < 1) port = 1142;
			if(user == null || user.isEmpty()) user = null;
			if(pass == null || pass.isEmpty()) pass = null;
			this.host = host;
			this.port = pxnUtils.MinMax(port, 1, 65535);
			this.user = user;
			this.pass = pass;
		}
		@Override
		public synchronized void run() {
			// connect to server
			try {
				pxnLogger.get().info("connecting..");
				socket = new pxnSocketClient(this.host, this.port, new gcSocketProcessor());
//				Main.getClient().setSocket(socket);
				// send HELLO packet
				try {
					sendClientPackets.sendHELLO(
							socket.getProcessor(),
							gcClient.version);
//							connectInfo.username,
//							connectInfo.password);
				} catch (Exception e) {
					pxnLogger.get().exception(e);
				}
			} catch (SocketTimeoutException ignore) {
				// connection timeout
				pxnLogger.get().warning("connection timeout!");
				return;
			} catch (ConnectException ignore) {
				// socket closed
				pxnLogger.get().warning("socket closed!");
				return;
			} catch (UnknownHostException ignore) {
				// unknown hostname
				pxnLogger.get().warning("unknown host!");
				return;
			} catch (IOException e) {
				pxnLogger.get().exception(e);
			}
pxnLogger.get().severe("CONNECTED!!!!!!!!!!!!!!!!!!!");
		}
//		Start();
	}


	// init client
	@Override
	public void Start() {
		super.Start();
		pxnLogger log = pxnLogger.get();
if(!consoleEnabled) {
System.out.println("Console input is disabled due to noconsole command argument.");
//TODO: currently no way to stop the server with no console input
System.exit(0);
}

		log.info("GrowControl "+version+" Client is starting..");
		// load configs
		ClientConfig config = ClientConfig.get(configsPath);
		if(config==null || config.config==null) {
			log.severe("Failed to load config.yml, exiting..");
			System.exit(1);
			return;
		}
		// set log level
		setLogLevel(config.LogLevel());
		// init listeners
		listeners = new ClientListeners();
		// start console input thread
		StartConsole();

		// load scheduler
		pxnScheduler.get("gcClient").start();
		// load ticker
		pxnTicker.get();

//		// load plugins
//		try {
//			pluginManager.LoadPlugins();
//			pluginManager.EnablePlugins();
//		} catch (Exception e) {
//			log.exception(e);
//			Shutdown();
//			return;
//		}

		// start gui manager
		guiManager.get();







		// load plugins
//		try {
//			pluginManager.LoadPlugins();
//			pluginManager.EnablePlugins();
//		} catch (Exception e) {
//			log.exception(e);
//			Shutdown();
//			return;
//e.printStackTrace();
//System.exit(1);
//		}

		// show connect window
//		state.setStateClosed();
//		// connect to server
//		conn = new connection("192.168.3.3", 1142);
//		conn.sendPacket(clientPacket.sendHELLO(version, "lorenzo", "pass"));

		log.printRaw("[[ GC Client Running! ]]");
	}


	@Override
	protected void doShutdown(int step) {
		switch(step) {
		// first step (announce)
		case 10:
			pxnLogger.get().info("Stopping GC Client..");
			break;
		case 9:
			// pause scheduler
			pxnScheduler.PauseAll();
			// close client socket
			if(socket != null)
				socket.stop();
			// pause scheduler
			pxnScheduler.PauseAll();
			break;
		case 8:
			// close windows
			guiManager.Shutdown();
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
//			pluginManager.UnloadPlugins();
			break;
		case 3:
			// close sockets
			if(socket != null)
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


	public static ClientListeners getListeners() {
		return listeners;
	}


//	// get plugin manager
//	public gcClientPluginManager getPluginManager() {
//		return pluginManager;
//	}


	// get zones
	public List<String> getZones() {
		synchronized(zones) {
			return new ArrayList<String>(zones);
		}
	}


}
