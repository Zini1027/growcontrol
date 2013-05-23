package com.gcCommon.pxnCommand;

import java.util.ArrayList;
import java.util.List;

import com.gcCommon.pxnEvent.pxnEvent;
import com.gcCommon.pxnListener.pxnListener;


public class pxnCommand extends pxnListener {

	private String commandStr = "";
	private List<String> aliases = new ArrayList<String>();
	private String usageMsg = null;


	// command
	public pxnCommand(String commandStr) {
		if(commandStr == null)   throw new NullPointerException("commandStr cannot be null!");
		if(commandStr.isEmpty()) throw new NullPointerException("commandStr cannot be empty!");
		this.commandStr = commandStr.toLowerCase();
	}
	public String get(int index) {
		if(index <= -1)
			return commandStr;
		if(index < aliases.size())
			return aliases.get(index);
		return null;
	}
	public String toString() {
		return this.get(-1);
	}


	// aliases
	public pxnCommand addAlias(String aliasStr) {
		if(aliasStr == null || aliasStr.isEmpty()) return this;
		this.aliases.add(aliasStr.toLowerCase());
		return this;
	}
	public pxnCommand addAliases(List<String> aliases) {
		if(aliases == null) return this;
		for(String alias : aliases)
			this.addAlias(alias);
		return this;
	}


	// is command/alias
	public boolean isCommand(String commandStr) {
		if(commandStr == null || commandStr.isEmpty()) return false;
		commandStr = commandStr.toLowerCase();
		// is command
		if(commandStr == this.commandStr)
			return true;
		// is alias
		if(this.hasAlias(commandStr))
			return true;
		// not a match
		return false;
	}
	public boolean hasAlias(String commandStr) {
		if(commandStr == null || commandStr.isEmpty()) return false;
		commandStr = commandStr.toLowerCase();
		for(String str : this.aliases)
			if(str == commandStr)
{
//System.out.println(this.name+"  [ "+pxnUtils.addStringSet("", aliases, ", ")+" ]");
				return true;
}
		return false;
	}


	// command usage
	public void setUsage(String usage) {
		if(usage == null || usage.isEmpty())
			this.usageMsg = null;
		else
			this.usageMsg = usage;
	}
	public String getUsage() {
		return this.usageMsg;
	}


	@Override
	public boolean doEvent(pxnEvent event) {
		// TODO Auto-generated method stub
		return false;
	}


}
