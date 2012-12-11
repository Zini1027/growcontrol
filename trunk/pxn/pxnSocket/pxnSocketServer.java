package com.poixson.pxnSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.poixson.pxnUtils;
import com.poixson.pxnLogger.pxnLogger;


public class pxnSocketServer implements pxnSocket {

//TODO:
//	protected final String bindHost;
	protected final int port;

	// socket listener
	protected ServerSocket listenerSocket = null;
	protected Thread threadListener = null;

	// processor
	protected final pxnSocketProcessorFactory processorFactory;

	// socket pool
	protected final List<pxnSocketWorker> socketWorkers = new ArrayList<pxnSocketWorker>();
//	protected boolean stopping = false;


	// new socket server
	public pxnSocketServer(int port, pxnSocketProcessorFactory processorFactory) {
		this(null, port, processorFactory);
	}
	public pxnSocketServer(String bindHost, int port, pxnSocketProcessorFactory processorFactory) {
		this.processorFactory = processorFactory;
		// bind to host
		if(bindHost != null && !bindHost.isEmpty()) {
//TODO:
		}
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
		threadListener.setName("SocketListener");
		threadListener.start();
		pxnUtils.Sleep(10);
	}


	// socket listener thread
	private void doListenerThread() {
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
				socketWorkers.add(new pxnSocketWorker(socket, processorFactory.newProcessor() ));
		}
	}


	// flush closed sockets from pool
	public void flushClosed() {
		for(Iterator<pxnSocketWorker> it = socketWorkers.iterator(); it.hasNext();)
			if(it.next().isClosed())
				it.remove();
		pxnLogger.log().debug("Sockets loaded: "+Integer.toString(socketWorkers.size()));
	}


	// close socket
	@Override
	public void close() {
	}
//	public void close(int socketId) {
//	}
//	public void closeAll() {
//	}
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
