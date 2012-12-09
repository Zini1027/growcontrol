package com.poixson.pxnSocket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.poixson.pxnLogger.pxnLogger;


public abstract class pxnSocketProcessorThreaded implements pxnSocketProcessor {

	// input/output queues
	protected BlockingQueue<String> queueIn;
	protected BlockingQueue<String> queueOut;

	// processing thread
	protected final Thread procThread;


	public pxnSocketProcessorThreaded() {
		// default size queues
		this(1, 1);
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
				processLine( queueIn.take() );
			} catch (InterruptedException e) {
				pxnLogger.log().exception(e);
			}
			// processed count
			if(queueIn.isEmpty()) {
				if(count != 0) {
					pxnLogger.log().debug("Processed [ "+Integer.toString(count)+" ] packets");
					count = 0;
				}
			} else {
				count++;
			}
		}
	}


	// add packet to queue
	@Override
	public void process(String line) {
		if(line == null) return;
		line = line.trim();
//		queueIn.offer(line);
//offer waits, add throws an exception - if full
//		queueIn.add(line);
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
