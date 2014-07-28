package com.calendar;


/** @class Date
 * 
 * @author Nicolas
 *
 *	A Date is represented by the year, the month from 0 to 11, the day, the hour and the minute.
 *	Bissextile years are recognized by the following rules : either (a multiple of 4 but not of 100) or (a multiple of 400) 
 *
 */
public class Date {
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private boolean leapYear; // whether the year is Bissextile or not, only used in addMonth and addDay
	private int[] months;	  // the right array of month depending on the year (Bissextile or not), also only used in addMonth and addDay
	private int[] monthOfLeapYear = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private int[] monthOfNotLeapYear = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	/** Creates a new date */
	public Date() {
		
	}
	
	/** Create a new date with the parameters you want
	 * 
	 * @param year
	 * @param month (from 0 to 11)
	 * @param day 
	 * @param hour
	 * @param minute
	 * 
	 */
	public Date(int year, int month, int day, int hour, int minute) {
		this.addMinute(minute);
		this.addHour(hour);
		this.addDay(day);
		this.addMonth(month);
		this.addYear(year);
	}

	/** Copies a date */
	public Date copy() {
		Date dateCopy = new Date();
		dateCopy.minute = this.minute;
		dateCopy.hour = this.hour;
		dateCopy.day = this.day;
		dateCopy.month = this.month;
		dateCopy.year = this.year;
		return dateCopy;
	}
	
	/** Checks whether or not it's leap year.
	 * 
	 * This is a leap year if this a multiple of 4 but not of 100 or if it's a multiple of 400)
	 * 
	 */
	private boolean isLeapYear() {
		return ((this.year % 4 == 0 && this.year % 100 != 0) || this.year % 400 == 0);
	}

	/** Adds the number of years you want to the current date
	 * 
	 * @param year
	 * 
	 */
	public Date addYear(int year) {
		this.year += year;
		return this;
	}

	/** Adds the number of months you want to the current date
	 * 
	 * @param month
	 * 
	 */
	public Date addMonth(int month) {
		int yearsToAdd = (this.month + month) / 12;
		this.month = (this.month + month) % 12;
		this.addYear(yearsToAdd);
		leapYear = this.isLeapYear();
		if (leapYear) {
			months = monthOfLeapYear;
		} else {
			months = monthOfNotLeapYear;
		}
		/* If the number of day is 30 and the current day is 31
		 * or if this is February,
		 * 		if this is a leap year, then check if current is less or equal to 29,
		 * 		and if it isn't a leap year, then check if current day is less or equal to 28,
		 * go to the next month and check if this is a new year
		 */
		if (((this.month == 3 || this.month == 5 || this.month == 8 || this.month == 10) && this.day == 31)
                  || (leapYear && this.month == 1 && this.day > 29) || (!leapYear && this.month == 1 && this.day > 28)) {
			this.month++;
			this.day = 1;
			if (this.month == 0) {
				this.year++;
			}
		}
		return this;
	}

	/** Adds the number of days you want to the current date
	 * 
	 * @param day
	 * 
	 */
	public Date addDay(int day) {
		int daysToAdd = day;
		leapYear = this.isLeapYear();
		if (leapYear) {
			months = monthOfLeapYear;
		} else {
			months = monthOfNotLeapYear;
		}
		/* while the number of days added to the current day exceeds the number of days in the current month, go to the next month and remove the number of days of the former current month */
		while (this.day + daysToAdd > months[this.month]) {
			this.month = (this.month + 1) % 12;
			if (this.month == 0) {
				this.year++;
				leapYear = this.isLeapYear();
				if (leapYear) {
					months = monthOfLeapYear;
				} else {
					months = monthOfNotLeapYear;
				}
				daysToAdd -= months[11];
			} else {
				daysToAdd -= months[this.month - 1];
			}
		}
		this.day += daysToAdd;
		return this;
	}

	/** Adds the number of hours you want to the current date
	 * 
	 * @param hour
	 * 
	 */
	public Date addHour(int hour) {
		int daysToAdd = (this.hour + hour) / 24;
		this.hour = (this.hour + hour) % 24;
		this.addDay(daysToAdd);
		return this;
	}

	/** Adds the number of minutes you want to the current date
	 * 
	 *  @param minute
	 *  
	 */
	public Date addMinute(int minute) {
		int hoursToAdd = (this.minute + minute) / 60;
		this.minute = (this.minute + minute) % 60;
		this.addHour(hoursToAdd);
		return this;
	}

	
	/** Adds the amout of time you want to the current date starting with the smallest
	 * period of time and so on (minute then hour etc..)
	 * 
	 *  @param year
	 *  @param month
	 *  @param day
	 *  @param hour
	 *  @param minute
	 *  
	 */
	public Date addTime(int year, int month, int day, int hour, int minute) {
		this.addMinute(minute);
		this.addHour(hour);
		this.addDay(day);
		this.addMonth(month);
		this.addYear(year);
		return this;
	}

}
