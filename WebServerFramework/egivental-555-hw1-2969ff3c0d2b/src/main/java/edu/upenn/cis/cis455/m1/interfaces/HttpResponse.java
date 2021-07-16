package edu.upenn.cis.cis455.m1.interfaces;

import java.io.DataOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.m1.server.HttpWorker;



public class HttpResponse extends Response {
	final static Logger logger = LogManager.getLogger(HttpResponse.class);
	private HttpRequest req;
	private String response;
	private File file;
	private boolean fileToSend;
	
	public HttpResponse(String resp) {
		this.response = resp;
		this.fileToSend = false;
		req = null;
	}
	
	public HttpResponse(HttpRequest req) {
		this.req = req;
		this.response = "";
		this.fileToSend = false;
		if (req.requestMethod().equals("GET")){
			getRequest();
		} else if (req.requestMethod().equals("POST")) {
			postRequest();
		} else if (req.requestMethod().equals("HEAD")) {
			headRequest();
		}
		
		
	}

	@Override
	public String getHeaders() {
		return "";
	}
	
	private void  getRequest() {
		file = new File(req.getHomeDirectory() + req.pathInfo());
		
		logger.debug(req.getHomeDirectory() + req.pathInfo());
		
		if (file.exists() && !file.isDirectory()) {
			logger.debug("File exists");
			this.response = req.protocol() + " 200 OK\n" 						+
			"Date: " + getDateLine() 											+
			"Content-Type: " + req.contentType() + "\n"							+
			"Content-Length: "+ file.length() + "\n"							+
			getConnection()	+ "\n";	
			
			fileToSend = true;
 		} else {
 			logger.debug("File does not exist");
			this.response = req.protocol() + " 404 Not Found\n"					+
					getConnection() + "\n" 										+
					"<html><body>\n" 											+ 
					"			<h2>File Not Found</h2>\n" 						+ 
					"			Here the file directory \n" 					+ 
					getDirectory() 												+
					"			</body></html>\n";
		}
	}
	
	private void postRequest() {
		
	}
	
	private void headRequest() {
		if (file.exists() && !file.isDirectory()) {
			
			this.response = req.protocol() + " 200 OK\n" 							+
			"Date: " + getDateLine() 											+
			"Content-Type: " + req.contentType() + "\n"							+
			"Content-Length: "+ (int) file.length() + "\n" 							+ 
			getConnection() + "\n";					
					
					
 		} else {
			this.response = req.protocol() + " 404 Not Found\n"					+
					getConnection() + "\n"										+
					"<html><body>\n" 											+ 
					"			<h2>File Not Found</h2>\n" 						+ 
					"			Here the file directory \n" 					+ 
					getDirectory() 												+
					"			</body></html>\n";
		}
	}
	
	private String getDateLine() {
		Date date = new Date();
		return date.toString() + "\n";
	}
	
	private String getDirectory() {
		return "";
	}
	
	private String getConnection(){
		return "Connection: Close\n";
	}
	
	public String getResponse(){
		return this.response;
	}
	public File getFileContent() {
		return this.file;
	}
	
	public boolean sendFile() {
		return fileToSend;
	}
}
