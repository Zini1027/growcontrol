package com.growcontrol.gcServer.socketServer;

import com.growcontrol.gcServer.gcServer;
import com.poixson.pxnParser.pxnParser;
import com.poixson.pxnSocket.pxnSocketProcessor;
import com.poixson.pxnSocket.pxnSocketProcessorThreaded;


public class gcSocketProcessor extends pxnSocketProcessorThreaded {


	public gcSocketProcessor() {
//		// in/out queue size
		super(1000, 1000);
	}


	@Override
	public void processNow(pxnSocketProcessor processor, pxnParser line) {
//System.out.println("PROCESSING: "+line.getOriginal());
		String first = line.getFirst();

		// HEY packet
		if(first.equalsIgnoreCase("HELLO")) {
gcServer.getLogger().severe("Got HELLO packet!");
			try {
				sendServerPackets.sendHEY(processor, gcServer.version);
			} catch (Exception e) {
e.printStackTrace();
			}
		} else

		// LIST request
		if(first.equalsIgnoreCase("LIST")) {
			processLIST(processor, line);
		} else

		// FILE request
		if(first.equalsIgnoreCase("FILE")) {
//			sendData("SENDFILE: test.txt");
gcServer.getLogger().severe("SENDING FILE");
			return;

		// unknown packet
		} else {
			System.out.println("Unknown packet: "+line.getOriginal());
		}
//		pxnLogger.log().warning("Unknown Packet: "+line.getOriginal());
	}


	// process list packet
	private void processLIST(pxnSocketProcessor processor, pxnParser line) {
		String next = line.getNext();
		if(next == null || next.isEmpty()) throw new NullPointerException("Invalid 'LIST' packet! "+line.getOriginal());

		// list zones
		if(next.equalsIgnoreCase("zones")) {
			try {
				sendServerPackets.sendLISTZones(processor);
			} catch (Exception e) {
e.printStackTrace();
			}
		} else

		// list plugins
		if(next.equalsIgnoreCase("plugins")) {
			next = line.getNext();
			if(next == null || next.isEmpty()) throw new NullPointerException("Invalid 'LIST plugins' packet! "+line.getOriginal());
			// client plugins
			if(next.equalsIgnoreCase("client")) {
				try {
					sendServerPackets.sendLISTPluginsClient(processor);
				} catch (Exception e) {
e.printStackTrace();
				}
			} else {
System.out.println("Invalid 'LIST plugins' packet!");
			}
		}
	}


}
