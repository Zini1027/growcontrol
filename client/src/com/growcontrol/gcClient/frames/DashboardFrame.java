package com.growcontrol.gcClient.frames;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.tree.DefaultMutableTreeNode;


public class DashboardFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	//private JPanel contentPane;

	static DashboardFrame frame;
	public static void main(String[] args) {
		frame = new DashboardFrame();
	}


	public DashboardFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Grow Control 3.0.2");
		setBounds(100, 100, 600, 600);
//		contentPane = new JPanel();
//		setContentPane(contentPane);
//		contentPane.setLayout(new BorderLayout(0, 0));
		setLayout(new BorderLayout(0, 0));

		// menu bar
		setJMenuBar(BuildMenuBar());

		// split pane
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);

		// tree (left side)
		splitPane.setLeftComponent(BuildTree());

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(new Color(51, 102, 0));
		splitPane.setRightComponent(desktopPane);

		JInternalFrame internalFrame = new JInternalFrame("Timer 1");
		internalFrame.setFrameIcon(null);
		internalFrame.setBounds(89, 38, 242, 161);
		desktopPane.add(internalFrame);

		JToggleButton tglbtnNewToggleButton = new JToggleButton("New toggle button");
		internalFrame.getContentPane().add(tglbtnNewToggleButton, BorderLayout.CENTER);

//		JInternalFrame internalFrame_1 = new JInternalFrame("New JInternalFrame");
//		internalFrame_1.setBounds(104, 264, 277, 226);
//		desktopPane.add(internalFrame_1);

//		JToggleButton tglbtnNewToggleButton = new JToggleButton("New toggle button");
//		internalFrame_1.getContentPane().add(tglbtnNewToggleButton, BorderLayout.CENTER);
//
//		JInternalFrame internalFrame_2 = new jinternalframe();
//		internalFrame_2.setBounds(216, 12, 308, 353);
//		desktopPane.add(internalFrame_2);
//
//		JButton btnNewButton_1 = new JButton("New button");
//		internalFrame_2.getContentPane().add(btnNewButton_1, BorderLayout.CENTER);
//
//		JButton btnNewButton_2 = new JButton("56456");
//		btnNewButton_2.setBounds(125, 23, 118, 25);
//		internalFrame_2.getContentPane().add(btnNewButton_2);
//		internalFrame_2.setVisible(true);
//		internalFrame_1.setVisible(true);
		internalFrame.setVisible(true);

		setVisible(true);
	}


	// build menu bar
	private JMenuBar BuildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// file menu
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuFile);
		// new menu
		JMenu menuFileNew = new JMenu("New");
		menuFileNew.setMnemonic(KeyEvent.VK_N);
		BuildMenuFileNew(menuFileNew);
		menuFile.add(menuFileNew);
		// -----
		menuFile.add(new JSeparator());
		// exit
		JMenuItem menuFileExit = new JMenuItem("Exit");
		menuFileExit.setMnemonic(KeyEvent.VK_X);
		menuFile.add(menuFileExit);
		return menuBar;
	}
	// file->new
	private void BuildMenuFileNew(JMenu menuFileNew) {
		JMenuItem menuFileNewTimer = new JMenuItem("Timer");
		menuFileNewTimer.setMnemonic(KeyEvent.VK_T);
		menuFileNew.add(menuFileNewTimer);
	}


	private CheckboxTree BuildTree() {
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Zones");
		rootNode.add(BuildZone("Zone 1"));
		rootNode.add(BuildZone("Zone 2"));
		rootNode.add(BuildZone("Zone 3"));
		CheckboxTree tree = new CheckboxTree(rootNode);
//		JTree tree = new JTree(rootNode);
//		someWindow.add(new JScrollPane(tree));
		return tree;
	}
	private DefaultMutableTreeNode BuildZone(String zoneName) {
		return new DefaultMutableTreeNode(zoneName);
	}


}
