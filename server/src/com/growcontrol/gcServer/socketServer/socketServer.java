package com.growcontrol.gcServer.socketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.growcontrol.gcServer.gcServer;
import com.poixson.pxnUtils;


public class socketServer implements Runnable {

	protected final int port;
	protected final Thread thread;
	protected ServerSocket listenerSocket = null;
	protected final List<socketWorker> socketPool = new ArrayList<socketWorker>();
	protected boolean stopping = false;


	public socketServer(int port) {
		if(port < 1 || port > 65536) {
			gcServer.log.severe("Invalid port "+Integer.toString(port)+" is not valid! Out of range!");
			throw new IllegalArgumentException("Invalid port "+Integer.toString(port));
//			this.port = 0;
//			thread = null;
//			return;
		}
		this.port = port;
		thread = new Thread(this);
//		thread.setDaemon(true);
		thread.start();
		pxnUtils.Sleep(10);
	}


	@Override
	public void run() {
		try {
			// start listening
			listenerSocket = new ServerSocket(port, 8);
		} catch (IOException e) {
			gcServer.log.severe("Failed to listen on port: "+Integer.toString(port));
			gcServer.log.exception(e);
			return;
		}
		gcServer.log.info("Listening on port: "+Integer.toString(port));
		// loop for new connections
		while(true) {
			checkClosed();
			Socket socket = null;
			try {
				// wait for a connection
				socket = listenerSocket.accept();
			} catch (SocketException ignore) {
			} catch (IOException e) {
				gcServer.log.exception(e);
			}
			// closing sockets
			if(stopping) {
				gcServer.log.info("Stopping socket listener..");
				break;
			}
			socketWorker worker = new socketWorker(socket);
			socketPool.add(worker);
		}
	}


	// stop socket server
	public void stop() {
		stopping = true;
		// stop listening
		if(listenerSocket != null) {
			try {
				listenerSocket.close();
			} catch (IOException e) {
				gcServer.log.exception(e);
			}
		}
		// close sockets
		for(Iterator<socketWorker> it = socketPool.iterator(); it.hasNext();) {
			it.next().close();
		}
	}


	// flush disconnected sockets
	public void checkClosed() {
		for(Iterator<socketWorker> it = socketPool.iterator(); it.hasNext();)
			if(it.next().isClosed())
				it.remove();
		gcServer.log.debug("Sockets loaded: "+Integer.toString(socketPool.size()));
	}


}