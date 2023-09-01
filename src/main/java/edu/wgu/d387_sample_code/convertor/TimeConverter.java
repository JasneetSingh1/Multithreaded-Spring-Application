package edu.wgu.d387_sample_code.convertor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class TimeConverter {

	public static String convertUTCToET(String utcTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		ZonedDateTime zdt = ZonedDateTime.parse(utcTime, formatter.withZone(ZoneId.of("UTC")));
		ZonedDateTime etZdt = zdt.withZoneSameInstant(ZoneId.of("America/New_York"));
		return formatter.format(etZdt);
	}

	public static String convertETToMT(String etTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		ZonedDateTime etZdt = ZonedDateTime.parse(etTime, formatter.withZone(ZoneId.of("America/New_York")));
		ZonedDateTime mtZdt = etZdt.withZoneSameInstant(ZoneId.of("America/Denver"));
		return formatter.format(mtZdt);
	}

	public static String currentUTCTime() {
		// handling ParseException
		// create an instance of the SimpleDateFormat class
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		// set UTC time zone by using SimpleDateFormat class
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		// create another instance of the SimpleDateFormat class for local date format
		SimpleDateFormat ldf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		// declare and initialize a date variable which we return to the main method
		Date d1 = null;
		// use try catch block to parse date in UTC time zone
		try {
			// parsing date using SimpleDateFormat class
			d1 = ldf.parse(sdf.format(new Date()));
		}
		// catch block for handling ParseException
		catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		// pass UTC date to main method.
		return sdf.format(new Date());

	}

	public static String dateTimeToAMPM(String timestamp) {
		LocalDateTime dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
		return dateTime.format(formatter);
	}

	public static String dateFormate(String timestamp) {
		 String[] parts = timestamp.split("T");
		LocalDate date = LocalDate.parse(parts[0]);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, uuuu");
		return date.format(formatter);

	}

}
