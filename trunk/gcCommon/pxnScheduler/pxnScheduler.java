package com.growcontrol.gcCommon.pxnScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.growcontrol.gcCommon.TimeU;
import com.growcontrol.gcCommon.TimeUnitTime;
import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


public class pxnScheduler extends Thread {

	// schedulers by name
	protected static HashMap<String, pxnScheduler> schedulers = new HashMap<String, pxnScheduler>();

	// scheduler instances
	protected final String schedulerName;
	protected final List<pxnSchedulerTask> tasks = new ArrayList<pxnSchedulerTask>();

	// thread pool
	protected final pxnThreadQueue threadPool;

	protected volatile boolean running  = false;
	protected volatile boolean stopping = false;
	protected TimeUnitTime sleepTime = new TimeUnitTime();


	// get scheduler
	public static pxnScheduler get(String name) {
		pxnScheduler sched = null;
		synchronized(schedulers) {
			if(schedulers.containsKey(name)) {
				sched = schedulers.get(name);
			} else {
				// generate unique task name
				if(name == null)
					name = generateRandomName();
				sched = new pxnScheduler(name);
				schedulers.put(name, sched);
			}
		}
		return sched;
	}
	// new scheduler
	protected pxnScheduler(String name) {
		if(name == null) throw new NullPointerException("schedulerName cannot be null!");
		this.schedulerName = name;
		this.threadPool = new pxnThreadQueue("scheduler_"+name);
		pxnLogger.get().debug("("+name+") New scheduler created");
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
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


	@Override
	public void run() {
		if(running) {
			pxnLogger.get().severe("("+schedulerName+") Scheduler already running!");
			return;
		}
		running = true;
		while(!stopping) {
			// tasks to run
			List<pxnSchedulerTask> tasksToRun = getTasksToRun();
			// run tasks
			if(tasksToRun != null)
				for(pxnSchedulerTask task : tasksToRun)
					RunTask(task);
			// sleep max 1 second
			long sleepMS = this.sleepTime.get(TimeU.MS);
			pxnUtils.Sleep(pxnUtils.MinMax(sleepMS, 1, 950));
		}
		running = false;
	}
	// check tasks to run (at least once a second)
	private List<pxnSchedulerTask> getTasksToRun() {
		List<pxnSchedulerTask> tasksToRun = new ArrayList<pxnSchedulerTask>();
		synchronized(this.tasks){ 
			for(pxnSchedulerTask task : this.tasks){
				// sleeping task
				if(task.isSleeping > 0) {
					task.isSleeping -= 1;
					continue;
				}
				TimeUnitTime untilNext = task.UntilNextTrigger();
				if(untilNext == null) {
					task.isSleeping = 5;
					continue;
				}
//System.out.println(task.getTaskName()+" "+"untilNext:"+untilNext.get(TimeU.MS));
				if(untilNext.get(TimeU.MS) > 0) {
					// thread sleep time
					if(untilNext.get(TimeU.MS) < this.sleepTime.get(TimeU.MS))
						this.sleepTime.set(untilNext);
					// set task sleeping
					if(untilNext.get(TimeU.S) > 60)
						task.isSleeping = 60;
					if(untilNext.get(TimeU.S) > 5)
						task.isSleeping = 5;
					continue;
				}
				// add to list
				tasksToRun.add(task);
				task.preRun();
			}
		}
		if(tasksToRun.size() == 0)
			return null;
		return tasksToRun;
	}
	private void RunTask(pxnSchedulerTask task) {
		String taskName = "SchedulerTask-"+schedulerName+"-"+task.getTaskName();
		task.onTrigger();
		if(task.multiThreaded)
			threadPool.addQueue(taskName, task);
		else
			pxnThreadQueue.addToMain(taskName, task);
	}


	// new task
	public void newTask(pxnSchedulerTask task) {
		if(task == null) throw new NullPointerException("task cannot be null!");
		synchronized(this.tasks) {
			this.tasks.add(task);
			pxnLogger.get().debug("("+task.getTaskName()+") New task created");
		}
	}


	// generate a random name
	public static String generateRandomName() {
		String name = null;
		while(true) {
			name = UUID.randomUUID().toString();
			if(name == null) throw new NullPointerException();
			name = name.substring(0, 6);
			if(!schedulers.containsKey(name))
				break;
		}
		return name;
	}


}
