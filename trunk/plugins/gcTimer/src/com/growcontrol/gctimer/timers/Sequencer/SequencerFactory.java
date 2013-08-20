package com.growcontrol.gctimer.timers.Sequencer;

import com.growcontrol.gctimer.timers.TimerFactory;
import com.growcontrol.gctimer.timers.TimerWorker;


public final class SequencerFactory implements TimerFactory {


	@Override
	public TimerWorker getNewTimer(TimerDAO dao) {
		return new SequencerWorker(dao);
	}


}
