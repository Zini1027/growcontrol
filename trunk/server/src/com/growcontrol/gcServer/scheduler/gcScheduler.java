package com.growcontrol.gcServer.scheduler;

import java.util.HashMap;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.logger.gcLogger;

public class gcScheduler implements Job {

	protected final gcLogger log;

	protected static Scheduler scheduler = null;
	protected static Boolean Paused = false;
	private static boolean Started = false;

	// instance / task maps
	protected static HashMap<String, gcScheduler> schedMap = new HashMap<String, gcScheduler>();
	protected HashMap<String, Runnable> taskMap = new HashMap<String, Runnable>();

	public static final String GROUP_NAME = "groupName";
	public static final String TASK_NAME = "taskName";
	public static final String EXECUTION_COUNT = "count";

	protected final String groupName;


	// scheduler instance
	public static synchronized gcScheduler getScheduler(String groupName) {
		if(schedMap.containsKey(groupName))
			return schedMap.get(groupName);
		gcScheduler sched = new gcScheduler(groupName);
		schedMap.put(groupName, sched);
		return sched;
	}
	public gcScheduler() {
		log = null;
		groupName = null;
	}
	public gcScheduler(String groupName) {
		this.groupName = groupName;
		log = gcLogger.getLogger("quartz:"+groupName);
		if(scheduler == null) {
			try {
				scheduler = StdSchedulerFactory.getDefaultScheduler();
			} catch (SchedulerException e) {
				log.exception(e);
			}
		}
	}


	// start/stop
	public static void Start() {
		synchronized(Paused) {
			if(Started) return;
			try {
				scheduler.start();
			} catch (SchedulerException e) {
				gcServer.log.exception(e);
			}
			Started = true;
		}
	}
	public static void Shutdown() {
		if(scheduler == null) return;
		synchronized(Paused) {
			try {
				scheduler.shutdown(true);
			} catch (SchedulerException e) {
				gcServer.log.exception(e);
			}
		}
	}


	// pause
	public static void pauseAll(boolean Paused) {
		synchronized(gcScheduler.Paused) {
			gcScheduler.Paused = Paused;
			try {
				if(Paused) {
					scheduler.pauseAll();
					gcServer.log.info("Schedulers are paused!");
				} else {
					scheduler.resumeAll();
					gcServer.log.info("Schedulers resumed");
				}
			} catch (SchedulerException e) {
				gcServer.log.exception(e);
			}
		}
	}
	public static void pauseAll() {
		pauseAll(!Paused);
	}


	// new task
	public boolean newTask(String taskName, Runnable task, ScheduleBuilder<?> schedBuilder) {
		// generate unique task name
		if(taskName == null) {
log.severe("unique task name generation not finished!!!");
		}
		if(taskName == null) return false;
		if(taskMap.containsKey(taskName)) {
			log.severe("Scheduler already contains a task named: "+taskName);
			return false;
		}
		try {
			log.debug("Creating scheduler: "+taskName);
			// new job
			JobDetail job = JobBuilder.newJob(gcScheduler.class)
				.withIdentity(taskName, groupName)
				.withDescription(taskName)
				.usingJobData(GROUP_NAME, groupName)
				.usingJobData(TASK_NAME, taskName)
				.usingJobData(EXECUTION_COUNT, 0)
				.build();
			//job.getJobDataMap().put("Test", "5");
			// new trigger
			Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(taskName+"_trigger", groupName)
				.startNow()
				.withSchedule(schedBuilder)
				.build();
			// done
			scheduler.scheduleJob(job, trigger);
			taskMap.put(taskName, task);
			return true;
		} catch(Exception e) {
			log.severe("Failed to create new scheduler task");
			log.exception(e);
		}
		return false;
	}


	// seconds trigger
	public static SimpleScheduleBuilder newTriggerSeconds(double seconds, int repeat) {
		SimpleScheduleBuilder sched = SimpleScheduleBuilder.simpleSchedule()
			.withIntervalInMilliseconds( (long)(seconds*1000.0) );
		if(repeat <= -1)
			return sched.repeatForever();
		return sched.withRepeatCount(repeat);
	}
	public static SimpleScheduleBuilder newTriggerSeconds(int seconds, int repeat) {
		return newTriggerSeconds((double)seconds, repeat);
	}
	public static SimpleScheduleBuilder newTriggerSeconds(double seconds, boolean repeat) {
		return newTriggerSeconds(seconds, repeat?-1:0);
	}
	public static SimpleScheduleBuilder newTriggerSeconds(int seconds, boolean repeat) {
		return newTriggerSeconds(seconds, repeat?-1:0);
	}

	// cron trigger
	// example: "0/20 * * * * ?"
	public static CronScheduleBuilder newTriggerCron(String cron) {
		return CronScheduleBuilder.cronSchedule(cron);
	}


	// run task
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail details = context.getJobDetail();
		JobDataMap dataMap = details.getJobDataMap();
		String groupName = dataMap.getString(GROUP_NAME);
		String taskName = dataMap.getString(TASK_NAME);
		if(groupName == null || taskName == null) {
			gcServer.log.severe("Scheduler or Task name missing!!! Cannot execute task "+groupName+":"+taskName);
			return;
		}
		gcScheduler sched = schedMap.get(groupName);
		if(sched == null) {
			gcServer.log.severe("Scheduler group unknown! "+groupName);
			return;
		}
		Runnable task = sched.taskMap.get(taskName);
		if(task == null) {
			gcServer.log.severe("Scheduler unable to find task! "+taskName);
			return;
		}
		task.run();
	}


}
