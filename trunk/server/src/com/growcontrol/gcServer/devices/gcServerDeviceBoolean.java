package com.growcontrol.gcServer.devices;


public class gcServerDeviceBoolean extends gcServerDevice {

//	protected boolean deviceState = false;


	// boolean device
	public gcServerDeviceBoolean(String deviceName) {
		super(deviceName);
	}
//	public void StartDevice(RunMode runMode) {
//		super.StartDevice(runMode);
//	}


//	// update state
//	public synchronized boolean updateState(boolean newState) {
//		if(newState != deviceState) {
//			deviceState = newState;
//			// run commands for this device
//			for(String commandStr : outputCommands)
//				gcServer.processCommand(commandStr.replace("%value%", newState?"1":"0"));
//			return true;
//		}
//		return false;
//	}


}
