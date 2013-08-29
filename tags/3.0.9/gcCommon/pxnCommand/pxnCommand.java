package com.growcontrol.gcCommon.pxnCommand;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.pxnParser.pxnParser;


public class pxnCommand {

	// command data
	public final String commandStr;
	public volatile List<String> aliases = new ArrayList<String>();
	public volatile String usageStr = null;


	public pxnCommand(String commandStr) {
		if(commandStr == null)   throw new NullPointerException("commandStr cannot be null!");
		if(commandStr.isEmpty()) throw new NullPointerException("commandStr cannot be empty!");
		this.commandStr = commandStr.toLowerCase();
	}
	// clone
	public pxnCommand clone() {
		pxnCommand command = new pxnCommand(this.commandStr);
		for(String alias : this.aliases)
			command.addAlias(alias);
		command.setUsage(this.usageStr);
		return command;
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


	// command usage
	public pxnCommand setUsage(String usageStr) {
		if(usageStr == null || usageStr.isEmpty())
			usageStr = null;
		this.usageStr = usageStr;
		return this;
	}
	public String getUsage() {
		// build aliases
		String aStr = "";
		for(String a : this.aliases) {
			if(a == null || a.isEmpty()) continue;
			if(!aStr.isEmpty()) aStr += " ";
			aStr += a;
		}
		// return usage string
		if(!aStr.isEmpty())
			return usageStr+"\nAliases: "+aStr;
		return usageStr;
	}
	public String getUsageStr() {
		String str = "";
		for(String s : pxnParser.parseMultiLine(getUsage()) ) {
			if(!str.isEmpty()) str += "\n";
			str += "  "+s;
		}
		if(str.isEmpty())
			return null;
		return str;
	}


}
