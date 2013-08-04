package com.growcontrol.gcCommon.pxnLogger.handlers;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcCommon.pxnLogger.pxnLogRecord;


public class pxnLogHandlerConsole implements pxnLogHandler {
	private static final String handlerName = "CONSOLE";


	// handler instance
	private static pxnLogHandlerConsole handler = null;
	public static synchronized pxnLogHandlerConsole get() {
		if(handler == null)
			handler = new pxnLogHandlerConsole();
		return handler;
	}
	private pxnLogHandlerConsole() {
		AnsiConsole.systemInstall();
		System.out.println();
		System.out.flush();
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


	@Override
	public String getName() {
		return handlerName;
	}


	// print to console
	@Override
	public void Publish(pxnLogRecord rec) {
		for(String line : rec.getMessage()) {
			System.out.println(
				Ansi.ansi().render(line).toString()
			);
		}
//ConsoleReader reader = new ConsoleReader();
//try {
//reader.printString(jline.ConsoleReader.RESET_LINE+"");
//reader.printString(msg);
//reader.printNewline();
//reader.flushConsole();
////reader.drawLine();
////reader.redrawLine();
//try {
//	reader.drawLine();
//} catch (Throwable e) {
//	reader.getCursorBuffer().clearBuffer();
//}
//reader.flushConsole();
//} catch (IOException e) {
//e.printStackTrace();
//System.out.print(msg);
//}
	}
	@Override
	public void Publish(String msg) {
		System.out.println(msg);
	}


	@Override
	public void Flush() {
		System.out.flush();
	}


}
