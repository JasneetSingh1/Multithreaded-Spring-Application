package edu.wgu.d387_sample_code;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.wgu.d387_sample_code.convertor.TimeConverter;

public class Was {

	public static void main(String[] args) {

		// create a list of Integer type
		List<String> list = new ArrayList<>();
		// add 5 element in ArrayList
		list.add("fr");
		list.add("en");

		// take a random element from list and print them
		// System.out.println(getRandomElement(list));
		//System.out.println(TimeConverter.currentUTCTime());
		String utcTime = TimeConverter.currentUTCTime();
		//System.out.println(TimeConverter.dateFormate(utcTime));
		//System.out.println("UTC time: " + TimeConverter.dateTimeToAMPM(utcTime));

		String etTime = TimeConverter.convertUTCToET(utcTime);
		//System.out.println("Eastern Time: " + TimeConverter.dateTimeToAMPM(etTime));

		String mtTime = TimeConverter.convertETToMT(etTime);
		//System.out.println("Mountain Time: " + TimeConverter.dateTimeToAMPM(mtTime));

		String time = "Join us for an online presentation held at the london hotel on "
				+ TimeConverter.dateFormate(utcTime) + " at " + TimeConverter.dateTimeToAMPM(utcTime)
				+ " Eastern Time | " + TimeConverter.dateTimeToAMPM(etTime) + "| Mountain Time | "
				+ TimeConverter.dateTimeToAMPM(mtTime) + " UTC ";
			System.out.println(time);
	}
//
//	public static String getRandomElement(List<String> list) {
//		//Random rand = new Random();
//		//return list.get(rand.nextInt(list.size()));
//
//		String utcTime = "2023-07-16T12:00:00";
//		System.out.println("UTC time: " + utcTime);
//
//		String etTime = TimeConverter.convertUTCToET(utcTime);
//		System.out.println("Eastern Time: " + etTime);
//
//		String mtTime = TimeConverter.convertETToMT(etTime);
//		System.out.println("Mountain Time: " + mtTime);
//	}

}
