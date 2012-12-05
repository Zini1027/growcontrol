package com.growcontrol.gcClient.frames;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.growcontrol.gcClient.gcClient;


public class LoginFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public static final String LOGIN_WINDOW_NAME = "login";
	public static final String CONNECTING_WINDOW_NAME = "connecting";
	protected CardLayout cardLayout = new CardLayout();
//	protected KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
//	protected InputMap inputMap;

	protected LoginHandler handler;
	protected JPanel panelLogin = new JPanel();
	protected JPanel panelConnecting = new JPanel();

	// input fields
	protected JTextField textHost;
	protected JTextField textPort;
	protected JTextField textUsername;
	protected JPasswordField textPassword;


	public LoginFrame(LoginHandler handler) {
		super();
		if(handler == null) throw new NullPointerException("login class is null!");
		this.handler = handler;
		this.setTitle("Connect to server..");
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(cardLayout);
		// key listener
		KeyboardFocusManager keyboard = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		keyboard.addKeyEventDispatcher(handler);
		// create panels
		createLoginPanel();
		createConnectingPanel();
		// create cards
		this.add(panelLogin, LOGIN_WINDOW_NAME);
		this.add(panelConnecting, CONNECTING_WINDOW_NAME);
		pack();
		// set window width
		this.setSize(280, this.getHeight());
		// display window
		this.setLocationRelativeTo(null);
		setVisible(true);
	}


	// login panel
	private void createLoginPanel() {
		MigLayout migLayout = new MigLayout();
		migLayout.setLayoutConstraints("");
		migLayout.setColumnConstraints("[]10[]");
		migLayout.setRowConstraints   ("[]20[]");
		panelLogin.setLayout(migLayout);

//		// separator - saved servers
//		JLabel labelServersList = new JLabel("Saved Servers");
//		labelServersList.setToolTipText("<html>" +
//				"</html>");
//		panelLogin.add(labelServersList, "split 2, span");
//		JSeparator separatorServersList = new JSeparator();
//		separatorServersList.setPreferredSize(new Dimension(200, 2));
//		panelLogin.add(separatorServersList, "growx, wrap");
		// servers list
		JComboBox comboSavedServers = new JComboBox();
		comboSavedServers.addItem("[ unsaved ]");
		comboSavedServers.addItem("[ Local Computer ]");
		comboSavedServers.addItem("home:1142");
		comboSavedServers.setEnabled(false);
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
		textHost = new JTextField();
		panelLogin.add(textHost, "growx, wrap");
		// port
		JLabel labelPort = new JLabel("Port:");
		panelLogin.add(labelPort, "");
		textPort = new JTextField();
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
		textUsername = new JTextField();
		panelLogin.add(textUsername, "growx, wrap");
		// password
		JLabel labelPassword = new JLabel("Password:");
		panelLogin.add(labelPassword, "");
		textPassword = new JPasswordField();
		panelLogin.add(textPassword, "growx, wrap");

		// connect button
		JButton buttonConnect = new JButton("Connect");
		panelLogin.add(buttonConnect, "span 2, center");
		buttonConnect.addActionListener(handler);
	}


	// connecting.. panel
	private void createConnectingPanel() {
		panelConnecting.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelConnecting.setBackground(Color.DARK_GRAY);
		ImageIcon loading = gcClient.loadImageResource("resources/icon-loading-animated.gif");
		JLabel labelAnimation = new JLabel();
		labelAnimation.setIcon(loading);
		panelConnecting.add(labelAnimation);
		// cancel button
		JButton buttonCancel = new JButton("Cancel");
		panelConnecting.add(buttonCancel);
		buttonCancel.addActionListener(handler);
		// status
		JLabel labelStatus = new JLabel("Connecting..");
		labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
		labelStatus.setPreferredSize(new Dimension(180, 35));
		labelStatus.setForeground(Color.WHITE);
		panelConnecting.add(labelStatus);
//		// progress bar
//		JProgressBar progress = new JProgressBar();
//		label.add(progress);
	}


	public void DisplayCard(String cardName) {
		if(cardName == null)   throw new NullPointerException("cardName can't be null");
		if(cardName.isEmpty()) throw new NullPointerException("cardName can't be empty");
		try {
			cardLayout.show(panelConnecting.getParent(), cardName);
		} catch(Exception ignore) {}
	}


}
