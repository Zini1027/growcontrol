package com.growcontrol.gcServer.serverPlugin.commands;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcServer.pxnUtils;

public class gcCommand {

	protected final String name;
	protected List<String> aliases = new ArrayList<String>();
	protected String usageMsg = null;


	public gcCommand(String name) {
		if(name == null) throw new NullPointerException("name cannot be null");
		this.name = name;
	}


	// command name
	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}


	// aliases
	public gcCommand addAlias(String alias) {
		if(alias == null) throw new NullPointerException("alias cannot be null");
		aliases.add(alias.toLowerCase());
		return this;
	}
	public gcCommand addAliases(List<String> aliases) {
		if(aliases == null) throw new NullPointerException("aliases cannot be null");
		for(String alias : aliases)
			aliases.add(alias);
		return this;
	}


	// has command/alias
	public boolean hasCommand(String name) {
		if(name == null) throw new NullPointerException("name cannot be null");
		// is command name
		if(this.name.equalsIgnoreCase(name)) return true;
		// has alias
System.out.println(this.name+"  [ "+pxnUtils.addStringSet("", aliases, ", ")+" ]");
		return aliases.contains(name.toLowerCase());
	}
	

	// command usage
	public String getUsage() {
		return usageMsg;
	}
	public void setUsage(String usageMsg) {
		this.usageMsg = usageMsg;
	}


}
