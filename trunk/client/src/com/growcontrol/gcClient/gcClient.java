package com.growcontrol.gcClient;

public class gcClient {
	public static final String version = "3.0.1";
	private static gcClient client = null;


	public static void main(String[] args) {
		if(client != null) return;
		for(String arg : args) {
			// version
			if(arg.equalsIgnoreCase("version")) {
				System.out.println("GrowControl "+version+" Server");
				System.exit(0);
			}
		}
		// start gc client gui
		client = new gcClient();
	}


	public gcClient() {
	}


}
