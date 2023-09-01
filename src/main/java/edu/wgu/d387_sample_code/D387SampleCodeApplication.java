package edu.wgu.d387_sample_code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.wgu.d387_sample_code.convertor.TimeConverter;

@SpringBootApplication
public class D387SampleCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(D387SampleCodeApplication.class, args);
		
		 String utcTime = "2023-07-16T12:00:00";
	        System.out.println("UTC time: " + utcTime);
	        
	        String etTime =TimeConverter.convertUTCToET(utcTime);
	        System.out.println("Eastern Time: " + etTime);
	        
	        String mtTime = TimeConverter.convertETToMT(etTime);
	        System.out.println("Mountain Time: " + mtTime);
	        
	        
	}

}
