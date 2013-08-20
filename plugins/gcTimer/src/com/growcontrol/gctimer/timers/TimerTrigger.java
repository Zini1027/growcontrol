package com.growcontrol.gctimer.timers;


public interface TimerTrigger {

	public String getName();
	public TimerType getTimerType();
	public String toString();

	public boolean TestSpan();

}
