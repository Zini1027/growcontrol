package com.gcCommon.pxnParser;

import java.util.ArrayList;
import java.util.List;


public class pxnParser {

	protected final String original;
	protected String temp;
	protected String first;
	protected String part;
	protected final char delim;
	private boolean hasNexted = false; // is on first iteration
	

	// new parser
	public pxnParser(String data) {
		this(data, ' ');
	}
	public pxnParser(String data, char delim) {
		if(data  == null) throw new NullPointerException("data can't be null!");
		this.original = data;
		this.temp     = data;
		this.delim    = delim;
		reset();
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
		hasNexted = true;
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
	// get rest of string
	public String getRest() {
		return temp.trim();
//		String str = "";
//		String part;
//		while((part = getNext()) != null)
//			str += part;
//		return str;
	}


	// reset parsing string to original
	public pxnParser reset() {
		if(hasNexted) {
			temp = original;
			next();
			this.first = part;
			hasNexted = false;
		}
		return this;
	}


	// get original string
	public String getOriginal() {
		return original;
	}


	// part equals
	public boolean isFirst(String equals) {
		if(first == null)
			return equals == null;
		return first.equalsIgnoreCase(equals);
	}
	public boolean isPart(String equals) {
		if(part == null)
			return equals == null;
		return part.equalsIgnoreCase(equals);
	}


}
