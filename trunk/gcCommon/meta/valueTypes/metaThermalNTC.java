package com.growcontrol.gcCommon.meta.valueTypes;


public class metaThermalNTC extends metaThermal {
	private static final long serialVersionUID = 7L;

	protected final transient ntcCalc ntc = new ntcCalc();
//	protected int value = 0;


//	public metaThermalNTC(String name) {
//		super(name);
//	}


//	public DataThermalNTC setDataValue(int value) {
//		this.dataValue = value;
//		this.value = this.ntc.calc(value);
//		return this;
//	}


}
