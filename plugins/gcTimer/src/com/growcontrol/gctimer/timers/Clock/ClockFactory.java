package com.growcontrol.gctimer.timers.types;

import com.growcontrol.gctimer.timers.TimerFactory;
import com.growcontrol.gctimer.timers.TimerWorker;


public final class ClockFactory implements TimerFactory {


	@Override
	public TimerWorker getNewTimer(String name) {
		return new Clock(name);
	}


}
