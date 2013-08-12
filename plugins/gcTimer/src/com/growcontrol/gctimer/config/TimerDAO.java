package com.growcontrol.gctimer.config;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.TimeUnitTime;
import com.growcontrol.gcCommon.pxnConfig.pxnConfig;
import com.growcontrol.gctimer.gcTimer;


public class TimerDAO {

	public final String name;
	public final gcTimer.Type type;
	public final boolean enabled;
	public final TimeUnitTime length;
	public final List<TriggerDAO> triggers;
	public final String output;


	// get from config (server)
	public static List<TimerDAO> get(List<pxnConfig> configList) {
		if(configList == null) return null;
		List<TimerDAO> timers = new ArrayList<TimerDAO>();
		for(pxnConfig config : configList) {
			try {
				TimerDAO timer = new TimerDAO(config);
				timers.add(timer);
			} catch (Exception ignore) {}
		}
		return timers;
	}
	// set from packet (client)
	public static List<TimerDAO> set(String data) {
		//TODO:
		return null;
	}
	public TimerDAO(pxnConfig config) {
		if(config == null) throw new NullPointerException("config cannot be null!");
		this.name = config.getString("Name");
		this.type = gcTimer.ParseTimerType(config.getString("Timer Type"));
		Boolean b = config.getBoolean("Enabled");
		this.enabled = (b == null ? false : b.booleanValue());
		this.length = TimeUnitTime.Parse(config.getString("Length")).setFinal();
		this.triggers = TriggerDAO.get(config.getStringList("Triggers"));
		this.output = config.getString("Output");
	}


}
