package com.growcontrol.gcServer.serverPlugin.listeners;

import com.growcontrol.gcCommon.pxnListener.pxnEvent;
import com.growcontrol.gcCommon.pxnListener.pxnListener;


public class gcServerListener extends pxnListener {

	@Override
	public boolean onEvent(pxnEvent event) {
		return false;
	}


//	@Override
//	public boolean doEvent(pxnEvent event) {
//		if(event instanceof gcServerEvent) {
//
//			// command event
//			if(event instanceof gcServerEventCommand) {
//				// set event command if found
//				if(this instanceof gcServerListenerCommand)
//					((gcServerListenerCommand) this).setHasCommand((gcServerEventCommand) event);
//				// trigger command event
//				return ((gcServerListenerCommand) this).onCommand( (gcServerEventCommand) event );
//
////			// somethingelse event
////			} else if(event instanceof gcServerEventSomethingelse)
////				return ((gcServerListenerSomethingelse) this).onSomethingelse( (gcServerEventSomethingelse) event );
//
//			}
//		}
//		return false;
//	}
//	return doEvent( (gcServerEvent) event );
//	public boolean doEvent(gcServerEvent event) {
//		public boolean doEvent(gcServerEvent event) {
//		if(event == null) throw new NullPointerException("event cannot be null");
//		if(event instanceof gcServerEventCommand)
//			return ((gcServerListenerCommand) this).doEvent( (gcServerEventCommand) event );
//		else if(event instanceof gcServerEventSomethingelse)
//			return ((gcServerListenerSomethingelse) this).doEvent( (gcServerEventSomethingelse) event );
//		gcServer.log.severe("Event not handled!");
//		return false;
//	}


}
