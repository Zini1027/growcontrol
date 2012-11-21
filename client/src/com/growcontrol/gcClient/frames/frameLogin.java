package com.growcontrol.gcClient.frames;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class frameLogin extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	CardLayout cardLayout = new CardLayout();

	JPanel panelLogin = new JPanel();
	JPanel panelConnecting = new JPanel();


	public frameLogin() {
		super();
		this.setTitle("Connect to server..");
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(cardLayout);

		// login form
		MigLayout migLayout = new MigLayout();
		migLayout.setLayoutConstraints("");
		migLayout.setColumnConstraints("[]10[]");
		migLayout.setRowConstraints   ("[]20[]");
		panelLogin.setLayout(migLayout);

		// saved servers
		JComboBox comboSavedServers = new JComboBox();
		comboSavedServers.addItem("[ unsaved ]");
		comboSavedServers.addItem("[ Local Computer]");
		comboSavedServers.addItem("home:1142");
		panelLogin.add(comboSavedServers, "growx, span 2, gapleft 10, gapright 10, center, wrap");

		// separator - Server Address
		JLabel labelLocation = new JLabel("Server Location");
		labelLocation.setToolTipText("<html>" +
				"The address for your Grow Control Server. This can<br>" +
				"be an IP address or a hostname.<br>" +
				"Example:<br>" +
				"192.168.1.120  or<br>" +
				"gcserver.mydomain.com" +
				"</html>");
		panelLogin.add(labelLocation, "split 2, span");
		JSeparator separatorLocation = new JSeparator();
//		separatorLocation.setPreferredSize(new Dimension(200, 2));
		panelLogin.add(separatorLocation, "growx, wrap");
		// hostname / ip
		JLabel labelHost = new JLabel("Hostname/IP:");
		panelLogin.add(labelHost, "");
		JTextField textHost = new JTextField();
		panelLogin.add(textHost, "growx, wrap");
		// port
		JLabel labelPort = new JLabel("Port:");
		panelLogin.add(labelPort, "");
		JTextField textPort = new JTextField();
		panelLogin.add(textPort, "growx, wrap");

		// separator - Username / Password
		JLabel labelLogin = new JLabel("Login");
		labelLogin.setToolTipText("<html>" +
				"</html>");
		panelLogin.add(labelLogin, "split 2, span");
		JSeparator separatorLogin = new JSeparator();
		separatorLogin.setPreferredSize(new Dimension(200, 2));
		panelLogin.add(separatorLogin, "growx, wrap");
		// username
		JLabel labelUsername = new JLabel("Username:");
		panelLogin.add(labelUsername, "");
		JTextField textUsername = new JTextField();
		panelLogin.add(textUsername, "growx, wrap");
		// password
		JLabel labelPassword = new JLabel("Password:");
		panelLogin.add(labelPassword, "");
		JPasswordField textPassword = new JPasswordField();
		panelLogin.add(textPassword, "growx, wrap");

		// connect button
		JButton buttonConnect = new JButton("Connect");
		panelLogin.add(buttonConnect, "span 2, center");
		buttonConnect.addActionListener(this);

		// connecting..
		panelConnecting.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelConnecting.setBackground(Color.DARK_GRAY);
		ImageIcon loading = new ImageIcon("icon-loading-animated.gif");
		JLabel label = new JLabel();
		label.setIcon(loading);
		panelConnecting.add(label);
		// cancel button
		JButton buttonCancel = new JButton("Cancel");
		panelConnecting.add(buttonCancel);
		buttonCancel.addActionListener(this);

		// create cards
		this.add(panelLogin, "Login");
		pack();
		this.add(panelConnecting, "Connecting");
		// display window
		this.setLocationRelativeTo(null);
		setVisible(true);
	}


	// button click event
	@Override
	public void actionPerformed(ActionEvent event) {
		String command = ((JButton) event.getSource()).getActionCommand();
		if(command.equals("Connect"))
			cardLayout.show(panelConnecting.getParent(), "Connecting");
		else if(command.equals("Cancel"))
			cardLayout.show(panelLogin.getParent(), "Login");
	}


}
