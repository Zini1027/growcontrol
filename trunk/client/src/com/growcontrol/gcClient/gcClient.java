package com.growcontrol.gcClient;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcClient.clientPlugin.gcClientPluginManager;
import com.growcontrol.gcClient.clientSocket.gcPacketReader;
import com.growcontrol.gcClient.clientSocket.gcPacketSender;
import com.growcontrol.gcCommon.pxnApp;
import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnScheduler.pxnScheduler;
import com.growcontrol.gcCommon.pxnScheduler.pxnTicker;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketClient;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketUtils.pxnSocketState;
import com.growcontrol.gcCommon.pxnSocket.processor.pxnSocketProcessorFactory;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


public class gcClient extends pxnApp {
	public static final String appName = "gcClient";
	public static final String version = "3.0.5";
	public static final String defaultPrompt = "";

	protected static gcClient client = null;

	// client socket
	private pxnSocketClient socket = null;
//	// client connection state
//	private final gcConnectState state = new gcConnectState();

	// zones
	private List<String> zones = new ArrayList<String>();


	public gcClient() {
	}
	public static gcClient get() {
		return client;
	}


	public void Connect(String host, int port, String user, String pass) {
		pxnThreadQueue.addToMain("SocketConnect",
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
		// connect to server
		@Override
		public synchronized void run() {
pxnLogger.get().info("Connecting..");
			// create socket
			if(socket == null)
				socket = new pxnSocketClient();
			socket.setHost(this.host);
			socket.setPort(this.port);
			// create processor
			socket.setFactory(new pxnSocketProcessorFactory() {
				@Override
				public gcPacketReader newProcessor() {
					return new gcPacketReader();
				}
			});
			socket.Start();
			if(!pxnSocketState.CONNECTED.equals(socket.getState())) {
				pxnLogger.get().warning("Failed to connect!");
				return;
			}
			// send HELLO packet
			gcPacketSender.sendHELLO(
				socket.getWorker(),
				gcClient.version);
//				connectInfo.username,
//				connectInfo.password);
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
		ClientListeners.get();
		// start console input thread
		StartConsole();

		// load scheduler
		pxnScheduler.get(getAppName()).start();
		// load ticker
		pxnTicker.get();

		// load plugins
		try {
			gcClientPluginManager pluginManager = gcClientPluginManager.get("plugins/");
			pluginManager.LoadPluginsDir();
			pluginManager.InitPlugins();
			pluginManager.EnablePlugins();
		} catch (Exception e) {
			log.exception(e);
			Shutdown();
			return;
		}

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
			// close socket
			if(socket != null)
				socket.Close();
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
			// stop plugins
			{
				gcClientPluginManager manager = gcClientPluginManager.get();
				if(manager != null)
					manager.DisablePlugins();
			}
			// end schedulers
			pxnScheduler.ShutdownAll();
			break;
		case 4:
			// unload plugins
			{
				gcClientPluginManager manager = gcClientPluginManager.get();
				if(manager != null)
					manager.UnloadPlugins();
			}
			break;
		case 3:
			// close sockets
			if(socket != null)
				socket.ForceClose();
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
		if(!ClientListeners.get().triggerCommand(line)) {
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


	// get zones
	public List<String> getZones() {
		synchronized(zones) {
			return new ArrayList<String>(zones);
		}
	}
	public String[] getZonesArray() {
		synchronized(zones) {
			return (String[]) zones.toArray();
		}
	}


}
