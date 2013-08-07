package com.growcontrol.gcCommon.meta;


public class metaThermal extends pxnMeta {
	private static final long serialVersionUID = 7L;

	public static enum ThermalUnit {CELSIUS, FAHRENHEIT, KELVIN};

	protected volatile Double value = null; // temperature C


	public metaThermal(String name) {
		super(name);
	}
	public metaThermal(String name, Double value, ThermalUnit unit) {
		super(name);
		set(value, unit);
	}


	// set value
	public void set(Double value, ThermalUnit unit) {
//TODO:
	}
	@Override
	public void set(String value) {
//TODO:
	}
	public void setC(double valueC) {
		set(valueC, ThermalUnit.CELSIUS);
	}
	public void setF(double valueF) {
		set(valueF, ThermalUnit.FAHRENHEIT);
	}
	public void setK(double valueK) {
		set(valueK, ThermalUnit.KELVIN);
	}


	// get value
	public Double get(ThermalUnit unit) {
		return value;
//TODO:
	}
	@Override
	public String toString() {
		return Double.toString(get(ThermalUnit.CELSIUS))+"C "+
			Double.toString(get(ThermalUnit.FAHRENHEIT))+"F";
	}
	public double getC() {
		return get(ThermalUnit.CELSIUS);
	}
	public double getF() {
		return get(ThermalUnit.FAHRENHEIT);
	}
	public double getK() {
		return get(ThermalUnit.KELVIN);
	}


//	if(type == null) throw new NullPointerException("type cannot be null!");
//	switch(type) {
//	case CELSIUS:
//		this.value = value;
//		return;
//	case FAHRENHEIT:
//		this.value = value;
//		return;
//	case KELVIN:
//		this.value = value;
//		return;
//	default:
//		break;
//	}
//	@Override
//	public String toString(String arg) {
//		if(arg == null) arg = "C";
//		if(arg.length() > 1)
//			arg = arg.substring(0, 1);
//		arg = arg.toUpperCase();
//		// celsius
//		if(arg.equals("C"))
//			return Double.toString(get(ThermalType.CELSIUS))+"C";
//		// fahrenheit
//		if(arg.equals("F"))
//			return Double.toString(get(ThermalType.FAHRENHEIT))+"F";
//		// kelvin
//		if(arg.equals("K"))
//			return Double.toString(get(ThermalType.KELVIN))+"K";
//		return toString("C");
//	}


}
