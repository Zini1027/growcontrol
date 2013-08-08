package com.growcontrol.gcCommon;

import com.growcontrol.gcCommon.pxnClock.pxnClock;
import com.growcontrol.gcCommon.pxnLogger.pxnConsole;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


public abstract class pxnApp {
	protected static pxnApp appInstance = null;

	// runtime args
	protected boolean consoleEnabled = true;
	protected boolean forceDebug = false;
	protected String configsPath = null;

	// run state
	private long startTime = -1;
	private volatile boolean stopping = false;

	public abstract String getAppName();
	public abstract String getVersion();

	protected abstract void updateLogLevel();
	public abstract void ProcessCommand(String line);
	protected abstract void doShutdown(int step);


	// app instance
	public static pxnApp get() {
		return appInstance;
	}
	protected pxnApp() {
		setAppInstance(this);
	}
	private static synchronized void setAppInstance(pxnApp app) {
		// already running?
		if(appInstance != null) {
			//throw new UnsupportedOperationException("cannot redefine singleton instance of this app; already running!");
			System.out.println("Program already started?");
			System.exit(1);
		}
		appInstance = app;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}


	public void Start() {
		Thread.currentThread().setName("Main-"+getAppName()+"-Thread");
		// single instance lock
		pxnUtils.lockInstance(getAppName()+".lock");
		pxnLog.get().Major("Starting "+getAppName()+"..");
		pxnUtils.addLibraryPath("lib");
		// query time server
		pxnClock clock = pxnClock.getBlocking();
		if(startTime == -1)
			startTime = clock.Millis();
//System.out.println(startTime);
	}
	// start console input thread
	protected void StartConsole() {
		if(consoleEnabled)
			pxnConsole.get().Start();
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
		pxnLog.get().Major("Stopping "+getAppName());
		for(int step=10; step>1; step--) {
			pxnUtils.Sleep(100);
			this.doShutdown(step);
			switch(step) {
			case 10:
				pxnLog.get().info("Waiting for things to finish..");
				break;
			case 5:
				pxnLog.get().debug("Waiting 500ms..");
				break;
			case 3:
				// stop console
				pxnConsole.Close();
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


	public void setConsoleEnabled(boolean enabled) {
		this.consoleEnabled = enabled;		
	}
	public void setForceDebug(boolean enabled) {
		this.forceDebug = enabled;
		updateLogLevel();
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


	// plugins path
	public String getPluginsPath() {
		return "plugins/";
	}
	public String getPluginPath(String pluginName) {
		return getPluginsPath()+pluginName+"/";
	}



}
