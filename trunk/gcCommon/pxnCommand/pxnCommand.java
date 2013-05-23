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
		if(commandStr == null) throw new NullPointerException("commandStr cannot be null");
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
		if(aliasStr == null) throw new NullPointerException("aliasStr cannot be null");
		this.aliases.add(aliasStr.toLowerCase());
		return this;
	}
	public pxnCommand addAliases(List<String> aliases) {
		if(aliases == null) throw new NullPointerException("aliases list cannot be null");
		for(String alias : aliases)
			this.addAlias(alias);
		return this;
	}


	// is command/alias
	public boolean isCommand(String commandStr) {
		if(commandStr == null) return false;
		commandStr = commandStr.toLowerCase();
		if(commandStr == this.commandStr)
			return true;
		if(this.hasAlias(commandStr))
			return true;
		return false;
	}
	public boolean hasAlias(String commandStr) {
		if(commandStr == null) return false;
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
		if(usage == null)
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
