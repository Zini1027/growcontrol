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
import com.growcontrol.gcCommon.pxnSocket.pxnSocketUtils.pxnSocketState;
import com.growcontrol.gcCommon.pxnSocket.processor.pxnSocketProcessorFactory;
import com.growcontrol.gcCommon.pxnSocket.worker.pxnSocketWorker;


public class pxnSocketServer implements pxnSocket {
	private static final String logName = "SocketServer";

	private volatile String host = null;
	private volatile int port = 0;

	// socket state
	private volatile pxnSocketState state = pxnSocketState.CLOSED;
	private volatile boolean stopping = false;

	// listener socket
	private ServerSocket listenerSocket = null;
	private Thread listenerThread = null;

	// workers
	private final List<pxnSocketWorker> workers = new ArrayList<pxnSocketWorker>();
	// processor factory
	private pxnSocketProcessorFactory factory = null;


	public pxnSocketServer() {
	}


	// start listening (threaded)
	@Override
	public synchronized void Start() {
		synchronized(state) {
			if(!pxnSocketState.CLOSED.equals(state)) return;
			stopping = false;
			state = pxnSocketState.WAITING;
		}
		pxnLogger.get(logName).info("Listening on port: "+Integer.toString(port));
		try {
			// listener socket
			listenerSocket = new ServerSocket(port, 4);
		} catch (IOException e) {
			listenerSocket = null;
			state = pxnSocketState.FAILED;
			pxnLogger.get(logName).exception("Failed to listen on port: "+Integer.toString(port), e);
			return;
		}
		// start listener thread
		if(listenerThread == null)
			listenerThread = new Thread(new Runnable() {
				@Override
				public void run() {
					runListener();
				}
			});
		listenerThread.start();
	}
	// listener thread
	private void runListener() {
		synchronized(state) {
			if(!pxnSocketState.WAITING.equals(state)) return;
			if(listenerSocket == null) return;
		}
		listenerThread.setName("SocketServer-"+Integer.toString(port));
		pxnUtils.Sleep(10);
		// loop for new connections
		synchronized(listenerThread) {
		while(!stopping && !listenerSocket.isClosed()) {
			try {
				// wait for a connection
				Socket socket = listenerSocket.accept();
				if(socket == null) {
					pxnUtils.Sleep(50);
					continue;
				}
				// new worker
				pxnSocketWorker worker = new pxnSocketWorker(socket, factory);
//				pxnLogger.get(logName).info("Connected socket #"+Integer.toString(worker.getSocketId()));
				worker.Start();
				synchronized(workers) {
					workers.add(worker);
				}
			} catch (SocketException ignore) {
				// socket listener closed
				break;
			} catch (IOException e) {
				pxnLogger.get(logName).exception(e);
			} finally {
				// flush closed sockets
				doFlush();
			}
		}}
		// stop socket listener
		Close();
		pxnLogger.get(logName).info("Stopping listener port: "+Integer.toString(port));
		if(listenerSocket != null) {
			try {
				listenerSocket.close();
			} catch (IOException e) {
				pxnLogger.get(logName).exception(e);
			}
		}
	}


	// close socket listener
	@Override
	public void Close() {
		synchronized(state) {
			stopping = true;
			state = pxnSocketState.CLOSED;
			// stop listening
			if(listenerSocket != null && !listenerSocket.isClosed()) {
				try {
					listenerSocket.close();
					pxnUtils.Sleep(10);
				} catch (IOException e) {
					pxnLogger.get(logName).exception("Failed to close socket listener: "+Integer.toString(port), e);
				} finally {
					listenerSocket = null;
				}
			}
		}
	}
	// close all sockets
	@Override
	public void ForceClose() {
		Close();
		synchronized(workers) {
			for(pxnSocketWorker w : workers)
				if(w != null) {
					w.Close();
pxnLogger.get(logName).info("CLOSED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!s");
				}
			doFlush();
		}
	}
	public void finalize() {
		ForceClose();
	}
	// flush closed sockets
	protected synchronized void doFlush() {
		int flushCount = 0;
		synchronized(workers) {
			for(Iterator<pxnSocketWorker> it = workers.iterator(); it.hasNext(); ) {
				if(it.next().isClosed()) {
					it.remove();
					flushCount++;
				}
			}
		}
		pxnLogger.get(logName).debug("Sockets loaded: "+Integer.toString(workers.size()));
		if(flushCount > 0)
			pxnLogger.get(logName).info("Flushed [ "+Integer.toString(flushCount)+" ] stale socket"+(flushCount>1?"s":""));
	}


	// host
	@Override
	public String getHost() {
		return host;
	}
	@Override
	public void setHost(String host) {
		synchronized(state) {
			if(!pxnSocketState.CLOSED.equals(state)) return;
			this.host = pxnSocketUtils.prepHost(host);
		}
	}


	// port
	@Override
	public int getPort() {
		return port;
	}
	@Override
	public void setPort(int port) {
		synchronized(state) {
			if(!pxnSocketState.CLOSED.equals(state)) return;
			this.port = pxnSocketUtils.prepPort(port);
		}
	}


	// processor factory
	@Override
	public pxnSocketProcessorFactory getFactory() {
		return factory;
	}
	@Override
	public void setFactory(pxnSocketProcessorFactory factory) {
		synchronized(state) {
			if(!pxnSocketState.CLOSED.equals(state)) return;
			this.factory = factory;
		}
	}


}
