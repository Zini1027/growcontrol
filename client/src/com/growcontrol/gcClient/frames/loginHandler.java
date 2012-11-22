package com.growcontrol.gcClient.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.growcontrol.gcClient.gcClient;
import com.growcontrol.gcClient.socketClient.connection;
import com.growcontrol.gcClient.socketClient.packets.clientPacket;

public class loginHandler implements ActionListener {

	loginFrame frame;


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
		String cardName = "";
		if(buttonName.equals("Connect")) {
			cardName = loginFrame.CONNECTING_WINDOW_NAME;


		// connect to server
		gcClient.conn = new connection("192.168.3.3", 1142);
		gcClient.conn.sendPacket(clientPacket.sendHELLO(gcClient.version, "lorenzo", "pass"));



		} else if(buttonName.equals("Cancel"))
			cardName = loginFrame.LOGIN_WINDOW_NAME;
		if(cardName.isEmpty()) return;
		frame.DisplayCard(cardName);
	}


}
