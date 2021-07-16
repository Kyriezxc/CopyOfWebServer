package edu.upenn.cis.cis455.m1.server;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Stub class for implementing the queue of HttpTasks
 */
public class HttpTaskQueue {
	final static Logger logger = LogManager.getLogger(HttpTaskQueue.class);
	
	public int sizeOfQueue;
	public int currentSize;
	private ArrayList<HttpTask> sharedQueue;
	
	public HttpTaskQueue (int size) {
		sharedQueue = new ArrayList<HttpTask>();
		sizeOfQueue = size;
		currentSize = 0;
	}
	
	public int add(HttpTask t, HttpListener l) {
		while(true) {
			synchronized(this) {
				if (this.isFull()) {
					try {
						this.wait();
						if (l.isShutdown()) {
							break;
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} else {
					currentSize++;
					this.sharedQueue.add(t);
					this.notifyAll();
					return 0;
				}
			}	
		}
		return -1;
	}
	
	//get a task from the queue and return it
	public HttpTask pop(HttpWorker w) {
		while(true) {
			synchronized(this) {
				if (this.isEmpty()) {
					try {
						this.wait();
						if (w.isShutdown()) {
							logger.debug("exiting pop wait for worker");
							break;
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}						
				} else {
					this.notifyAll();
					currentSize--;
					return this.sharedQueue.remove(0);
				}
			}
		}
		return null;
	}
	
	public void wakeWorkersForShutdown() {
		synchronized(this) {
			this.notifyAll();
		}
		
		
	}
	
	public boolean isFull() {
		if (currentSize == sizeOfQueue) {
			return true;
		}
		return false;
	}
	
	public boolean isEmpty() {
		if (currentSize == 0) {
			return true;
		} 
		return false;
	}

	
	
	
}
