package com.growcontrol.gcServer.devices;

import java.util.ArrayList;
import java.util.List;

public class gcServerDevice {

	public final String deviceName;
	public static enum RunMode {AlwaysRun, TriggeredRun};
	protected RunMode runMode = null;
	protected boolean running = false;
	protected boolean retention = false;
	protected List<String> outputCommands = new ArrayList<String>();


	public gcServerDevice(String deviceName) {
		this.deviceName = deviceName;
	}
	public void StartDevice(RunMode runMode) {
		synchronized(runMode) {
			if(this.runMode != null) return;
			this.runMode = runMode;
			if(runMode.equals(RunMode.AlwaysRun)) running = true;
//TODO: update state now?
		}
	}


	// device name
	public String getDeviceName() {
		return deviceName;
	}


	// output commands
	public void addOutputCommand(String commandStr) {
		synchronized(outputCommands) {
			this.outputCommands.add(commandStr);
		}
	}
	public void addOutputCommands(List<String> outputCommands) {
		synchronized(outputCommands) {
			this.outputCommands.addAll(outputCommands);
		}
	}


}
