package com.growcontrol.gcCommon.meta.valueTypes;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.meta.metaType;
import com.growcontrol.gcCommon.meta.metaValue;
import com.growcontrol.gcCommon.meta.valueFactory;


public class metaThermal implements metaValue {
	private static final long serialVersionUID = 9L;

	public static enum ThermalUnit {CELSIUS, FAHRENHEIT, KELVIN};

	// raw value
	protected volatile Double value = null; // temperature C
	protected final Object lock = new Object();


	// static type
	public static final metaType THERMAL = new metaType("THERMAL",
		new valueFactory() {
			@Override
			public metaValue newValue() {
				return new metaThermal();
			}
	});


	// instance
	public metaThermal() {
		set((Double) null);
	}
	public metaThermal(Double value) {
		set(value);
	}
	public metaThermal(metaThermal meta) {
		this(meta.getValue());
	}
	@Override
	public metaValue clone() {
		return new metaThermal(this);
	}


	// type
	@Override
	public metaType getType() {
		return THERMAL;
	}


	// get value
	public Double getValue() {
		synchronized(lock) {
			if(value == null)
				return null;
			return value.doubleValue();
		}
	}
	@Override
	public String getString() {
		Double i = getValue();
		if(i == null)
			return null;
		return Double.toString(i)+"c";
	}


	// set value
	public void set(Double value) {
		synchronized(lock) {
			this.value = pxnUtils.MinMax(
					value.doubleValue(),
					-20.0,
					100.0
				);
		}
	}
	public void set(String value) {
		if(value == null || value.isEmpty()) {
			set((Double) null);
			return;
		}
		Double c = null;
		try {
			c = Double.valueOf(value);
		} catch (Exception ignore) {}
		set(c);
	}


//	// set value
//	public void set(Double value, ThermalUnit unit) {
//TODO:
//	}
//	@Override
//	public void set(String value) {
//TODO:
//		synchronized(lock) {
//		}
//	}
//	public void setC(double valueC) {
//		set(valueC, ThermalUnit.CELSIUS);
//	}
//	public void setF(double valueF) {
//		set(valueF, ThermalUnit.FAHRENHEIT);
//	}
//	public void setK(double valueK) {
//		set(valueK, ThermalUnit.KELVIN);
//	}


//	// get value
//	public Double get(ThermalUnit unit) {
//TODO:
//		synchronized(lock) {
//			if(value == null)
//				return null;
//			return value.doubleValue();
//		}
//	}
//	@Override
//	public String toString() {
//		Double c = get(ThermalUnit.CELSIUS);
//		Double f = get(ThermalUnit.FAHRENHEIT);
//		String cStr = (c==null ? "<na>" : Double.toString(c));
//		String fStr = (f==null ? "<na>" : Double.toString(f));
//		return cStr+"C "+fStr+"F";
//	}
//	public double getC() {
//		return get(ThermalUnit.CELSIUS);
//	}
//	public double getF() {
//		return get(ThermalUnit.FAHRENHEIT);
//	}
//	public double getK() {
//		return get(ThermalUnit.KELVIN);
//	}


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
