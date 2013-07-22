package com.growcontrol.gcClient;

import java.io.File;

import javax.swing.ImageIcon;

import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcCommon.pxnLogger.pxnLevel;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnLogger.pxnLoggerConsole;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


public class Main {

//	// command line arguments
//	private static String argsMsgStr = "";


	// app startup
	public static void main(String[] args) {
		AnsiConsole.systemInstall();
		System.out.println();
		if(gcClient.client != null) throw new UnsupportedOperationException("Cannot redefine singleton gcClient; already running");
		pxnLogger.addLogHandler(
			"console",
			new pxnLoggerConsole(pxnLogger.getReader(),
			new pxnLevel(pxnLevel.LEVEL.DEBUG))
		);

//		pxnLogger.addLogHandler(
//			"console",
//			new pxnLoggerConsole(pxnLogger.getReader(),
//				new pxnLevel(pxnLevel.LEVEL.DEBUG)) );
		gcClient.client = new gcClient();
//		pluginManager.setMainClassYmlName("Client Main");
		// process args
		for(int i=0; i<args.length; i++) {
			String arg = args[i];
			// version
			if(arg.equalsIgnoreCase("--version")) {
				System.out.println("GrowControl "+gcClient.version+" Client");
				System.exit(0);
			}
//			// debug mode
//			// configs path
//			// plugins path
		}
//		// build argsMsgStr
//		argsMsgStr = "";
//		if(argsMsgList.size() > 0) {
//			for(String argStr : argsMsgList) {
//				if(argStr == null || argStr.isEmpty()) continue;
//				if(!argsMsgStr.isEmpty()) argsMsgStr += " ";
//				argsMsgStr += argStr.replace(" ", "_");
//			}
//		}
//		displayLogoHeader();
//		displayStartupVars();
		System.out.flush();
		// queue startup in main thread
		pxnThreadQueue.addToMain("client-startup", new Runnable() {
			@Override
			public void run() {
				// start client gui
				gcClient.get().Start();
			}
		});
		// start/hand-off thread to main queue
		pxnThreadQueue.getMain().run();
		// main thread ended
		pxnLogger.get().warning("Main process ended (this shouldn't happen)");
		System.out.println();
		System.out.println();
		System.exit(0);
	}


	// shutdown client
	public static void Shutdown() {
		if(gcClient.client == null) {
			// stop gui manager only
			guiManager.Shutdown();
			System.exit(0);
		} else {
			// stop client instance
			gcClient.get().Shutdown();
		}
	}
	public static void HideGUI() {
//		client.HideGUI();
	}


//	// get client
//	public static gcClient getClient() {
//		return client;
//	}


//	// get main logger
//	public static gcLogger getLogger() {
//		return log;
//	}


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
				image = new ImageIcon(gcClient.get().getClass().getResource(path));
			} catch(Exception ignore) {}
		}
		return image;
	}


}
