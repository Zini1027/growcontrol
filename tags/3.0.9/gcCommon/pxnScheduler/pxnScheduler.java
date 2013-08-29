package com.growcontrol.gcCommon.pxnScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.growcontrol.gcCommon.pxnApp;
import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnClock.pxnClock;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


public class pxnScheduler extends Thread {
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	private static final int MaxSleepPerCycle = 1000;

	// schedulers by name
	protected static HashMap<String, pxnScheduler> schedulers = new HashMap<String, pxnScheduler>();

	// scheduler instances
	protected final String schedulerName;
	protected final List<pxnSchedulerTask> tasks = new ArrayList<pxnSchedulerTask>();

	// thread pool
	protected final pxnThreadQueue threadPool;

	// cycle state
	protected volatile long currentTime = -1;
	protected volatile boolean running  = false;
	protected volatile boolean stopping = false;
	// sleep time current cycle
	protected volatile long sleepTime = MaxSleepPerCycle;


	// get scheduler
	public static pxnScheduler get() {
		return get(null);
	}
	public static pxnScheduler get(String name) {
		if(name == null || name.isEmpty())
			name = pxnApp.get().getAppName();
		synchronized(schedulers) {
			// existing scheduler
			if(schedulers.containsKey(name))
				return schedulers.get(name);
			// new scheduler
			pxnScheduler sched = new pxnScheduler(name);
			schedulers.put(name, sched);
			return sched;
		}
	}
	// new scheduler
	protected pxnScheduler(String name) {
		if(name == null) throw new NullPointerException("name cannot be null!");
		this.schedulerName = name;
		this.threadPool = new pxnThreadQueue("sched_"+name);
		getLogger().debug("New scheduler created");
	}


	// start
	public synchronized void Start() {
		if(!this.isAlive())
			this.start();
	}
	// stop/pause
	public void Pause() {
		stopping = true;
	}
	// shutdown
	public void Shutdown() {
		Pause();
		threadPool.Shutdown();
	}
	// all schedulers
	public static void PauseAll() {
		synchronized(schedulers) {
			for(pxnScheduler sched : schedulers.values())
				sched.Pause();
		}
	}
	public static void ShutdownAll() {
		synchronized(schedulers) {
			for(pxnScheduler sched : schedulers.values())
				sched.Shutdown();
		}
	}


	// scheduler manager loop
	@Override
	public void run() {
		synchronized(schedulers) {
			if(running) {
				getLogger().warning("Scheduler still running!");
				return;
			}
			running = true;
		}
		while(!stopping) {
			currentTime = pxnClock.get().Millis();
			// get tasks to run
			List<pxnSchedulerTask> tasksToRun = getTasksToRun(currentTime);
			// run tasks
			if(tasksToRun != null && !tasksToRun.isEmpty()) {
				for(pxnSchedulerTask task : tasksToRun)
					RunTask(task);
				continue;
			}
			// check tasks to run (at least once a second)
			int s = (int)( ((double)this.sleepTime)*0.9 );
			s = pxnUtils.MinMax(s, 1, MaxSleepPerCycle);
			getLogger().finest("Sleeping for: "+Integer.toString(s)+"ms");
			pxnUtils.Sleep(s);
		}
		running = false;
	}
	// check tasks to run (and update sleep)
	private List<pxnSchedulerTask> getTasksToRun(long time) {
		if(time < 1)
			time = pxnClock.get().Millis();
		List<pxnSchedulerTask> tasksToRun = new ArrayList<pxnSchedulerTask>();
		this.sleepTime = MaxSleepPerCycle * 2;
		synchronized(tasks){ 
			for(pxnSchedulerTask task : tasks) {
				// sleep task
				if(task.SleepCycles() > 0) {
					continue;
				}
				// get time until next trigger
				long untilNext = task.UntilNextTrigger(time);
				if(untilNext == Long.MIN_VALUE) {
					getLogger().finest("untilNext is NULL");
					task.SleepCycles(1);
					continue;
				}
				if(untilNext > 1) {
					// thread sleep time
					if(untilNext < this.sleepTime)
						this.sleepTime = untilNext;
					// set task sleeping
					double d = (double) untilNext;
					d = d * 0.9;
					d = d / ((double) MaxSleepPerCycle);
					task.SleepCycles( (int) Math.floor(d) );
					continue;
				}
				// add to run list
				if(!task.preRun(time))
					tasksToRun.add(task);
			}
		}
		if(tasksToRun.isEmpty())
			return null;
		return tasksToRun;
	}
	private void RunTask(pxnSchedulerTask task) {
		String taskName = "SchedTask-"+schedulerName+"-"+task.getTaskName();
		task.doingTrigger(currentTime);
		if(task.multiThreaded)
			threadPool.addQueue(taskName, task);
		else
			pxnThreadQueue.addToMain(taskName, task);
	}


	// new task
	public void newTask(pxnSchedulerTask task) {
		if(task == null) throw new NullPointerException("task cannot be null!");
		synchronized(tasks) {
			tasks.add(task);
			getLogger().debug("("+task.getTaskName()+") New task created");
		}
	}


	// logger
	private volatile pxnLogger log = null;
	public synchronized pxnLogger getLogger() {
		if(log == null) {
			log = pxnLog.get("scheduler").get(schedulerName);
			log.setBracers("(", ")");
		}
		return log;
	}


}
