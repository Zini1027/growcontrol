package com.growcontrol.gcCommon.meta;

import com.growcontrol.gcCommon.meta.NTC.ntcCalc;


public class DataThermalNTC extends DataThermal {

	protected ntcCalc ntc = new ntcCalc();
	protected int dataValue = 0;


	public DataThermalNTC setDataValue(int value) {
		this.dataValue = value;
		this.value = this.ntc.calc(value);
		return this;
	}


}
