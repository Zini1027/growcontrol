package com.growcontrol.gcCommon;

import java.io.IOException;

import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcCommon.pxnClock.pxnClock;
import com.growcontrol.gcCommon.pxnLogger.pxnLevel;
import com.growcontrol.gcCommon.pxnLogger.pxnLevel.LEVEL;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


public abstract class pxnApp {

	protected Thread consoleInputThread = null;

	// runtime args
	protected boolean consoleEnabled = true;
	protected boolean forceDebug = false;
	private long startTime = -1;
	private volatile boolean stopping = false;
	protected String configsPath = null;


	public abstract String getAppName();
	public abstract String getVersion();

	protected abstract void processCommand(String line);
	protected abstract void doShutdown(int step);


	public void Start() {
		Thread.currentThread().setName("Main-"+getAppName()+"-Thread");
		// single instance lock
		pxnUtils.lockInstance(getAppName()+".lock");
		pxnLogger.get().printMajor("Starting "+getAppName());
		pxnUtils.addLibraryPath("lib");
		// query time server
		pxnClock clock = pxnClock.getBlocking();
		if(startTime == -1)
			startTime = clock.Millis();
System.out.println(startTime);
	}
	// start console input thread
	protected void StartConsole() {
		// start console input thread
		if(!consoleEnabled) return;
		// new thread
		if(consoleInputThread == null) {
			consoleInputThread = new Thread("ConsoleInput") {
				@Override
				public void run() {
					ConsoleInputThread();
				}
			};
		}
		// start thread
		if(!consoleInputThread.isAlive())
			consoleInputThread.start();
	}
	protected void ConsoleInputThread() {
		if(!consoleEnabled) return;
//TODO: password login
// If we input the special word then we will mask
// the next line.
//if ((trigger != null) && (line.compareTo(trigger) == 0))
//	line = reader.readLine("password> ", mask);
//if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) break;
		// console input loop
		while(!stopping) {
			if(consoleInputThread.isInterrupted()) break;
			// wait for commands
			String line;
			try {
				line = pxnLogger.readLine();
			} catch (IOException e) {
				pxnLogger.get().exception(e);
				pxnUtils.Sleep(200);
				continue;
			}
			if(line == null) break;
			if(!line.isEmpty())
				processCommand(line);
		}
		System.out.println();
		System.out.println();
	}


	// exit app
	public void Shutdown() {
		// queue shutdown
		pxnThreadQueue.addToMain("BeginShutdown", new Runnable() {
			@Override
			public void run() {
				ShutdownThread();
				pxnThreadQueue.getMain().Exit();
			}
		});
	}
	protected void ShutdownThread() {
		stopping = true;
		pxnLogger.get().printMajor("Stopping "+getAppName());
		for(int step=10; step>1; step--) {
			pxnUtils.Sleep(100);
			this.doShutdown(step);
			switch(step) {
			case 10:
				pxnLogger.get().info("Waiting for things to finish..");
				break;
			case 5:
				pxnLogger.get().debug("Waiting 500ms..");
				break;
			case 3:
				// stop console
				consoleInputThread.interrupt(); // doesn't do much of anything
				AnsiConsole.systemUninstall();
				break;
			}
		}
	}


	public boolean isStopping() {
		return stopping;
	}
	public long getStartTime() {
		return startTime;
	}
	public long getUptime() {
		return pxnClock.get().Millis() - getStartTime();
	}


	// log level
	public void setLogLevel(String levelStr) {
		if(forceDebug)
			levelStr = "debug";
		if(levelStr != null && !levelStr.isEmpty()) {
			LEVEL level = pxnLevel.levelFromString(levelStr);
			pxnLogger.setLevel("console", level);
//			pxnLogger.get().print(level, "Set log level: "+level.toString());
		}
	}


	public void setConsoleEnabled(boolean enabled) {
		this.consoleEnabled = enabled;		
	}
	public void setForceDebug(boolean enabled) {
		this.forceDebug = enabled;
		pxnLogger.setForceDebug("console", enabled);
	}
	public void setConfigsPath(String path) {
		this.configsPath = path;
	}
	public boolean consoleEnabled() {
		return this.consoleEnabled;
	}
	public boolean forceDebug() {
		return this.forceDebug;
	}


}
