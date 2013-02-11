package com.attendr.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.attendr.net.Utils;

public class ScheduleItem {
	private String title, description, start, end;
	
	public Date getStartTime() {
		return Utils.parseStringAsDate(start);
	}
	
	public int getStartHour() {
		DateFormat df = new SimpleDateFormat("hh");
		String hour = df.format(getStartTime());
		return Integer.parseInt(hour);
	}
	
	public int getEndHour() {
		DateFormat df = new SimpleDateFormat("hh");
		String hour = df.format(getEndTime());
		return Integer.parseInt(hour);
	}
	
	public int getStartMinute() {
		DateFormat df = new SimpleDateFormat("mm");
		String hour = df.format(getStartTime());
		return Integer.parseInt(hour);
	}
	
	public int getEndMinute() {
		DateFormat df = new SimpleDateFormat("mm");
		String hour = df.format(getEndTime());
		return Integer.parseInt(hour);
	}
	
	public Date getEndTime() {
		return  Utils.parseStringAsDate(end);
	}
	
	public int getLengthInMinutes() {
		return (int)(getEndTime().getTime()-getStartTime().getTime())/(1000)/60;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean overlaps(ScheduleItem item) {
		//1-3 && 1-2 true
		//1-3 && 1-3 true
		if (getStartTime().getTime()<=item.getStartTime().getTime() && 
				getEndTime().getTime()>=item.getEndTime().getTime())
			return true;
		//1-3 && 2-4 true
		//1-3 && 3-5 false
		if (getStartTime().getTime()<=item.getStartTime().getTime() && 
				getEndTime().getTime()<=item.getEndTime().getTime() &&
				getEndTime().getTime()>item.getStartTime().getTime())
			return true;
		//2-4 && 1-3
		if (getStartTime().getTime()>=item.getStartTime().getTime() && 
				getEndTime().getTime()>=item.getEndTime().getTime() &&
				getEndTime().getTime()<item.getStartTime().getTime())
			return true;
		
		return false;
	}
	
	public int getPositionStart() {
		return 60*(getStartHour()-7) + getStartMinute() + 2;
	}
}
