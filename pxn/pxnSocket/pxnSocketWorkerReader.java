package com.poixson.pxnSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import com.poixson.pxnLogger.pxnLogger;


public class pxnSocketWorkerReader extends Thread {

	private pxnSocketWorker worker;
	private BufferedReader in;


	public pxnSocketWorkerReader(pxnSocketWorker worker, Socket socket) {
		super("Socket-Reader-"+Integer.toString(worker.socketId));
		this.worker = worker;
		try {
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			pxnLogger.getLogger().exception(e);
		}
	}


	// reader thread
	@Override
	public void run() {
		pxnLogger.getLogger().info("Connected: "+worker.getIPString());
		String line = "";
		while(!worker.closed) {
			try {
				line = in.readLine();
			} catch (SocketException ignore) {
				// socket closed
				break;
			} catch (IOException e) {
e.printStackTrace();
break;
			}
			if(line == null) break;
			if(line.isEmpty()) continue;
			try {
				worker.processor.processData(line);
			} catch (Exception e) {
e.printStackTrace();
			}
		}
		worker.close();
	}


	// receive file
//	private void receiveFileNow() {
//	}


}
