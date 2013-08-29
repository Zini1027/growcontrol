package com.growcontrol.arduinogc.interfaces;

import java.util.HashMap;

import com.growcontrol.arduinogc.ArduinoGC;
import com.growcontrol.arduinogc.ArduinoPin;
import com.growcontrol.arduinogc.ArduinoPin.PinMode;
import com.growcontrol.arduinogc.msgQueue;


public class ArduinoInterface extends Thread {
	protected static final int THREAD_HEARTBEAT = 50;
	protected static final int THREAD_WAIT = 100;

	// controller types
	public static enum ControllerType{USB, NET};
	// pins map
	public HashMap<Integer, ArduinoPin> outputPins = new HashMap<Integer, ArduinoPin>();
	// queue
	protected msgQueue queue = new msgQueue();
	// interface state
	protected boolean ready = false;
	protected boolean stopping = false;


	public void StartInterface() {
		this.start();
	}
	public void StopInterface() {
		stopping = true;
	}
	public boolean isReady() {
		return ready && !stopping;
	}


//	public void setReady() {
//		// register output pins
////		for(Map.Entry<Integer, Pin> entry : outputPins.entrySet()) {
////			registerOutputs(entry.getKey());
////		}
//		for(int pin : outputPins.keySet())
//			registerOutputs(pin);
//		super.setReady();
//	}


	public boolean sendPinMode(int pinNum, PinMode pinMode) {
//TODO:
ArduinoGC.log.severe("mode "+Integer.toString(pinNum)+" "+pinMode.toString());
//		sendMessage("mode "+Integer.toString(pinNum)+" "+pinMode.toString());
		return false;
	}
	public boolean sendPinState(int pinNum, int pinState) {
//TODO: is command handled?
		queue.setPinState(pinNum, pinState);
		return true;
//ArduinoGC.log.warning("ASDFKJBGDSFN BDSJKFNKLJASDNFASDF "+Integer.toString(pinNum));
//		if(!super.setOutput(pinNum, pinState)) return false;
//		gcPin p = outputPins.get(pinNum);
//		p.pinState = pinState;
//		String data;
//		// io output
//		if(p.pinMode.equals(PinMode.io))
//			data = "pin "+Integer.toString(pinNum)+" "+(pinState==0?"off":"on");
//		// pwm output
//		else if(p.pinMode.equals(PinMode.pwm))
//			data = "pin "+Integer.toString(pinNum)+" "+Integer.toString(GrowControl.MinMax(pinState, 0, 255));
//		else
//			return false;
//		netTester.sendMessage(data);
//		return true;
	}


//	public boolean onOutput(String[] args) {
//		return false;
//	}


	// enum from string
	public static ControllerType controllerTypeFromString(String type) {
		if(type == null) return null;
		if(type.equalsIgnoreCase("usb") || type.equalsIgnoreCase("com"))
			return ControllerType.USB;
		else if(type.equalsIgnoreCase("net") || type.equalsIgnoreCase("ethernet"))
			return ControllerType.NET;
		return null;
	}


}
