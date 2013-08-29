package com.growcontrol.gctimer.timers;

import com.growcontrol.gcCommon.meta.types.metaIO;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gctimer.config.TimerDAO;


public abstract class TimerWorker extends metaIO {
	private static final long serialVersionUID = 1L;

	protected volatile String timerName = null;
	protected volatile boolean enabled = false;
	protected volatile String outputName = null;

	public abstract void Tick();
	public abstract boolean TestSpans();
	public abstract void Close();


	public TimerWorker(TimerDAO dao) {
		super();
		this.timerName = dao.name;
		this.enabled = dao.enabled;
		this.outputName = dao.output;
pxnLog.get().Publish("NEW TIMER WORKER! "+dao.name);
	}


	@Override
	public void finalize() {
		Close();
	}


	// timer enabled
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


//	public TimerType getTimerType();
//	public void onTick();

//	public void setDuration(String duration);

//	public void addOutputCommand(String commandStr);
//	public void addOutputCommands(List<String> outputCommands);

}
