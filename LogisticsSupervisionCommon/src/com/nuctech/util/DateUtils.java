/**
 * 描述: 
 * 项目名称:              
 * 版本: 1.0                
 * 作者	: 孙明				    
 * 创建日期: 2010-6-12			    
 * 修改记录:(包括修改人，修改日期修改内容,原因)
 * 孙明 	2010-6-2
 **/
package com.nuctech.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * <p>
 * 日期帮助类
 * </p>
 * 
 * 功能:日期类型之间的相互转化
 */
public class DateUtils {
	
	public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/**
	 * 将给定的日期时间，根据给定的格式转换成标准的日期格式
	 * @param dateStr
	 * @param dateFormat
	 * @return
	 */
	public static String toStdString(String dateStr,String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		Date date;
		try {
			date = formatter.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
		SimpleDateFormat stdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String retStr = stdFormatter.format(date);
		return retStr;
	}
	/**
	 * 获得系统时间 TODO: format:yyyy-MM-dd HH:mm:ss
	 * 
	 * @return time
	 */
	public static String getSystemTime(String format) {
		// 系统时间
		String time = "";
		Locale systime = Locale.CHINA;
		SimpleDateFormat timeformat = new SimpleDateFormat(format, systime);
		time = timeformat.format(new Date());// 求得本地机的系统时间;
		return time;
	}

	public static Calendar getDateFromString(String timeStr, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		try {
			Date date = formatter.parse(timeStr);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}

	/**
	 * 获得系统时间 TODO: format:yyyy-MM-dd HH:mm:ss
	 * 
	 * @return Calendar
	 */
	public static Calendar getSystemTime() {
		// 系统时间
		Calendar time = Calendar.getInstance();
		time.setTime(new Date());// 求得本地机的系统时间;
		return time;
	}

	/**
	 * 时间比较
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isDateBefore(Calendar time1, Calendar time2) {
		if (time1 != null && time2 != null) {
			return time1.getTime().before(time2.getTime());
		}
		return false;
	}

	/**
	 * 时间比较
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isDateAfter(Calendar time1, Calendar time2) {
		if (time1 != null && time2 != null) {
			return time1.getTime().before(time2.getTime());
		}
		return false;
	}

	/**
	 * 时间比较 系统时间早于输入时间
	 * 
	 * @param date2
	 * @return
	 */
	public static boolean isDateBefore(String date2) {
		try {
			// 获得系统时间
			Date date1 = new Date();
			DateFormat df = DateFormat.getDateTimeInstance();
			return date1.before(df.parse(date2));
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 系统时间晚于输入时间
	 * 
	 * @param date2
	 * @return
	 */
	public static boolean isDateAfter(String date2) {
		try {
			// 获得系统时间
			Date date1 = new Date();
			DateFormat df = DateFormat.getDateTimeInstance();
			return date1.after(df.parse(date2));
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 时间差，返回毫秒time
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long differenceBetweenDate(Date date1,Date date2){
		return Math.abs(date1.getTime() - date2.getTime());
	}
	
	/**
	 * 时间比较(不含日期) 系统时间早于输入时间
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isTimeBefore(String time) {
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dd = Calendar.getInstance().getTime();
		date = sdf.format(dd);
		return isDateBefore(date + " " + time);
	}

	/**
	 * 时间比较(不含日期) 系统时间晚于输入时间
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isTimeAfter(String time) {
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dd = Calendar.getInstance().getTime();
		date = sdf.format(dd);
		return isDateAfter(date + " " + time);
	}

	/**
	 * 时间转换 TODO：将框架时间控件获得的时间转化成自己想要的时间
	 * 
	 * @return
	 */
	public static String timeFormat(String format, String oldTime) {
		String formatTime = "";

		if (!"".equals(oldTime)) {
			try {
				SimpleDateFormat time = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = time.parse(oldTime);
				SimpleDateFormat timeformat = new SimpleDateFormat(format);
				formatTime = timeformat.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return formatTime;
	}

	/**
	 * Calendar转字符串
	 * 
	 * @param Calendar
	 *            calendar
	 * @return String timeStr
	 */
	public static String date2String(Calendar calendar) {
		Date date = calendar.getTime();
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String timeStr = dateFormatter.format(date);
		return timeStr;
	}

	/**
	 * Calendar转字符串
	 * 
	 * @param Calendar
	 *            calendar
	 * @return String timeStr
	 */
	public static String date2StringYMD(Calendar calendar) {
		Date date = calendar.getTime();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String timeStr = dateFormatter.format(date);
		return timeStr;
	}

	/**
	 * Date 转字符串
	 * 
	 * @param Date
	 *            date
	 * @return String timeStr
	 */
	public static String date2String(Date date) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String timeStr = dateFormatter.format(date);
		return timeStr;
	}

	/**
	 * Date 转字符串
	 * 
	 * @param Date
	 *            date
	 * @return String timeStr
	 */
	public static String date2String(Date date, String fomartStr) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(fomartStr);
		String timeStr = dateFormatter.format(date);
		return timeStr;
	}

	/**
	 * Date 转字符串
	 * 
	 * @param Date
	 *            date
	 * @return String timeStr
	 */
	public static String date2String(Calendar date, String fomartStr) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(fomartStr);
		String timeStr = dateFormatter.format(date.getTime());
		return timeStr;
	}

	/**
	 * 字符串转Calendar
	 * 
	 * @param String
	 *            timeStr
	 * @return Calendar calendar
	 */
	public static Calendar string2Date(String timeStr) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();

		try {
			Date date = dateFormatter.parse(timeStr);
			calendar.setTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return calendar;
	}

	/***
	 * String转换为Calendar
	 * 
	 * @param timeStr
	 *            时间字符串
	 * @param formaStr
	 *            匹配格式
	 * @return
	 */
	public static Calendar strToDate(String timeStr, String formaStr) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(formaStr);
		Calendar calendar = Calendar.getInstance();

		try {
			Date date = dateFormatter.parse(timeStr);
			calendar.setTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return calendar;
	}

	/**
	 * 字符串转Date
	 * 
	 * @param String
	 *            timeStr
	 * @return Calendar calendar
	 * @throws ParseException
	 */
	public static Date stringToDate(String timeStr) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		Date date = null;
		try {
			date = dateFormatter.parse(timeStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * 获得当前时间的前3天
	 * 
	 */

	public static String getTimeBefore3() {
		Date myDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		long myTime = (myDate.getTime() / 1000) - 60 * 60 * 24 * 3;
		myDate.setTime(myTime * 1000);
		String mDate = formatter.format(myDate);
		return mDate;
	}

	/**
	 * 获得当前时间前几天
	 * 
	 * @param days
	 *            前几天
	 * @return
	 */
	public static String getTimeBefore(int days, Date myDate) {
		Date testDate = myDate;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		long myTime = (testDate.getTime() / 1000) - 60 * 60 * 24 * days;
		testDate.setTime(myTime * 1000);
		String mDate = formatter.format(testDate);
		return mDate;
	}
	/**
	 * 获得当前时间前几小时的时间
	 * 
	 * @param hour
	 *            前几小时
	 * @return
	 */
	public static String getHourTimeBefore(int hour, Date myDate) {
		Date testDate = myDate;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long myTime = (testDate.getTime() / 1000) - 60 * 60 * hour;
		testDate.setTime(myTime * 1000);
		String mDate = formatter.format(testDate);
		return mDate;
	}
	/**
	 * 获得指定时间前几天，格式yyyy-MM-dd
	 * 
	 * @param days
	 *            前几天
	 * @return
	 */
	public static String getTimeBefore(int days, String date) {
		Date myDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			myDate = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (myDate != null) {
			long myTime = (myDate.getTime() / 1000) - 60 * 60 * 24 * days;
			myDate.setTime(myTime * 1000);
			String mDate = formatter.format(myDate);
			return mDate;
		}

		return null;
	}

	/**
	 * String转XMLGregorianCalendar
	 * 
	 * @param timeStr
	 * @return
	 */
	public static XMLGregorianCalendar string2XMLGregorianCalendar(
			String timeStr) {
		if (null != timeStr && !"".equals(timeStr)) {
			Calendar calendar = string2Date(timeStr);
			XMLGregorianCalendar cal = new XMLGregorianCalendarImpl();
			cal.setYear(calendar.get(Calendar.YEAR));
			cal.setMonth(calendar.get(Calendar.MONTH) + 1);
			cal.setDay(calendar.get(Calendar.DAY_OF_MONTH));
			cal.setHour(calendar.get(Calendar.HOUR_OF_DAY));
			cal.setMinute(calendar.get(Calendar.MINUTE));
			cal.setSecond(calendar.get(Calendar.SECOND));
			cal.setMillisecond(calendar.get(Calendar.MILLISECOND));
			cal.setTimezone(calendar.get(Calendar.ZONE_OFFSET) / 60000);
			return cal;
		} else {
			return null;
		}
	}

	/**
	 * XMLGregorianCalendar转String
	 * 
	 * @param cal
	 * @return
	 */
	public static String xMLGregorianCalendar2String(XMLGregorianCalendar cal) {
		if (null != cal) {
			Calendar calendar = cal.toGregorianCalendar();
			String result = date2String(calendar);
			return result;
		} else {
			return null;
		}
	}

	/**
	 * 字符串转为Calendar
	 * 
	 * @param timeStr
	 *            时间字符串格式为yyyy-MM-dd
	 * @return
	 */
	public static Calendar str2Date(String timeStr) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			Date date = dateFormatter.parse(timeStr);
			calendar.setTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return calendar;
	}

	/**
	 * java.util.Date转java.sql.date
	 */
	public static Timestamp utilDate2SqlDate(java.util.Date utildate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		formatter.format(utildate);
		java.sql.Date sqldate = new java.sql.Date(utildate.getTime());
		java.sql.Timestamp result = new java.sql.Timestamp(sqldate.getTime());
		return result;
	}

	/**
	 * 获取当前天数是当年的第几天
	 * 
	 * @return
	 */
	public static String getCurrentDaysofYear() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentDay = format.format(new Date());
		return getDayIndexofYear(currentDay);
	}

	/**
	 * 判断是否为闰年
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		// 判断是不是闰年
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
			return true;
		}
		return false;
	}

	/**
	 * 获得当月的天数
	 * 
	 * @param month
	 *            月份
	 * @param isLeapYear
	 *            是否为闰年
	 * @return
	 */
	public static int getDayofMonth(int month, boolean isLeapYear) {
		// 非闰年
		final int[] normal = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		// 闰年
		final int[] leapYear = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if (month != 0 && !isLeapYear) {
			return normal[month - 1];
		}
		if (month != 0 && isLeapYear) {
			return leapYear[month - 1];
		}
		return 0;
	}

	public static int getDayofYear(int year) {
		if (isLeapYear(year))
			return 366;
		return 365;
	}

	/**
	 * 根据日期获取日期在当年中的天数
	 * 
	 * @param date
	 * @return
	 */
	public static String getDayIndexofYear(String date) {
		int year = 0;
		int month = 0;
		int day = 0;
		String strIndex = "";
		boolean isLeapYear = false;
		int index = 0;
		if (date != null) {
			String[] timeArray = date.split("-");
			if (timeArray != null && timeArray.length > 0) {
				year = Integer.valueOf(timeArray[0]);
				month = Integer.valueOf(timeArray[1]);
				day = Integer.valueOf(timeArray[2]);

				isLeapYear = isLeapYear(year);
				for (int i = 1; i < month; i++) {
					index += getDayofMonth(i, isLeapYear);
				}

				index += day;
				
				if(index < 10){ strIndex = "00" + String.valueOf(index);}
				else
				if(index < 100 && index >= 10) {strIndex = "0" + String.valueOf(index);}
				else{ strIndex = index+"";}
				return strIndex;
			}
		}

		return strIndex;
	}

}
