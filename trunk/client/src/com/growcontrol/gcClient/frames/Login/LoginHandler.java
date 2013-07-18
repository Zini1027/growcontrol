package com.growcontrol.gcClient.frames.Login;

import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.growcontrol.gcClient.Main;
import com.growcontrol.gcClient.gcClient;
import com.growcontrol.gcClient.frames.gcFrameHandlerInterface;
import com.growcontrol.gcClient.frames.Login.LoginFrame.LoginWindows;
import com.growcontrol.gcClient.socketClient.gcSocketProcessor;
import com.growcontrol.gcClient.socketClient.sendClientPackets;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketClient;


public class LoginHandler implements gcFrameHandlerInterface, ActionListener, KeyEventDispatcher {

	protected static LoginHandler handler = null;
	protected LoginFrame frame = null;
	protected LoginWindows currentCard = LoginWindows.LOGIN;


	// login frame handler
	public static LoginHandler get() {
		synchronized(handler) {
			if(handler == null)
				handler = new LoginHandler();
		}
		return handler;
	}
	public LoginHandler() {
	}


	@Override
	public void Show() {
		frame = new LoginFrame(this);
	}
	@Override
	public void Close() {
		frame.dispose();
		frame = null;
	}
	@Override
	public JFrame getFrame() {
		return frame;
	}


	// button click event
	@Override
	public void actionPerformed(ActionEvent event) {
		String buttonName = "";
//		try {
		buttonName = ((JButton) event.getSource()).getActionCommand();
//		} catch(Exception ignore) {
//			return;
//		}

		if(buttonName.equals("Connect")) {
			Main.getClient().getConnectState().setStateConnecting();
//			setDisplay(loginFrame.CONNECTING_WINDOW_NAME);
			// connect to server
			pxnSocketClient socket = null;
			try {
				// get connect info from window
				ConnectInfo connectInfo = new ConnectInfo();
				// connect to server
				socket = new pxnSocketClient(connectInfo.host, connectInfo.port, new gcSocketProcessor());
				Main.getClient().setSocket(socket);
				// send HELLO packet
				try {
					sendClientPackets.sendHELLO( Main.getClient().getSocket().getProcessor(),
							gcClient.version,
							connectInfo.username,
							connectInfo.password);
				} catch (Exception e) {
e.printStackTrace();
				}
			} catch (SocketTimeoutException e) {
				// connection timeout
				return;
			} catch (ConnectException e) {
				// socket closed
				return;
			} catch (UnknownHostException e) {
				// unknown hostname
				return;
			} catch (IOException e) {
e.printStackTrace();
			}

		} else if(buttonName.equals("Cancel"))
			Main.getClient().getConnectState().setStateClosed();
//			setDisplay(loginFrame.LOGIN_WINDOW_NAME);
	}
	// get info from text boxes
	public void getConnectInfo(StringBuilder host, Integer port, StringBuilder username, StringBuilder password) {
	}
	// get fields
	private class ConnectInfo {
		public final String host;
		public final int port;
		public final String username;
		public final String password;
		public ConnectInfo() {
			host = frame.textHost.getText();
			String portStr = frame.textPort.getText();
			port = (portStr != null && !portStr.isEmpty()) ?
				Integer.parseInt(portStr) :
				1142;
			username = frame.textUsername.getText();
			password = pxnUtils.MD5(frame.textHost.getText());
		}
	}


	// display window card
	public LoginWindows getDisplay() {
		return currentCard;
	}
	public void setDisplay(LoginWindows displayCard) {
		if(displayCard == null) throw new NullPointerException("displayCard can't be null!");
		this.currentCard = displayCard;
		// call directly
		if(SwingUtilities.isEventDispatchThread()) {
			new setDisplayTask(displayCard).run();
			return;
		}
		try {
			// invoke gui event
			SwingUtilities.invokeAndWait(new setDisplayTask(displayCard));
		} catch (InterruptedException e) {
			return;
		} catch (InvocationTargetException e) {
e.printStackTrace();
		}
	}
	private class setDisplayTask implements Runnable {
		private LoginWindows currentCard;
		public setDisplayTask(LoginWindows displayCard) {
			this.currentCard = displayCard;
		}
		@Override
		public void run() {
			frame.DisplayCard(currentCard);
		}
	}


	// key press event
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			frame.dispose();
		return false;
	}


//	public String getHost() {
//		String host = frame.textHost.getText();
//		if(host == null || host.isEmpty())
//			return "127.0.0.1";
//		return host;
//	}
//	public int getPort() {
//		int port = 1142;
//		String portStr = frame.textPort.getText();
//		if(portStr == null || portStr.isEmpty())
//			return port;
//		port = Integer.parseInt(portStr);
//		return port;
//	}
//	public String getUsername() {
//		return frame.textUsername.getText();
//	}
//	public String getPassword() {
//		return pxnUtils.MD5(new String(frame.textPassword.getPassword()));
//	}


	// set connecting message
	public void setMessage(String message) {
		// call directly
		if(SwingUtilities.isEventDispatchThread()) {
			new setMessageTask(message).run();
			return;
		}
		try {
			// invoke gui event
			SwingUtilities.invokeAndWait(new setMessageTask(message));
		} catch (InterruptedException ignore) {
			return;
		} catch (InvocationTargetException e) {
e.printStackTrace();
		}
	}
	private class setMessageTask implements Runnable {
		private final String message;
		public setMessageTask(String message) {
			this.message = message;
		}
		@Override
		public void run() {
			frame.setMessage(message);
		}
	}


}
