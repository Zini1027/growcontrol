package com.poixson.pxnLogger;

import java.text.SimpleDateFormat;

import org.fusesource.jansi.Ansi;

import com.poixson.pxnClock.pxnClock;


public final class pxnLogRecord {

//	private java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})?)?[m|K]");
//	private boolean strip = false;

	public final String msg;
	public final pxnLevel.LEVEL level;
	public final String loggerName;
	public final long millis;


	pxnLogRecord(String msg, pxnLevel.LEVEL level, String loggerName) {
		if(level == null) throw new NullPointerException("level cannot be null");
		if(msg == null)
			this.msg = "null";
		else
			this.msg = Ansi.ansi().render(msg).toString();
		this.level = level;
		this.loggerName = loggerName;
		this.millis = pxnClock.getClock().getTimeMillis();
	}


	// build log line
	public String toString() {
		String line = formatDate(millis)+" "+
			"["+pxnLevel.levelToString(level)+"] ";
		if(loggerName != null)
			if(!loggerName.isEmpty())
				line += "["+loggerName+"] ";
		line += msg;
		return line;
	}


	// format date
	public static String formatDate(long millis) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("D yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(millis);
	}


}
