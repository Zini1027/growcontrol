package com.poixson.pxnSocket;

import java.util.concurrent.BlockingQueue;


public interface pxnSocketProcessor {

	// add to queue
	public void process(String line);
	// process now
	public void processLine(String line);

	// queues
	public BlockingQueue<String> getInputQueue();
	public BlockingQueue<String> getOutputQueue();

}
