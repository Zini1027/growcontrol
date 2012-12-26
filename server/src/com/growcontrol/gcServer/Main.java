package com.growcontrol.gcServer;

import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcServer.logger.gcLogger;
import com.poixson.pxnLogger.pxnLevel;
import com.poixson.pxnLogger.pxnLogger;
import com.poixson.pxnLogger.pxnLoggerConsole;
import com.poixson.pxnThreadQueue.pxnThreadQueue;


public class Main {

	// server instance
	private static gcServer server = null;
	private static pxnThreadQueue mainThread = new pxnThreadQueue();

	// logger
	private static final gcLogger log = gcLogger.getLogger();
	protected static boolean forceDebug = false;

	// runtime args
	private static boolean consoleEnabled = true;


	// app startup
	public static void main(String[] args) {
		AnsiConsole.systemInstall();
		Thread.currentThread().setName("Main-Server-Thread");
		System.out.println();
		if(server != null) throw new UnsupportedOperationException("Cannot redefine singleton gcServer; already running");
		pxnLogger.addLogHandler(
			"console",
			new pxnLoggerConsole(pxnLogger.getReader(),
				new pxnLevel(pxnLevel.LEVEL.DEBUG)) );
		server = new gcServer();
//		pluginManager.setMainClassYmlName("Server Main");
//doesn't log anything
//try {
//	pxnLogger.getReader().setDebug(new PrintWriter(new FileWriter("log.txt", true)));
//} catch (IOException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
//		pxnLogger.addLogHandler(
//			"file",
//			new pxnLoggerFile(
//				new pxnLevel(pxnLevel.LEVEL.DEBUG)) );
		// process args
		for(int i=0; i<args.length; i++) {
			String arg = args[i];
			// version
			if(arg.equalsIgnoreCase("--version")) {
//				System.out.println("GrowControl "+gcServer.version+" Server");
				System.exit(0);
			// no console
			} else if(arg.equalsIgnoreCase("--no-console")) {
				consoleEnabled = false;
			// debug mode
			} else if(arg.equalsIgnoreCase("--debug")) {
				forceDebug = true;
				gcLogger.setForceDebug("console", true);
//				forceDebug = true;
//				try {
//					gcLogger.setLevel("console", pxnLevel.LEVEL.DEBUG);
//				} catch (NullPointerException ignore) {
//ignore.printStackTrace();
//				}
//				try {
//					gcLogger.setLevel("file",    pxnLevel.LEVEL.DEBUG);
//				} catch (NullPointerException ignore) {
//ignore.printStackTrace();
//				}
			// configs path
			} else if(arg.equalsIgnoreCase("--configs-path")) {
				i++; if(i <= args.length) {
					System.out.println("Incomplete --configs-path argument!");
					break;
				}
				getServer().configsPath = args[i];
				System.out.println("Set configs path to: "+getServer().configsPath);
			// plugins path
			} else if(arg.equalsIgnoreCase("--plugins-path")) {
				i++; if(i <= args.length) {
					System.out.println("Incomplete --plugins-path argument!");
					break;
				}
				getServer().getPluginManager().setPath(args[i]);
				System.out.println("Set plugins path to: "+getServer().getPluginManager().getPath());
			} else {
				System.out.println("Unknown argument: "+arg);
			}
		}
		System.out.flush();
		// queue startup in main thread
		mainThread.addQueue(new Runnable() {
			@Override
			public void run() {
				// start server
				getServer().Start();
			}
		});
		// hand-off thread to queue
		mainThread.run();
	}


	// get server
	public static gcServer getServer() {
		return server;
	}
	public static pxnThreadQueue getMainThread() {
		return mainThread;
	}
	// shutdown server
	public static void Shutdown() {
		getServer().Shutdown();
	}


	// is console input enabled
	public static boolean isConsoleEnabled() {
		return consoleEnabled;
	}


	// get main logger
	public static gcLogger getLogger() {
		return log;
	}


}
