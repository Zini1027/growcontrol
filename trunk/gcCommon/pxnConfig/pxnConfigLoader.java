package com.growcontrol.gcCommon.pxnConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;


public final class pxnConfigLoader {
	private pxnConfigLoader() {}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}


	// load config
	public static pxnConfig Load(String path, String file) {
		if(file == null || file.isEmpty()) return null;
		String f = pxnUtils.BuildFilePath(path, file);
		return Load(f);
	}
	public static pxnConfig Load(String filePath) {
		if(filePath == null || filePath.isEmpty()) return null;
		pxnLog.get().fine("Loading config file: "+filePath);
		InputStream fileInput = pxnUtils.OpenFile(filePath);
		if(fileInput == null) return null;
		return Load(fileInput);
	}
	// load yml contents
	public static pxnConfig Load(InputStream fileInput) {
		if(fileInput == null) throw new NullPointerException("fileInput cannot be null!");
		pxnConfig config = null;
		try {
			Yaml yml = new Yaml();
			@SuppressWarnings("unchecked")
			HashMap<String, Object> d = (HashMap<String, Object>) yml.load(fileInput);
			if(d != null && !d.isEmpty())
				config = new pxnConfig(d);
		} catch(Exception e) {
			pxnLog.get().exception(e);
		} finally {
			// close resources
			if(fileInput != null) {
				try {
					fileInput.close();
					fileInput = null;
				} catch (IOException ignore) {}
			}
		}
		return config;
	}
	// load yml from jar
	public static pxnConfig LoadFromJar(File jarFile, String fileName) {
		if(jarFile == null) return null;
		if(fileName == null || fileName.isEmpty()) return null;
		pxnUtils.InputJar inputJar = pxnUtils.OpenJarResource(jarFile, fileName);
		pxnConfig config = null;
		if(inputJar != null && inputJar.fileInput != null) {
			config = Load(inputJar.fileInput);
			inputJar.Close();
			inputJar = null;
		}
		return config;
//		pxnConfig config = null;
//		InputStream fileInput = null;
//		JarFile jar = null;
//		try {
//			jar = new JarFile(jarFile);
//			JarEntry entry = jar.getJarEntry(fileName);
//			if(entry != null) {
//				fileInput = jar.getInputStream(entry);
//				if(fileInput != null)
//					config = Load(fileInput);
//			}
//		} catch (Exception e) {
//			pxnLog.get().exception(e);
//		} finally {
//			// close resources
//			if(fileInput != null) {
//				try {
//					fileInput.close();
//					fileInput = null;
//				} catch (Exception ignore) {}
//			}
//			if(jar != null) {
//				try {
//					jar.close();
//					jar = null;
//				} catch (Exception ignore) {}
//			}
//		}
//		return config;
	}


//	public boolean CopyResource(String fileName) {
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


}
