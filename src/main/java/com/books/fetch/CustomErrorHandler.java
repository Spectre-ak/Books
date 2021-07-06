package com.books.fetch;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorHandler implements ErrorController{
   
    @RequestMapping(value ="/error",method = RequestMethod.GET)
    public String handleError(ModelMap model,HttpServletRequest request) {
    	System.out.println(request);
    
        System.out.println();
        return "<h1>Endpoint not found</h1><hr>"
        		+ "<h2>Available endpoints</h2>"
        		+ "<h4><a href='https://book-fetch.azurewebsites.net/'>/ (home)</a></h4>"
        		+ "<h4><a href='https://book-fetch.azurewebsites.net/search/search_query'>/search/search_query</a></h4>"
        		+ "<h4><a href='https://book-fetch.azurewebsites.net/bookId'>/bookId</a></h4>";
    }


}
