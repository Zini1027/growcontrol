package com.growcontrol.gctimer.timers;

import java.util.List;

import com.growcontrol.gctimer.gcTimer.TimerType;


public interface deviceTimer {


	public TimerType getTimerType();
	public void onTick();

	public void setDuration(String duration);

	public void addOutputCommand(String commandStr);
	public void addOutputCommands(List<String> outputCommands);


}
