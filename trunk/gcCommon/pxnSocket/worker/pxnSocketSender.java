package com.growcontrol.gcCommon.pxnSocket.worker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.growcontrol.gcCommon.TimeU;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public class pxnSocketSender extends Thread {
	private final String logName;
	private static final String EOL = "\r\n";

	private final pxnSocketWorker worker;
	private final Socket socket;

	private BlockingQueue<String> queueOut = null;
	private PrintWriter out = null;
	private volatile int countPacketsSent = 0;

	private final Object runLock = new Object();


	public pxnSocketSender(pxnSocketWorker worker, Socket socket) {
		if(worker == null) throw new NullPointerException("worker cannot be null!");
		if(socket == null) throw new NullPointerException("socket cannot be null!");
		logName = "SocketSender-"+Integer.toString(worker.getSocketId());
		setName(logName);
		this.worker = worker;
		this.socket = socket;
	}


	// output thread
	@Override
	public void run() {
		synchronized(runLock) {
			if(queueOut != null) {
				pxnLogger.get(logName).exception(
					new Exception("Thread already running, queue not null!"));
				return;
			}
			queueOut = new ArrayBlockingQueue<String>(100,  true);
		}
		// output stream
		if(out == null) {
			try {
				out = new PrintWriter(socket.getOutputStream());
				out.flush();
			} catch (IOException e) {
				pxnLogger.get(logName).exception(e);
			}
		}
		String line = null;
		while(!worker.isClosed()) {
			try {
				line = queueOut.poll(100, TimeU.MS);
			} catch (InterruptedException ignore) {
				// closing interrupt
				break;
			}
			if(line == null) continue;
			countPacketsSent++;
			out.print(line+EOL);
			out.flush();
			line = null;
//	// send file
//	if(line.startsWith("SENDFILE:")) {
//		sendFileNow(line.substring(9).trim());
		}
		worker.Close();
	}


	public void Closing() {
		this.interrupt();
		synchronized(runLock) {
			queueOut.clear();
			queueOut = null;
		}
	}


	// add to send/out queue (returns false if failed)
	public boolean Send(String line) {
		try {
			if(queueOut.offer(line, 1, TimeU.S))
				return true;
			pxnLogger.get(logName).severe("Queue is full!");
		} catch (InterruptedException ignore) {}
		return false;
	}


	public int getPacketsCount() {
		return countPacketsSent;
	}


//	// send file
//	public void sendFileNow(String fileName) {
//		pxnLogger log = pxnLogger.get(logName);
//		log.info("Sending file: "+fileName);
//		File file = new File(fileName);
//		final int bufferSize = 1000;
//		byte[] buffer = new byte[bufferSize];
//		try {
//			FileInputStream fileStream = new FileInputStream(file);
//			BufferedInputStream  inputStream  = new BufferedInputStream(fileStream);
//			BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
//			long fileSize = file.length();
//			long bytesSent = 0;
//log.debug("Sending file size: "+Long.toString(fileSize));
//			while(fileSize > bytesSent) {
////				pxnUtils.Sleep(100);
//				// chunk size
//				int size = (int) (
//					(fileSize - bytesSent > buffer.length) ?
//						buffer.length :
//						fileSize - bytesSent
//					);
////log.debug("Sending [ "+Long.toString(size)+" ] bytes!");
//				// get chunk from file
//				inputStream.read(buffer, 0, size);
//				// send chunk
//				outputStream.write(buffer, 0, size);
//				outputStream.flush();
//				bytesSent += buffer.length;
//log.debug("Sent [ "+Long.toString(bytesSent)+" ] bytes!");
//			}
//log.debug("Finished [ "+Long.toString(bytesSent)+" ] bytes!");
//			inputStream.close();
//			inputStream = null;
//			fileStream = null;
//			file = null;
//		} catch (FileNotFoundException e) {
//			log.exception(e);
//		} catch (IOException e) {
//			log.exception(e);
//		}
//	}


}
