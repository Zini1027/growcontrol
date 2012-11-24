package com.growcontrol.gcClient.frames;

import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

import com.growcontrol.gcClient.gcClient;
import com.growcontrol.gcClient.gcClient.ConnectState;
import com.growcontrol.gcClient.pxnUtils;
import com.growcontrol.gcClient.socketClient.connection;
import com.growcontrol.gcClient.socketClient.packets.clientPacket;

public class loginHandler implements ActionListener, KeyEventDispatcher {

	protected loginFrame frame;
	protected String cardName = loginFrame.LOGIN_WINDOW_NAME;


	public loginHandler() {
		frame = new loginFrame(this);
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
			gcClient.setConnectState(ConnectState.CONNECTING);
//			setDisplay(loginFrame.CONNECTING_WINDOW_NAME);


		// connect to server
		gcClient.setConnectionClass(new connection(getHost(), getPort()));
		gcClient.getConnectionClass().sendPacket(clientPacket.sendHELLO(gcClient.version, "lorenzo", "pass"));



		} else if(buttonName.equals("Cancel"))
			gcClient.setConnectState(ConnectState.CLOSED);
//			setDisplay(loginFrame.LOGIN_WINDOW_NAME);
	}


	public String getDisplay() {
		return cardName;
	}
	public void setDisplay(String cardName) {
		if(cardName == null) throw new NullPointerException("showCard can't be null!");
		this.cardName = cardName;
		frame.DisplayCard(cardName);
	}


	// key press event
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			frame.dispose();
		return false;
	}


	// get fields
	public String getHost() {
		String host = frame.textHost.getText();
		if(host == null || host.isEmpty())
			return "127.0.0.1";
		return host;
	}
	public int getPort() {
		int port = 1142;
		String portStr = frame.textPort.getText();
		if(portStr == null || portStr.isEmpty())
			return port;
		port = Integer.parseInt(portStr);
		return port;
	}
	public String getUsername() {
		return frame.textUsername.getText();
	}
	public String getPassword() {
		return pxnUtils.MD5(new String(frame.textPassword.getPassword()));
	}


}
