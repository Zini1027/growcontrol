package com.growcontrol.gcClient.frames.Login;

import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.growcontrol.gcClient.Main;
import com.growcontrol.gcClient.gcClient;
import com.growcontrol.gcClient.frames.gcFrameHandlerInterface;
import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public class LoginHandler implements gcFrameHandlerInterface, KeyEventDispatcher {
	protected static LoginHandler handler = null;

	// display mode
	public enum CONN {WAITING, CONNECT, AUTH, READY};
	protected volatile CONN connMode     = null;
	protected volatile CONN lastConnMode = null;
	private final Object modeLock = new Object();

	// static entries of saved servers list
	public final String SavedStatic_Unsaved        = "[ Unsaved ]";
//	public final String SavedStatic_RunServerLocal = "[ Run Internal Server ]";
	public final String SavedStatic_LocalHost      = "[ Local Host ]";

	// objects
	protected LoginFrame frame = null;


	// login frame handler
	public static synchronized LoginHandler get() {
		if(handler == null)
			handler = new LoginHandler();
		return handler;
	}
	private LoginHandler() {}


	@Override
	public synchronized void Show() {
		synchronized(modeLock) {
			pxnLogger.get().info("Displaying window: Login");
			if(connMode == null)
				connMode = CONN.WAITING;
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						if(frame == null)
							frame = new LoginFrame(handler);
						frame.DisplayCard(connMode);
					}
				});
			} catch (InvocationTargetException e) {
				pxnLogger.get().exception(e);
			} catch (InterruptedException ignore) {}
		}
	}
	@Override
	public void Close() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(frame == null)
					return;
				pxnLogger.get().info("Closing window: Login");
				synchronized(frame) {
					frame.dispose();
					frame = null;
Main.Shutdown();
				}
			}
		});
	}
	@Override
	public JFrame getFrame() {
		synchronized(frame) {
			return frame;
		}
	}


	// get/set gui mode
	public synchronized CONN getMode() {
		if(connMode == null)
			connMode = CONN.WAITING;
		return connMode;
	}
	public CONN setMode(CONN mode) {
		synchronized(modeLock) {
			lastConnMode = connMode;
			connMode = mode;
frame.DisplayCard(connMode);
			return lastConnMode;
		}
	}


	// button click event
	public void ButtonClicked(ActionEvent event, String buttonName) {
		pxnLogger.get().info("Button pressed: "+buttonName);
		switch(buttonName) {
		case "Save":
			frame.setButtonName("Connect");
			break;
		case "Connect":
			setMode(CONN.CONNECT);
			String host = frame.textHost.getText();
			String portStr = frame.textPort.getText();
			int port = 0;
			if(portStr != null && !portStr.isEmpty()) {
				try {
					port = Integer.parseInt(portStr);
				} catch (Exception ignore) {}
			}
			String user = frame.textUsername.getText();
			String pass = pxnUtils.MD5(frame.textHost.getText());
			gcClient.get().Connect(host, port, user, pass);
			break;
		case "Cancel":
			break;
		}
//		if(buttonName.equals("Connect")) {
//			Main.getClient().getConnectState().setStateConnecting();
//			setDisplay(loginFrame.CONNECTING_WINDOW_NAME);

//		} else if(buttonName.equals("Cancel")) {
//			Main.getClient().getConnectState().setStateClosed();
////			setDisplay(loginFrame.LOGIN_WINDOW_NAME);
//		}
	}
	public void ComboChanged(ItemEvent event, String selectedStr) {
		switch(selectedStr) {
		case SavedStatic_Unsaved:
			return;
//		case SavedStatic_RunServerLocal:
//			frame.setHost("127.0.0.1");
//			frame.setPort(1142);
//			break;
		case SavedStatic_LocalHost:
			frame.setHost("127.0.0.1");
			frame.setPort(1142);
			break;
		case "home:1142":
			frame.setHost("home");
			frame.setPort(1142);
			frame.setUsername("user");
			frame.setPassword("pass");
		default:
			break;
		}
	}
//	// get info from text boxes
//	public void getConnectInfo(StringBuilder host, Integer port, StringBuilder username, StringBuilder password) {
//	}


	// populate saved servers
	public void populateSavedServers(JComboBox<String> combo) {
		combo.addItem(SavedStatic_Unsaved);
//		combo.addItem(SavedStatic_RunServerLocal);
		combo.addItem(SavedStatic_LocalHost);
		combo.addItem("home:1142");
	}


//	// display window card
//	public LoginWindows getDisplay() {
//		return currentCard;
//	}
//	public void setDisplay(LoginWindows displayCard) {
//		if(displayCard == null) throw new NullPointerException("displayCard can't be null!");
//		this.currentCard = displayCard;
//		// call directly
//		if(SwingUtilities.isEventDispatchThread()) {
//			new setDisplayTask(displayCard).run();
//			return;
//		}
//		try {
//			// invoke gui event
//			SwingUtilities.invokeAndWait(new setDisplayTask(displayCard));
//		} catch (InterruptedException e) {
//			return;
//		} catch (InvocationTargetException e) {
//e.printStackTrace();
//		}
//	}
//	private class setDisplayTask implements Runnable {
//		private LoginWindows currentCard;
//		public setDisplayTask(LoginWindows displayCard) {
//			this.currentCard = displayCard;
//		}
//		@Override
//		public void run() {
//			frame.DisplayCard(currentCard);
//		}
//	}


	// key press event
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			Close();
		return false;
	}


////	public String getHost() {
////		String host = frame.textHost.getText();
////		if(host == null || host.isEmpty())
////			return "127.0.0.1";
////		return host;
////	}
////	public int getPort() {
////		int port = 1142;
////		String portStr = frame.textPort.getText();
////		if(portStr == null || portStr.isEmpty())
////			return port;
////		port = Integer.parseInt(portStr);
////		return port;
////	}
////	public String getUsername() {
////		return frame.textUsername.getText();
////	}
////	public String getPassword() {
////		return pxnUtils.MD5(new String(frame.textPassword.getPassword()));
////	}


//	// set connecting message
//	public void setMessage(String message) {
//		// call directly
//		if(SwingUtilities.isEventDispatchThread()) {
//			new setMessageTask(message).run();
//			return;
//		}
//		try {
//			// invoke gui event
//			SwingUtilities.invokeAndWait(new setMessageTask(message));
//		} catch (InterruptedException ignore) {
//			return;
//		} catch (InvocationTargetException e) {
//e.printStackTrace();
//		}
//	}
//	private class setMessageTask implements Runnable {
//		private final String message;
//		public setMessageTask(String message) {
//			this.message = message;
//		}
//		@Override
//		public void run() {
//			frame.setMessage(message);
//		}
//	}


}
