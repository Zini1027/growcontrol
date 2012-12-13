package com.growcontrol.gctimer.client.frames;

import javax.swing.JButton;

import com.growcontrol.gcClient.clientPlugin.gcPluginFrame;


public class gcTimerFrame extends gcPluginFrame {
	private static final long serialVersionUID = 1L;


	public gcTimerFrame() {
		JButton button = new JButton("TIMER");
		add(button);
		setVisible(true);
	}


}
