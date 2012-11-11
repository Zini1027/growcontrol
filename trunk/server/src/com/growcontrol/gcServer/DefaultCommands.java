package com.growcontrol.gcServer;

import com.growcontrol.gcServer.commands.gcCommand;
import com.growcontrol.gcServer.commands.gcCommandsHolder;
import com.growcontrol.gcServer.scheduler.gcScheduler;
import com.growcontrol.gcServer.serverPlugin.gcServerPluginLoader;

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
		commands.addCommand("start");
		commands.addCommand("stop");
		commands.addCommand("pause");
		commands.addCommand("clear")
			.addAlias("cls");
		commands.addCommand("help")
			.addAlias("?");
		commands.addCommand("version");
		commands.addCommand("say")
		.addAlias("broadcast");
		commands.addCommand("list");
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
		if(command == null) throw new NullPointerException();
		if(args    == null) throw new NullPointerException();
		// stop
		if(command.equals("stop")) {
			gcServer.Shutdown();
			return true;
		}
		// kill
		if(command.equals("kill"))
			System.exit(0);
		// start
		if(command.equals("start")) {
			gcScheduler.pauseAll(false);
			return true;
		}
		// stop
		if(command.equals("stop")) {
			gcScheduler.pauseAll(true);
			return true;
		}
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
			gcServer.log.info("[ Basic Commands ]");
			gcServer.log.info("version - Displays the current running version.");
			gcServer.log.info("start - Starts the scheduler.");
			gcServer.log.info("stop  - Stops the scheduler.");
			gcServer.log.info("pause - Pauses the scheduler.");
			gcServer.log.info("clear - Clears the screen.");
			gcServer.log.info("say - Broadcasts a message.");
			gcServer.log.info("list plugins - Lists the loaded plugins.");
			gcServer.log.info("list devices - Lists the loaded devices.");
			gcServer.log.info("list outputs - Lists the available outputs.");
			gcServer.log.info("list inputs  - Lists the available inputs.");
			gcServer.log.info("[ Tools ]");
			gcServer.log.info("ping - ");
			gcServer.log.info("threads - ");
// input / output
//set
//get
//watch
//stop
//exit
//quit
//shutdown
//kill
			return true;
		}
		// version
		if(command.equals("version")) {
			//TODO:
			gcServer.log.info("GrowControl "+gcServer.version);
			return true;
		}
		// say
		if(command.equals("say")) {
			String msg = "";
			for(String line : args) msg += " "+line;
			gcServer.log.info("Server says:"+msg);
			return true;
		}

		// list plugins/devices/inputs/outputs
		if(command.equals("list")) {
			if(args.length >= 1) {
				if(args[0].equalsIgnoreCase("plugins")) {
					gcServerPluginLoader.listPlugins();
					return true;
				} else if(args[0].equalsIgnoreCase("devices")) {
					listDevices();
					return true;
				} else if(args[0].equalsIgnoreCase("outputs")) {
					listOutputs();
					return true;
				} else if(args[0].equalsIgnoreCase("inputs")) {
					listInputs();
					return true;
				}
			}
			gcServer.log.info("Usage: "+command.getUsage());
			return true;
		}
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
			if(gcServerPluginLoader.doOutput(args)) {
				String msg = ""; for(String arg : args) msg += arg+" ";
				gcServer.log.debug("set> "+msg);
				return true;
			}
			String msg = ""; for(String arg : args) msg += arg+" ";
			gcServer.log.warning("Failed to find an output plugin! "+msg);
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


	private static void listDevices() {
	}
	private static void listOutputs() {
	}
	private static void listInputs() {
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
