package com.growcontrol.gcServer.socketServer;

import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnParser.pxnParser;
import com.growcontrol.gcCommon.pxnSocket.processor.pxnSocketProcessor;
import com.growcontrol.gcCommon.pxnSocket.worker.pxnSocketWorker;
import com.growcontrol.gcServer.gcServer;


public class gcPacketReader extends pxnSocketProcessor {


	public gcPacketReader() {
//		// in/out queue size
//		super(1000, 1000);
	}


	@Override
	public void ProcessData(pxnSocketWorker worker, pxnParser line) {
//System.out.println("PROCESSING: "+line.getOriginal());
		String first = line.getFirst();
		switch(first.toUpperCase()) {
		case "HELLO":
pxnLogger.get().severe("Got HELLO packet!");
// send HEY packet
gcPacketSender.sendHEY(worker, gcServer.version);
			break;
		case "LIST":
// LIST request
processLIST(worker, line);
			break;
		case "FILE":
// FILE request
//		sendData("SENDFILE: test.txt");
pxnLogger.get().severe("SENDING FILE");
			break;
		default:
			// unknown packet
			pxnLogger.get().warning("Unknown Packet: "+line.getOriginal());
			break;
		}
	}


//	// process list packet
	private void processLIST(pxnSocketWorker worker, pxnParser line) {
//		String next = line.getNext();
//		if(next == null || next.isEmpty()) throw new NullPointerException("Invalid 'LIST' packet! "+line.getOriginal());
//
//		// list zones
//		if(next.equalsIgnoreCase("zones")) {
//			try {
//				gcPacketSender.sendLISTZones(processor);
//			} catch (Exception e) {
//pxnLogger.get().exception(e);
//			}
//		} else
//
//		// list plugins
//		if(next.equalsIgnoreCase("plugins")) {
//			next = line.getNext();
//			if(next == null || next.isEmpty()) throw new NullPointerException("Invalid 'LIST plugins' packet! "+line.getOriginal());
//			// client plugins
//			if(next.equalsIgnoreCase("client")) {
//				try {
//					gcPacketSender.sendLISTPluginsClient(processor);
//				} catch (Exception e) {
//pxnLogger.get().exception(e);
//				}
//			} else {
//System.out.println("Invalid 'LIST plugins' packet!");
//			}
//		}
	}


}
