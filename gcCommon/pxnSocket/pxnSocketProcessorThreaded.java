package com.growcontrol.gcCommon.pxnSocket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.growcontrol.gcCommon.pxnParser.pxnParser;


public abstract class pxnSocketProcessorThreaded extends Thread implements pxnSocketProcessor {

	// input/output queues
	protected BlockingQueue<String> queueIn;
	protected BlockingQueue<String> queueOut;


	public pxnSocketProcessorThreaded() {
		// default size queues
		this(100, 100);
	}
	public pxnSocketProcessorThreaded(int sizeIn, int sizeOut) {
		// create queues
		this(
			new ArrayBlockingQueue<String>(sizeIn,  true),
			new ArrayBlockingQueue<String>(sizeOut, true)
		);
	}
	public pxnSocketProcessorThreaded(BlockingQueue<String> queueIn, BlockingQueue<String> queueOut) {
		// queues
		this.queueIn  = queueIn;
		this.queueOut = queueOut;
		// processing thread
		this.setName( "Socket-Processor-"+Integer.toString(pxnSocketWorker.getNextId()) );
		// start processing thread
		this.start();
	}


	// processing thread
	@Override
	public void run() {
		int count = 0;
		while(true) {
			// consume queue
			try {
				// submit packet for processing
				String line = queueIn.take();
				processNow(this, new pxnParser(line));
			} catch (InterruptedException ignore) {
				break;
			}
			// processed count
			count++;
			if(queueIn.size() == 0 && count > 0) {
System.out.println("Processed [ "+Integer.toString(count)+" ] packets");
//TODO: debug isn't working for some reason
//				pxnLogger.log().debug("Processed [ "+Integer.toString(count)+" ] packets");
				count = 0;
			}
		}
	}


	// add to queue
	@Override
	public void processData(String line) throws Exception {
		if(line == null) return;
		queueIn.offer(line.trim(), 1, TimeUnit.SECONDS);
//if full:
// .offer waits
// .add throws an exception
	}
	@Override
	public void sendData(String line) throws Exception {
		if(line == null) return;
		queueOut.offer(line.trim(), 1, TimeUnit.SECONDS);
	}


	// get input/processing queue
	@Override
	public BlockingQueue<String> getInputQueue() {
		return queueIn;
	}
	// get output/sending queue
	@Override
	public BlockingQueue<String> getOutputQueue() {
		return queueOut;
	}


}
