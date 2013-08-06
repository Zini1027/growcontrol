package com.growcontrol.gcCommon.pxnLogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnClock.pxnClock;


public final class pxnLogRecord implements java.io.Serializable {
	private static final long serialVersionUID = 6L;

	private final String msg;
	private final Throwable ex;
	private final pxnLevel level;
	private final long millis;
	private final String[] msgBuilt;
	private final transient pxnLogger log;


	public static pxnLogRecord Create(pxnLogger log, pxnLevel level, String msg) {
		return new pxnLogRecord(log, level, msg, null);
	}
	public static pxnLogRecord Create(pxnLogger log, pxnLevel level, String msg, Throwable ex) {
		return new pxnLogRecord(log, level, msg, ex);
	}
	private pxnLogRecord(pxnLogger log, pxnLevel level, String msg, Throwable ex) {
		if(log   == null) throw new NullPointerException("log cannot be null!");
		if(level == null) throw new NullPointerException("level cannot be null!");
		if(msg == null) msg = "<null>";
		this.msg = msg;
		this.level = level;
		this.log = log;
		this.millis = pxnClock.get().Millis();
		this.ex = ex;
		msgBuilt = BuildMessage();
	}


//TODO: add formatter class
	private synchronized String[] BuildMessage() {
		List<String> builder = new ArrayList<String>();
		// message
		if(msg != null) {
			StringBuilder line = new StringBuilder();
			line.append(formatDate(millis)).append(" ").append("[").append(level.toString()).append("] ");
			// get log name tree
			String logTree = "";
			if(this.log != null && !this.log.getName().equals(pxnLog.MainLoggerName)) {
				logTree = this.log.getNameFormatted()+" ";
				pxnLogger p = log.getParent();
				while(p != null) {
					if(p.getName().equals(pxnLog.MainLoggerName))
						break;
					logTree = p.getNameFormatted()+logTree;
					p = p.getParent();
				}
			}
			// build message
			line.append(logTree).append(msg);
			builder.add(line.toString());
		}
		// exception
		if(ex != null)
			builder.add(pxnUtils.ExceptionToString(ex));
		// finished
		return builder.toArray(new String[0]);
	}


	// format date
	public static String formatDate(long millis) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("D yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(millis);
	}


	// get stored vars
	public String[] getMessage() {
		return msgBuilt;
	}
	public String getMsg() {
		return msg;
	}
	public Throwable getThrowable() {
		return ex;
	}
	public pxnLevel getLevel() {
		return level;
	}


}
