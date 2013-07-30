package com.growcontrol.gcCommon.pxnSocket;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketUtils.pxnSocketState;
import com.growcontrol.gcCommon.pxnSocket.processor.pxnSocketProcessorFactory;
import com.growcontrol.gcCommon.pxnSocket.worker.pxnSocketWorker;


public class pxnSocketClient implements pxnSocket {
	private static final String logName = "SocketClient";

	private volatile String host = null;
	private volatile int port = 0;

	// socket state
	private volatile pxnSocketState state = pxnSocketState.CLOSED;
//	private volatile boolean stopping = false;

	// worker
	private pxnSocketWorker worker = null;
	// processor factory
	private pxnSocketProcessorFactory factory = null;


	public pxnSocketClient() {
	}


	// connect to host (blocking)
	@Override
	public synchronized void Start() {
		synchronized(state) {
			if(!pxnSocketState.CLOSED.equals(state)) return;
//			stopping = false;
			state = pxnSocketState.WAITING;
		}
		pxnLogger.get(logName).info("Connecting to host: "+host+":"+Integer.toString(port));
		try {
			// connect to host
			Socket socket = new Socket(host, port);
if(!socket.isConnected()){
pxnLogger.get(logName).severe("NOT CONNECTED - pxnSOcketClient:70");
socket.close();
return;
}
			// new worker
			if(worker == null)
				worker = new pxnSocketWorker(socket, factory);
			worker.Start();
			state = pxnSocketState.CONNECTED;
//TODO:
//gcClient.setConnectState(ConnectState.CONNECTED);
			return;
		} catch (UnknownHostException e) {
			// unknown host
			pxnLogger.get(logName).exception(e);
//TODO:
//gcClient.setConnectState(ConnectState.CLOSED);
		} catch (SocketTimeoutException e) {
			// connect timeout
			pxnLogger.get(logName).exception(e);
//TODO:
//gcClient.setConnectState(ConnectState.CLOSED);
		} catch(ConnectException e) {
			// connection refused
			pxnLogger.get(logName).exception(e);
//TODO:
//JOptionPane.showMessageDialog(null, e.getMessage(), "Connection failed!", JOptionPane.ERROR_MESSAGE);
//gcClient.setConnectState(ConnectState.CLOSED);
		} catch (IOException e) {
			pxnLogger.get(logName).warning("Failed to connect to: "+host+":"+Integer.toString(port));
			pxnLogger.get(logName).exception(e);
//TODO:
//JOptionPane.showMessageDialog(null, e.getMessage(), "Connection failed!", JOptionPane.ERROR_MESSAGE);
//gcClient.setConnectState(ConnectState.CLOSED);
		} catch (Exception e) {
			pxnLogger.get(logName).exception(e);
		}
		Close();
		state = pxnSocketState.FAILED;
	}


	// close socket client
	@Override
	public void Close() {
		synchronized(state) {
//			stopping = true;
			state = pxnSocketState.CLOSED;
			worker.Close();
		}
	}
	// close all sockets
	@Override
	public void ForceClose() {
		Close();
	}
	public void finalize() {
		Close();
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


//	public void sendData(String line) throws Exception {
//	if(worker == null) {
//		// sleep 5 seconds max
//		for(int i=0; i<20; i++) {
//			pxnUtils.Sleep(250);
//			if(worker != null) break;
//		}
//		if(worker == null) {
//socket = null;
////TODO: disconnect socket and queue a reconnect
//throw new NullPointerException("worker can't be null! 5 second timeout expired!");
//		}
//	}
//	worker.sendData(line);
//}


}
