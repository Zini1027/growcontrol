package com.growcontrol.gcCommon.pxnThreadQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.growcontrol.gcCommon.TimeU;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public class pxnThreadQueue implements Runnable {

	protected final List<Thread> threads;
	protected final String queueName;
	protected final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);

//	protected volatile boolean running  = false;
	protected volatile boolean stopping = false;
	protected volatile int active = 0;
	protected volatile int maxThreads = 1;


	// main thread
	protected static pxnThreadQueue mainThread = null;
	public static pxnThreadQueue getMainThread() {
		if(mainThread == null)
			mainThread = new pxnThreadQueue("main");
		return mainThread;
	}


	public pxnThreadQueue(String name) {
		queue.add(new Runnable() {
			@Override
			public void run() {
				pxnLogger.get().debug("("+queueName+") Started thread queue..");
			}
		});
		this.queueName = name;
		if(name == "main") {
			threads = null;
		} else {
			threads = new ArrayList<Thread>();
			addThread();
		}
	}


	// run state
	public boolean isRunning() {
		return active > 0;
//		return running;
	}
	public boolean isStopping() {
		return stopping;
	}
//	public boolean isActive() {
//		return (active>0);
//	}
	public int activeCount() {
		return active;
	}


	// run thread queue
	@Override
	public void run() {
//		running = true;
		while(!stopping) {
			Runnable run = null;
			try {
				run = queue.take();
			} catch (InterruptedException e) {
				pxnLogger.get().exception(e);
				break;
			}
			if(run != null) {
				active += 1;
				try {
					run.run();
				} catch (Exception e) {
					pxnLogger.get().exception(e);
				}
				active -= 1;
			}
		}
//		running = false;
		pxnLogger.get().info("("+queueName+") Stopped thread queue");
	}


	// queue thread shutdown
	public void Shutdown() {
		addQueue(new Runnable() {
			@Override
			public void run() {
				pxnLogger.get().info("("+queueName+") Stopping thread queue..");
				stopping = true;
			}
		});
	}
	// exit program
	public void Exit() {
		mainThread.addQueue(new Runnable() {
			@Override
			public void run() {
				// threads still running
				displayStillRunning();
				System.exit(0);
			}
		});
//		addQueue(new Runnable() {
//			@Override
//			public void run() {
//				System.exit(0);
//			}
//		});
	}
	// display threads still running
	public void displayStillRunning() {
		pxnLogger log = pxnLogger.get();
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		List<String> threadNames = new ArrayList<String>();
		int count = 0;
		for(Thread t : threadSet) {
			String threadName = t.getName();
			if(threadName.equals("Main-Server-Thread"))	continue;
			if(threadName.equals("Reference Handler"))	continue;
			if(threadName.equals("process reaper"))		continue;
			if(threadName.equals("Signal Dispatcher"))	continue;
			if(threadName.equals("Finalizer"))			continue;
			count++;
			threadNames.add(t.getName());
		}
		log.severe("Threads still running: ("+count+")");
		//Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		for(String t : threadNames)
			log.printRaw(t);
	}


	// add to queue
	public void addQueue(Runnable runnable) {
		if(runnable == null) throw new NullPointerException("("+queueName+") runnable can't be null!");
		try {
			if(!queue.offer(runnable, 1, TimeU.S))
				pxnLogger.get().severe("("+queueName+") Thread queue is full!");
		} catch (InterruptedException e) {
			pxnLogger.get().exception(e);
		}
		addThread();
	}


	// add new thread (if needed)
	protected void addThread() {
		if(threads == null) return;
		int count = threads.size();
		int free = count - active;
		// not needed
		if(free > 0) return;
		// max threads
		if(count >= maxThreads) return;
		// new thread
		Thread thread = new Thread(this);
		thread.setName("pxnThreadQueue_"+queueName);
		this.threads.add(thread);
		thread.start();
		pxnLogger.get().info("("+queueName+") threads: "+count+" active: "+active+" max: "+maxThreads);
	}


}
