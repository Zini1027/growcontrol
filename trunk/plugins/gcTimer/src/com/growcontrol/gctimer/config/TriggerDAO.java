package com.growcontrol.gctimer.config;

import java.util.ArrayList;
import java.util.List;


public class TriggerDAO {

	public final String[] triggers;


	public static List<TriggerDAO> get(List<String> list) {
		if(list == null) return null;
		List<TriggerDAO> triggers = new ArrayList<TriggerDAO>();
		for(String line : list) {
			try {
				TriggerDAO trigger = new TriggerDAO(line);
				triggers.add(trigger);
			} catch (Exception ignore) {}
		}
		return triggers;
	}
	public TriggerDAO(String line) {
		if(line == null) throw new NullPointerException("line cannot be null!");
		this.triggers = line.split(" ");
	}


}
