package com.growcontrol.gcServer;

import com.growcontrol.gcServer.commands.gcCommand;
import com.growcontrol.gcServer.commands.gcCommandsHolder;
import com.growcontrol.gcServer.scheduler.gcScheduler;

public class DefaultCommands {

	public static final gcCommandsHolder commands = getDefaultCommands();


	private static gcCommandsHolder getDefaultCommands() {
		gcCommandsHolder commands = new gcCommandsHolder();
		// basic commands
		commands.addCommand("stop")
			.addAlias("exit")
			.addAlias("quit")
			.addAlias("shutdown");
		commands.addCommand("kill");
		commands.addCommand("pause");
		commands.addCommand("clear")
			.addAlias("cls");
		commands.addCommand("help")
			.addAlias("?");
		commands.addCommand("version");
		commands.addCommand("say")
		.addAlias("broadcast");
		// lists
		commands.addCommand("plugins");
		commands.addCommand("devices");
		// input / output
		commands.addCommand("set");
		commands.addCommand("get");
		commands.addCommand("watch");
		// tools
		commands.addCommand("ping");
		commands.addCommand("threads");
		return commands;
	}


	// run commands
	public static boolean onCommand(gcCommand command, String[] args) {
		// stop
		if(command.equals("stop")) {
			gcServer.Shutdown();
			return true;
		}
		// kill
		if(command.equals("kill"))
			System.exit(0);
		// pause
		if(command.equals("pause")) {
			gcScheduler.pauseAll();
			return true;
		}
		// clear
		if(command.equals("clear")) {
			gcServer.log.clear();
			return true;
		}
		// help
		if(command.equals("help")) {
			//TODO:
			gcServer.log.warning("command not yet implemented");
			return true;
		}
		// version
		if(command.equals("version")) {
			//TODO:
			gcServer.log.warning("command not yet implemented");
			return true;
		}
		// say
		if(command.equals("say")) {
			String msg = "";
			for(String line : args) msg += " "+line;
			gcServer.log.info("Server says: "+msg);
			return true;
		}

		// list plugins
		if(command.equals("plugins")) {
			//TODO:
			gcServer.log.warning("command not yet implemented");
//			gcPluginLoader.listPlugins();
			return true;
		}
		// list devices
		if(command.equals("devices")) {
			//TODO:
			gcServer.log.warning("command not yet implemented");
//			gcServer.deviceLoader.listDevices();
			return true;
		}

		// set input / output
		if(command.equals("set")) {
			//TODO:
			gcServer.log.warning("command not yet implemented");
//			return gcServer.deviceLoader.onCommand(command, args, args.length);
			return true;
		}
		// get input / output
		if(command.equals("get")) {
			//TODO:
			gcServer.log.warning("command not yet implemented");
			return true;
		}
		// watch input / output
		if(command.equals("watch")) {
			//TODO:
			gcServer.log.warning("command not yet implemented");
			return true;
		}

		// ping
		if(command.equals("ping")) {
			//TODO:
			gcServer.log.warning("command not yet implemented");
			return true;
		}

		return false;
	}


//	if(command.equalsIgnoreCase("threads")) {
//	// Find the root thread group
//	ThreadGroup root = Thread.currentThread().getThreadGroup().getParent();
//	while (root.getParent() != null) {
//	    root = root.getParent();
//	}
//GrowControl.log.severe(Integer.toString(root.activeCount()));
//	// Visit each thread group
//GrowControl.log.severe(Integer.toString(Thread.activeCount()));
//	visitThread(root, 0);
//	return true;
//}
//else if(line.equals("list coms"))
//Serial.listPorts();
//	// This method recursively visits all thread groups under `group'.
//	private static void visitThread(ThreadGroup group, int level) {
//	    // Get threads in `group'
//	    int numThreads = group.activeCount();
//	    Thread[] threads = new Thread[numThreads*2];
//	    numThreads = group.enumerate(threads, false);
//	    // Enumerate each thread in `group'
//	    for(int i=0; i<numThreads; i++)
//	        Thread thread = threads[i];
//	    // Get thread subgroups of `group'
//	    int numGroups = group.activeGroupCount();
//	    ThreadGroup[] groups = new ThreadGroup[numGroups*2];
//	    numGroups = group.enumerate(groups, false);
//	    // Recursively visit each subgroup
//	    for(int i=0; i<numGroups; i++) {
//	    	visitThread(groups[i], level+1);
//	    }
//	}


}
