package com.growcontrol.gcServer.serverPlugin.listeners;

import com.growcontrol.gcServer.commands.gcCommand;

public interface gcServerPluginListenerCommand extends gcServerPluginListener {


	public boolean onCommand(gcCommand command, String[] args);


}
