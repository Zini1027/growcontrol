package com.growcontrol.gcCommon.pxnLogger.handlers;

import com.growcontrol.gcCommon.pxnLogger.pxnLogRecord;


public interface pxnLogHandler {

	public String getName();

	public void Publish(pxnLogRecord rec);
	public void Publish(String msg);

	public void Flush();
	public void Close();

}
