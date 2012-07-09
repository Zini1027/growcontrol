package com.growcontrol.arduinogc;


public class ArduinoPin {


	public int pinNum = 0;
	public PinMode pinMode = PinMode.disabled;
	public int pinState = 0;

	public static enum PinMode {
		disabled,
		io,
		pwm,
		in,
		inh,
		analog
	};


	public ArduinoPin() {}
	public ArduinoPin(PinMode pinMode) {
		this.pinMode = pinMode;
	}
	public ArduinoPin(String pinMode) {
		this.pinMode = fromString(pinMode.trim());
	}


	public static PinMode fromString(String pinMode) {
		if(pinMode.equalsIgnoreCase("x"))
			return PinMode.disabled;
		else if(pinMode.equalsIgnoreCase("io"))
			return PinMode.io;
		else if(pinMode.equalsIgnoreCase("pwm"))
			return PinMode.pwm;
		else if(pinMode.equalsIgnoreCase("in"))
			return PinMode.in;
		else if(pinMode.equalsIgnoreCase("inh"))
			return PinMode.inh;
		else if(pinMode.equalsIgnoreCase("analog"))
			return PinMode.analog;
		return PinMode.disabled;
	}


	public void setState(String state) {
		state = state.trim();
		if(state.equalsIgnoreCase("on"))
			pinState = 1;
		else if(state.equalsIgnoreCase("off"))
			pinState = 0;
		else if(state.equalsIgnoreCase("x"))
			pinState = -1;
		else
			pinState = Integer.valueOf(state);
	}


}
