package com.books.fetch;

import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BooksAPIFetcher {

	/**
	 * This is to store the result from making unnecessary fetch calls
	 */
	String result="";
	
	/**
	 * returns the whole Books API from below URL
	 * https://s3-ap-southeast-1.amazonaws.com/he-public-data/books8f8fe52.json
	 */
	@GetMapping("/")
	public String index() {
		if(result.isBlank())
			fetch();
		
		return result;
	}
	
	
	
	public synchronized void fetch() {
		try {
			URL bookUrl=new URL("https://s3-ap-southeast-1.amazonaws.com/he-public-data/books8f8fe52.json");
			HttpsURLConnection httpsURLConnection=(HttpsURLConnection) bookUrl.openConnection();
			Scanner scanner=new Scanner(httpsURLConnection.getInputStream());
			String response="";
			while (scanner.hasNextLine()) {
				response+=scanner.nextLine();
			}
			this.result=response;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
