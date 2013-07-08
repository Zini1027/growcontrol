package com.growcontrol.gcCommon.pxnCommand;

import java.util.ArrayList;
import java.util.List;


public class pxnCommand {

	// command data
	public final String commandStr;
	public List<String> aliases = new ArrayList<String>();
	public String usageStr = null;


	public pxnCommand(String commandStr) {
		if(commandStr == null)   throw new NullPointerException("commandStr cannot be null!");
		if(commandStr.isEmpty()) throw new NullPointerException("commandStr cannot be empty!");
		this.commandStr = commandStr.toLowerCase();
	}


	// command alias
	public pxnCommand addAlias(String aliasStr) {
		if(aliasStr == null || aliasStr.isEmpty()) return null;
		aliases.add(aliasStr.toLowerCase());
		return this;
	}
	public boolean hasAlias(String str) {
		if(str == null || str.isEmpty()) return false;
		for(String alias : aliases)
			if(str.equals(alias))
				return true;
		return false;
	}
//	public String getAlias(int index) {
//		if(index < 0)
//			return commandStr;
//		if(index < aliases.size())
//			return aliases.get(index);
//		return null;
//	}
//	public String toString() {
//		return this.getAlias(-1);
//	}


	// command usage
	public pxnCommand setUsage(String usageStr) {
		if(usageStr == null || usageStr.isEmpty())
			usageStr = null;
		this.usageStr = usageStr;
		return this;
	}
	public String getUsage() {
		return usageStr;
	}


}
