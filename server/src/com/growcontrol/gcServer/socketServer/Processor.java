package com.growcontrol.gcServer.socketServer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.growcontrol.gcServer.gcServer;


public class Processor extends Thread {

	protected BlockingQueue<String> queueIn;
	protected BlockingQueue<String> queueOut;


	public Processor() {
		this(1, 1);
	}
	public Processor(int inSize, int outSize) {
		this(
			new ArrayBlockingQueue<String>(inSize,  true),
			new ArrayBlockingQueue<String>(outSize, true)
		);
	}
	public Processor(BlockingQueue<String> queueIn, BlockingQueue<String> queueOut) {
//TODO: else here?
		if(queueIn  != null) this.queueIn  = queueIn;
		if(queueOut != null) this.queueOut = queueOut;
		this.start();
	}


	// process queue thread (incoming)
	public void run() {
		int count = 0;
		while(true) {
			// consume queue
			try {
				String line = queueIn.take();
				processLine(line);
			} catch (InterruptedException e) {
				gcServer.log.exception(e);
			}
			// processed count
			if(queueIn.isEmpty()) {
				if(count != 0) {
					gcServer.log.debug("Processed [ "+Integer.toString(count)+" ] packets");
					count = 0;
				}
			} else {
				count++;
			}
		}
	}
	public void processLine(String line) {
System.out.println("PROCESSING: "+line);
	}


	// add packet to queue
	public void add(String line) {
		try {
			queueIn.offer(line, 1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			gcServer.log.exception(e);
		}
	}


	// packet queue to transmit
	public BlockingQueue<String> getQueueOut() {
		return queueOut;
	}


}
