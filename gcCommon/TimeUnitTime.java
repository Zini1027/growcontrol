package com.growcontrol.gcCommon;

import java.util.concurrent.TimeUnit;


public class TimeUnitTime {

	protected volatile long value = 0;
	protected volatile boolean isFinal = false;


	// new time holder
	public TimeUnitTime(long value, TimeUnit unit) {
		this.set(value, unit);
	}
	public TimeUnitTime(TimeUnitTime value) {
		set(value);
	}
	public TimeUnitTime() {
	}


	// set time
	public void set(long value, TimeUnit unit) {
		if(isFinal) return;
		if(unit == null) {
			this.value = 0;
			return;
		}
		this.value = TimeUnit.MILLISECONDS.convert(value, unit);
	}
	public void set(TimeUnitTime value) {
		if(value == null) return;
		set(value.get(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
	}


	// get time
	public long get(TimeUnit unit) {
		if(unit == null)
			return 0;
		return unit.convert(this.value, TimeUnit.MILLISECONDS);
	}
	@Override
	public String toString() {
		return Long.toString(get(TimeU.S))+"s";
	}


	// final value
	public TimeUnitTime setFinal() {
		this.isFinal = true;
		return this;
	}
	public boolean isFinal() {
		return this.isFinal;
	}


	// parse time duration
	// example: "5s 3h 10d"
	public static TimeUnitTime Parse(String str) {
		if(str == null) throw new NullPointerException("str cannot be null!");
		if(str.isEmpty()) return null;
		long value = 0;
		int v = 0;
		for(int i=0; i<str.length(); i++) {
			String chr = Character.toString(str.charAt(i));
			if(chr.equals(" ")) {
				v = 0;
				continue;
			}
			try {
				v = (v * 10) + Integer.parseInt(chr);
			} catch(Exception ignore) {
				if(v == 0) continue;
				chr = chr.toLowerCase();
				if(chr.equals("n"))
					value += TimeU.MS.convert(v, TimeUnit.MILLISECONDS);
				else
				if(chr.equals("s"))
					value += TimeU.MS.convert(v, TimeUnit.SECONDS);
				else
				if(chr.equals("m"))
					value += TimeU.MS.convert(v, TimeUnit.MINUTES);
				else
				if(chr.equals("h"))
					value += TimeU.MS.convert(v, TimeUnit.HOURS);
				else
				if(chr.equals("d"))
					value += TimeU.MS.convert(v, TimeUnit.DAYS);
				else
				if(chr.equals("w"))
					value += (TimeU.MS.convert(v, TimeUnit.DAYS) * 7);
				else
				if(chr.equals("m"))
					value += (TimeU.MS.convert(v, TimeUnit.DAYS) * 30);
				else
				if(chr.equals("y"))
					value += (TimeU.MS.convert(v, TimeUnit.DAYS) * 365);
				// reset temp value
				v = 0;
			}
		}
		return new TimeUnitTime(value, TimeU.MS);
	}


}
