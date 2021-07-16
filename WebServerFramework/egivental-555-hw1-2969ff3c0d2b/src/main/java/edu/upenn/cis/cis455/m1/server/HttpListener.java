package edu.upenn.cis.cis455.m1.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.m1.handling.*;


import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Stub for your HTTP server, which listens on a ServerSocket and handles
 * requests
 */
public class HttpListener implements Runnable {
	final static Logger logger = LogManager.getLogger(HttpListener.class);
	private HttpTaskQueue sharedQueue;
	private boolean shutdown;
	private ServerSocket server;
	

	public HttpListener(HttpTaskQueue sq, ServerSocket s){
		this.sharedQueue = sq;
		this.shutdown = false;
		this.server = s;
	}
	
	public void shutdown() {
		this.shutdown = true;
	}
	
    @Override
    public void run() {
    	
        while(!shutdown) {
        	Socket socket = null;
        	try {
				socket = server.accept();
			} catch (IOException e ) {
				break;
			}
        	
        	if (socket != null) {
        		HttpTask t = new HttpTask(socket);
    			sharedQueue.add(t, this);
        	}
        }
    }
    
    public boolean isShutdown() {
    	return shutdown;
    }
}
