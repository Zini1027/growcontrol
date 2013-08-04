package com.growcontrol.gcCommon;

import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gcCommon.pxnParser.pxnParser;


public abstract class pxnMain {
	protected static pxnMain instance = null;

	// new app instance
	protected abstract pxnApp getAppInstance();
	protected abstract void StartMain(pxnParser args);

	// command line arguments
	protected static volatile String argsMsg = "";


	protected static void Init(pxnMain main, String[] args) {
		if(main == null) throw new NullPointerException("main cannot be null!");
		setInstance(main);
		// init logger
		pxnLog.get();
boolean b = true; if(b) return;
		// start app main
		instance.StartMain(
			new pxnParser(
				pxnUtils.addStringSet("", args, " ")
			)
		);
	}
	private static synchronized void setInstance(pxnMain main) {
		// already running?
		if(instance != null) {
			//throw new UnsupportedOperationException("cannot redefine singleton instance of this app; already running!");
			System.out.println("Program already started?");
			System.exit(1);
		}
		instance = main;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}


	protected void addArgsMsg(String text) {
		if(text == null || text.isEmpty()) return;
		pxnUtils.addStringSet(argsMsg, text.replace(" ", "_"), " ");
	}
	public static String getArgsMsg() {
		return argsMsg;
	}


}
