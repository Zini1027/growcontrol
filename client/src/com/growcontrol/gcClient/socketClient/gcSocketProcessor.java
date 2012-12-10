package com.growcontrol.gcClient.socketClient;

import com.poixson.pxnSocket.pxnSocketProcessorThreaded;


public class gcSocketProcessor extends pxnSocketProcessorThreaded {


	public gcSocketProcessor() {
		// in/out queue size
//		super(1, 1);
	}


	@Override
	public void processNow(String line) {
System.out.println("PROCESSING: "+line);
if(line.startsWith("HELLO"))
System.out.println("HEY");
	}


}
