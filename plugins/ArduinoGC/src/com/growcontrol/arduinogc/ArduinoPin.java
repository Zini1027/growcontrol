package com.growcontrol.arduinogc;


public class ArduinoPin {

	public int pinNum = 0;
	public PinMode pinMode = PinMode.DISABLED;
	public int pinState = 0;


	public static enum PinMode {
		DISABLED,
		IO,
		PWM,
		IN,
		INH,
		ANALOG
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
			return PinMode.DISABLED;
		else if(pinMode.equalsIgnoreCase("io"))
			return PinMode.IO;
		else if(pinMode.equalsIgnoreCase("pwm"))
			return PinMode.PWM;
		else if(pinMode.equalsIgnoreCase("in"))
			return PinMode.IN;
		else if(pinMode.equalsIgnoreCase("inh"))
			return PinMode.INH;
		else if(pinMode.equalsIgnoreCase("analog"))
			return PinMode.ANALOG;
		return PinMode.DISABLED;
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
