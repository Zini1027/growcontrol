package com.growcontrol.gcCommon.pxnSocket.worker;

import java.net.Socket;


public abstract class pxnSocketWorkerThread extends Thread {

	protected final pxnSocketWorker worker;
	protected final Socket socket;

	protected volatile int packetsCount = 0;


	public pxnSocketWorkerThread(pxnSocketWorker worker, Socket socket) {
		if(worker == null) throw new NullPointerException("worker cannot be null!");
		if(socket == null) throw new NullPointerException("socket cannot be null!");
		this.worker = worker;
		this.socket = socket;
	}


	public void Closing() {
		this.interrupt();
	}


	public int getPacketsCount() {
		return packetsCount;
	}


}
