package edu.upenn.cis.cis455.m1.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.m1.handling.HttpIoHandler;
import edu.upenn.cis.cis455.m1.interfaces.*;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Stub class for a thread worker that handles Web requests
 */
public class HttpWorker implements Runnable {
	final static Logger logger = LogManager.getLogger(HttpWorker.class);
	
	private HttpTaskQueue sharedQueue;
	private int index;
	boolean shutdown;
	private String status;
	private BufferedReader in;
	private WebService ws;
	private String homeDirectory;
	

	public HttpWorker(HttpTaskQueue sq, int index, WebService ws){
		this.sharedQueue = sq;
		this.index = index;
		this.shutdown = false;
		this.status = "SHUTDOWN";
		this.ws = ws;
		this.homeDirectory = ws.getStaticFileLocation();
	}
	
	public void shutdown() {
		this.shutdown = true;
	}
	
	public boolean isShutdown() {
		return shutdown;
	}
	
	public String getStatus() {
		return status;
	}
	
    @Override
    public void run() {
    	this.status = "RUNNING";
    	int i = 0;
    	HttpResponse response;
        // TODO Auto-generated method stub
    	while(!shutdown) {
    		try {
    			this.status = "WAITING FOR JOB";
				HttpTask t = sharedQueue.pop(this);
				if (t == null) {
					logger.debug("task was null, shutting down");
					break;
				}
				this.status = "PROCESSING HTTP REQUEST";
				
				
				InputStream sock =  t.getSocket().getInputStream();
				in = new BufferedReader(new InputStreamReader(sock));
				String line = "";
				String newLine = "";
				boolean empty = false;
				boolean done = false;
				while(newLine != null && !done) {
					newLine = in.readLine();
					if (newLine == null) {
						break;
					}
					if (newLine.isEmpty()) {
						done = true;
						break;
					}
					
					line += newLine;
					line += "\n";
				}

				HttpRequest req = new HttpRequest(line, this.homeDirectory);
				boolean open;
				if (req.pathInfo().equals("/control")) {
					String controlPanel = ws.getStatuses(req.protocol());
					response = new HttpResponse(controlPanel);
					open = HttpIoHandler.sendResponse(t.getSocket(), response);
					logger.debug("Sending Control Panel\n" + controlPanel);
				} else if (req.pathInfo().equals("/shutdown")) {
					ws.stop();
					this.status = "SHUTDOWN";
					this.shutdown = true;
					open = HttpIoHandler.sendResponse(t.getSocket(), new HttpResponse(shutdownMessage(req.protocol())));
				} else if (req.malformed()) {
					HaltException except = new HaltException(req.protocol() + " 400 Bad Request\n"	+
							"Connection: Closed\n"												+
							"<html><body>\n" 													+ 
							"<h2>Host Not Found OR Method Improper</h2>\n" 			+ 
							"</body></html>\n\n");
					open = HttpIoHandler.sendException(t.getSocket(), req, except);
				} else {
					response = new HttpResponse(req);
					open = HttpIoHandler.sendResponse(t.getSocket(), response);
				}
				
				if (!open){
					t.getSocket().close();
				} else {
					//do something
				}
			} catch(IOException in) {
				logger.error("IO exception - here");
				in.printStackTrace();
			}	
					
			logger.debug("Processed task number " + i + " on worker indexed " + index);
			i++;
			
    	}
    	logger.debug("Worker shutting down");
    	this.status = "SHUTDOWN";
    }
    
    private String shutdownMessage(String protocol) {
    	Date date = new Date();
    	
    	
    	
    	String response = protocol + " 200 OK\nContent-Length: 100\nConnection: Close\nDate: " + date.toString()+ "\n\n"
    			+ "<!DOCTYPE html>" 						+
    			"<html>\n<body>\n" 										+ 
    			"<h2>Thank you for shutting us down!</h2>\n" 			+ 
    			"</body>\n</html>\n";
    	return response;
    }
    
}
