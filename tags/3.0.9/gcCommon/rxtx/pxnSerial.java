package com.growcontrol.gcCommon.rxtx;

import gnu.io.CommPortIdentifier;

import java.util.Enumeration;
import java.util.HashMap;


public class pxnSerial {


	public static synchronized HashMap<String, String> listPorts() {
		HashMap<String, String> comPorts = new HashMap<String, String>();
//		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		while(portEnum.hasMoreElements()) {
			CommPortIdentifier portId = portEnum.nextElement();
			comPorts.put( portId.getName(), getPortTypeName(portId.getPortType()) );
		}
		return comPorts;
	}


	// port type to string
	private static String getPortTypeName(int portType) {
		switch(portType) {
		case CommPortIdentifier.PORT_I2C:
			return "I2C";
		case CommPortIdentifier.PORT_PARALLEL:
			return "Parallel";
		case CommPortIdentifier.PORT_RAW:
			return "Raw";
		case CommPortIdentifier.PORT_RS485:
			return "RS485";
		case CommPortIdentifier.PORT_SERIAL:
			return "Serial";
		}
		return "Unknown type";
	}


}
