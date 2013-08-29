package com.growcontrol.gctimer.timers;

import com.growcontrol.gctimer.config.TimerDAO;


public interface TimerFactory {

	public TimerWorker newTimer(TimerDAO dao);

}
