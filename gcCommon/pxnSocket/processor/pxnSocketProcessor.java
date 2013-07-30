package com.growcontrol.gcCommon.pxnSocket.processor;

import java.util.concurrent.BlockingQueue;

import com.growcontrol.gcCommon.pxnParser.pxnParser;
import com.growcontrol.gcCommon.pxnSocket.worker.pxnSocketWorker;


public abstract class pxnSocketProcessor {

	// input/output queues
	protected BlockingQueue<String> queueIn;
	protected BlockingQueue<String> queueOut;

//	private volatile boolean stopping = false;


	public pxnSocketProcessor() {
//		// default size queues
//		this(100, 100);
	}
//	public pxnSocketProcessorThreaded(int sizeIn, int sizeOut) {
//		// create queues
//		this(
//			new ArrayBlockingQueue<String>(sizeIn,  true),
//			new ArrayBlockingQueue<String>(sizeOut, true)
//		);
//	}
//	public pxnSocketProcessorThreaded(BlockingQueue<String> queueIn, BlockingQueue<String> queueOut) {
//		// queues
//		this.queueIn  = queueIn;
//		this.queueOut = queueOut;
//		// processing thread
//		this.setName( "Socket-Processor-"+Integer.toString(pxnSocketWorker.getNextId()) );
//		// start processing thread
//		this.start();
//	}


//	// processing thread
//	@Override
//	public void run() {
//		int count = 0;
//		while(true) {
//			if(stopping && queueIn.size() == 0) break;
//			// consume queue
//			try {
//				// submit packet for processing
//				String line = queueIn.take();
//				processNow(this, new pxnParser(line));
//				count++;
//			} catch (InterruptedException ignore) {
//				break;
//			}
//			if(queueIn.size() == 0 && count > 0) {
//System.out.println("Processed [ "+Integer.toString(count)+" ] packets");
////TODO: debug isn't working for some reason
////				pxnLogger.log().debug("Processed [ "+Integer.toString(count)+" ] packets");
//				count = 0;
//			}
//		}
//	}


//	@Override
//	public void Closing() {
//		stopping = true;
//		// clear output queue
//		queueOut.clear();
////		this.interrupt();
//	}


//	// socket state
//	public pxnSocketState StateChanged(pxnSocketState newState);
//	public void Closing();


	// add to queue
	public void ProcessData(String line) {
		
	}
	public void ProcessData(pxnSocketWorker worker, pxnParser line) {
		
	}


//	// add to queue
//	@Override
//	public void processData(String line) throws Exception {
//		if(line == null) return;
//		queueIn.offer(line.trim(), 1, TimeUnit.SECONDS);
////if full:
//// .offer waits
//// .add throws an exception
//	}
//	@Override
//	public void sendData(String line) throws Exception {
//		if(line == null) return;
//		queueOut.offer(line.trim(), 1, TimeUnit.SECONDS);
//	}


//	// get input/processing queue
//	@Override
//	public BlockingQueue<String> getInputQueue() {
//		return queueIn;
//	}
//	// get output/sending queue
//	@Override
//	public BlockingQueue<String> getOutputQueue() {
//		return queueOut;
//	}


}
