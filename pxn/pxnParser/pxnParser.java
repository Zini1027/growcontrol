package com.poixson.pxnParser;

import java.util.ArrayList;
import java.util.List;


public class pxnParser {

	protected String original;
	protected String temp;
	protected String first;
	protected String part;
	protected char delim;
	

	// new parser
	public pxnParser(String data) {
		this(data, ' ');
	}
	public pxnParser(String data, char delim) {
		if(data  == null) throw new NullPointerException("data can't be null!");
		this.original = data;
		this.temp     = data;
		this.delim    = delim;
		next();
		this.first = part;
	}
	// parse into list
	public List<String> getList() {
		pxnParser parser = new pxnParser(original);
		List<String> list = new ArrayList<String>();
		while(parser.hasNext())
			list.add(parser.getNext());
		parser = null;
		return list;
	}


	// multi-line parser
	public static String[] parseMultiLine(String data, char delim) {
		if(data == null) throw new NullPointerException("data can't be null!");
		return data.replaceAll("\r", "").split("\n");
	}


	// parse next part
	public boolean next() {
		if(temp.isEmpty()) {
			part = null;
			return false;
		}
		temp = temp.trim();
		int index = temp.indexOf(delim);
		if(index == -1) {
			// last part
			part = temp;
			temp = "";
		} else{
			// next part
			part = temp.substring(0, index);
			temp = temp.substring(index+1);
		}
		return true;
	}
	public String getNext() {
		if(!next()) return null;
		return getPart();
	}
	// get part
	public String getFirst() {
		return first;
	}
	public String getPart() {
		return part;
	}
	public boolean hasNext() {
		if(temp == null)   return false;
		if(temp.isEmpty()) return false;
		return true;
	}


	// reset parsing string to original
	public void reset() {
		temp = original;
	}


	// get original string
	public String getOriginal() {
		return original;
	}


}
