package com.poixson.pxnThreadQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.poixson.pxnLogger.pxnLogger;


public class pxnThreadQueue implements Runnable {

	protected final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);

	protected boolean stopping = false;


	public pxnThreadQueue() {
		queue.add(new Runnable() {
			@Override
			public void run() {
				pxnLogger.getLogger().info("Started thread queue..");
			}
		});
	}


	// run thread queue
	public void run() {
		while(!stopping) {
			try {
				Runnable runnable = queue.take();
				if(runnable != null)
					runnable.run();
			} catch (InterruptedException e) {
e.printStackTrace();
			}
		}
		pxnLogger.getLogger().info("Stopped thread queue");
	}


	// queue thread shutdown
	public void Shutdown() {
System.out.println("Stopping thread queue..1");
		addQueue(new Runnable() {
			@Override
			public void run() {
				stopping = true;
				pxnLogger.getLogger().info("Stopping thread queue..");
System.out.println("Stopping thread queue..");
			}
		});
	}
	// exit program
	public void exit() {
		addQueue(new Runnable() {
			@Override
			public void run() {
				System.exit(0);
			}
		});
	}


	// add to queue
	public void addQueue(Runnable runnable) {
		if(runnable == null) throw new NullPointerException("runnable can't be null!");
		try {
			queue.offer(runnable, 1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
e.printStackTrace();
		}
	}


}
