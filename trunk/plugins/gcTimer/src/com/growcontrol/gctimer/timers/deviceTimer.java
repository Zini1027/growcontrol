package com.growcontrol.gctimer.timers;

import com.growcontrol.gctimer.gcTimer.TimerType;


public interface deviceTimer {


	public TimerType getTimerType();
	public void onTick();


}
