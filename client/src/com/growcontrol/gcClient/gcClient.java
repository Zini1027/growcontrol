package com.growcontrol.gcClient;

import java.io.File;

import javax.swing.ImageIcon;

import com.growcontrol.gcClient.ConnectState.gcConnectState;
import com.growcontrol.gcClient.clientPlugin.gcClientPluginManager;
import com.growcontrol.gcClient.frames.DashboardHandler;
import com.growcontrol.gcClient.frames.LoginHandler;
import com.poixson.pxnSocket.pxnSocketClient;


public class gcClient {
	public static final String version = "3.0.3";
	public static gcClient client = null;
	private static boolean stopping = false;
	public static pxnSocketClient socket = null;

	// client plugin manager
	public static final gcClientPluginManager pluginManager = new gcClientPluginManager();

	// client connection state
	protected static final gcConnectState state = new gcConnectState();

	// frame handlers (windows)
	protected LoginHandler loginWindow = null;
	protected DashboardHandler dashboardWindow = null;


	public static void main(String[] args) {
		if(client != null) throw new UnsupportedOperationException("Cannot redefine singleton gcClient; already running");
//		pluginManager.setMainClassYmlName("Client Main");
		// process args
		for(String arg : args) {
			// version
			if(arg.equalsIgnoreCase("version")) {
				System.out.println("GrowControl "+version+" Client");
				System.exit(0);
			}
		}
//			// debug mode
//			} else if(arg.equalsIgnoreCase("debug")) {
//				gcLogger.setLevel("console", pxnLevel.LEVEL.DEBUG);
//				gcLogger.setLevel("file",    pxnLevel.LEVEL.DEBUG);
//			// configs path
//			} else if(arg.startsWith("configspath=")) {
//				configsPath = arg.substring(12);
//				log.debug("Set configs path to: "+configsPath);
//			// plugins path
//			} else if(arg.startsWith("pluginspath=")) {
//				pluginManager.setPath(arg.substring(12));
//				log.debug("Set plugins path to: "+pluginManager.getPath());
//			}
		// start gc client gui
		client = new gcClient();
	}


	// wait for connection state change
	public gcClient() {
		// start jline console
		pluginManager.registerCommandListener(new ClientCommands());

		// load plugins
		try {
			pluginManager.LoadPlugins();
			pluginManager.EnablePlugins();
		} catch (Exception e) {
//			log.exception(e);
//			Shutdown();
e.printStackTrace();
System.exit(1);
			return;
		}

		// show connect window
		state.setStateClosed();
//		// connect to server
//		conn = new connection("192.168.3.3", 1142);
//		conn.sendPacket(clientPacket.sendHELLO(version, "lorenzo", "pass"));
	}


	public static boolean isStopping() {
		return stopping;
	}


	// get connect state manager
	public static gcConnectState getConnectState() {
		return state;
	}


	// load image file/resource
	public static ImageIcon loadImageResource(String path) {
		ImageIcon image = null;
		File file = new File(path);
		// open file
		if(file.exists()) {
			try {
				image = new ImageIcon(path);
			} catch(Exception ignore) {}
		}
		// open resource
		if(image == null) {
			try {
				image = new ImageIcon(client.getClass().getResource(path));
			} catch(Exception ignore) {}
		}
		return image;
	}


}
