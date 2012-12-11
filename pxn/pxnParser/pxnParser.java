package com.poixson.pxnParser;


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
			part = temp.substring(0, index-1);
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


	public void reset() {
		temp = original;
	}


	public String getOriginal() {
		return original;
	}


}
