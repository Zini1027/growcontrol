package com.growcontrol.gcCommon.pxnSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


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
	protected boolean stopping = false;


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
			pxnLogger.getLogger().severe("Invalid port "+Integer.toString(port)+" is not valid! Out of range!");
			throw new IllegalArgumentException("Invalid port "+Integer.toString(port));
		}
		this.port = port;
		// start listening
		try {
			listenerSocket = new ServerSocket(port, 8);
			pxnLogger.getLogger().info("Listening on port: "+Integer.toString(port));
		} catch (IOException e) {
			pxnLogger.getLogger().severe("Failed to listen on port: "+Integer.toString(port));
			pxnLogger.getLogger().exception(e);
			return;
		}
		// socket listener thread
		threadListener = new Thread("Socket-Server-Listener-"+Integer.toString(port)) {
			@Override
			public void run() {
				startListenerThread();
			}
		};
		threadListener.start();
		pxnUtils.Sleep(10);
	}


	// socket listener thread
	private void startListenerThread() {
		// loop for new connections
		while(!stopping) {
			// queue flushing closed sockets
			pxnThreadQueue.getMainThread().addQueue(new Runnable() {
				@Override
				public void run() {
					flushClosed();
				}
			});
			Socket socket = null;
			try {
				// wait for a connection
				socket = listenerSocket.accept();
			} catch (SocketException ignore) {
				// socket listener closed
				stopping = true;
				break;
			} catch (IOException e) {
				pxnLogger.getLogger().exception(e);
			}
			// add socket to pool
			if(socket != null) {
				synchronized(socketWorkers) {
					socketWorkers.add(new pxnSocketWorker(socket, processorFactory.newProcessor()));
				}
			}
		}
		// stopping socket listener
		pxnLogger.getLogger().info("Stopping socket listener..");
		if(listenerSocket != null && !listenerSocket.isClosed()) {
			try {
				listenerSocket.close();
			} catch (IOException e) {
e.printStackTrace();
			}
		}
	}


	// flush closed sockets from pool
	public void flushClosed() {
		int flushCount = 0;
		synchronized(socketWorkers) {
			for(Iterator<pxnSocketWorker> it = socketWorkers.iterator(); it.hasNext();) {
				if(it.next().isClosed()) {
					it.remove();
					flushCount++;
				}
			}
		}
		pxnLogger.getLogger().debug("Sockets loaded: "+Integer.toString(socketWorkers.size()));
		if(flushCount > 0)
			pxnLogger.getLogger().info("Flushed [ "+Integer.toString(flushCount)+" ] closed sockets.");
	}


	// close socket
	@Override
	public void close() {
	}
//	public void close(int socketId) {
//	}
	@Override
	public void forceCloseAll() {
		synchronized(socketWorkers) {
			for(pxnSocketWorker worker : socketWorkers)
				worker.close();
		}
		flushClosed();
	}
	@Override
	public void stop() {
		stopping = true;
		// stop listening
		if(listenerSocket != null) {
			try {
				listenerSocket.close();
			} catch (IOException e) {
pxnLogger.getLogger().exception(e);
			}
		}
	}


}
