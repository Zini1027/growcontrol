package com.growcontrol.gcServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.scheduler.gcSchedulerManager;
import com.growcontrol.gcServer.scheduler.gcTicker;
import com.growcontrol.gcServer.serverPlugin.gcServerPluginManager;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEventCommand;
import com.growcontrol.gcServer.socketServer.socketServer;
import com.poixson.pxnUtils;
import com.poixson.ntp.pxnClock;
import com.poixson.pxnLogger.pxnLevel;
import com.poixson.pxnLogger.pxnLogger;
import com.poixson.pxnLogger.pxnLoggerConsole;


public class gcServer extends Thread {
	public static final String version = "3.0.2";
	public static final String prompt = ">";

	private static gcServer server = null;
	private static boolean stopping = false;

	// logger
	public static final gcLogger log = gcLogger.getLogger(null);

	// server modules
	public static final gcServerPluginManager pluginManager =
		new gcServerPluginManager(
			gcLogger.getLogger("ServerPlugin") );
//	public static final gcServerDeviceLoader deviceLoader = new gcServerDeviceLoader();

	// config files
	public static ServerConfig config = null;
	public static String configsPath = null;

	// server scheduler
	private static gcSchedulerManager scheduler = null;
	private static gcTicker ticker = null;
	// clock
	private static pxnClock clock = null;

	// socket pool
	public static socketServer socket = null;

	// zones
	List<String> zones = null;

	// runtime args
	private static boolean noconsole = false;


	public static void main(String[] args) {
		if(server != null) throw new UnsupportedOperationException("Cannot redefine singleton gcServer; already running");
		pxnLogger.addLogHandler(
			"console",
			new pxnLoggerConsole(pxnLogger.getReader(),
				new pxnLevel(pxnLevel.LEVEL.DEBUG)) );
//		pxnLogger.addLogHandler(
//			"file",
//			new pxnLoggerFile(
//				new pxnLevel(pxnLevel.LEVEL.DEBUG)) );
		// process args
		for(String arg : args) {
			// version
			if(arg.equalsIgnoreCase("version")) {
				System.out.println("GrowControl "+version+" Server");
				System.exit(0);
			// no console
			} else if(arg.equalsIgnoreCase("noconsole")) {
				noconsole = true;
			// debug mode
			} else if(arg.equalsIgnoreCase("debug")) {
				gcLogger.setLevel("console", pxnLevel.LEVEL.DEBUG);
				gcLogger.setLevel("file",    pxnLevel.LEVEL.DEBUG);
			// configs path
			} else if(arg.startsWith("configspath=")) {
				configsPath = arg.substring(12);
				log.debug("Set configs path to: "+configsPath);
			// plugins path
			} else if(arg.startsWith("pluginspath=")) {
				pluginManager.setPath(arg.substring(12));
				log.debug("Set plugins path to: "+pluginManager.getPath());
			}
		}
		// start gc server
		server = new gcServer();
	}


	// server instance
	public gcServer() {
		// single instance lock
		pxnUtils.lockInstance("gc.lock");
		if(noconsole) {
			System.out.println("Console input is disabled due to noconsole command argument.");
//TODO: currently no way to stop the server with no console input
System.exit(0);
		} else {
			AnsiConsole.systemInstall();
			ASCIIHeader();
		}
		log.printRaw("");
		log.printRaw("[[ Starting GC Server ]]");
		log.info("GrowControl "+version+" Server is starting..");
		pxnUtils.addLibraryPath("lib");

		// load configs
		config = new ServerConfig(configsPath);
		if(config==null || config.config==null) {
			log.severe("Failed to load config.yml");
			System.exit(1);
		}

		// set log level
		String logLevel = config.getLogLevel();
		if(logLevel != null && !logLevel.isEmpty()) {
//			pxnLevel level = gcLogger.getLevel("console");
			gcLogger.setLevel("console", logLevel);
//			gcLogger.setLevel("file",    logLevel);
		}

		// start jline console
		pluginManager.registerCommandListener(new ServerCommands());
		if(!noconsole) this.start();

		// query time server
		if(clock == null)
			clock = pxnUtils.getClock();
//TODO: figure out why this is locking up
//		gcClock.setUsingNTP(true);

		// rooms
		zones = config.getZones();
		if(zones == null) zones = new ArrayList<String>();
		log.info("Loaded "+Integer.toString(zones.size())+" zones");

		// load scheduler (paused)
		scheduler = gcSchedulerManager.getScheduler("gcServer");
		// load ticker
		ticker = new gcTicker();

		// load plugins
		try {
			pluginManager.LoadPlugins();
		} catch (Exception e) {
			log.exception(e);
			Shutdown();
			return;
		}

//		// load devices
//		deviceLoader.LoadDevices(Arrays.asList(new String[] {"Lamp"}));

		// start socket listener
		socket = new socketServer(config.getListenPort());

		// start schedulers
		log.info("Starting schedulers..");
		gcSchedulerManager.StartAll();

//TODO: remove this
//log.severe("Listing Com Ports:");
//for(Map.Entry<String, String> entry : Serial.listPorts().entrySet())
//log.severe(entry.getKey()+" - "+entry.getValue());
		log.printRaw("[[ GC Server Running! ]]");
	}


	public static void Shutdown() {
//TODO: display total time running
//TODO: make this threaded!
		stopping = true;
		log.printRaw("[[ Stopping GC Server ]]");
		log.warning("Stopping GC Server..");
		// pause scheduler
		gcSchedulerManager.StopAll();
		// close sockets
		socket.stop();
		// plugins
		pluginManager.UnloadPlugins();
		// end schedulers
		gcSchedulerManager.ShutdownAll();
		// loggers
		AnsiConsole.systemUninstall();
// display threads still running
log.severe("Threads still running:");
Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
for(Thread t : threadSet) {
	log.printRaw(t.getName());
}

	}
	public static void Reload() {
	}
	public static boolean isStopping() {
		return stopping;
	}


	// console input loop
	public void run() {
		if(noconsole) return;
		//TODO: password login
		// If we input the special word then we will mask
		// the next line.
		//if ((trigger != null) && (line.compareTo(trigger) == 0))
//			line = reader.readLine("password> ", mask);
		//if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) break;
		while(!stopping) {
			try {
				// wait for commands
				String line = gcLogger.readLine();
				if(line == null) break;
				if(line.isEmpty()) continue;
				processCommand(line);
			} catch(Exception e) {
				log.exception(e);
			}
		}
		System.out.println();
		System.out.println();
	}
	// process command
	public static void processCommand(String line) {
		if(line == null) throw new NullPointerException("line cannot be null");
		line = line.trim();
		if(line.isEmpty()) return;
		String commandStr;
		String[] args;
		// get args list
		if(line.contains(" ")) {
			int index = line.indexOf(" ");
			commandStr = line.substring(0, index);
			List<String> argsList = new ArrayList<String>();
			for(String arg : line.substring(index+1).split(" "))
				if(!arg.isEmpty())
					argsList.add(arg);
			args = (String[]) argsList.toArray(new String[argsList.size()]);
			argsList = null;
		} else {
			commandStr = new String(line);
			args = new String[0];
		}
		// trigger event
		if(pluginManager.triggerEvent(new gcServerEventCommand(commandStr, args)))
			return;
		// command not found
		for(String arg : args) commandStr += " "+arg;
		log.warning("Command not processed: "+commandStr);
	}


	// schedulers
	public static gcSchedulerManager getScheduler() {
		return scheduler;
	}
	public static gcTicker getTicker() {
		return ticker;
	}
	public static pxnClock getClock() {
		return clock;
	}


	// is console input enabled
	public static boolean isConsoleEnabled() {
		return !noconsole;
	}


	// ascii header
	private static void ASCIIHeader() {
		AnsiConsole.out.println();
		// line 1
		AnsiConsole.out.println(Ansi.ansi()
			.a(" ").bg(Ansi.Color.BLACK)
			.bold().a("      ")
			.fg(Ansi.Color.GREEN).a("P")
			.fg(Ansi.Color.WHITE).a("oi")
			.fg(Ansi.Color.GREEN).a("X")
			.fg(Ansi.Color.WHITE).a("son")
			.a("                                                    ")
			.reset() );
		// line 2
		AnsiConsole.out.println(Ansi.ansi()
			.a(" ").bg(Ansi.Color.BLACK)
			.bold().a("    ")
			.fg(Ansi.Color.GREEN).a("GROW")
			.fg(Ansi.Color.WHITE).a("CONTROL")
			.fg(Ansi.Color.YELLOW).a("     _")
			.a("                                            ")
			.reset() );
		// line 3
		AnsiConsole.out.println(Ansi.ansi()
			.a(" ").bg(Ansi.Color.BLACK)
			.a("                  ")
			.fg(Ansi.Color.YELLOW).bold().a("_(_)_                          ").boldOff()
			.fg(Ansi.Color.MAGENTA).a("wWWWw   ")
			.fg(Ansi.Color.YELLOW).bold().a("_")
			.a("       ")
			.reset() );
		// line 4
		AnsiConsole.out.println(Ansi.ansi()
			.a(" ").bg(Ansi.Color.BLACK)
			.a("      ")
			.fg(Ansi.Color.RED).a("@@@@").a("       ")
			.fg(Ansi.Color.YELLOW).bold().a("(_)@(_)   ").boldOff()
			.fg(Ansi.Color.MAGENTA).a("vVVVv     ")
			.fg(Ansi.Color.YELLOW).bold().a("_     ").boldOff()
			.fg(Ansi.Color.BLUE).a("@@@@  ")
			.fg(Ansi.Color.MAGENTA).a("(___) ")
			.fg(Ansi.Color.YELLOW).bold().a("_(_)_")
			.a("     ")
			.reset() );
		// line 5
		AnsiConsole.out.println(Ansi.ansi()
			.a(" ").bg(Ansi.Color.BLACK)
			.a("     ")
			.fg(Ansi.Color.RED).a("@@()@@ ")
			.fg(Ansi.Color.MAGENTA).bold().a("wWWWw  ")
			.fg(Ansi.Color.YELLOW).a("(_)").boldOff()
			.fg(Ansi.Color.GREEN).a("\\    ")
			.fg(Ansi.Color.MAGENTA).a("(___)   ")
			.fg(Ansi.Color.YELLOW).bold().a("_(_)_  ").boldOff()
			.fg(Ansi.Color.BLUE).a("@@()@@   ")
			.fg(Ansi.Color.MAGENTA).a("Y  ")
			.fg(Ansi.Color.YELLOW).bold().a("(_)@(_)")
			.a("    ")
			.reset() );
		// line 6
		AnsiConsole.out.println(Ansi.ansi()
			.a(" ").bg(Ansi.Color.BLACK)
			.a("      ")
			.fg(Ansi.Color.RED).a("@@@@  ")
			.fg(Ansi.Color.MAGENTA).bold().a("(___)     ").boldOff()
			.fg(Ansi.Color.GREEN).a("`|/    ")
			.fg(Ansi.Color.MAGENTA).a("Y    ")
			.fg(Ansi.Color.YELLOW).bold().a("(_)@(_)  ").boldOff()
			.fg(Ansi.Color.BLUE).a("@@@@   ")
			.fg(Ansi.Color.GREEN).a("\\|/   ")
			.fg(Ansi.Color.YELLOW).bold().a("(_)").boldOff()
			.fg(Ansi.Color.GREEN).a("\\")
			.a("     ")
			.reset() );
		// line 7
		AnsiConsole.out.println(Ansi.ansi()
			.a(" ").bg(Ansi.Color.BLACK)
			.fg(Ansi.Color.GREEN).a("       /      ")
			.fg(Ansi.Color.MAGENTA).a("Y       ")
			.fg(Ansi.Color.GREEN).a("\\|    \\|/    /")
			.fg(Ansi.Color.YELLOW).bold().a("(_)    ").boldOff()
			.fg(Ansi.Color.GREEN).a("\\|      |/      |     ")
			.reset() );
		// line 8
		AnsiConsole.out.println(Ansi.ansi()
			.a(" ").bg(Ansi.Color.BLACK)
			.fg(Ansi.Color.GREEN).a("    \\ |     \\ |/       | / \\ | /  \\|/       |/    \\|      \\|/    ")
			.reset() );
		// line 9
		AnsiConsole.out.println(Ansi.ansi()
			.a(" ").bg(Ansi.Color.BLACK)
			.fg(Ansi.Color.GREEN).a("    \\\\|//   \\\\|///  \\\\\\|//\\\\\\|/// \\|///  \\\\\\|//  \\\\|//  \\\\\\|//   ")
			.reset() );
		// line 10
		AnsiConsole.out.println(Ansi.ansi()
			.a(" ").bg(Ansi.Color.BLACK)
			.fg(Ansi.Color.GREEN).a("^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^/^")
			.reset() );
		// line 11
		AnsiConsole.out.println(Ansi.ansi()
			.a(" ").bg(Ansi.Color.BLACK)
			.fg(Ansi.Color.GREEN).a("/////////////////////////////////////////////////////////////////")
			.reset() );
		AnsiConsole.out.println();

		AnsiConsole.out.println(" Copyright (C) 2007-2013 PoiXson, Mattsoft");
		AnsiConsole.out.println(" This program comes with absolutely no warranty. This is free software,");
		AnsiConsole.out.println(" and you are welcome to redistribute it under certain conditions.");
		AnsiConsole.out.println(" For details type 'show w' for warranty, or 'show c' for conditions.");
		AnsiConsole.out.println();

// 1 |      PoiXson
// 2 |    ©GROWCONTROL    _
// 3 |                  _(_)_                          wWWWw   _
// 4 |      @@@@       (_)@(_)   vVVVv     _     @@@@  (___) _(_)_
// 5 |     @@()@@ wWWWw  (_)\    (___)   _(_)_  @@()@@   Y  (_)@(_)
// 6 |      @@@@  (___)     `|/    Y    (_)@(_)  @@@@   \|/   (_)\
// 7 |       /      Y       \|    \|/    /(_)    \|      |/      |
// 8 |    \ |     \ |/       | / \ | /  \|/       |/    \|      \|/
// 9 |    \\|//   \\|///  \\\|//\\\|/// \|///  \\\|//  \\|//  \\\|//
//10 |^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

//System.out.println("      "+Ansi.Color.MAGENTA+"PoiXson");
//System.out.println("    ©GROWCONTROL    _");
//System.out.println("                  _(_)_                          wWWWw   _");
//System.out.println("      @@@@       (_)@(_)   vVVVv     _     @@@@  (___) _(_)_");
//System.out.println("     @@()@@ wWWWw  (_)\\    (___)   _(_)_  @@()@@   Y  (_)@(_)");
//System.out.println("      @@@@  (___)     `|/    Y    (_)@(_)  @@@@   \\|/   (_)\\");
//System.out.println("       /      Y       \\|    \\|/    /(_)    \\|      |/      |");
//System.out.println("    \\ |     \\ |/       | / \\ | /  \\|/       |/    \\|      \\|/");
//System.out.println("    \\\\|//   \\\\|///  \\\\\\|//\\\\\\|/// \\|///  \\\\\\|//  \\\\|//  \\\\\\|//");
//System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

//System.out.println("                     .==IIIIIIIIIIII=:.");
//System.out.println("               .7IIII777777II7I7I77777III.");
//System.out.println(" .+?IIII7IIIIII7777I++III+I+++?+I77IIII777II");
//System.out.println(" .II=+?7777777II?+==+====?+=++?7++++?I?I7777II");
//System.out.println("  ~II=III+?=+=======+==III=?+==+I??+?+7???II77I.");
//System.out.println("   +I7?==+===?=III+?I=?===+=?=?I??=?+++??III?I77=");
//System.out.println("     I77II+=+=7=?======?=?~+=I====?7+=???+++II?7I7        .II7I7=.");
//System.out.println("      II7I7+===I=?I+++++~=~+7~~=??I=I=+=++?7++??777...7I7I7??7++7I77IIIIIIIII");
//System.out.println("        II7+I77I+=~?7=I~?7I=?7I~~+=++7=I===+II++77I7777I??77I++=?7I===+?7?+=7I");
//System.out.println("          II7++~+I7I77777777777777I?=~77+=I+?=++I7777??I?+++=?I?===77I+=+7+7:");
//System.out.println("            ~IIIII+==+~~~~+:?~=:~~+II77777??=I+?+777I??+=7?=?=II==I==I+?77=");
//System.out.println("                IIIIII7777??+=::~?:~~~=I=I777=?I??7?+I7?7+~7777=+77777?+I");
//System.out.println("                    :IIIIIIII7777777I:?~~~+~I77I=?I=I+77?=?::=~??+?777I");
//System.out.println("                             IIIIIII7777+~~?~=+777?I77?~~77777II,");
//System.out.println("                                  ~7III777?:~~?~777I~:?77II");
//System.out.println("                                      ~III77~~~++7~I=77");
//System.out.println("                                         :II7I:::I7:7I.");
//System.out.println("                                           ~I77:~~:I7II");
//System.out.println("                                            +I77:::I7II");
//System.out.println("                                             II7,,::77II");
//System.out.println("                            PoiXson           I7?~~,=7I");
//System.out.println("                          ©GROWCONTROL         I7?,:III");
//System.out.println("                                                ~III+.");
	}


}
