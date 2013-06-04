package com.growcontrol.gcCommon;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.channels.FileLock;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public class pxnUtils {


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
		pxnLogger.getLogger().debug("Adding lib path: "+libDir);
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
			pxnLogger.getLogger().exception(e);
		} catch (NoSuchFieldException e) {
			pxnLogger.getLogger().exception(e);
		} catch (IllegalArgumentException e) {
			pxnLogger.getLogger().exception(e);
		} catch (IllegalAccessException e) {
			pxnLogger.getLogger().exception(e);
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
							pxnLogger.getLogger().severe("Unable to remove lock file: "+lockFile);
							pxnLogger.getLogger().exception(e);
						}
					}
				});
				return true;
			}
		} catch (Exception e) {
			pxnLogger.getLogger().severe("Unable to create and/or lock file: "+lockFile);
			pxnLogger.getLogger().exception(e);
		}
		return false;
	}
	// get pid for process (if possible)
	public static int getPid() {
		int pid = -1;
		try {
			pid = Integer.parseInt( ( new File("/proc/self")).getCanonicalFile().getName() );
		} catch (NumberFormatException e) {
			pxnLogger.getLogger().exception(e);
		} catch (IOException e) {
			pxnLogger.getLogger().exception(e);
		}
		return pid;
	}


	// sleep thread
//TODO: add TimeUnit variant
	public static void Sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			pxnLogger.getLogger().exception(e);
		}
	}
	// current time ms
	public static long getCurrentMillis() {
		return System.currentTimeMillis();
	}


	// compare version numbers
	public static String compareVersions(String oldVersion, String newVersion) {
		if(oldVersion == null || newVersion == null) return null;
		oldVersion = normalisedVersion(oldVersion);
		newVersion = normalisedVersion(newVersion);
		int cmp = oldVersion.compareTo(newVersion);
		if(cmp < 0) return "<";
		if(cmp > 0) return ">";
		return "=";
		//return cmp<0 ? "<" : cmp>0 ? ">" : "=";
	}
	public static String normalisedVersion(String version) {
		String delim = ".";
		int maxWidth = 5;
		String[] split = Pattern.compile(delim, Pattern.LITERAL).split(version);
		String output = "";
		for(String s : split)
			output += String.format("%"+maxWidth+'s', s);
		return output;
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


	// cast a collection to list
	public static <T> List<T> castList(Class<? extends T> clss, Collection<?> c) {
		if(clss == null) throw new NullPointerException("clss can't be null!");
		if(c    == null) throw new NullPointerException("c can't be null!");
	    List<T> result = new ArrayList<T>(c.size());
	    for(Object o : c)
	    	result.add(clss.cast(o));
	    return result;
	}
	@SuppressWarnings("unchecked")
	public static <T> List<T> castList(Class<? extends T> clss, Object object) {
		if(clss   == null) throw new NullPointerException("clss can't be null!");
		if(object == null) throw new NullPointerException("object can't be null!");
		try {
			return castList(clss, (Collection<T>) object);
		} catch(Exception ignore) {
			return null;
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
