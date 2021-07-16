package edu.upenn.cis.cis455.m1.handling;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;

import edu.upenn.cis.cis455.exceptions.ClosedConnectionException;


// This needs to be caught by the request handler in the event of an error
import edu.upenn.cis.cis455.exceptions.HaltException;


public class ResponseHandler {
	final static Logger logger = LogManager.getLogger(ResponseHandler.class);
	    
	    /**
	     * Initial fetch buffer for the HTTP request header
	     * 
	     */

}
