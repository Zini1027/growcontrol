package com.poixson.pxnSocket;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.poixson.pxnUtils;
import com.poixson.pxnLogger.pxnLogger;


public class pxnSocketClient implements pxnSocket {

	protected final String host;
	protected final int port;

	// socket
	protected Socket socket = null;
	protected pxnSocketWorker worker = null;


	// new socket client
	public pxnSocketClient(String host, int port, pxnSocketProcessor processor)
			throws UnknownHostException, SocketTimeoutException, ConnectException, IOException {
		// host
		if(host == null || host.isEmpty()) throw new IllegalArgumentException("host can't be null!");
		// trim http:// or other prefix
//TODO: this is untested
		if(host.contains("://")) host = host.substring(host.indexOf("://")+1);
		if(host.contains("/")) host = host.substring(0, host.indexOf("/"));
		this.host = host;
		// port
		if(port < 1 || port > 65536) {
			pxnLogger.getLogger().severe("Invalid port "+Integer.toString(port)+" is not valid! Out of range!");
			throw new IllegalArgumentException("Invalid port "+Integer.toString(port));
		}
		this.port = port;
		// start connecting
//		try {
			pxnLogger.getLogger().info("Connecting to: "+host+":"+Integer.toString(port));
			socket = new Socket(host, port);
			worker = new pxnSocketWorker(socket, processor);
//		} catch(UnknownHostException e) {
//			// unknown host
//			pxnLogger.log().exception(e);
//			e.printStackTrace();
//			gcClient.setConnectState(ConnectState.CLOSED);
//			return;
//		} catch (SocketTimeoutException e) {
//			// connect timeout
//			e.printStackTrace();
//		} catch(ConnectException e) {
//			// connection refused
//JOptionPane.showMessageDialog(null, e.getMessage(), "Connection failed!", JOptionPane.ERROR_MESSAGE);
//			gcClient.setConnectState(ConnectState.CLOSED);
//			close();
//			return;
//		} catch (IOException e) {
//			pxnLogger.getLogger().severe("Failed to connect to: "+host+":"+Integer.toString(port));
//			pxnLogger.getLogger().exception(e);
//JOptionPane.showMessageDialog(null, e.getMessage(), "Connection failed!", JOptionPane.ERROR_MESSAGE);
//gcClient.setConnectState(ConnectState.CLOSED);
//			close();
//			return;
//		} finally {
//gcClient.setConnectState(ConnectState.READY);
//		}
//TODO: remove this
	}


//	@Override
//	public void flushClosed() {
//TODO: not needed?
//	}


	// close socket
	@Override
	public void close() {
	}
//	public void close() throws IOException {
//		if(socket == null) return;
//		if(socket.isConnected() || !socket.isClosed())
//			socket.close();
//		socket = null;
//	}
	@Override
	public void stop() {
	}


	public void sendData(String line) {
		if(worker == null) {
			// sleep 5 seconds max
			for(int i=0; i<20; i++) {
				pxnUtils.Sleep(250);
				if(worker != null) break;
			}
			if(worker == null) {
socket = null;
//TODO: disconnect socket and queue a reconnect
				return;
//throw new NullPointerException("worker can't be null!");
			}
		}
		worker.sendData(line);
	}


	public void finalize() {
//		try {
//			close();
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
//TODO: when does this run?
System.out.println("END - finalize()");
	}


}
