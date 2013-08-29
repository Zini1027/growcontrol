package com.growcontrol.gcCommon.pxnSocket;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketUtils.pxnSocketState;
import com.growcontrol.gcCommon.pxnSocket.processor.pxnSocketProcessorFactory;
import com.growcontrol.gcCommon.pxnSocket.worker.pxnSocketWorker;


public class pxnSocketClient implements pxnSocket {
	private static final String logName = "SocketClient";

	private volatile String host = null;
	private volatile int port = 0;

	// socket state
	private volatile pxnSocketState state = pxnSocketState.CLOSED;

	// worker
	private pxnSocketWorker worker = null;
	// processor factory
	private volatile pxnSocketProcessorFactory factory = null;


	public pxnSocketClient() {}


	// connect to host (blocking)
	@Override
	public void Start() {
		synchronized(state) {
			// not closed
			if(!pxnSocketState.CLOSED.equals(state)) return;
			// waiting for user
			state = pxnSocketState.WAITING;
		}
		pxnLog.get(logName).info("Connecting to host: "+host+":"+Integer.toString(port));
		try {
			// connect to host
			Socket socket = new Socket(host, port);
if(!socket.isConnected()){
pxnLog.get(logName).severe("NOT CONNECTED - pxnSOcketClient:70");
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
		} catch (UnknownHostException ignore) {
			// unknown host
			pxnLog.get(logName).warning("Unknown host!");
//TODO:
//gcClient.setConnectState(ConnectState.CLOSED);
		} catch (SocketTimeoutException ignore) {
			// connect timeout
			pxnLog.get(logName).warning("Connection timeout!");
//TODO:
//gcClient.setConnectState(ConnectState.CLOSED);
		} catch(ConnectException ignore) {
			// connection refused
			pxnLog.get(logName).warning("Connection refused!");
//TODO:
//JOptionPane.showMessageDialog(null, e.getMessage(), "Connection failed!", JOptionPane.ERROR_MESSAGE);
//gcClient.setConnectState(ConnectState.CLOSED);
		} catch (IOException e) {
			pxnLog.get(logName).warning("Failed to connect to: "+host+":"+Integer.toString(port));
			pxnLog.get(logName).exception(e);
//TODO:
//JOptionPane.showMessageDialog(null, e.getMessage(), "Connection failed!", JOptionPane.ERROR_MESSAGE);
//gcClient.setConnectState(ConnectState.CLOSED);
		} catch (Exception e) {
			pxnLog.get(logName).exception(e);
		}
		Close();
		state = pxnSocketState.FAILED;
	}


	// close socket client
	@Override
	public void Close() {
		synchronized(state) {
			state = pxnSocketState.CLOSED;
			if(worker != null)
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


	// socket state
	@Override
	public pxnSocketState getState() {
		return state;
	}


	// get socket worker
	public pxnSocketWorker getWorker() {
		return worker;
	}


	// host
	@Override
	public String getHost() {
		return host;
	}
	@Override
	public void setHost(String host) {
		synchronized(state) {
			// not closed
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
			// not closed
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
			// not closed
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
