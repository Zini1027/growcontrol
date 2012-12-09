package com.poixson.pxnSocket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public abstract class pxnSocketProcessorBlocking implements pxnSocketProcessor {

	// output queue
	protected BlockingQueue<String> queueOut;


	public pxnSocketProcessorBlocking() {
		// default size queue
		this(1);
	}
	public pxnSocketProcessorBlocking(int sizeOut) {
		// create queue
		this(
			new ArrayBlockingQueue<String>(sizeOut, true)
		);
	}
	public pxnSocketProcessorBlocking(BlockingQueue<String> queueOut) {
		// queue
		this.queueOut = queueOut;
	}


	// submit packet for processing
	@Override
	public void process(String line) {
		processLine(line);
	}


	// get input/processing queue
	@Override
	public BlockingQueue<String> getInputQueue() {
		return null;
	}
	// get output/sending queue
	@Override
	public BlockingQueue<String> getOutputQueue() {
		return queueOut;
	}


}
