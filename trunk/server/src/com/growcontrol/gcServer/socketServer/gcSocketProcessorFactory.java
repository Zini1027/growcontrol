package com.growcontrol.gcServer.socketServer;

import com.poixson.pxnSocket.pxnSocketProcessor;
import com.poixson.pxnSocket.pxnSocketProcessorFactory;


public class gcSocketProcessorFactory implements pxnSocketProcessorFactory {


	@Override
	public pxnSocketProcessor newProcessor() {
		return new gcSocketProcessor();
	}


}
