package com.growcontrol.gcCommon.meta.valueTypes;


public class ntcCalc {

	protected double polyA = 0.0;
	protected double polyB = 0.0;
	protected double polyC = 0.0;


	public void Calibrate(
			int dataValue1, double tempK1,
			int dataValue2, double tempK2,
			int dataValue3, double tempK3) {
		try {
			// pre-calculate values
			double resist1 = calcResistance(dataValue1);
			double resist2 = calcResistance(dataValue2);
			double resist3 = calcResistance(dataValue3);
			double log1 = Math.log(resist1);
			double log2 = Math.log(resist2);
			double log3 = Math.log(resist3);
			// calculate
			double z = log1 - log2;
			double Y = log1 - log3;
			double X = (1.0 / tempK1) - (1.0 / tempK2);
			double w = (1.0 / tempK1) - (1.0 / tempK3);
			double v = Math.pow(log1, 3) - Math.pow(log2, 3);
			double u = Math.pow(log1, 3) - Math.pow(log3, 3);
			this.polyC = (X - ((z * w) / Y)) / (v - ((z * u) / Y));
			this.polyB = (X - (this.polyC * v)) / z;
			this.polyA = (1 / tempK1) - (this.polyC * Math.pow(log1, 3)) - (this.polyB * log1);
		} catch(Exception ignore) {
			this.polyA = 0.0;
			this.polyB = 0.0;
			this.polyC = 0.0;
		}
	}


	// adc value to resistance
	public double calcResistance(double value) {
		return (102400000.0 / value) - 100000.0;
	}


	public double calc(int value) {
		double resist = calcResistance((double) value);
		double resistLog = Math.log(resist);
		double x = 1 / (
				polyA +
				(polyB * resistLog) +
				(polyC * Math.pow(resistLog, 3))
			);
		return x - 273.15;
	}


	public double getPolyA() {
		return this.polyA;
	}
	public double getPolyB() {
		return this.polyB;
	}
	public double getPolyC() {
		return this.polyC;
	}


}
