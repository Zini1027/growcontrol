package com.poixson.pxnSocket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.poixson.pxnParser.pxnParser;


public abstract class pxnSocketProcessorBlocking implements pxnSocketProcessor {

	// output queue
	protected BlockingQueue<String> queueOut;


	public pxnSocketProcessorBlocking() {
		// default size queue
		this(100);
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
	public void processData(String line) {
		processNow(this, new pxnParser(line));
	}
	@Override
	public void sendData(String line) {
		if(line == null) return;
		queueOut.offer(line.trim());
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
