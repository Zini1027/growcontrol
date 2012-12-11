package com.growcontrol.gcServer.socketServer;

import com.poixson.pxnLogger.pxnLogger;
import com.poixson.pxnParser.pxnParser;
import com.poixson.pxnSocket.pxnSocketProcessorThreaded;


public class gcSocketProcessor extends pxnSocketProcessorThreaded {


//	public gcSocketProcessor() {
//		// in/out queue size
//		super(1, 1);
//	}


	@Override
	public void processNow(pxnParser line) {
System.out.println("PROCESSING: "+line.getOriginal());
		String first = line.getFirst();
		// HEY packet
		if(first.equalsIgnoreCase("HELLO")) {
			sendData("HEY");
			pxnLogger.log().severe("Got HELLO packet!");
			return;
		}
		pxnLogger.log().warning("Unknown Packet: "+line.getOriginal());
	}


}
