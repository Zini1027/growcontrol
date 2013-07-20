package com.growcontrol.gcClient;

import java.io.File;

import javax.swing.ImageIcon;

import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcCommon.pxnLogger.pxnLevel;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnLogger.pxnLoggerConsole;


public class Main {

	// client instance
	private static gcClient client = null;

//	// logger
//	private static final gcLogger log = gcLogger.getLogger();

//	// runtime args
//	private static boolean consoleEnabled = true;


	// app startup
	public static void main(String[] args) {
		AnsiConsole.systemInstall();
		System.out.println();
		if(client != null) throw new UnsupportedOperationException("Cannot redefine singleton gcClient; already running");
		pxnLogger.addLogHandler(
			"console",
			new pxnLoggerConsole(pxnLogger.getReader(),
			new pxnLevel(pxnLevel.LEVEL.DEBUG))
		);

//		pxnLogger.addLogHandler(
//			"console",
//			new pxnLoggerConsole(pxnLogger.getReader(),
//				new pxnLevel(pxnLevel.LEVEL.DEBUG)) );
		client = new gcClient();
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
		System.out.flush();
		// start client gui
		client.Start();
	}


	// shutdown client
	public static void Shutdown() {
		if(gcClient.client == null) {
			// stop gui manager only
			guiManager.Shutdown();
			System.exit(0);
		} else {
			// stop client instance
			getClient().Shutdown();
		}
	}
	public static void HideGUI() {
//		client.HideGUI();
	}


	// get client
	public static gcClient getClient() {
		return client;
	}


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
				image = new ImageIcon(client.getClass().getResource(path));
			} catch(Exception ignore) {}
		}
		return image;
	}


}
