package com.growcontrol.gcCommon.pxnSocket;

import java.util.concurrent.BlockingQueue;

import com.growcontrol.gcCommon.pxnParser.pxnParser;


public interface pxnSocketProcessor {

	// in queue
	public void processData(String line) throws Exception;
	public void processNow(pxnSocketProcessor processor, pxnParser line);
	// out queue
	public void sendData(String line) throws Exception;

	// queues
	public BlockingQueue<String> getInputQueue();
	public BlockingQueue<String> getOutputQueue();

	public void interrupt();

}
