package com.growcontrol.gcCommon.pxnSocket.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public class pxnSocketReader extends Thread {

	private pxnSocketWorker worker;
	private BufferedReader in;


	public pxnSocketReader(pxnSocketWorker worker, Socket socket) {
		setName("SocketReader-"+Integer.toString(worker.socketId));
		this.worker = worker;
		try {
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			pxnLogger.get().exception(e);
		}
	}


	// reader thread
	@Override
	public void run() {
		pxnLogger.get().info("Connected: "+worker.getIPString());
		String line = "";
		while(!worker.closed) {
			try {
				line = in.readLine();
			} catch (SocketException ignore) {
				// socket closed
				break;
			} catch (IOException e) {
				pxnLogger.get().exception(e);
				break;
			}
			if(line == null) break;
			if(line.isEmpty()) continue;
			try {
				worker.processor.processData(line);
			} catch (Exception e) {
				pxnLogger.get().exception(e);
			}
		}
		worker.close();
	}


	// receive file
//	private void receiveFileNow() {
//	}


}
