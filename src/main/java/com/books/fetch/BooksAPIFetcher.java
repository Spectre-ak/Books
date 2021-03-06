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
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Books API fetcher class provides a search interface for books API available at 
 * https://s3-ap-southeast-1.amazonaws.com/he-public-data/books8f8fe52.json
 * 
 * @author upadhyayakash2002@gmail.com
 *
 */


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

	/**
	 * This get method takes the book based on the id provided
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Object getById(@PathVariable String id) {
		if (result==null)
			fetch();
		JSONObject requestedBook=new JSONObject();
		for(int i=0;i<result.length();i++) {
			JSONObject jsonObject=result.getJSONObject(i);
			if(jsonObject.get("bookID").toString().equals(id)) {
				requestedBook=jsonObject;
				break;
			}
		}
		return requestedBook.toMap();
	}
	
	/**
	 * this get method takes a search parameter and returns all books which contains the search parameter
	 * in the title 
	 * @param term
	 * @return
	 */
	@GetMapping("/search/{term}")
	public Object getBySearchTerm(@PathVariable String term) {
		if (result==null)
			fetch();
		JSONArray jsonArray=new JSONArray();
		term=term.toLowerCase();
		for(int i=0;i<result.length();i++) {
			JSONObject jsonObject=result.getJSONObject(i);
			String title=jsonObject.get("title").toString().toLowerCase();
			if(title.contains(term)) {
				jsonArray.put(jsonObject);
			}
		}
		return jsonArray.toList();
	}
	
	
	/**
	 * this is the synchronized method use for downloading the books api and saving it locally so that processing can be easy
	 */
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
