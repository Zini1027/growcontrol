package com.growcontrol.gctimer.timers.Ticker;

import com.growcontrol.gctimer.config.TimerDAO;
import com.growcontrol.gctimer.timers.TimerFactory;
import com.growcontrol.gctimer.timers.TimerWorker;


public final class TickerFactory implements TimerFactory {


	@Override
	public TimerWorker newTimer(TimerDAO dao) {
		return new TickerWorker(dao);
	}


}
