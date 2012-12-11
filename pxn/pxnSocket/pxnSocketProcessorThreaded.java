package com.poixson.pxnSocket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.poixson.pxnLogger.pxnLogger;
import com.poixson.pxnParser.pxnParser;


public abstract class pxnSocketProcessorThreaded implements pxnSocketProcessor {

//TODO: queues should be moved to worker
	// input/output queues
	protected BlockingQueue<String> queueIn;
	protected BlockingQueue<String> queueOut;

	// processing thread
	protected final Thread procThread;


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
		procThread = new Thread() {
			@Override
			public void run() {
				doProcessorThread();
			}
		};
		procThread.setName("Socket Processor");
		// start processing thread
		procThread.start();
	}


	// processing thread
	private void doProcessorThread() {
		int count = 0;
		while(true) {
			// consume queue
			try {
				// submit packet for processing
				String line = queueIn.take();
				processNow(new pxnParser(line));
			} catch (InterruptedException e) {
				pxnLogger.log().exception(e);
			}
			// processed count
			count++;
System.out.println(Integer.toString(queueIn.size()));
			if(queueIn.size() == 0) {
				if(count != 0) {
					pxnLogger.log().debug("Processed [ "+Integer.toString(count)+" ] packets");
					count = 0;
				}
			}
		}
	}


	// add to queue
	@Override
	public void processData(String line) {
		if(line == null) return;
		queueIn.offer(line.trim());
//if full:
// .offer waits
// .add throws an exception
	}
	@Override
	public void sendData(String line) {
		if(line == null) return;
		queueOut.offer(line.trim());
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
