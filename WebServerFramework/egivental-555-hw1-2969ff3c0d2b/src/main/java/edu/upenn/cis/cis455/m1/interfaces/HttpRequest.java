package edu.upenn.cis.cis455.m1.interfaces;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.m1.server.HttpWorker;

public class HttpRequest extends Request {
	final static Logger logger = LogManager.getLogger(HttpRequest.class);

	private String req;
	private String method;
	private String host;
	private String userAgent;
	private int port;
	private String pathInfo;
	private String protocol;
	private String url;
	private String uri;
	private String body;
	private int length;
	private String contentType;
	private String ip;
	private boolean malformed;
	private String homeDirectory;
	
	public HttpRequest(String httpRequest, String homeDirectory) {
		req = httpRequest;
		
		this.malformed = false;
		method="";
		host="";
		userAgent="";
		port=45555;
		pathInfo="";
		protocol="";
		url="";
		uri="";
		body="";
		length=-1;
		contentType="";
		ip="";
		this.homeDirectory = homeDirectory;
		parseRequest();
	}
	
	private void parseRequest() {
		boolean hostFound = false;
		boolean dataNeeded = false;
		
		
		String[] lines = req.split("\\r?\\n");
		String[] firstLine = lines[0].split(" ");
		if (firstLine.length != 3) {
			malformed = true;
			return;
		} 
		switch(firstLine[0]) {
		case "GET":
			method = "GET";
			break;
		case "POST":
			dataNeeded = true;
			method = "POST";
			break;
		case "HEAD":
			method = "HEAD";
			break;
		default:
			malformed = true;
		}
		
		pathInfo = firstLine[1];
		protocol = firstLine[2];
		
		for (int i = 1; i < lines.length; i++) {
			String[] thisLine = lines[i].split(" ");
			switch(lines[i].split(" ")[0]) {
			case "Host:":
				
				this.host = thisLine[1].split(":")[0];
				this.port = Integer.parseInt(thisLine[1].split(":")[1]);
				hostFound = true;
				break;
			case "Content-Type:":
				this.contentType = thisLine[1];
				break;
			case "From:":
				break;
			case "Content-Length:":
				this.length = Integer.parseInt(thisLine[1]);
				break;
			case "User-Agent:":
				this.userAgent = thisLine[1];
				break;
			default: 
				body += lines[i];
			}
		}
		if (!hostFound) {
			malformed = true;
		}
		if (dataNeeded) {
			if (length == -1) {
				malformed = true;
			}
		}
	}

	@Override
	public String requestMethod() {
		// TODO Auto-generated method stub
		return method;
	}
	
	public boolean malformed() {
		return malformed;
	}

	@Override
	public String host() {
		// TODO Auto-generated method stub
		return host;
	}

	@Override
	public String userAgent() {
		// TODO Auto-generated method stub
		return userAgent;
	}

	@Override
	public int port() {
		// TODO Auto-generated method stub
		return port;
	}

	@Override
	public String pathInfo() {
		// TODO Auto-generated method stub
		return pathInfo;
	}

	@Override
	public String url() {
		// TODO Auto-generated method stub
		return url;
	}

	@Override
	public String uri() {
		// TODO Auto-generated method stub
		return uri;
	}

	@Override
	public String protocol() {
		// TODO Auto-generated method stub
		return protocol;
	}

	@Override
	public String contentType() {
		// TODO Auto-generated method stub
		return contentType;
	}

	@Override
	public String ip() {
		// TODO Auto-generated method stu)b
		return ip;
	}

	@Override
	public String body() {
		// TODO Auto-generated method stub
		return body;
	}

	@Override
	public int contentLength() {
		// TODO Auto-generated method stub
		return length;
	}

	@Override
	public String headers(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getHomeDirectory() {
		return this.homeDirectory;
	}

	@Override
	public Set<String> headers() {
		// TODO Auto-generated method stub
		return null;
	}

}
