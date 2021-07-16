/**
 * CIS 455/555 route-based HTTP framework
 * 
 * V. Liu, Z. Ives
 * 
 * Portions excerpted from or inspired by Spark Framework, 
 * 
 *                 http://sparkjava.com,
 * 
 * with license notice included below.
 */

/*
 * Copyright 2011- Per Wendel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.upenn.cis.cis455.m1.server;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import edu.upenn.cis.cis455.exceptions.*;

import java.io.IOException;
import java.net.ServerSocket;

public class WebService {
    final static Logger logger = LogManager.getLogger(WebService.class);

    protected static int numWorkers;
    
    protected HttpListener listener;
    protected ArrayList<HttpWorker> workers;
    protected HttpTaskQueue sharedQueue; 
    
    
    protected boolean running;
    protected ServerSocket server;
    protected int port;
    protected static String root;

    public WebService(int size, int port, String root) {
    	this.numWorkers = size;
    	running = false;
    	this.port = port;
    	this.root = root;
    }
    
    /**
     * Launches the Web server thread pool and the listener
     * @throws IOException 
     */
    private void start() throws IOException {
    	if (!running) {
    		
    		server = new ServerSocket(port);
    		
    		//server = getMockServerSocket();
    		workers = new ArrayList<HttpWorker>();
    		
    		this.sharedQueue = new HttpTaskQueue(numWorkers);
        	
        	//Creating a producer thread
        	this.listener = new HttpListener(sharedQueue, server);
        	Thread listenerThread = new Thread(listener);
        	listenerThread.start();

        	//Creating consumer thread.
        	
        	for (int  i = 0; i < this.numWorkers; i++) {
        		HttpWorker w = new HttpWorker(sharedQueue, i, this);
        		workers.add(w);
        		Thread t = new Thread(w);
        		t.start();
        	}
        	running = true;
    	} else {
    		return;
    	}
    }

    /**
     * Gracefully shut down the server
     * @throws IOException 
     */
    public void stop() throws IOException {
    	for (int i = 0; i < numWorkers; i++) {
    		workers.get(i).shutdown();
    	}
    	this.listener.shutdown();
    	this.sharedQueue.wakeWorkersForShutdown();
    	server.close();
    }
    
    public String getStatuses(String protocol) {
    	String statuses = "";
    	for (int i = 0; i < this.numWorkers; i++) {
    		statuses += "Worker " + i +" "+ workers.get(i).getStatus() + "\n";
    	}
    	String response = protocol + " 200 OK\nContent-Length:" + (statuses.length() + 106) + "\n\n" +
    			"<!DOCTYPE html>\n" 										+
    			"<html><body>\n" 										+ 
    			"<h2>Worker Statuses </h2>\n" 							+ 
    			statuses 												+ 
    			"</body></html>\n";
    	
    	logger.debug(response.length());
    	return response;
    	
    }

    /**
     * Hold until the server is fully initialized.
     * Should be called after everything else.
     */
    public void awaitInitialization() {
        logger.info("Initializing server");
        try {
        	start();
        } catch (IOException i) {
        	i.printStackTrace();
        }
        
    }

    /**
     * Triggers a HaltException that terminates the request
     */
    public HaltException halt() {
        throw new HaltException();
    }

    /**
     * Triggers a HaltException that terminates the request
     */
    public HaltException halt(int statusCode) {
        throw new HaltException(statusCode);
    }

    /**
     * Triggers a HaltException that terminates the request
     */
    public HaltException halt(String body) {
        throw new HaltException(body);
    }

    /**
     * Triggers a HaltException that terminates the request
     */
    public HaltException halt(int statusCode, String body) {
        throw new HaltException(statusCode, body);
    }

    ////////////////////////////////////////////
    // Server configuration
    ////////////////////////////////////////////

    /**
     * Set the root directory of the "static web" files
     */
    public void staticFileLocation(String directory) {
    	this.root = directory;
    }

    public String getStaticFileLocation() {
    	return this.root;
    }
    /**
     * Set the IP address to listen on (default 0.0.0.0)
     */
    public void ipAddress(String ipAddress) {}

    /**
     * Set the TCP port to listen on (default 45555)
     */
    public void port(int port) {}

    /**
     * Set the size of the thread pool
     */
    public void threadPool(int threads) {}

}
