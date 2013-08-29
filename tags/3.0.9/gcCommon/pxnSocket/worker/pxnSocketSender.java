package com.growcontrol.gcCommon.pxnSocket.worker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.growcontrol.gcCommon.TimeU;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;


public class pxnSocketSender extends pxnSocketWorkerThread {
	private static final String EOL = "\r\n";
	private final String logName;

	private volatile BlockingQueue<String> queueOut = null;
	private volatile PrintWriter out = null;

	private volatile Boolean running = false;


	public pxnSocketSender(pxnSocketWorker worker, Socket socket) {
		super(worker, socket);
		logName = "SocketSender-"+Integer.toString(worker.getSocketId());
		setName(logName);
		// blocking queue
		queueOut = new ArrayBlockingQueue<String>(100,  true);
		// output stream
		try {
			out = new PrintWriter(socket.getOutputStream());
			out.flush();
		} catch (IOException e) {
			pxnLog.get(logName).exception(e);
		}
	}


	// output thread
	@Override
	public void run() {
		synchronized(running) {
			if(running) {
				pxnLog.get(logName).severe("Thread already running!");
				return;
			}
			running = true;
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
			packetsCount++;
			out.print(line+EOL);
			out.flush();
			line = null;
//	// send file
//	if(line.startsWith("SENDFILE:")) {
//		sendFileNow(line.substring(9).trim());
		}
		worker.Close();
		running = false;
	}


	@Override
	public void Closing() {
		super.Closing();
		synchronized(running) {
			queueOut.clear();
			queueOut = null;
		}
	}


	// add to send/out queue (returns false if failed)
	public boolean Send(String line) {
//		synchronized(running) {
//			if(!running) {
//				pxnLogger.get(logName).warning("Can't send packet, not connected!");
//				return false;
//			}
//		}
		try {
			if(queueOut.offer(line, 1, TimeU.S))
				return true;
			pxnLog.get(logName).severe("Queue is full!");
		} catch (InterruptedException ignore) {}
		return false;
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
