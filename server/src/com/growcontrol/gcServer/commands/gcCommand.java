package com.growcontrol.gcServer.commands;

import java.util.ArrayList;
import java.util.List;

public class gcCommand {

	protected final String name;
	protected String desc = null;
	protected String usageMsg = null;
	protected List<String> aliases = new ArrayList<String>();


	public gcCommand(String name) {
		this.name = name;
	}


	// command name
	public String toString() {
		return name;
	}
	public boolean equals(String name) {
		if(name == null) return (this.name==null);
		return name.equalsIgnoreCase(this.name);
	}


	// aliases
	public gcCommand addAlias(String alias) {
		if(alias != null) aliases.add(alias);
		return this;
	}
	public boolean hasAlias(String alias) {
		return aliases.contains(alias);
	}


}
