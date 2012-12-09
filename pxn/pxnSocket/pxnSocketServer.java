package com.poixson.pxnSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.poixson.pxnUtils;
import com.poixson.pxnLogger.pxnLogger;


public class pxnSocketServer implements pxnSocket {

	protected final int port;
//	protected final String bind;

	// socket listener
	protected ServerSocket listenerSocket = null;
	protected Thread threadListener = null;

	// processor
	protected pxnSocketProcessorFactory processorFactory = null;

	// socket pool
	protected final List<pxnSocketWorker> socketPool = new ArrayList<pxnSocketWorker>();
//	protected boolean stopping = false;


	// new socket server
	public pxnSocketServer(int port, pxnSocketProcessorFactory processorFactory) {
		// port
		if(port < 1 || port > 65536) {
			pxnLogger.log().severe("Invalid port "+Integer.toString(port)+" is not valid! Out of range!");
			throw new IllegalArgumentException("Invalid port "+Integer.toString(port));
		}
		this.port = port;
		// start listening
		try {
			listenerSocket = new ServerSocket(port, 8);
			pxnLogger.log().info("Listening on port: "+Integer.toString(port));
		} catch (IOException e) {
			pxnLogger.log().severe("Failed to listen on port: "+Integer.toString(port));
			pxnLogger.log().exception(e);
			return;
		}
		// socket listener thread
		threadListener = new Thread() {
			@Override
			public void run() {
				doListenerThread();
			}
		};
		this.processorFactory = processorFactory;
		threadListener.setName("SocketListener");
		threadListener.start();
		pxnUtils.Sleep(10);
	}


	// socket listener thread
	private void doListenerThread() {
System.out.println("LISTENER THREAD RUNNING");
		// loop for new connections
		while(true) {
			flushClosed();
			Socket socket = null;
			try {
				// wait for a connection
				socket = listenerSocket.accept();
			} catch (SocketException ignore) {
pxnLogger.log().exception(ignore);
			} catch (IOException e) {
				pxnLogger.log().exception(e);
			}
//			// closing sockets
//			if(stopping) {
//				pxnLogger.log().info("Stopping socket listener..");
//				break;
//			}
			// add socket to pool
			if(socket != null)
				socketPool.add(new pxnSocketWorker(socket, processorFactory.newProcessor() ));
		}
	}


	// flush closed sockets from pool
	@Override
	public void flushClosed() {
//		for(Iterator<socketWorker> it = socketPool.iterator(); it.hasNext();)
//			if(it.next().isClosed())
//				it.remove();
//		gcServer.log.debug("Sockets loaded: "+Integer.toString(socketPool.size()));
	}


	@Override
	public void stop() {
//		stopping = true;
//		// stop listening
//		if(listenerSocket != null) {
//			try {
//				listenerSocket.close();
//			} catch (IOException e) {
//				gcServer.log.exception(e);
//			}
//		}
//		// close sockets
//		for(Iterator<socketWorker> it = socketPool.iterator(); it.hasNext();) {
//			it.next().close();
//		}
	}


}
