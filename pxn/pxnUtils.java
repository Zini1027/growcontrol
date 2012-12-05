package com.poixson;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.channels.FileLock;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import com.poixson.pxnClock.pxnClock;
import com.poixson.pxnLogger.pxnLogger;


public class pxnUtils {


	// static clock
	protected static pxnClock clock = null;
	public static pxnClock getClock() {
		if(clock == null) {
			clock = new pxnClock();
			clock.update(false);
		}
		return clock;
	}
	public static void setClock(pxnClock clock) {
		pxnUtils.clock = clock;
	}


	// add lib to paths
	public static void addLibraryPath(String libDir) {
		if(libDir == null) throw new NullPointerException("libDir cannot be null");
		// get lib path
		File file = new File(libDir);
		if(file==null || !file.exists() || !file.isDirectory()) return;
		String libPath = file.getAbsolutePath();
		if(libPath == null || libPath.isEmpty()) return;
		// get current paths
		String currentPaths = System.getProperty("java.library.path");
		if(currentPaths == null) return;
		pxnLogger.log().debug("Adding lib path: "+libDir);
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
			pxnLogger.log().exception(e);
		} catch (NoSuchFieldException e) {
			pxnLogger.log().exception(e);
		} catch (IllegalArgumentException e) {
			pxnLogger.log().exception(e);
		} catch (IllegalAccessException e) {
			pxnLogger.log().exception(e);
		}
	}


	// single instance lock
	public static boolean lockInstance(final String lockFile) {
		try {
			final File file = new File(lockFile);
			final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
			final FileLock fileLock = randomAccessFile.getChannel().tryLock();
			int pid = getPid();
			if(pid > 0)
				randomAccessFile.write(Integer.toString(pid).getBytes());
			if(fileLock != null) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run() {
						try {
							fileLock.release();
							randomAccessFile.close();
							file.delete();
						} catch (Exception e) {
							pxnLogger.log().severe("Unable to remove lock file: "+lockFile);
							pxnLogger.log().exception(e);
						}
					}
				});
				return true;
			}
		} catch (Exception e) {
			pxnLogger.log().severe("Unable to create and/or lock file: "+lockFile);
			pxnLogger.log().exception(e);
		}
		return false;
	}
	// get pid for process (if possible)
	public static int getPid() {
		int pid = -1;
		try {
			pid = Integer.parseInt( ( new File("/proc/self")).getCanonicalFile().getName() );
		} catch (NumberFormatException e) {
			pxnLogger.log().exception(e);
		} catch (IOException e) {
			pxnLogger.log().exception(e);
		}
		return pid;
	}


	// sleep thread
	public static void Sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			pxnLogger.log().exception(e);
		}
	}
	// current time ms
	public static long getCurrentMillis() {
		return System.currentTimeMillis();
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
		if(value == null) throw new NullPointerException("value cannot be null");
		boolean changed = false;
		if(value < min) {value = min; changed = true;}
		if(value > max) {value = max; changed = true;}
		return changed;
	}
	public static boolean MinMax(Long value, long min, long max) {
		if(value == null) throw new NullPointerException("value cannot be null");
		boolean changed = false;
		if(value < min) {value = min; changed = true;}
		if(value > max) {value = max; changed = true;}
		return changed;
	}
	public static boolean MinMax(Double value, double min, double max) {
		if(value == null) throw new NullPointerException("value cannot be null");
		boolean changed = false;
		if(value < min) {value = min; changed = true;}
		if(value > max) {value = max; changed = true;}
		return changed;
	}


	// random number (unique)
	public static int getRandom(int minNumber, int maxNumber) {
		Random randomGen = new Random(getCurrentMillis());
		return randomGen.nextInt(maxNumber) + minNumber;
	}
	public static int getNewRandom(int minNumber, int maxNumber, int oldNumber) {
		if(minNumber == maxNumber) return minNumber;
		if((maxNumber - minNumber) == 1)
			if(oldNumber == minNumber)
				return maxNumber;
			else
				return minNumber;
		int newNumber;
		while(true) {
			newNumber = getRandom(minNumber, maxNumber);
			if (newNumber != oldNumber) return newNumber;
		}
	}


	// md5
	public static String MD5(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(str.getBytes());
		byte[] byteData = md.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xFF & byteData[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}


	// add strings with delimiter
	public static String addStringSet(String baseString, String addThis, String delim) {
		if(addThis.isEmpty())    return baseString;
		if(baseString.isEmpty()) return addThis;
		return baseString + delim + addThis;
	}
	public static String addStringSet(String baseString, List<String> addThis, String delim) {
		if(baseString == null) baseString = "";
		for(String line : addThis) {
			if(!baseString.isEmpty()) baseString += delim;
			baseString += line;
		}
		return baseString;
	}


}