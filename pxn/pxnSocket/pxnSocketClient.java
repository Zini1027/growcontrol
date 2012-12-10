package com.poixson.pxnSocket;

import java.io.IOException;
import java.net.Socket;

import com.growcontrol.gcClient.gcClient;
import com.growcontrol.gcClient.gcClient.ConnectState;
import com.poixson.pxnLogger.pxnLogger;


public class pxnSocketClient implements pxnSocket {

	protected final String host;
	protected final int port;

	// socket
	protected Socket socket = null;
//	protected pxnSocketWorker socket = null;

	// processor
	protected final pxnSocketProcessor processor;


	// new socket client
	public pxnSocketClient(String host, int port, pxnSocketProcessor processor) {
		this.processor = processor;
		// host
		if(host == null || host.isEmpty()) throw new IllegalArgumentException("host can't be null!");
		// trim http:// or other prefix
//TODO: this is untested
		if(host.contains("://")) host = host.substring(host.indexOf("://")+1);
		if(host.contains("/")) host = host.substring(0, host.indexOf("/"));
		this.host = host;
		// port
		if(port < 1 || port > 65536) {
			pxnLogger.log().severe("Invalid port "+Integer.toString(port)+" is not valid! Out of range!");
			throw new IllegalArgumentException("Invalid port "+Integer.toString(port));
		}
		this.port = port;
		// start connecting
		try {
			pxnLogger.log().info("Connecting to: "+host+":"+Integer.toString(port));
			socket = new Socket(host, port);
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
//			JOptionPane.showMessageDialog(null, e.getMessage(), "Connection failed!", JOptionPane.ERROR_MESSAGE);
//			gcClient.setConnectState(ConnectState.CLOSED);
//			return;
		} catch (IOException e) {
			pxnLogger.log().severe("Failed to connect to: "+host+":"+Integer.toString(port));
			pxnLogger.log().exception(e);
			JOptionPane.showMessageDialog(null, e.getMessage(), "Connection failed!", JOptionPane.ERROR_MESSAGE);
gcClient.setConnectState(ConnectState.CLOSED);
			return;
		}
//socket.setSoTimeout(1000);
//TODO: remove this
gcClient.setConnectState(ConnectState.READY);
	}


//	@Override
//	public void flushClosed() {
//TODO: not needed?
//	}


	// close socket
	@Override
	public void close() {
	}
	@Override
	public void stop() {
	}


	public void sendData(String line) {
		processor.process(line);
	}


//	public void finalize() {
//		try {
//			close();
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
//	}
//	public void close() throws IOException {
//		if(socket == null) return;
//		if(socket.isConnected() || !socket.isClosed())
//			socket.close();
//		socket = null;
//	}


//	public boolean sendPacket(clientPacket packet) {
//		if(packet == null) throw new NullPointerException();
//		if(socket == null || !socket.isConnected()) return false;
//		out.print(clientPacket.EOL);
//		out.print(packet.getPacketString());
//		out.print(clientPacket.EOL);
//		out.flush();
//		return false;
//	}


//	public boolean isConnected() {
//		return false;
//	}


}
