package com.growcontrol.gcCommon.pxnLogger.handlers;

import java.io.IOException;

import jline.console.ConsoleReader;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcCommon.pxnLogger.pxnConsole;
import com.growcontrol.gcCommon.pxnLogger.pxnLogRecord;


public class pxnLogHandlerConsole implements pxnLogHandler {
	private static final String handlerName = "CONSOLE";

	// handler instance
	private static volatile pxnLogHandlerConsole handler = null;
	private static final Object lock = new Object();
	private final ConsoleReader reader;


	public static pxnLogHandlerConsole get() {
		if(handler == null) {
			synchronized(lock) {
				if(handler == null)
					handler = new pxnLogHandlerConsole();
			}
		}
		return handler;
	}
	private pxnLogHandlerConsole() {
		reader = pxnConsole.getReader();
		AnsiConsole.systemInstall();
		System.out.println();
		Flush();
	}


	// close
	@Override
	public void Close() {
		AnsiConsole.systemUninstall();
	}
	@Override
	protected void finalize() {
		Close();
	}


	// handler name
	@Override
	public String getName() {
		return handlerName;
	}


	// print to console
	@Override
	public void Publish(pxnLogRecord rec) {
		String[] msgLines = rec.getMessage();
		if(msgLines.length == 0) return;
		// print single line
		if(msgLines.length == 1) {
			Publish(msgLines[0]);
			return;
		}
		// print multiple lines
		String msg = "";
		for(String line : msgLines) {
			// trim line endings from end
			while(line.endsWith("\n") || line.endsWith("\r"))
				line = line.substring(0, line.length() - 1);
			if(!msg.isEmpty())
				msg += "\n";
			msg += Ansi.ansi().render(line).toString();
		}
		Publish(msg);
	}
	@Override
	public synchronized void Publish(String msg) {
		if(msg == null) return;
		if(msg.length() < reader.getPrompt().length()+2)
			msg += "    ";
		try {
			System.out.println("\r"+msg);
			reader.drawLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Flush();
	}


	@Override
	public void Flush() {
		try {
			reader.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
