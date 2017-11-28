package com.java.back.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
	private final static SimpleDateFormat sdfDays = new SimpleDateFormat(
			"yyyyMMdd");

	public static final int getyear() {
		Calendar c = Calendar.getInstance();
		return c.get(1);
	}

	/**
	 * 根据出生日期计算年龄
	 * 
	 * @param bir
	 * @return
	 */
	public static int getAgefromBir(String bir) {
		Calendar cal = Calendar.getInstance();
		int iCurrYear = cal.get(Calendar.YEAR);
		int iAge = iCurrYear - Integer.valueOf(bir);
		return iAge;
	}

	/**
	 * 获取YYYYMMDD格式
	 * 
	 * @return
	 */
	public static String getDays() {
		return sdfDays.format(new Date());
	}

	public static final int getmonth() {
		Calendar c = Calendar.getInstance();
		return c.get(2) + 1;
	}

	public static final int getday() {
		Calendar c = Calendar.getInstance();
		return c.get(5);
	}

	public static final int gethour() {
		Calendar c = Calendar.getInstance();
		return c.get(10);
	}

	public static final int getmini() {
		Calendar c = Calendar.getInstance();
		return c.get(12);
	}

	public static final int getsec() {
		Calendar c = Calendar.getInstance();
		return c.get(13);
	}

	public static void main(String[] args) {
		// System.out.println(DateUtil.getweek());
		//
		// String data="2017-03-23";
		// System.out.println(DateUtil.getNextDay(data, 30));
		// int timeDiff =
		// compareTime(StrToTime("2017-7-19 18:00:00"),StrToTime("2017-7-19 17:58:00"));
		// System.out.println(timeDiff);
		// int aa =
		// compareDay(StrToTime("2017-7-19 00:00:00"),StrToTime("2017-7-19 17:58:00"));
		// System.out.println(aa);
		String nextMonthDay = getNextMonthDay(getDate2(), 2);
		System.out.println(nextMonthDay);
		
		int agefromBir = getAgefromBir("1990");
		System.out.println(agefromBir);
	}

	/**
	 * 获得当前时间，如（2016-05-27 21:45:30）
	 * 
	 * @return
	 */
	public static final String getAllDate() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String hehe = dateFormat.format(now);
		return hehe;
	}

	/**
	 * 获得当前时间，如（2016-05-27 21:45）
	 * 
	 * @return
	 */
	public static final String getDate() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String hehe = dateFormat.format(now);
		return hehe;
	}

	/**
	 * 获得当前日期，如（2016-06-12）
	 * 
	 * @return
	 */
	public static final String getDate2() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String hehe = dateFormat.format(now);
		return hehe;
	}

	/**
	 * 判断当前时间是否在制定时间内
	 * 
	 * @param date
	 * @param today
	 *            （是否包含当天，true表示包括，false标示不包括）
	 * @return
	 */
	public static boolean checkCurrTime(Date date, boolean today) {
		boolean falg = false;

		Date currdate = new Date();
		if (currdate.before(date)) {
			falg = true;
		}
		if (today) {
			int year = currdate.getYear();
			int month = currdate.getMonth();
			int day = currdate.getDate();

			int year2 = date.getYear();
			int month2 = date.getMonth();
			int day2 = date.getDate();
			if (year == year2 && month == month2 && day == day2) {
				falg = true;
			}
		}
		return falg;

	}

	/**
	 * 判断当前时间是否在指定时间内(包括当天)
	 */
	public static boolean checkCurrTime(Date date) {
		return checkCurrTime(date, true);
	}

	/**
	 * 判断当前时间是否在指定时间内
	 * 
	 * @return
	 */
	public static boolean checkCurrTime(Date starTime, Date endTime) {
		boolean falg = false;

		Date currdate = new Date();
		if (currdate.before(endTime) && currdate.after(starTime)) {
			falg = true;
		}
		int year = currdate.getYear();
		int month = currdate.getMonth();
		int day = currdate.getDate();

		int year2 = endTime.getYear();
		int month2 = endTime.getMonth();
		int day2 = endTime.getDate();
		if (year == year2 && month == month2 && day == day2) {
			falg = true;
		}
		return falg;

	}

	/**
	 * 比较日期的年月日 忽略时间 当 date2年月日 >= date1年月日 返回true
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean compareDate(Date date1, Date date2) {
		boolean falg = false;
		int year = date1.getYear();
		int month = date1.getMonth();
		int day = date1.getDate();

		int year2 = date2.getYear();
		int month2 = date2.getMonth();
		int day2 = date2.getDate();
		if (year > year2) {
			return false;
		} else if (year2 > year) {
			return true;
		} else if (month > month2) {
			return false;
		} else if (month2 > month) {
			return true;
		} else if (day > day2) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 计算几天之后的日期
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static String getNextDay(String date, int days) {
		if ((date == null) || (date.trim().length() == 0)) {
			return "";
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(f.parse(date));
		} catch (com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException ex) {
			return date;
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		calendar.add(5, days);
		return f.format(calendar.getTime());
	}

	/**
	 * 获取几个月后的日期
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static String getNextMonthDay(String date, int month) {
		if ((date == null) || (date.trim().length() == 0)) {
			return "";
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(f.parse(date));
		} catch (com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException ex) {
			return date;
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.MONTH, month);
		return f.format(calendar.getTime());
	}

	public static final String getTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String hehe = dateFormat.format(now);
		return hehe;
	}

	public static final String getLastDayInfo(String nowDate) {
		String yesterday = "";
		int year = 0;
		int month = 0;
		int day = 0;
		try {
			year = Integer.parseInt(nowDate.substring(0, nowDate.indexOf("-")));
			month = Integer.parseInt(nowDate.substring(
					nowDate.indexOf("-") + 1, nowDate.lastIndexOf("-")));
			day = Integer
					.parseInt(nowDate.substring(nowDate.lastIndexOf("-") + 1));
			day--;
			if (day == 0) {
				month--;
				if (month == 0) {
					month = 12;
					day = 31;
					year--;
				} else {
					switch (month) {
					case 1:
						day = 31;
						break;
					case 3:
						day = 31;
						break;
					case 5:
						day = 31;
						break;
					case 7:
						day = 31;
						break;
					case 8:
						day = 31;
						break;
					case 10:
						day = 31;
						break;
					case 12:
						day = 31;
						break;
					case 4:
						day = 30;
						break;
					case 6:
						day = 30;
						break;
					case 9:
						day = 30;
						break;
					case 11:
						day = 30;
						break;
					case 2:
						if (((year % 4 == 0) && (year % 100 != 0))
								|| (year % 400 == 0)) {
							day = 29;
						} else {
							day = 28;
						}
						break;
					}
				}
			}
			String monthStr = "";
			String dayStr = "";
			if (month < 10) {
				monthStr = "0" + String.valueOf(month);
			} else {
				monthStr = String.valueOf(month);
			}
			if (day < 10) {
				dayStr = "0" + String.valueOf(day);
			} else {
				dayStr = String.valueOf(day);
			}
			yesterday = String.valueOf(year) + "-" + monthStr + "-" + dayStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return yesterday;
	}

	public static final int getIntervalDays(Date enddate, Date begindate) {
		long millisecond = enddate.getTime() - begindate.getTime();
		int day = (int) (millisecond / 24L / 60L / 60L / 1000L);
		return day;
	}

	public static final int isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return 1;
		}
		return 0;
	}

	public static final Date StrToDate(String str) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串转date 形式:（2016-05-27 21:45:30）
	 * 
	 * @param str
	 * @return
	 */
	public static final Date StrToTime(String str) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static final Date StrToDateTime(String str) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 计算时间的差（返回小时数）
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static final String Time1_Time2(Date date1, Date date2) {
		long between = (date1.getTime() - date2.getTime()) / 1000L;// 除以1000是为了转换成秒
		long day1 = between / (24 * 3600);
		long hour1 = between % (24 * 3600) / 3600;// 转为小时
		// return day1+"天"+(hour1-day1*24)+"小时";
		long minute1 = between % 3600 / 60;// 分钟
		return day1 + "天" + hour1 + "小时" + minute1 + "分";
	}

	/**
	 * 计算时间的差（返回分钟）
	 * 
	 * @param date1
	 *            2017-7-19 18:00:00
	 * @param date2
	 *            2017-7-19 17:58:00
	 * @return 2
	 */
	public static final int compareTime(Date date1, Date date2) {
		long between = (date1.getTime() - date2.getTime()) / 1000L;// 除以1000是为了转换成秒
		long minute1 = between % 3600 / 60;// 分钟
		return (int) minute1;
	}

	/**
	 * 计算时间差
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回年与天数
	 */
	public static final int compareDay(Date date1, Date date2) {
		long between = (date1.getTime() - date2.getTime()) / 1000L;// 除以1000是为了转换成秒
		long day1 = between / (24 * 3600);// 转为天数
		return (int) day1;
	}

	/**
	 * 计算时间差
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回年与天数
	 */
	public static final String Time1_Time2ForDay(Date date1, Date date2) {
		long between = (date1.getTime() - date2.getTime()) / 1000L;// 除以1000是为了转换成秒
		long day1 = between / (24 * 3600);// 转为天数
		long year = day1 / 365;// 转为年
		if (year < 1) {
			return day1 + "天";
		}
		return year + "年" + (day1 - year * 365) + "天";
	}

	public static final int Date1_Compare_Date2(Date date1, Date date2) {
		if (date1.getDate() > date2.getDate()) {
			return 1;
		}
		if (date1.getDate() == date2.getDate()) {
			return 2;
		}
		return 0;
	}

	public static final boolean Is_Fw(Date date1, Date date2, Date x) {
		if ((x.getDate() >= date1.getDate())
				&& (x.getDate() <= date2.getDate())) {
			return true;
		}
		return false;
	}

	public static final long Date1_Date2(Date date1, Date date2) {
		long day = (date1.getTime() - date2.getTime()) / 86400000L;
		return day;
	}

	public static final String getFileName() {
		long bd = System.currentTimeMillis();

		long bd1 = bd % 1000L;

		Calendar c = Calendar.getInstance();
		int y = c.get(1);
		int m = c.get(2) + 1;
		int d = c.get(5);
		int h = c.get(10);
		int mm = c.get(12);
		int s = c.get(13);
		String filename = Integer.toString(y) + Integer.toString(m)
				+ Integer.toString(d) + Integer.toString(h)
				+ Integer.toString(mm) + Integer.toString(s) + "_" + bd1;
		return filename;
	}

	public static final Long RandomApp(int i) {
		Random rd = new Random();
		StringBuffer sb = new StringBuffer("");
		int len = 0;
		sb.append(String.valueOf(rd.nextInt((int) (Math.pow(10.0D, i) - 1.0D))));
		while (sb.length() != i) {
			len = i - sb.length();
			sb.append(String.valueOf(rd.nextInt((int) (Math.pow(10.0D, len) - 1.0D))));
		}
		return Long.valueOf(Long.parseLong(sb.toString()));
	}

	public static final String dateformat(Date d, String dateformat) {
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		String hehe = format.format(d);
		return hehe;
	}

	public static final String deteToString(Date d) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String hehe = format.format(d);
		return hehe;
	}

	public static final int getweek() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}
}
