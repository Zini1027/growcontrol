package com.growcontrol.gcServer;

import com.growcontrol.gcServer.commands.gcCommandsHolder;

public class DefaultCommands {

	private gcCommandsHolder commands = null;


	public DefaultCommands() {
		if(commands != null) return;
		commands = new gcCommandsHolder();
		// basic commands
		// lists
		// input / output
		// tools
	}



}
