package com.growcontrol.gcServer.socketServer;

import com.growcontrol.gcServer.gcServer;
import com.poixson.pxnParser.pxnParser;
import com.poixson.pxnSocket.pxnSocketProcessorThreaded;


public class gcSocketProcessor extends pxnSocketProcessorThreaded {


	public gcSocketProcessor() {
//		// in/out queue size
		super(1000, 1000);
	}


	@Override
	public void processNow(pxnParser line) {
//System.out.println("PROCESSING: "+line.getOriginal());
		String first = line.getFirst();
		// HEY packet
		if(first.equalsIgnoreCase("HELLO")) {
			sendData("HEY");
			gcServer.getLogger().severe("Got HELLO packet!");
			return;
		}
		if(first.equalsIgnoreCase("FILE")) {
			sendData("SENDFILE: test.txt");
gcServer.getLogger().severe("SENDING FILE");
			return;
		}
//		pxnLogger.log().warning("Unknown Packet: "+line.getOriginal());
	}


}
