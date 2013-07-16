package com.growcontrol.gcServer.socketServer;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnParser.pxnParser;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketProcessor;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketProcessorThreaded;


public class gcSocketProcessor extends pxnSocketProcessorThreaded {


	public gcSocketProcessor() {
		// in/out queue size
		super(1000, 1000);
	}


	@Override
	public void processNow(pxnSocketProcessor processor, pxnParser line) {
//System.out.println("PROCESSING: "+line.getOriginal());
		String first = line.getFirst();

		// HEY packet
		if(first.equalsIgnoreCase("HELLO")) {
pxnLogger.get().severe("Got HELLO packet!");
			try {
				sendServerPackets.sendHEY(processor, gcServer.version);
			} catch (Exception e) {
pxnLogger.get().exception(e);
			}
		} else

		// LIST request
		if(first.equalsIgnoreCase("LIST")) {
			processLIST(processor, line);
		} else

		// FILE request
		if(first.equalsIgnoreCase("FILE")) {
//			sendData("SENDFILE: test.txt");
pxnLogger.get().severe("SENDING FILE");
			return;
		}
		// unknown packet
		pxnLogger.get().warning("Unknown Packet: "+line.getOriginal());
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
pxnLogger.get().exception(e);
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
pxnLogger.get().exception(e);
				}
			} else {
System.out.println("Invalid 'LIST plugins' packet!");
			}
		}
	}


}
