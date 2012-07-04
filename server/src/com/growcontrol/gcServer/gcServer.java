package com.growcontrol.gcServer;

import org.fusesource.jansi.AnsiConsole;

public class gcServer {
	public static final String version = "3.0.1";
	private static gcServer server = null;

	// runtime args
	private static boolean noconsole = false;


	public static void main(String[] args) {
		if(server != null) return;
		for(String arg : args) {
			// version
			if(arg.equalsIgnoreCase("version")) {
				System.out.println("GrowControl "+version+" Server");
				System.exit(0);
			// no console
			} else if(arg.equalsIgnoreCase("noconsole"))
				noconsole = true;
		}
		// start gc server
		server = new gcServer();
	}


	public gcServer() {
		if(!noconsole) {
			AnsiConsole.systemInstall();
			ASCIIHeader();
		}
	}












	private static void ASCIIHeader() {
		
	}


}
