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
	public static final String defaultPrompt = "> ";

	// console instance
	private static volatile pxnConsole console = null;
	private static final Object lock = new Object();

	// jLine reader
	private static volatile ConsoleReader reader = null;

	private Thread thread = null;
	private volatile boolean stopping = false;
	private volatile Boolean running = false;


	public static pxnConsole get() {
		if(console == null) {
			synchronized(lock) {
				if(console == null)
					console = new pxnConsole();
			}
		}
		return console;
	}
	private pxnConsole() {
		pxnLogger.Init();
	}


	// jline console reader
	public static ConsoleReader getReader() {
		if(reader == null) {
			synchronized(lock) {
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
						pxnLog.get().exception(e);
					}
				}
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
		if(getReader() == null) {
			pxnLog.get().exception(new NullPointerException("reader not set!"));
			return;
		}
		String line;
		while(!stopping) {
			line = null;
			if(thread != null && thread.isInterrupted()) break;
			// wait for commands
			try {
				System.out.print('\r');
				line = reader.readLine();
			} catch (IOException e) {
				line = null;
				pxnLog.get().severe("Failed to get console input.", e);
				pxnUtils.Sleep(200);
				continue;
			} catch (Exception e) {
				line = null;
				pxnLog.get().fatal("Failed to get console input.", e);
				break;
			}
			if(line == null) continue;
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
		synchronized(lock) {
			if(console == null) return;
			console.stopping = true;
			if(console.running)
				console.thread.interrupt();
// causes thread to block!
//			if(reader != null)
//				reader.shutdown();
			AnsiConsole.systemUninstall();
		}
	}


}
