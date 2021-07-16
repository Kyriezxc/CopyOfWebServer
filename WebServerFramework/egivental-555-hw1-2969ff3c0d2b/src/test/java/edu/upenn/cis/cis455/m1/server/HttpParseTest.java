package edu.upenn.cis.cis455.m1.server;

import static org.junit.Assert.*;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.upenn.cis.cis455.m1.interfaces.HttpRequest;

public class HttpParseTest {
	final static Logger logger = LogManager.getLogger(HttpParseTest.class);
	
	@Before
	public void setUp() {
		org.apache.logging.log4j.core.config.Configurator.setLevel("edu.upenn.cis.cis455", Level.DEBUG);
		
		
	}
	
	@Test
	public void testBasicGET() {
		
		HttpRequest h = new HttpRequest("GET /path/file.html HTTP/1.0 \nHost: www.host1.com:80 \nFrom:"
				+ " someuser@jmarshall.com\nUser-Agent: HTTPTool/1.0", "");
		
		
		logger.debug("checking Http Parsing Logic for basic GET");
		assertTrue(h.port() == 80);
		assertTrue(h.host().equals("www.host1.com"));
		assertTrue(h.requestMethod().equals("GET"));
		assertTrue(h.protocol().equals("HTTP/1.0"));
		assertTrue(h.pathInfo().equals("/path/file.html"));
		assertTrue(h.userAgent().equals("HTTPTool/1.0"));
	}
	
	@Test
	public void testBasicPOST() {
		HttpRequest p = new HttpRequest("POST /path/script.cgi HTTP/1.0\n" + 
				"Host: www.host1.com:80\n" +
				"From: frog@jmarshall.com\n" + 
				"User-Agent: HTTPTool/1.0\n" + 
				"Content-Type: application/x-www-form-urlencoded\n" + 
				"Content-Length: 32\n", "");
		
		logger.debug("checking HTTP Parsing Logic for basic POST");
		logger.debug("port" + p.port());
		assertTrue(p.port() == 80);
		assertTrue(p.host().equals("www.host1.com"));
		assertTrue(p.requestMethod().equals("POST"));
		assertTrue(p.protocol().equals("HTTP/1.0"));
		assertTrue(p.pathInfo().equals("/path/script.cgi"));
		assertTrue(p.userAgent().equals("HTTPTool/1.0"));
		assertTrue(p.contentType().equals("application/x-www-form-urlencoded"));
		assertTrue(p.contentLength() == 32);
	}
	
	@After
	public void tearDown() {};
}
