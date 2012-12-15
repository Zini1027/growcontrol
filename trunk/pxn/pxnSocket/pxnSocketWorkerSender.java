package com.poixson.pxnSocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import com.poixson.pxnLogger.pxnLogger;


public class pxnSocketWorkerSender extends Thread {

	private pxnSocketWorker worker;
	private Socket socket;
	private PrintWriter out;
	private BlockingQueue<String> queueOut;


	public pxnSocketWorkerSender(pxnSocketWorker worker, Socket socket, BlockingQueue<String> queueOut) {
		this.worker = worker;
		this.socket = socket;
		try {
			out = new PrintWriter(socket.getOutputStream());
			out.flush();
		} catch (IOException e) {
			pxnLogger.log().exception(e);
		}
		this.queueOut = queueOut;
	}


	// sender thread
	@Override
	public void run() {
		while(!worker.closed) {
			try {
				String line = queueOut.take();
				// send file
				if(line.startsWith("SENDFILE:")) {
					sendFileNow(line.substring(9).trim());
				} else {
					// send string
					out.println(line);
					out.flush();
				}
			} catch (InterruptedException e) {
				pxnLogger.log().exception(e);
			}
		}
	}


	// send file
	public void sendFileNow(String fileName) {
		pxnLogger.log().info("Sending file: "+fileName);
		File file = new File(fileName);
		final int bufferSize = 1000;
		byte[] buffer = new byte[bufferSize];
		try {
			FileInputStream fileStream = new FileInputStream(file);
			BufferedInputStream  inputStream  = new BufferedInputStream(fileStream);
			BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
			long fileSize = file.length();
			long bytesSent = 0;
System.out.println("Sending file size: "+Long.toString(fileSize));
			while(fileSize > bytesSent) {
//				pxnUtils.Sleep(100);
				// chunk size
				int size = (int) (
					(fileSize - bytesSent > buffer.length) ?
						buffer.length :
						fileSize - bytesSent
					);
//System.out.println("Sending [ "+Long.toString(size)+" ] bytes!");
				// get chunk from file
				inputStream.read(buffer, 0, size);
				// send chunk
				outputStream.write(buffer, 0, size);
				outputStream.flush();
				bytesSent += buffer.length;
System.out.println("Sent [ "+Long.toString(bytesSent)+" ] bytes!");
			}
System.out.println("Finished [ "+Long.toString(bytesSent)+" ] bytes!");
			inputStream.close();
			inputStream = null;
			fileStream = null;
			file = null;
		} catch (FileNotFoundException e) {
			pxnLogger.log().exception(e);
		} catch (IOException e) {
			pxnLogger.log().exception(e);
		}
	}


}
