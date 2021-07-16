package edu.upenn.cis.cis455.m1.handling;

import java.io.*;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.m1.interfaces.HttpResponse;
import edu.upenn.cis.cis455.m1.interfaces.HttpRequest;

/**
 * Handles marshaling between HTTP Requests and Responses
 */
public class HttpIoHandler {
    final static Logger logger = LogManager.getLogger(HttpIoHandler.class);

    /**
     * Sends an exception back, in the form of an HTTP response code and message.
     * Returns true if we are supposed to keep the connection open (for persistent
     * connections).
     */
    
    public static boolean sendException(Socket socket, HttpRequest request, HaltException except) {
    	try {
    		logger.debug("sending exception with body: " + except.body());
        	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        	out.writeUTF(except.body());     
    	} catch (IOException i) {
    		i.printStackTrace();
    	} 
        return false;
    }

    /**
     * Sends data back. Returns true if we are supposed to keep the connection open
     * (for persistent connections).
     * @throws IOException 
     */
    public static boolean sendResponse(Socket socket, HttpResponse response) throws IOException {
    	DataOutputStream out = new DataOutputStream(socket.getOutputStream()); 
    	logger.debug("response is:" + response.getResponse());
    	out.writeBytes(response.getResponse());
    	if (response.sendFile()) {
    		byte[] fileData;
    		File file = response.getFileContent();
    		
    		long len = (int) file.length();
    		fileData = new byte[(int) len];
    		
    		DataInputStream fileReader = new DataInputStream(new FileInputStream(file));
    		fileReader.read(fileData, 0, (int) len);
    		out.write(fileData);
    	}
    	
    	out.close();
    	
        return false;
    }
}
