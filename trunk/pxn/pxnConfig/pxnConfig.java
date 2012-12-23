package com.poixson.pxnConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.yaml.snakeyaml.Yaml;

import com.poixson.pxnUtils;
import com.poixson.pxnLogger.pxnLogger;


public class pxnConfig {

	protected HashMap<String, Object> data = new HashMap<String, Object>();


	// new instance (file)
	public static pxnConfig loadFile(String filePath, String fileName) {
		if(fileName == null)   throw new NullPointerException("fileName cannot be null!");
		if(fileName.isEmpty()) throw new NullPointerException("fileName cannot be empty!");
		InputStream fileStream = openFile(filePath, fileName);
		if(fileStream == null) return null;
		return new pxnConfig(fileStream);
	}
	public static pxnConfig loadFile(String fileName) {
		return loadFile(null, fileName);
	}
	// load yml path / filename
	public static pxnConfig loadResource(String filePath, String fileName) {
		InputStream fileStream = openResource(filePath, fileName);
		if(fileStream == null) return null;
		return new pxnConfig(fileStream);
	}
	// load yml filename
	public static pxnConfig loadResource(String fileName) {
		return loadResource(null, fileName);
	}
	// load yml from jar
	public static pxnConfig loadJarResource(File jarFile, String fileName) throws IOException {
		if(jarFile  == null)   throw new NullPointerException("jarFile cannot be null!");
		if(fileName == null)   throw new NullPointerException("fileName cannot be null!");
		if(fileName.isEmpty()) throw new NullPointerException("fileName cannot be empty!");
		JarFile jar = new JarFile(jarFile);
		JarEntry entry = jar.getJarEntry(fileName);
		if(entry == null) return null;
		InputStream fileStream = jar.getInputStream(entry);
		if(fileStream == null) return null;
		return new pxnConfig(fileStream);
	}


	// config instance
	public pxnConfig(InputStream fileInput) {
		if(fileInput == null) throw new NullPointerException("fileInput cannot be null!");
		try {
			Yaml yml = new Yaml();
			synchronized(data) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> data = (HashMap<String, Object>) yml.load(fileInput);
				this.data = data;
				try {
					fileInput.close();
				} catch (IOException ignore) {}
			}
		} catch(Exception e) {
			this.data = null;
			pxnLogger.log().exception(e);
		}
	}


	// load config file
	protected static InputStream openFile(String filePath, String fileName) {
		if(fileName == null)   throw new NullPointerException("fileName cannot be null!");
		if(fileName.isEmpty()) throw new NullPointerException("fileName cannot be empty!");
		String path = sanFilePath(filePath, fileName);
		pxnLogger.log().debug("Loading config file: "+path);
		return openFile(path);
	}
	protected static InputStream openFile(String fileStr) {
		if(fileStr == null)   throw new NullPointerException("fileStr cannot be null!");
		if(fileStr.isEmpty()) throw new NullPointerException("fileStr cannot be empty!");
		try {
			File file = new File(fileStr);
			if(!file.exists()) throw new FileNotFoundException("File not found!");
			return new FileInputStream(file);
		} catch (FileNotFoundException ignore) {
			pxnLogger.log().debug("Failed to load config file: "+fileStr);
			return openResource(fileStr);
		}
	}


	// load resource config
	protected static InputStream openResource(String filePath, String fileName) {
		if(fileName == null)   throw new NullPointerException("fileName cannot be null!");
		if(fileName.isEmpty()) throw new NullPointerException("fileName cannot be empty!");
		return openResource(sanFilePath(filePath, fileName));
	}
	protected static InputStream openResource(String fileStr) {
		if(fileStr == null)   throw new NullPointerException("fileStr cannot be null!");
		if(fileStr.isEmpty()) throw new NullPointerException("fileStr cannot be empty!");
		try {
			return pxnConfig.class.getResourceAsStream(fileStr);
		} catch(Exception ignore) {
			pxnLogger.log().debug("Not found as a resource either!");
			return null;
		}
	}


	protected static String sanFilePath(String filePath, String fileName) {
		if(fileName == null)   throw new NullPointerException("fileName cannot be null!");
		if(fileName.isEmpty()) throw new NullPointerException("fileName cannot be empty!");
		if(!fileName.endsWith(".yml")) fileName += ".yml";
		if(filePath == null || filePath.isEmpty())
			return fileName;
		if(filePath.endsWith("/") || filePath.endsWith("\\") || fileName.startsWith("/") || fileName.startsWith("\\"))
			return filePath+fileName;
		else
			return filePath+File.separator+fileName;
	}


//	public boolean copyResource(String fileName) {
//		try {
//			InputStream input = getClass().getResourceAsStream("/configs/"+fileName);
//			if(input == null) {
//				GrowControl.log.severe("Failed to load resource config: "+fileName);
//				return false;
//			}
//			OutputStream output = new FileOutputStream("configs/"+fileName);
//			byte[] buffer = new byte[4096];
//			int length;
//			while ((length = input.read(buffer)) > 0) {
//				output.write(buffer, 0, length);
//			}
//			output.close();
//			input.close();
//			return true;
//		} catch (Exception e) {
//			GrowControl.log.exception(e);
//		}
//		return false;
//	}


	// get value
	public Object get(String path) {
		try {
			return data.get(path);
		} catch(Exception ignore) {
			return null;
		}
	}
	public String getString(String path) {
		try {
			return (String) data.get(path);
		} catch(Exception ignore) {
			return null;
		}
	}
	public boolean getBoolean(String path) {
		try {
			return (Boolean) data.get(path);
		} catch(Exception ignore) {
			return false;
		}
	}
	public int getInt(String path) {
		try {
			return (Integer) data.get(path);
		} catch(Exception ignore) {
			return 0;
		}
	}
	public long getLong(String path) {
		try {
			return ((Integer) data.get(path)).longValue();
		} catch(Exception ignore) {
			return 0;
		}
	}
	public double getDouble(String path) {
		try {
			return (Double) data.get(path);
		} catch(Exception ignore) {
			return 0.0;
		}
	}
	// get list
	public <T> List<T> getList(Class<? extends T> clss, String path) {
		try {
			return pxnUtils.castList(clss, data.get(path));
		} catch (Exception ignore) {
ignore.printStackTrace();
			return null;
		}
	}
	public List<String> getStringList(String path) {
		return getList(String.class, path);
//		try {
//			return pxnUtils.castList(String.class, data.get(path));
////			return (List<String>) data.get(path);
//		} catch(Exception ignore) {
//			return new ArrayList<String>();
//		}
	}


	// set value
	public void set(String path, String value) {
//TODO:
pxnLogger.log().severe("CONFIG FUNCTION NOT FINISHED!");
	}
	public void set(String path, boolean value) {
//TODO:
pxnLogger.log().severe("CONFIG FUNCTION NOT FINISHED!");
	}
	public void set(String path, int value) {
//TODO:
pxnLogger.log().severe("CONFIG FUNCTION NOT FINISHED!");
	}
	public void set(String path, long value) {
//TODO:
pxnLogger.log().severe("CONFIG FUNCTION NOT FINISHED!");
	}
	public void set(String path, double value) {
//TODO:
pxnLogger.log().severe("CONFIG FUNCTION NOT FINISHED!");
	}


}
