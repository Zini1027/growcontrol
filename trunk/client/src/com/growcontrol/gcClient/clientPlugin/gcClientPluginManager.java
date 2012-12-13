package com.growcontrol.gcClient.clientPlugin;

import java.util.ArrayList;
import java.util.List;

import com.poixson.pxnPlugin.pxnPluginManager;


public class gcClientPluginManager extends pxnPluginManager {

	// plugin owned frames
	protected List<gcPluginFrame> frames = new ArrayList<gcPluginFrame>();


	// client gui frames for plugins
	public gcPluginFrame newFrame(String title) {
		gcPluginFrame frame = new gcPluginFrame(title);
		addFrame(frame);
		return frame;
	}
	public void addFrame(gcPluginFrame frame) {
		frames.add(frame);
	}


}
