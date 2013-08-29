package com.growcontrol.gcCommon.meta.valueTypes;

import com.growcontrol.gcCommon.meta.metaType;
import com.growcontrol.gcCommon.meta.metaValue;
import com.growcontrol.gcCommon.meta.valueFactory;


public class metaThermalNTC extends metaThermal {
	private static final long serialVersionUID = 9L;


	// static type
	public static final metaType NTC = new metaType("NTC",
		new valueFactory() {
		@Override
		public metaValue newValue() {
			return new metaThermalNTC();
		}
		@Override
		public metaValue newValue(String value) {
			return new metaThermalNTC(value);
		}
	});
	public static void Init() {
		if(NTC == null) System.out.println("Failed to load meta type NTC!");
	}


	// instance
	public metaThermalNTC() {
//		set((Boolean) null);
	}
	public metaThermalNTC(Integer value) {
//		set(value);
	}
	public metaThermalNTC(String value) {
//		set(value);
	}
	public metaThermalNTC(metaValue meta) {
//		if(meta instanceof metaIO)
//			set( ((metaIO) meta).getValue() );
//		else
//			set(meta.getString());
	}
	@Override
	public metaValue clone() {
		return new metaIO(this);
	}


	// type
	@Override
	public metaType getType() {
		return NTC;
	}


//	protected final transient ntcCalc ntc = new ntcCalc();
//	protected int value = 0;


//	public DataThermalNTC setDataValue(int value) {
//		this.dataValue = value;
//		this.value = this.ntc.calc(value);
//		return this;
//	}


}
