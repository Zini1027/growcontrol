package com.growcontrol.gcCommon.pxnSocket.processor;

import com.growcontrol.gcCommon.pxnParser.pxnParser;
import com.growcontrol.gcCommon.pxnSocket.worker.pxnSocketWorker;


public interface pxnSocketProcessor {

	public void ProcessData(pxnSocketWorker worker, pxnParser line);
	public void Closing();

}
