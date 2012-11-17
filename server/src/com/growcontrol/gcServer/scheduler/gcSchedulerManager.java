package com.growcontrol.gcServer.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.growcontrol.gcServer.gcServer;

public class gcSchedulerManager {

	// schedulers by name
	protected static HashMap<String, gcSchedulerManager> schedulers = new HashMap<String, gcSchedulerManager>();
	// scheduler instances
	protected final String schedulerName;
	protected final List<gcSchedulerTask> tasks = new ArrayList<gcSchedulerTask>();


	public static gcSchedulerManager getScheduler(String schedulerName) {
		gcSchedulerManager sched = null;
		synchronized(schedulers) {
			if(schedulers.containsKey(schedulerName)) {
				sched = schedulers.get(schedulerName);
			} else {
				// generate unique task name
				if(schedulerName == null)
					schedulerName = generateRandomName();
				sched = new gcSchedulerManager(schedulerName);
				schedulers.put(schedulerName, sched);
			}
		}
		return sched;
	}
	private gcSchedulerManager(String schedulerName) {
		if(schedulerName == null) throw new NullPointerException();
		this.schedulerName = schedulerName;
		gcServer.log.info("New scheduler created: "+schedulerName);
	}


	// new task
	public void newTask(gcSchedulerTask task) {
		if(task == null) throw new NullPointerException();
		synchronized(tasks) {
			this.tasks.add(task);
			gcServer.log.info("Created new task: "+task.getTaskName());
		}
	}


	// generate a random name
	public static String generateRandomName() {
		String name = null;
		while(true) {
			name = UUID.randomUUID().toString();
			if(name == null) throw new NullPointerException();
			name = name.substring(0, 6);
			if(!schedulers.containsKey(name)) break;
		}
		return name;
	}


	// start
	public static void StartAll() {
	}
	public void Start() {
	}
	// stop
	public static void StopAll() {
	}
	public void Stop() {
	}
	// shutdown scheduler
	public static void ShutdownAll() {
	}
	public void Shutdown() {
	}


}
