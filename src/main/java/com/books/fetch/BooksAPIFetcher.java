package com.books.fetch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BooksAPIFetcher {

	/**
	 * This is to store the result from making unnecessary fetch calls
	 */

	JSONArray result=null;

	/**
	 * returns the whole Books API from below URL
	 * https://s3-ap-southeast-1.amazonaws.com/he-public-data/books8f8fe52.json
	 */
	@GetMapping("/")
	public Object index() {
		if (result==null)
			fetch();
		
		return result.toList();
	}

	
	
	public synchronized void fetch() {
		try {
			URL bookUrl=new URL("https://s3-ap-southeast-1.amazonaws.com/he-public-data/books8f8fe52.json");
			HttpsURLConnection httpsURLConnection=(HttpsURLConnection) bookUrl.openConnection();
			Scanner scanner=new Scanner(httpsURLConnection.getInputStream());
			Files.copy(httpsURLConnection.getInputStream(), Path.of("book.json"), StandardCopyOption.REPLACE_EXISTING);

			JSONTokener jsonTokener=new JSONTokener(new FileInputStream("book.json"));
			this.result=new JSONArray(jsonTokener);
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
