package com.growcontrol.gcServer.socketServer;

import com.poixson.pxnSocket.pxnSocketProcessorThreaded;


public class gcSocketProcessor extends pxnSocketProcessorThreaded {


	@Override
	public void processLine(String line) {
System.out.println("PROCESSING: "+line);
if(line.startsWith("HELLO"))
System.out.println("HEY");
	}


}
