package com.growcontrol.gcCommon.pxnLogger;

import java.io.File;
import java.io.IOException;

import jline.console.ConsoleReader;
import jline.console.history.FileHistory;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcCommon.pxnApp;
import com.growcontrol.gcCommon.pxnUtils;


//TODO: password login
//If we input the special word then we will mask
//the next line.
//if ((trigger != null) && (line.compareTo(trigger) == 0))
//	line = reader.readLine("password> ", mask);
//if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) break;
public class pxnConsole implements Runnable {
	public static final String defaultPrompt = ">";

	// console instance
	private static pxnConsole console = null;
	// jLine reader
	private static ConsoleReader reader = null;
//	// jAnsi
//	private static PrintWriter out = null;

	private Thread thread = null;
	private volatile boolean stopping = false;
	private volatile Boolean running = false;


	public static synchronized pxnConsole get() {
		if(console == null)
			console = new pxnConsole();
		return console;
	}
	private pxnConsole() {
		pxnLogger.Init();
//		out = new PrintWriter(AnsiConsole.out);
	}


	// jline console reader
	public static ConsoleReader getReader() {
		if(reader == null) {
			try {
				reader = new ConsoleReader();
				reader.setBellEnabled(false);
				reader.setPrompt(defaultPrompt);
				FileHistory history = new FileHistory(new File("history.txt"));
				history.setMaxSize(100);
				reader.setHistory(history);
				reader.setHistoryEnabled(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return reader;
	}


//	public static void setPrompt(String promptStr) throws IOException {
//	if(promptStr == null)
//		reader.setDefaultPrompt(defaultPrompt);
//	else
//		reader.setDefaultPrompt(promptStr);
//	reader.redrawLine();
//	reader.flushConsole();
//}


//// read console input
//public static String readLine() throws IOException {
//	if(reader == null) throw new NullPointerException("reader can't be null!");
//	return reader.readLine();
//}


	public static void Clear() {
		AnsiConsole.out.println(
			Ansi.ansi()
			.eraseScreen()
			.cursor(0, 0)
		);
	}


	// console input thread
	@Override
	public void run() {
		synchronized(running) {
			if(running) return;
			running = true;
		}
		while(!stopping) {
			if(thread != null && thread.isInterrupted()) break;
			// wait for commands
			String line = null;
			try {
				line = reader.readLine();
			} catch (IOException e) {
				pxnLog.get().exception(e);
				pxnUtils.Sleep(200);
				line = null;
			}
			if(line == null) break;
			if(!line.isEmpty())
				pxnApp.get().ProcessCommand(line);
		}
		running = false;
		System.out.println();
		System.out.println();
	}
	// start thread
	public synchronized void Start() {
		if(thread == null)
			thread = new Thread(this);
		thread.start();
	}
	public static void Close() {
		if(console == null) return;
		console.stopping = true;
		if(console.running)
			console.thread.interrupt();
		AnsiConsole.systemUninstall();
	}


}