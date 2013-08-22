package com.growcontrol.gcCommon;

import com.growcontrol.gcCommon.meta.valueTypes.metaCommand;
import com.growcontrol.gcCommon.meta.valueTypes.metaEC;
import com.growcontrol.gcCommon.meta.valueTypes.metaIO;
import com.growcontrol.gcCommon.meta.valueTypes.metaPH;
import com.growcontrol.gcCommon.meta.valueTypes.metaThermal;
import com.growcontrol.gcCommon.meta.valueTypes.metaThermalNTC;
import com.growcontrol.gcCommon.meta.valueTypes.metaTrigger;
import com.growcontrol.gcCommon.meta.valueTypes.metaVariable;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gcCommon.pxnParser.pxnParser;


public abstract class pxnMain {
	protected static pxnMain mainInstance = null;

	// new app instance
	protected abstract pxnApp getAppInstance();
	protected abstract void StartMain();

	// command line arguments
	protected pxnParser args = null;
	protected static volatile String argsMsg = "";


	protected static void Init(pxnMain main, String[] args) {
		if(main == null) throw new NullPointerException("main cannot be null!");
		setMainInstance(main);
		// init meta types
		metaCommand.Init();
		metaEC.Init();
		metaIO.Init();
		metaPH.Init();
		metaThermal.Init();
		metaThermalNTC.Init();
		metaTrigger.Init();
		metaVariable.Init();
		// init logger
		pxnLog.get();
		try {
			// start app main
			mainInstance.args = new pxnParser( "args:- "+pxnUtils.addStringSet("", args, " ") );
			mainInstance.StartMain();
		} catch (Exception e) {
			pxnLog.get().fatal("Failed to execute StartMain()", e);
		}
	}
	private static synchronized void setMainInstance(pxnMain main) {
		// already running?
		if(mainInstance != null) {
			//throw new UnsupportedOperationException("cannot redefine singleton instance of this app; already running!");
			System.out.println("Program already started?");
			System.exit(1);
		}
		mainInstance = main;
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
