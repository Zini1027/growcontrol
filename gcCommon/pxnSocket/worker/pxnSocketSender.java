package com.growcontrol.gcCommon.pxnSocket.worker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public class pxnSocketSender extends Thread {
	private static final String EOL = "\r\n";

	private pxnSocketWorker worker;
	private Socket socket;
	private PrintWriter out;
	private BlockingQueue<String> queueOut;


	public pxnSocketSender(pxnSocketWorker worker, Socket socket) {
		setName("SocketSender-"+Integer.toString(worker.socketId));
		this.worker = worker;
		this.socket = socket;
		try {
			out = new PrintWriter(socket.getOutputStream());
			out.flush();
		} catch (IOException e) {
			pxnLogger.get().exception(e);
		}
		this.queueOut = worker.processor.getOutputQueue();
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
					out.print(line + EOL);
					out.flush();
				}
			} catch (InterruptedException e) {
				// socket closed
				break;
			}
		}
	}


	// send file
	public void sendFileNow(String fileName) {
		pxnLogger log = pxnLogger.get();
		log.info("Sending file: "+fileName);
		File file = new File(fileName);
		final int bufferSize = 1000;
		byte[] buffer = new byte[bufferSize];
		try {
			FileInputStream fileStream = new FileInputStream(file);
			BufferedInputStream  inputStream  = new BufferedInputStream(fileStream);
			BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
			long fileSize = file.length();
			long bytesSent = 0;
log.debug("Sending file size: "+Long.toString(fileSize));
			while(fileSize > bytesSent) {
//				pxnUtils.Sleep(100);
				// chunk size
				int size = (int) (
					(fileSize - bytesSent > buffer.length) ?
						buffer.length :
						fileSize - bytesSent
					);
//log.debug("Sending [ "+Long.toString(size)+" ] bytes!");
				// get chunk from file
				inputStream.read(buffer, 0, size);
				// send chunk
				outputStream.write(buffer, 0, size);
				outputStream.flush();
				bytesSent += buffer.length;
log.debug("Sent [ "+Long.toString(bytesSent)+" ] bytes!");
			}
log.debug("Finished [ "+Long.toString(bytesSent)+" ] bytes!");
			inputStream.close();
			inputStream = null;
			fileStream = null;
			file = null;
		} catch (FileNotFoundException e) {
			log.exception(e);
		} catch (IOException e) {
			log.exception(e);
		}
	}


}
