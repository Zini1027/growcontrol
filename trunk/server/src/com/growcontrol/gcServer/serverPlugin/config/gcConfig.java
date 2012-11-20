package com.growcontrol.gcServer.serverPlugin.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.yaml.snakeyaml.Yaml;

import com.growcontrol.gcServer.gcServer;

public class gcConfig {

	protected HashMap<String, Object> data = new HashMap<String, Object>();


	// new instance (file)
	public static gcConfig loadFile(String filePath, String fileName) {
		if(fileName == null) throw new NullPointerException();
		if(fileName.isEmpty()) return null;
		return new gcConfig(openFile(filePath, fileName));
	}
	public static gcConfig loadFile(String fileName) {
		return loadFile(null, fileName);
	}


	// load yml path / filename
	public static gcConfig loadResource(String filePath, String fileName) {
		return new gcConfig(openResource(filePath, fileName));
	}
	// load yml filename
	public static gcConfig loadResource(String fileName) {
		return loadResource(null, fileName);
	}


	// load yml from jar
	public static gcConfig loadJarResource(File jarFile, String fileName) throws IOException {
		if(jarFile == null) throw new NullPointerException();
		if(fileName == null) throw new NullPointerException();
		JarFile jar = new JarFile(jarFile);
		JarEntry entry = jar.getJarEntry(fileName);
		if(entry == null) return null;
		InputStream fileStream = jar.getInputStream(entry);
		return new gcConfig(fileStream);
	}


	// config instance
	public gcConfig(InputStream fileInput) {
		if(fileInput == null) throw new NullPointerException();
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
			gcServer.log.exception(e);
		}
	}


	// load config file
	protected static InputStream openFile(String filePath, String fileName) {
		if(fileName == null) throw new NullPointerException();
		if(fileName.isEmpty()) return null;
		String path = sanFilePath(filePath, fileName);
		gcServer.log.debug("Loading config file: "+path);
		return openFile(path);
	}
	protected static InputStream openFile(String fileStr) {
		if(fileStr == null) throw new NullPointerException();
		if(fileStr.isEmpty()) return null;
		try {
			File file = new File(fileStr);
			if(!file.exists()) throw new FileNotFoundException("File not found!");
			return new FileInputStream(file);
		} catch (FileNotFoundException ignore) {
			gcServer.log.debug("Failed to load config file: "+fileStr);
			return openResource(fileStr);
		}
	}


	// load resource config
	protected static InputStream openResource(String filePath, String fileName) {
		if(fileName == null) throw new NullPointerException();
		if(fileName.isEmpty()) return null;
		return openResource(sanFilePath(filePath, fileName));
	}
	protected static InputStream openResource(String fileStr) {
		if(fileStr == null) throw new NullPointerException();
		if(fileStr.isEmpty()) return null;
		try {
			return gcServer.class.getResourceAsStream(fileStr);
		} catch(Exception ignore2) {
			gcServer.log.debug("Not found as a resource either!");
			return null;
		}
	}


	protected static String sanFilePath(String filePath, String fileName) {
		if(fileName == null) throw new NullPointerException();
		if(fileName.isEmpty()) return null;
		if(!fileName.endsWith(".yml")) fileName += ".yml";
		if(filePath == null || filePath.isEmpty())
			return fileName;
		if(filePath.endsWith("/") || filePath.endsWith("\\") || fileName.startsWith("/") || fileName.startsWith("\\"))
		{
			return filePath + fileName;
		}
		else
		{
			return filePath + File.separator + fileName;
		}
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
	@SuppressWarnings("unchecked")
	public List<String> getStringList(String path) {
		try {
			return (List<String>) data.get(path);
		} catch(Exception ignore) {
			return new ArrayList<String>();
		}
	}


	// set value
	public void set(String path, String value) {
//TODO:
gcServer.log.severe("CONFIG FUNCTION NOT FINISHED!");
	}
	public void set(String path, boolean value) {
//TODO:
gcServer.log.severe("CONFIG FUNCTION NOT FINISHED!");
	}
	public void set(String path, int value) {
//TODO:
gcServer.log.severe("CONFIG FUNCTION NOT FINISHED!");
	}
	public void set(String path, long value) {
//TODO:
gcServer.log.severe("CONFIG FUNCTION NOT FINISHED!");
	}
	public void set(String path, double value) {
//TODO:
gcServer.log.severe("CONFIG FUNCTION NOT FINISHED!");
	}


}
