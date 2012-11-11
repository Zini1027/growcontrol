package com.growcontrol.gcServer.socketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.growcontrol.gcServer.gcServer;

public class socketServer implements Runnable {

	protected final int port;
	protected final Thread thread;
	protected ServerSocket listenerSocket = null;
	protected final List<socketWorker> socketPool = new ArrayList<socketWorker>();
	protected boolean stopping = false;


	public socketServer(int port) {
		if(port < 1 || port > 65536) {
			gcServer.log.severe("Invalid port "+Integer.toString(port)+" is not valid! Out of range!");
			this.port = 0;
			thread = null;
			return;
		}
		this.port = port;
		thread = new Thread(this);
//		thread.setDaemon(true);
		thread.start();
	}


	@Override
	public void run() {
		try {
			// start listening
			listenerSocket = new ServerSocket(port);
		} catch (IOException e) {
			gcServer.log.exception(e);
		}
		gcServer.log.info("Listening on port: "+Integer.toString(port));
		// loop for new connections
		while(!stopping) {
			checkClosed();
			Socket socket = null;
			try {
				// wait for a connection
				socket = listenerSocket.accept();
			} catch (SocketException e) {
				if(stopping) {
					gcServer.log.info("Stopping socket listener");
					break;
				}
			} catch (IOException e) {
				gcServer.log.exception(e);
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
