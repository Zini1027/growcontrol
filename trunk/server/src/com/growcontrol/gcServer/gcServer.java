package com.growcontrol.gcServer;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcServer.commands.gcCommand;
import com.growcontrol.gcServer.devices.gcServerDeviceLoader;
import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.ntp.gcClock;
import com.growcontrol.gcServer.scheduler.gcScheduler;
import com.growcontrol.gcServer.scheduler.gcTicker;
import com.growcontrol.gcServer.serverPlugin.gcServerPluginLoader;

public class gcServer extends Thread {
	public static final String version = "3.0.1";
	public static final String prompt = ">";

	private static gcServer server = null;
	private static boolean stopping = false;

	// logger
	public static final gcLogger log = gcLogger.getLogger(null);

	// server modules
	public static final gcServerPluginLoader pluginLoader = new gcServerPluginLoader();
	public static final gcServerDeviceLoader deviceLoader = new gcServerDeviceLoader();
	public static ServerConfig config = null;

	// schedulers
	public static gcScheduler sched = null;
	private static gcTicker ticker = null;

	// rooms
	List<String> rooms = null;

	// runtime args
	private static boolean noconsole = false;


	public static void main(String[] args) {
		if(server != null) throw new UnsupportedOperationException("Cannot redefine singleton gcServer; already running");
		for(String arg : args) {
			// version
			if(arg.equalsIgnoreCase("version")) {
				System.out.println("GrowControl "+version+" Server");
				System.exit(0);
			// no console
			} else if(arg.equalsIgnoreCase("noconsole"))
				//noconsole = true;
				noconsole = false; // disabled for now
		}
		// start gc server
		server = new gcServer();
	}


	// server instance
	public gcServer() {
		if(noconsole) {
			System.out.println("Console input is disabled due to noconsole command argument.");
//TODO: currently no way to stop the server with no console input
System.exit(0);
		} else {
			AnsiConsole.systemInstall();
			ASCIIHeader();
		}
		log.info("GrowControl "+version+" Server is starting..");
		addLibraryPath("lib");

		// load configs
		config = new ServerConfig();
		if(config==null || config.config==null) {
			log.severe("Failed to load config.yml");
		}
		log.setLogLevel(gcLogger.levelFromString(config.logLevel));

		// start jline console
		if(!noconsole) this.start();

		// query time server
		gcClock.setUsingNTP(true);
		gcClock.updateNTP_Blocking();

		// rooms
		rooms = config.getRooms();
		if(rooms == null) rooms = new ArrayList<String>();
		log.info("Loaded "+Integer.toString(rooms.size())+" rooms");

		// load scheduler paused
		sched = gcScheduler.getScheduler("gcServer");
		ticker = new gcTicker();
		sched.newTask("gcTicker", ticker, gcScheduler.newTriggerSeconds(1, true));

		// load plugins
		pluginLoader.LoadPlugins();

//		// load devices
//		deviceLoader.LoadDevices(Arrays.asList(new String[] {"Lamp"}));

		// start schedulers
		log.info("Starting schedulers..");
		gcScheduler.Start();

//TODO: remove this
//log.severe("Listing Com Ports:");
//for(Map.Entry<String, String> entry : Serial.listPorts().entrySet())
//log.severe(entry.getKey()+" - "+entry.getValue());
	}


	public static void Shutdown() {
		gcScheduler.pauseAll(true);
		log.info("Stopping GC Server..");
		stopping = true;
		// schedulers
		gcScheduler.Shutdown();
		// plugins
		pluginLoader.UnloadPlugins();
		// loggers
		AnsiConsole.systemUninstall();
	}
	public static void Reload() {
	}
	public static boolean isStopping() {
		return stopping;
	}


	// console loop
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
	}


	public static void processCommand(String line) {
		if(line == null) return;
		line = line.trim();
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
		// try plugins first
		if(gcServerPluginLoader.doCommand(commandStr, args)) return;
		// try default internal commands
		gcCommand command = DefaultCommands.commands.getCommandOrAlias(commandStr);
		if(command != null)
			if(DefaultCommands.onCommand(command, args))
				return;
		// command not found
		for(String arg : args) commandStr += " "+arg;
		log.warning("Command not processed! "+commandStr);
	}


	public static boolean isConsoleEnabled() {
		return !noconsole;
	}


	// add lib to paths
	private static void addLibraryPath(String libDir) {
		// get lib path
		File file = new File(libDir);
		if(file==null || !file.exists() || !file.isDirectory()) return;
		String libPath = file.getAbsolutePath();
		if(libPath == null || libPath.isEmpty()) return;
		// get current paths
		String currentPaths = System.getProperty("java.library.path");
		if(currentPaths == null) return;
		log.debug("Adding lib path");
		// set library paths
		if(currentPaths.isEmpty()) {
			System.setProperty("java.library.path", libPath);
		} else {
			if(currentPaths.contains(libPath)) return;
			System.setProperty("java.library.path", currentPaths+(currentPaths.contains(";")?";":":")+libPath);
		}
		// force library paths to refresh
		try {
			Field fieldSysPath;
			fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (SecurityException e) {
			log.exception(e);
		} catch (NoSuchFieldException e) {
			log.exception(e);
		} catch (IllegalArgumentException e) {
			log.exception(e);
		} catch (IllegalAccessException e) {
			log.exception(e);
		}
	}


	// sleep thread
	public static void Sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			log.exception(e);
		}
	}


	// min/max value
	public static int MinMax(int value, int min, int max) {
		if(value < min) value = min;
		if(value > max) value = max;
		return value;
	}
	public static long MinMax(long value, long min, long max) {
		if(value < min) value = min;
		if(value > max) value = max;
		return value;
	}
	public static double MinMax(double value, double min, double max) {
		if(value < min) value = min;
		if(value > max) value = max;
		return value;
	}
	// min/max by object
	public static boolean MinMax(Integer value, int min, int max) {
		boolean changed = false;
		if(value < min) {value = min; changed = true;}
		if(value > max) {value = max; changed = true;}
		return changed;
	}
	public static boolean MinMax(Long value, long min, long max) {
		boolean changed = false;
		if(value < min) {value = min; changed = true;}
		if(value > max) {value = max; changed = true;}
		return changed;
	}
	public static boolean MinMax(Double value, double min, double max) {
		boolean changed = false;
		if(value < min) {value = min; changed = true;}
		if(value > max) {value = max; changed = true;}
		return changed;
	}


	// ascii header
	private static void ASCIIHeader() {
		AnsiConsole.out.println();
		// line 1
		AnsiConsole.out.println(Ansi.ansi().bg(Ansi.Color.BLACK)
			.bold().a("      ")
			.fg(Ansi.Color.GREEN).a("P")
			.fg(Ansi.Color.WHITE).a("oi")
			.fg(Ansi.Color.GREEN).a("X")
			.fg(Ansi.Color.WHITE).a("son")
			.a("                                                    ")
			.reset() );
		// line 2
		AnsiConsole.out.println(Ansi.ansi().bg(Ansi.Color.BLACK)
			.bold().a("    ")
			.fg(Ansi.Color.WHITE).a("©")
			.fg(Ansi.Color.GREEN).a("GROW")
			.fg(Ansi.Color.WHITE).a("CONTROL")
			.fg(Ansi.Color.YELLOW).a("    _")
			.a("                                            ")
			.reset() );
		// line 3
		AnsiConsole.out.println(Ansi.ansi().bg(Ansi.Color.BLACK)
			.a("                  ")
			.fg(Ansi.Color.YELLOW).bold().a("_(_)_                          ").boldOff()
			.fg(Ansi.Color.MAGENTA).a("wWWWw   ")
			.fg(Ansi.Color.YELLOW).bold().a("_")
			.a("       ")
			.reset() );
		// line 4
		AnsiConsole.out.println(Ansi.ansi().bg(Ansi.Color.BLACK)
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
		AnsiConsole.out.println(Ansi.ansi().bg(Ansi.Color.BLACK)
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
		AnsiConsole.out.println(Ansi.ansi().bg(Ansi.Color.BLACK)
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
		AnsiConsole.out.println(Ansi.ansi().bg(Ansi.Color.BLACK)
			.fg(Ansi.Color.GREEN).a("       /      ")
			.fg(Ansi.Color.MAGENTA).a("Y       ")
			.fg(Ansi.Color.GREEN).a("\\|    \\|/    /")
			.fg(Ansi.Color.YELLOW).bold().a("(_)    ").boldOff()
			.fg(Ansi.Color.GREEN).a("\\|      |/      |     ")
			.reset() );
		// line 8
		AnsiConsole.out.println(Ansi.ansi().bg(Ansi.Color.BLACK)
			.fg(Ansi.Color.GREEN).a("    \\ |     \\ |/       | / \\ | /  \\|/       |/    \\|      \\|/    ")
			.reset() );
		// line 9
		AnsiConsole.out.println(Ansi.ansi().bg(Ansi.Color.BLACK)
			.fg(Ansi.Color.GREEN).a("    \\\\|//   \\\\|///  \\\\\\|//\\\\\\|/// \\|///  \\\\\\|//  \\\\|//  \\\\\\|//   ")
			.reset() );
		// line 10
		AnsiConsole.out.println(Ansi.ansi().bg(Ansi.Color.BLACK)
			.fg(Ansi.Color.GREEN).a("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
			.reset() );
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
