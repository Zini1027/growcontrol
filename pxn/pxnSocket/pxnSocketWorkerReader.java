package com.poixson.pxnSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import com.poixson.pxnLogger.pxnLogger;


public class pxnSocketWorkerReader extends Thread {

	private pxnSocketWorker worker;
	private BufferedReader in;


	public pxnSocketWorkerReader(pxnSocketWorker worker, Socket socket, BlockingQueue<String> queueIn) {
		super("Socket-Reader-"+Integer.toString(worker.socketId));
		this.worker = worker;
		try {
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			pxnLogger.log().exception(e);
		}
	}


	// reader thread
	@Override
	public void run() {
		pxnLogger.log().info("Connected: "+worker.getIPString());
		String line = "";
		while(!worker.closed) {
			try {
				line = in.readLine();
			} catch (SocketException ignore) {
pxnLogger.log().exception(ignore);
				break;
			} catch (IOException e) {
				e.printStackTrace();
				pxnLogger.log().exception(e);
				break;
			}
			if(line == null) break;
			if(line.isEmpty()) continue;
			worker.processor.processData(line);
		}
		worker.close();
	}


	// receive file
//	private void receiveFileNow() {
//	}


}
