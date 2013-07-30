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

	private pxnSocketWorker worker;
	private BufferedReader in;


	public pxnSocketReader(pxnSocketWorker worker, Socket socket) {
		if(worker == null) throw new NullPointerException("worker cannot be null!");
		if(socket == null) throw new NullPointerException("socket cannot be null!");
		logName = "SocketReader-"+Integer.toString(worker.getSocketId());
		setName(logName);
		this.worker = worker;
		try {
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			pxnLogger.get(logName).exception(e);
		}
	}


	// input thread
	@Override
	public void run() {
		int packetCount = 0;
		while(!worker.isClosed()) {
			String line = null;
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
			packetCount++;
			pxnThreadQueue.addToMain(
				"Packet-"+Integer.toString(packetCount),
				new DataThread(line)
			);
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


	// receive file
//	private void receiveFileNow() {
//	}


}
