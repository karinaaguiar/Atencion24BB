package com.atencion24.control;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ControlDates {
	
	public ControlDates() {}
	
	public Date stringToDate(String s) 
	{
	    Calendar c = Calendar.getInstance(TimeZone.getDefault());
	    c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s.substring(0, 2)));
	    c.set(Calendar.MONTH, Integer.parseInt(s.substring(3, 5)) - 1);
	    c.set(Calendar.YEAR, Integer.parseInt(s.substring(6, 10)));
	    /*c.set(Calendar.HOUR, Integer.parseInt(s.substring(11, 13)) % 12);
	    c.set(Calendar.MINUTE, Integer.parseInt(s.substring(14, 16)));
	    c.set(Calendar.SECOND, 0);
	    c.set(Calendar.AM_PM, s.substring(17, 19).toLowerCase().equals("am") ? Calendar.AM : Calendar.PM);*/
	    return c.getTime();
	}

}
