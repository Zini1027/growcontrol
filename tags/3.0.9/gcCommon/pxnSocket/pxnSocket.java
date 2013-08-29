package com.growcontrol.gcCommon.pxnSocket;

import com.growcontrol.gcCommon.pxnSocket.pxnSocketUtils.pxnSocketState;
import com.growcontrol.gcCommon.pxnSocket.processor.pxnSocketProcessorFactory;


public interface pxnSocket {

	public void Start();
	public void Close();
	public void ForceClose();
	public pxnSocketState getState();

	public String getHost();
	public void setHost(String host);
	public int getPort();
	public void setPort(int port);
	public pxnSocketProcessorFactory getFactory();
	public void setFactory(pxnSocketProcessorFactory factory);

}
