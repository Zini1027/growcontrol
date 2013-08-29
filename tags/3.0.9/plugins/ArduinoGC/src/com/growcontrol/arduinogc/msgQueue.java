package com.growcontrol.arduinogc;

import java.util.HashMap;
import java.util.Map;

import com.growcontrol.arduinogc.ArduinoPin.PinMode;


public class msgQueue {

	// queue hash map
	protected int currentQueue = 0;
	public HashMap<Integer, msgQueueItem> queue = new HashMap<Integer, msgQueueItem>();
	public class msgQueueItem {
		public boolean sent = false;
		public String rawCommand = null;
		public final int pinNum;
		public final PinMode pinMode;
		public final int pinState;
		// set raw command
		public msgQueueItem(String rawCommand) {
			this.pinNum = -1;
			this.pinMode = null;
			this.pinState = -1;
			this.rawCommand = rawCommand;
		}
		// set pin mode
		public msgQueueItem(int pinNum, PinMode pinMode) {
			this.pinNum = pinNum;
			this.pinMode = pinMode;
			this.pinState = -1;
		}
		// set pin state
		public msgQueueItem(int pinNum, int pinState) {
			this.pinNum = pinNum;
			this.pinMode = null;
			this.pinState = pinState;
		}
	}


	// count
	public int countNeedSending() {
		int count = 0;
		for(msgQueueItem item : queue.values())
			if(!item.sent) count++;
		return count;
	}


	// send raw command
	public void sendRawCommand(String rawCommand) {
		synchronized(queue) {
			currentQueue++;
			queue.put(currentQueue, new msgQueueItem(rawCommand));
		}
	}
	// set pin mode
	public void setPinMode(int pinNum, PinMode pinMode) {
		synchronized(queue) {
			currentQueue++;
			queue.put(currentQueue, new msgQueueItem(pinNum, pinMode));
		}
	}
	// set pin state
	public void setPinState(int pinNum, int pinState) {
		synchronized(queue) {
			currentQueue++;
			queue.put(currentQueue, new msgQueueItem(pinNum, pinState));
		}
	}


	// build message to send
	public synchronized String getMessages() {
		String msgOutput = "";
		synchronized(queue) {
			if(queue.size() == 0) return null;
			for(Map.Entry<Integer, msgQueueItem> entry : queue.entrySet()) {
				//TODO: remove these entries?
				if(entry.getValue() == null) continue;
				if(entry.getValue().sent) continue;
				String msg = getMessage(entry.getKey(), entry.getValue());
				if(msg == null || msg.isEmpty()) continue;
				if(msgOutput.length()+msg.length() >= 20) break;
				entry.getValue().sent = true;
				msgOutput += msg+"\r\n";
			}
//TODO:
ArduinoGC.log.severe("Size of queue: "+Integer.toString(queue.size()));
ArduinoGC.log.severe("Sending message: "+msgOutput);
		}
		return msgOutput;
	}
	protected String getMessage(int msgNumber, msgQueueItem queueItem) {
		if(queueItem.rawCommand != null)
			return queueItem.rawCommand;
		if(queueItem.pinMode != null)
			return "pin "+Integer.toString(queueItem.pinNum)+" "+Integer.toString(queueItem.pinState);
		if(queueItem.pinState != -1)
			return "pin "+Integer.toString(queueItem.pinNum)+" "+Integer.toString(queueItem.pinState);
		return null;
	}


}
