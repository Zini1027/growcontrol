package com.growcontrol.gcCommon.pxnConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.growcontrol.gcCommon.pxnUtils;


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


	// get list
	public <T> List<T> getList(Class<? extends T> clss, String path) {
		try {
			return pxnUtils.castList(clss, data.get(path));
		} catch (Exception ignore) {}
		return null;
	}
	public List<String> getStringList(String path) {
		return getList(String.class, path);
	}


	public List<HashMap<String, Object>> getKeyList(String path) {
		try {
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) get(path);
			return list;
		} catch (Exception ignore) {}
		return null;
	}
	public List<pxnConfig> getConfigList(String path) {
		List<HashMap<String, Object>> map = getKeyList(path);
		if(map == null) return null;
		List<pxnConfig> list = new ArrayList<pxnConfig>();
		for(HashMap<String, Object> d : map)
			list.add(new pxnConfig(d));
		return list;
	}


}
