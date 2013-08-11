package com.growcontrol.gcCommon.pxnConfig;

import java.util.HashMap;


// config file loader
public class pxnConfig {

	protected final HashMap<String, Object> data;


	public pxnConfig(HashMap<String, Object> data) {
		this.data = data;
	}


	// get object
	public Object get(String path) {
		try {
			return data.get(path);
		} catch(Exception ignore) {}
		return null;
	}
	// get string
	public String getString(String path) {
		try {
			return (String) data.get(path);
		} catch(Exception ignore) {}
		return null;
	}
	// get boolean
	public Boolean getBoolean(String path) {
		try {
			return (Boolean) data.get(path);
		} catch(Exception ignore) {}
		return null;
	}
	// get integer
	public Integer getInt(String path) {
		try {
			return (Integer) data.get(path);
		} catch(Exception ignore) {}
		return null;
	}
	// get long
	public Long getLong(String path) {
		try {
			return (Long) data.get(path);
			//return ((Integer) data.get(path)).longValue();
		} catch(Exception ignore) {}
		return null;
	}
	// get double
	public Double getDouble(String path) {
		try {
			return (Double) data.get(path);
		} catch(Exception ignore) {}
		return null;
	}
//	// get list
//	public <T> List<T> getList(Class<? extends T> clss, String path) {
//		try {
//			return pxnUtils.castList(clss, data.get(path));
//		} catch (Exception ignore) {
//ignore.printStackTrace();
//			return null;
//		}
//	}
//	public List<String> getStringList(String path) {
//		return getList(String.class, path);
////		try {
////			return pxnUtils.castList(String.class, data.get(path));
//////			return (List<String>) data.get(path);
////		} catch(Exception ignore) {
////			return new ArrayList<String>();
////		}
//	}


}
