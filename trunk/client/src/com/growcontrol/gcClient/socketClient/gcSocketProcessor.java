package com.growcontrol.gcClient.socketClient;

import com.growcontrol.gcClient.socketClient.protocol.sendClientPackets;
import com.poixson.pxnParser.pxnParser;
import com.poixson.pxnSocket.pxnSocketProcessor;
import com.poixson.pxnSocket.pxnSocketProcessorThreaded;


public class gcSocketProcessor extends pxnSocketProcessorThreaded {


	public gcSocketProcessor() {
		// in/out queue size
		super(1000, 1000);
	}


	@Override
	public void processNow(pxnSocketProcessor processor, pxnParser line) {
		String first = line.getFirst();
		// HEY packet
		if(first.equalsIgnoreCase("HEY")) {
System.out.println("Got HEY packet!");
			sendClientPackets.sendLIST(processor, "zones");
			sendClientPackets.sendLIST(processor, "plugins client");
		} else
		// ZONE packet
		if(first.equalsIgnoreCase("ZONE")) {
System.out.println("Got ZONE packet! "+line.getRest());
		} else
		// PLUGIN packet
		if(first.equalsIgnoreCase("PLUGIN")) {
System.out.println("Got PLUGIN packet! "+line.getRest());
		} else {
System.out.println("Unknown packet: "+line.getOriginal());
		}
	}

//private boolean sendingFile = false;
//private boolean sendingFile = true;

//if(sendingFile) return;
//System.out.println("PROCESSING: "+line.getOriginal());

//processor.sendData("TEST");
//boolean b = true;
//if(b) return;
//System.out.println("SETTING READY!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//Main.getConnectState().setStateReady();
//for(int i=0; i<1000; i++)
//sendData("TEST"+Integer.toString(i));
//			sendData("HEYBACK! gimme some plugins!");
//			gcLogger.getLogger().severe("Got HEY packet back!");
//sendingFile = true;
//			sendData("FILE");
//			return;
//		}
//		gcLogger.getLogger().warning("Unknown Packet: "+line.getOriginal());


}
