package com.growcontrol.gcCommon.pxnSocket.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


public class pxnSocketReader extends pxnSocketWorkerThread {
	private final String logName;

	private BufferedReader in = null;


	public pxnSocketReader(pxnSocketWorker worker, Socket socket) {
		super(worker, socket);
		logName = "SocketReader-"+Integer.toString(worker.getSocketId());
		setName(logName);
	}


	// input thread
	@Override
	public void run() {
		if(!getRunLock()) return;
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
			packetsCount++;
			pxnThreadQueue.addToMain(
				"PacketIn-"+Integer.toString(packetsCount),
				new DataThread(line)
			);
			line = null;
		}
		worker.Close();
	}
	@Override
	protected boolean getRunLock() {
		synchronized(runLock) {
			if(in != null) {
				pxnLogger.get(logName).severe("Thread already running, 'in' not null!");
				return false;
			}
			// buffered reader
			try {
				in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				pxnLogger.get(logName).exception(e);
			}
		}
		return true;
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
