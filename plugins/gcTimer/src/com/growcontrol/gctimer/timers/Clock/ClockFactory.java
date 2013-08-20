package com.growcontrol.gctimer.timers.Clock;

import com.growcontrol.gctimer.timers.TimerFactory;
import com.growcontrol.gctimer.timers.TimerWorker;


public final class ClockFactory implements TimerFactory {


	@Override
	public TimerWorker getNewTimer(TimerDAO dao) {
		return new ClockWorker(dao);
	}


}
