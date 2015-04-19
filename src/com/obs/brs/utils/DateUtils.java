/**
 * 
 */
package com.obs.brs.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author ram
 *
 */
public class DateUtils {

	//get age from dob
	public static int getAge (Date dob) throws Exception{
		if(dob == null){
			return 0;
		}
		Calendar cal1 = new GregorianCalendar();
		Calendar cal2 = new GregorianCalendar();
		int age = 0;
		int factor = 0;
		cal1.setTime(dob);
		cal2.setTime(new Date());
		if(cal2.get(Calendar.DAY_OF_YEAR) < cal1.get(Calendar.DAY_OF_YEAR)) {
			factor = -1;
		}
		age = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR) + factor;
		return age;
	}

	//different between start date and end date , return value is no of days(long)
	public static long diffBetweenStartDateAndCompletionDate(Date startDate,Date completionDate) throws Exception{
		if(startDate.before(completionDate)){
			long timeDiff = Math.abs(startDate.getTime() - completionDate.getTime());
			long durationDays = timeDiff / (1000 * 3600 * 24); 
			return durationDays;
		}
		else
			return 0L;
	}

	//different between two days, return value is no of days(long)
	public static long diffBetweenTwoDates(Date date1,Date date2) throws Exception{
		long timeDiff = Math.abs(date1.getTime() - date2.getTime());
		long durationDays = timeDiff / (1000 * 3600 * 24); 
		return durationDays;
	}

	//retrun sql timestamp for a date
	public static java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}

	//get date list from the startdate and end date
	public static List<Date> loadDate(Date startDate, Date endDate){
		List<Date> dateList = new ArrayList<Date>();
		if(startDate!=null){
			if(endDate!=null){
				Calendar start = Calendar.getInstance();
				start.setTime(startDate);
				Calendar end  = Calendar.getInstance();
				end.setTime(endDate);
				for (; !start.after(end); start.add(Calendar.DATE, 1)) {	
					dateList.add(start.getTime());
				}
			}
			else{
				dateList.add(startDate);
			}
		}
		return dateList;
	}
}
