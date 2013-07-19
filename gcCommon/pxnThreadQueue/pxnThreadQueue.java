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
	protected final BlockingQueue<pxnRunnable> queue = new ArrayBlockingQueue<pxnRunnable>(10);

	protected volatile boolean stopping = false;
	protected volatile int maxThreads = 1;
	protected volatile int active = 0;
	protected volatile int runCount = 0;


	// main thread
	protected static pxnThreadQueue mainThread = null;
	public static pxnThreadQueue getMain() {
		if(mainThread == null)
			mainThread = new pxnThreadQueue("main");
		return mainThread;
	}
	public static void addToMain(String name, Runnable runnable) {
		getMain().addQueue(name, runnable);
	}


	public pxnThreadQueue(String name) {
		this.queueName = name;
		// main thread (single)
		if(name == "main") {
			threads = null;
		// thread pool (multi)
		} else {
			threads = new ArrayList<Thread>();
			addThread();
		}
		// thread queue started
		addQueue("Thread-Startup", new Runnable() {
			@Override
			public void run() {
				pxnLogger.get().debug("("+queueName+") Started thread queue..");
			}
		});
	}


	// run state
	public boolean isRunning() {
		return active > 0;
	}
	public boolean isStopping() {
		return stopping;
	}
	public int activeCount() {
		return active;
	}


	// run thread queue
	@Override
	public void run() {
		// main thread
		if(threads == null)
			if(isRunning() || stopping)
				return;
		int inactiveCount = 0;
		while(!stopping) {
			pxnRunnable run = null;
			try {
				//run = queue.take();
				run = queue.poll(1, TimeU.S);
				inactiveCount += 1;
			} catch (InterruptedException e) {
				pxnLogger.get().exception(e);
				break;
			}
			if(active < 0) active = 0;
			// inactive thread
			if(run == null) {
				// inactive thread after 5 minutes
				if(inactiveCount > 300 && threads != null) {
					pxnLogger.get().info("("+queueName+") Inactive thread");
					break;
				}
			// active thread
			} else {
				runCount++;
				inactiveCount = 0;
				active++;
				try {
					run.run();
				} catch (Exception e) {
					pxnLogger.get().exception(e);
				}
				active--;
			}
		}
		pxnLogger.get().info("("+queueName+") Stopped thread queue");
	}


	// queue thread shutdown
	public void Shutdown() {
		addQueue("Thread-Queue-Stopping", new Runnable() {
			@Override
			public void run() {
				pxnLogger.get().info("("+queueName+") Stopping thread queue..");
				stopping = true;
			}
		});
	}
	// exit program
	public void Exit() {
		mainThread.addQueue("Exit", new Runnable() {
			@Override
			public void run() {
				// threads still running
				displayStillRunning();
				System.out.println();
				System.out.println();
				System.exit(0);
			}
		});
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
		if(count > 0) {
			log.severe("Threads still running: ("+count+")");
			for(String t : threadNames)
				log.printRaw(t);
		}
	}


	// add to queue
	public void addQueue(String name, Runnable runnable) {
		if(runnable == null) throw new NullPointerException("("+queueName+") runnable can't be null!");
		try {
			pxnRunnable run = new pxnRunnable(name, runnable);
			if(queue.offer(run, 1, TimeU.S)) {
//				String msg = "("+queueName+") Thread task queued";
//				if(run.getTaskName() != null && !run.getTaskName().equals(""))
//					msg += ": "+run.getTaskName();
//				pxnLogger.get().debug(msg);
			} else {
				pxnLogger.get().severe("("+queueName+") Thread queue is full!");
			}
		} catch (InterruptedException e) {
			pxnLogger.get().exception(e);
		}
		// add new thread (if all are in use)
		addThread();
	}


	// add new thread (if all are in use)
	protected synchronized void addThread() {
		if(stopping) return;
		// multi-threaded mode only
		if(this.threads == null) return;
		int count = this.threads.size();
		int free = count - active;
		// not needed
		if(free > 0) return;
		// restart existing thread
		for(Thread t : this.threads) {
			if(t == null) continue;
			if(!t.isAlive()) {
				t.start();
				return;
			}
		}
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
