package com.growcontrol.gcCommon.pxnLogger;


public abstract class pxnLogPrinter implements pxnLoggerInterface {


	@Override
	public abstract void Publish(pxnLevel level, String msg);
	@Override
	public abstract void Publish(pxnLevel level, String msg, Throwable ex);


	// print writers
	@Override
	public void fatal(String msg) {
		Publish(pxnLevel.FATAL,   msg);
	}
	@Override
	public void severe(String msg) {
		Publish(pxnLevel.SEVERE,  msg);
	}
	@Override
	public void warning(String msg) {
		Publish(pxnLevel.WARNING, msg);
	}
	@Override
	public void info(String msg) {
		Publish(pxnLevel.INFO,    msg);
	}
	@Override
	public void config(String msg) {
		Publish(pxnLevel.CONFIG,  msg);
	}
	@Override
	public void debug(String msg) {
		Publish(pxnLevel.DEBUG,   msg);
	}
	@Override
	public void fine(String msg) {
		Publish(pxnLevel.FINE,    msg);
	}
	@Override
	public void finer(String msg) {
		Publish(pxnLevel.FINER,   msg);
	}
	@Override
	public void finest(String msg) {
		Publish(pxnLevel.FINEST,  msg);
	}


	// exception
	@Override
	public void exception(Throwable ex) {
		Publish(pxnLevel.ALL, "EXCEPTION", ex);
	}


	// message with exception
	@Override
	public void exception(String msg, Throwable ex) {
		Publish(pxnLevel.ALL,     msg, ex);
	}
	@Override
	public void fatal(String msg, Throwable ex) {
		Publish(pxnLevel.FATAL,   msg, ex);
	}
	@Override
	public void severe(String msg, Throwable ex) {
		Publish(pxnLevel.SEVERE,  msg, ex);
	}
	@Override
	public void warning(String msg, Throwable ex) {
		Publish(pxnLevel.WARNING, msg, ex);
	}
	@Override
	public void info(String msg, Throwable ex) {
		Publish(pxnLevel.INFO,    msg, ex);
	}
	@Override
	public void config(String msg, Throwable ex) {
		Publish(pxnLevel.CONFIG,  msg, ex);
	}
	@Override
	public void debug(String msg, Throwable ex) {
		Publish(pxnLevel.DEBUG,   msg, ex);
	}
	@Override
	public void fine(String msg, Throwable ex) {
		Publish(pxnLevel.FINE,    msg, ex);
	}
	@Override
	public void finer(String msg, Throwable ex) {
		Publish(pxnLevel.FINER,   msg, ex);
	}
	@Override
	public void finest(String msg, Throwable ex) {
		Publish(pxnLevel.FINEST,  msg, ex);
	}


}
