package com.iop.indiaonphone.utils;

/**
 * 
 * A util class for <b>Date/Time</b> operations.
 * 
 * @author Atish Agrawal
 * 
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;

public class DateTimeUtils {

	/**
	 * 
	 * @return returns current date as string in "01/31/2014" format
	 */

	public static String getDate() {

		// Fetching date and time from the system
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy ",
				Locale.getDefault()); // getting default locale of the system

		String dateFormat = s.format(new Date(System.currentTimeMillis())); // Formatting
																			// Date

		return dateFormat;
	}

	/**
	 * 
	 * @return returns current Unix Timestamp
	 */

	public static long getTime() {

		Date cdate = new Date(System.currentTimeMillis());
		return cdate.getTime();

	}

	/**
	 * Pass your date format and no of days for minus from current If you want
	 * to get previous date then pass days with minus sign else you can pass as
	 * it is for next date
	 * 
	 * @param dateFormat
	 * @param days
	 * @return Calculated Date
	 */
	public static String getCalculatedDate(int days) {
		// Fetching date and time from the system
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy ",
				Locale.getDefault()); // getting default locale of the system
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, days);
		return s.format(new Date(cal.getTimeInMillis()));
	}

	/**
	 * HH:MM:SS
	 * 
	 * @param millis
	 *            time in milliseconds
	 * @return hh:mm:ss as String
	 */

	@SuppressWarnings("deprecation")
	public static String getTimeFromMillis(long millis) {

		Date date = new Date(millis);

		return date.getHours() + ":" + date.getMinutes() + ":"
				+ date.getSeconds();

	}

	@SuppressLint("DefaultLocale")
	public static String totalTimeDifference(long millis) {

		String hms = String.format(
				"%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));

		return hms;

	}

	/**
	 * 
	 * @return the current date time in "01-31-2013 23:59:59" format
	 */
	public static String getDateTime() {

		// Fetching date and time from the system
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss",
				Locale.getDefault()); // getting default locale of the system

		String dateFormat = s.format(new Date(System.currentTimeMillis())); // Formatting
																			// Date

		return dateFormat;
	}

}
