package com.growcontrol.gcClient.socketClient;

import com.growcontrol.gcClient.gcClient;
import com.growcontrol.gcClient.gcClient.ConnectState;
import com.poixson.pxnLogger.pxnLogger;
import com.poixson.pxnParser.pxnParser;
import com.poixson.pxnSocket.pxnSocketProcessorThreaded;


public class gcSocketProcessor extends pxnSocketProcessorThreaded {


	public gcSocketProcessor() {
//		// in/out queue size
		super(1000, 1000);
	}


	@Override
	public void processNow(pxnParser line) {
		System.out.println("PROCESSING: "+line.getOriginal());
		String first = line.getFirst();
		// HEY packet
		if(first.equalsIgnoreCase("HEY")) {
gcClient.setConnectState(ConnectState.READY);
for(int i=0; i<1000; i++)
sendData("TEST"+Integer.toString(i));
			sendData("HEYBACK! gimme some plugins!");
			pxnLogger.log().severe("Got HEY packet back!");
			sendData("FILE");
			return;
		}
		pxnLogger.log().warning("Unknown Packet: "+line.getOriginal());
	}


}
