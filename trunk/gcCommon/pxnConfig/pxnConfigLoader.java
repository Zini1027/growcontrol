package com.growcontrol.gcCommon.pxnConfig;

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
	public static pxnConfig LoadConfig(String pathStr, String fileStr) {
		String file = pxnUtils.BuildFilePath(pathStr, fileStr);
		pxnLog.get().debug("Loading config file: "+file);
		return LoadConfig(file);
	}
	public static pxnConfig LoadConfig(String fileStr) {
		return LoadConfig(pxnUtils.OpenFile(fileStr));
	}
	public static pxnConfig LoadConfig(InputStream fileInput) {
		if(fileInput == null) throw new NullPointerException("fileInput cannot be null!");
		try {
			Yaml yml = new Yaml();
			@SuppressWarnings("unchecked")
			HashMap<String, Object> d = (HashMap<String, Object>) yml.load(fileInput);
			try {
				fileInput.close();
			} catch (IOException ignore) {}
			if(d != null && !d.isEmpty())
				return new pxnConfig(d);
		} catch(Exception e) {
			pxnLog.get().exception(e);
		}
		return null;
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
