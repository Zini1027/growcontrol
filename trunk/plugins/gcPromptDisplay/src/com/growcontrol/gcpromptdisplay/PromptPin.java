package com.growcontrol.gcpromptdisplay;


public class PromptPin {

	public int pinNum = 0;
	public PinMode pinMode = PinMode.disabled;
	public int pinState = 0;


	public static enum PinMode {
		disabled,
		io,
		pwm,
	};


	public PromptPin() {}
	public PromptPin(PinMode pinMode) {
		this.pinMode = pinMode;
	}
	public PromptPin(String pinMode) {
		this.pinMode = fromString(pinMode.trim());
	}


	// to/from string
	public static String toString(PinMode pinMode) {
		switch(pinMode) {
		case disabled:	return "disabled";
		case io:		return "io";
		case pwm:		return "pwm";
		}
		return "disabled";
	}
	public static String toString(PinMode pinMode, int pinState) {
		switch(pinMode) {
		case disabled:	return "disabled";
		case io:		return (pinState==0?"off":"ON ");
		case pwm:		return Integer.toString(pinState)+"%";
		}
		return "disabled";
	}
	public static PinMode fromString(String pinMode) {
		if(pinMode.equalsIgnoreCase("x"))
			return PinMode.disabled;
		else if(pinMode.equalsIgnoreCase("io"))
			return PinMode.io;
		else if(pinMode.equalsIgnoreCase("pwm"))
			return PinMode.pwm;
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
