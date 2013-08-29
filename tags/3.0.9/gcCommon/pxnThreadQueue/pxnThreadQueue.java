package com.growcontrol.gcCommon.pxnThreadQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.growcontrol.gcCommon.TimeU;
import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnClock.pxnCoolDown;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public class pxnThreadQueue implements Runnable {
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	// thread count limits
	public static final int HardLimit = 100;
//	public static volatile int GlobalMax = 200;

	protected final List<Thread> threads;
	protected final String queueName;
	protected final String logName;
	protected final BlockingQueue<pxnRunnable> queue = new ArrayBlockingQueue<pxnRunnable>(10);

	protected volatile boolean stopping = false;
	protected volatile int maxThreads = 1;
	protected volatile int active = 0;
	protected volatile int runCount = 0;
	protected final pxnCoolDown coolMaxReached = pxnCoolDown.get("5s");

	// main thread
	protected static volatile pxnThreadQueue mainThread = null;
	protected static final Object lock = new Object();


	public static pxnThreadQueue getMain() {
		if(mainThread == null) {
			synchronized(lock) {
				if(mainThread == null)
					mainThread = new pxnThreadQueue("main");
			}
		}
		return mainThread;
	}
	public static void addToMain(String name, Runnable runnable) {
		getMain().addQueue(name, runnable);
	}


	public pxnThreadQueue(String name) {
		this(name, null);
	}
	public pxnThreadQueue(String name, Integer maxThreads) {
		if(name == null || name.isEmpty()) throw new NullPointerException("name cannot be null!");
		this.queueName = name;
		this.logName = name;
		if(maxThreads != null)
			setMax(maxThreads);
		getLogger().setBracers("(", ")");
		// main thread (single)
		if(name == "main") {
			threads = null;
			maxThreads = 1;
		// thread pool (multi)
		} else {
			threads = new ArrayList<Thread>();
			addThread();
		}
		// thread queue started
		addQueue("Thread-Startup", new Runnable() {
			@Override
			public void run() {
				getLogger().debug("Started thread queue..");
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


	// max threads
	public int getMax() {
		return maxThreads;
	}
	public void setMax(int maxThreads) {
		if(queueName.equalsIgnoreCase("main")) return;
		this.maxThreads = pxnUtils.MinMax(maxThreads, 0, HardLimit);
	}


	// logger
	protected pxnLogger getLogger() {
		return pxnLog.get(logName);
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
			pxnRunnable task = null;
			try {
				//run = queue.take();
				task = queue.poll(1, TimeU.S);
				inactiveCount += 1;
			} catch (InterruptedException e) {
				getLogger().exception(e);
				break;
			}
			if(active < 0) active = 0;
			// inactive thread
			if(task == null) {
				// inactive thread after 5 minutes
				if(inactiveCount > 300 && threads != null) {
					getLogger().info("Inactive thread");
					break;
				}
			// active thread
			} else {
				runCount++;
				inactiveCount = 0;
				active++;
				getLogger().finer("Running thread task: "+task.getTaskName());
				Thread.currentThread().setName(task.getTaskName());
				try {
					task.run();
				} catch (Exception e) {
					getLogger().exception(e);
				}
				active--;
			}
		}
		getLogger().finer("Stopped thread queue");
	}


	// queue thread shutdown
	public void Shutdown() {
		addQueue("Thread-Queue-Stopping", new Runnable() {
			@Override
			public void run() {
//				pxnLogger.get().info("("+queueName+") Stopping thread queue..");
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
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		List<String> threadNames = new ArrayList<String>();
		int count = 0;
		for(Thread t : threadSet) {
			String threadName = t.getName();
			if(threadName.equals("Main-Server-Thread"))           continue;
			if(threadName.equals("Reference Handler"))            continue;
			if(threadName.equals("NonBlockingInputStreamThread")) continue;
			if(threadName.equals("process reaper"))               continue;
			if(threadName.equals("Signal Dispatcher"))            continue;
			if(threadName.equals("Finalizer"))                    continue;
			if(threadName.equals("Exit"))                         continue;
			count++;
			threadNames.add(t.getName());
		}
		pxnLogger log = getLogger();
		if(count > 0) {
			log.severe("Threads still running: ("+count+")");
			for(String t : threadNames)
				log.Publish(t);
		}
	}


	// add to queue
	public void addQueue(String name, Runnable run) {
		if(run == null) throw new NullPointerException("run can't be null!");
		// run in main thread queue
		if(getMax() <= 0) {
			addToMain(name, run);
			return;
		}
		try {
			pxnRunnable r = new pxnRunnable(name, run);
			if(queue.offer(r, 1, TimeU.S))
				getLogger().finer("Thread task queued..");
			else
				getLogger().severe("Thread queue is full!!!");
		} catch (InterruptedException e) {
			getLogger().exception(e);
		}
		// add new thread (if all are in use)
		addThread();
	}


	// add new thread (if all are in use)
	protected synchronized void addThread() {
		if(stopping || getMax() <= 0) return;
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
		if(count >= getMax()) {
			if(getMax() > 1) {
				if(coolMaxReached.Again())
					getLogger().warning("Max threads limit reached!");
				else
					getLogger().finest("Max threads limit reached!");
			}
			pxnUtils.Sleep(10);
			return;
		}
		// new thread
		Thread thread = new Thread(this);
		thread.setName("pxnThreadQueue_"+queueName);
		this.threads.add(thread);
		thread.start();
		getLogger().info(
			"Threads: "+count+
			" active: "+Integer.toString(active)+
			" max: "+Integer.toString(getMax())
		);
	}


}
