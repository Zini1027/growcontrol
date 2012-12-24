package com.growcontrol.gcClient.clientPlugin;

import java.io.File;

import com.growcontrol.gcClient.Main;
import com.growcontrol.gcClient.clientPlugin.events.gcClientEventCommand;
import com.growcontrol.gcClient.clientPlugin.listeners.gcClientListenerCommand;
import com.growcontrol.gcClient.clientPlugin.listeners.gcClientListenerGroup;
import com.growcontrol.gcClient.clientPlugin.listeners.gcClientListenerGroup.ListenerType;
import com.poixson.pxnEvent.pxnEvent.EventPriority;
import com.poixson.pxnPlugin.pxnPluginManager;
import com.poixson.pxnPlugin.pxnPluginYML;


public class gcClientPluginManager extends pxnPluginManager {

	// listeners
	protected final gcClientListenerGroup commandListenerGroup = new gcClientListenerGroup(ListenerType.COMMAND);


//	// client gui frames for plugins
//	public gcPluginFrame newFrame(String title) {
//		gcPluginFrame frame = new gcPluginFrame(title);
//		addFrame(frame);
//		return frame;
//	}
//	public void addFrame(gcPluginFrame frame) {
//		frames.add(frame);
//	}


	// get plugin.yml
	@Override
	protected pxnPluginYML getPluginYML(File f) {
		return new gcPluginYML(f, this.pluginYmlFileName);
	}


	// register listeners
	public void registerCommandListener(gcClientListenerCommand listener) {
		commandListenerGroup.register(listener);
	}
//	public gcServerListenerGroup getCommandListenerGroup() {
//		return commandListenerGroup;
//	}
	public boolean triggerEvent(gcClientEventCommand event) {
//		gcClient.log.debug("Triggering event: "+event.getLine().getFirst());
		for(EventPriority priority : EventPriority.values()) {
			if(commandListenerGroup.triggerEvent(event, priority))
				event.setHandled();
		}
		return event.isHandled();
	}


	// add frame to dashboard
	public void addFrame(gcPluginFrame frame) {
		Main.getConnectState().getFrame("dashboard");
	}


}
