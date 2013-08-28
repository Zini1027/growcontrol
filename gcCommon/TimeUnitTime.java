package com.growcontrol.gcCommon;

import java.util.concurrent.TimeUnit;


public class TimeUnitTime {

	public static final long MSEC = 1;
	public static final long SEC  = MSEC * 1000;
	public static final long MIN  = SEC  * 60;
	public static final long HOUR = MIN  * 60;
	public static final long DAY  = HOUR * 24;
	public static final long WEEK = DAY  * 7;
	public static final long MONTH= DAY  * 30;
	public static final long YEAR = DAY  * 365;

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
	@Override
	public TimeUnitTime clone() {
		return new TimeUnitTime(this);
	}


	// set time
	public void set(long value, TimeUnit unit) {
		if(isFinal) return;
		if(unit == null) {
			this.value = 0;
			return;
		}
		this.value = TimeU.MS.convert(value, unit);
	}
	public void set(TimeUnitTime value) {
		if(value == null) return;
		set(value.get(TimeU.MS), TimeU.MS);
	}


	// get time
	public long get(TimeUnit unit) {
		if(unit == null)
			return 0;
		return unit.convert(this.value, TimeU.MS);
	}
	// time to string
	public String getString() {
		long l = get(TimeU.MS);
		String str = "";
		// year
		if(l > YEAR) {
			str = pxnUtils.addStringSet(str, buildString(l, YEAR, "y"), " ");
			l = l % YEAR;
		}
		// week
		if(l > WEEK) {
			str = pxnUtils.addStringSet(str, buildString(l, WEEK, "w"), " ");
			l = l % WEEK;
		}
		// day
		if(l > DAY) {
			str = pxnUtils.addStringSet(str, buildString(l, DAY, "d"), " ");
			l = l % DAY;
		}
		// hour
		if(l > HOUR) {
			str = pxnUtils.addStringSet(str, buildString(l, HOUR, "h"), " ");
			l = l % HOUR;
		}
		// minute
		if(l > MIN) {
			str = pxnUtils.addStringSet(str, buildString(l, MIN, "m"), " ");
			l = l % MIN;
		}
		// second
		if(l > SEC) {
			str = pxnUtils.addStringSet(str, buildString(l, SEC, "s"), " ");
			l = l % SEC;
		}
		// ms
		if(l > MSEC) {
			str = pxnUtils.addStringSet(str, buildString(l, MSEC, "n"), " ");
		}
		if(str.isEmpty())
			return null;
		return str;
	}
	private String buildString(long value, long unitValue, String unit) {
		value = (long) Math.floor(
			((double)value) / ((double)unitValue)
		);
		if(value <= 0)
			return null;
		return Long.toString(value)+unit;
	}
	public static String toString(TimeUnitTime value) {
		if(value == null)
			return null;
		return value.getString();
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
					value += TimeU.MS.convert(v, TimeU.MS);
				else
				if(chr.equals("s"))
					value += TimeU.MS.convert(v, TimeU.S);
				else
				if(chr.equals("m"))
					value += TimeU.MS.convert(v, TimeU.M);
				else
				if(chr.equals("h"))
					value += TimeU.MS.convert(v, TimeU.H);
				else
				if(chr.equals("d"))
					value += TimeU.MS.convert(v, TimeU.D);
				else
				if(chr.equals("w"))
					value += (TimeU.MS.convert(v, TimeU.D) * 7);
				else
				if(chr.equals("m"))
					value += (TimeU.MS.convert(v, TimeU.D) * 30);
				else
				if(chr.equals("y"))
					value += (TimeU.MS.convert(v, TimeU.D) * 365);
				// reset temp value
				v = 0;
			}
		}
		return new TimeUnitTime(value, TimeU.MS);
	}


}
