package com.growcontrol.gcCommon.pxnSocket.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


public class pxnSocketReader extends Thread {
	private final String logName;

	private final pxnSocketWorker worker;
	private final Socket socket;

	private BufferedReader in = null;
	private volatile int countPacketsRead = 0;

	private final Object runLock = new Object();


	public pxnSocketReader(pxnSocketWorker worker, Socket socket) {
		if(worker == null) throw new NullPointerException("worker cannot be null!");
		if(socket == null) throw new NullPointerException("socket cannot be null!");
		logName = "SocketReader-"+Integer.toString(worker.getSocketId());
		setName(logName);
		this.worker = worker;
		this.socket = socket;
	}


	// input thread
	@Override
	public void run() {
		synchronized(runLock) {
			if(in != null) {
				pxnLogger.get(logName).exception(
					new Exception("Thread already running, in buffered reader not null!"));
				return;
			}
			try {
				in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				pxnLogger.get(logName).exception(e);
			}

		}
		String line = null;
		while(!worker.isClosed()) {
			try {
				line = in.readLine();
			} catch (SocketException ignore) {
				// socket closed
				break;
			} catch (IOException e) {
				pxnLogger.get(logName).exception(e);
				break;
			}
			if(line == null) break;
			if(line.isEmpty()) continue;
			countPacketsRead++;
			pxnThreadQueue.addToMain(
				"PacketIn-"+Integer.toString(countPacketsRead),
				new DataThread(line)
			);
			line = null;
		}
		worker.Close();
	}
	protected class DataThread implements Runnable {
		private final String line;
		public DataThread(String line) {
			this.line = line;
		}
		@Override
		public void run() {
			worker.doProcessData(line);
		}
	}


	public void Closing() {
		this.interrupt();
	}


	public int getPacketsCount() {
		return countPacketsRead;
	}


	// receive file
//	private void receiveFileNow() {
//	}


}
