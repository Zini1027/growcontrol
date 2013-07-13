package com.growcontrol.gcCommon.meta;


public class DataThermal implements DataType {

	public enum ThermalType {CELSIUS, FAHRENHEIT, KELVIN}
	protected double value; // temperature C


	@Override
	public String toString() {
		return toString(null);
	}
	@Override
	public String toString(String arg) {
		if(arg == null) arg = "C";
		if(arg.length() > 1)
			arg = arg.substring(0, 1);
		arg = arg.toUpperCase();
		// celsius
		if(arg.equals("C"))
			return Double.toString(get(ThermalType.CELSIUS))+"C";
		// fahrenheit
		if(arg.equals("F"))
			return Double.toString(get(ThermalType.FAHRENHEIT))+"F";
		// kelvin
		if(arg.equals("K"))
			return Double.toString(get(ThermalType.KELVIN))+"K";
		return toString("C");
	}
	public double get(ThermalType type) {
		if(type == null) throw new NullPointerException("type cannot be null!");
		switch(type) {
		case CELSIUS:
			return this.value;
		case FAHRENHEIT:
			return this.value;
		case KELVIN:
			return this.value;
		default:
			break;
		}
		return 0;
	}
	public double getC() {
		return get(ThermalType.CELSIUS);
	}
	public double getF() {
		return get(ThermalType.FAHRENHEIT);
	}
	public double getK() {
		return get(ThermalType.KELVIN);
	}


	public void set(double value, ThermalType type) {
		if(type == null) throw new NullPointerException("type cannot be null!");
		switch(type) {
		case CELSIUS:
			this.value = value;
			return;
		case FAHRENHEIT:
			this.value = value;
			return;
		case KELVIN:
			this.value = value;
			return;
		default:
			break;
		}
	}
	public void setC(double valueC) {
		set(valueC, ThermalType.CELSIUS);
	}
	public void setF(double valueF) {
		set(valueF, ThermalType.FAHRENHEIT);
	}
	public void setK(double valueK) {
		set(valueK, ThermalType.KELVIN);
	}


}
