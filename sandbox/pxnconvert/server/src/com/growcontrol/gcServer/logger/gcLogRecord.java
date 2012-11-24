package com.growcontrol.gcServer.logger;

import java.text.SimpleDateFormat;

import org.fusesource.jansi.Ansi;

import com.growcontrol.gcServer.ntp.gcClock;

public final class gcLogRecord {

//	private java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})?)?[m|K]");
//	private boolean strip = false;

	protected final String msg;
	protected final gcLogger.LEVEL level;
	protected final String postfix;
	protected final long millis;


	gcLogRecord(String msg, gcLogger.LEVEL level, String postfix) {
		if(level == null) throw new NullPointerException("level cannot be null");
		if(msg == null)
			this.msg = "null";
		else
			this.msg = Ansi.ansi().render(msg).toString();
		this.level = level;
		this.postfix = postfix;
		this.millis = gcClock.getTimeMillis();
	}


	public String toString() {
		String line = formatDate(millis)+" "+
			"["+gcLogger.levelToString(level)+"] ";
		if(postfix != null)
			if(!postfix.isEmpty())
				line += "["+postfix+"] ";
		line += msg;
		return line;
	}


	public static String formatDate(long millis) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("D yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(millis);
	}


}
