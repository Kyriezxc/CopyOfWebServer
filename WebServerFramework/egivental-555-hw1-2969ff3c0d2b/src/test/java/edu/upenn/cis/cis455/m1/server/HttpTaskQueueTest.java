package edu.upenn.cis.cis455.m1.server;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

import edu.upenn.cis.cis455.TestHelper;
import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.m1.handling.HttpIoHandler;

import org.apache.logging.log4j.Level;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HttpTaskQueueTest {

	private HttpWorker w1;
	private HttpWorker w2;
	private HttpTaskQueue q;
	private WebService ws;
	private HttpListener l;
	
	@Before
	public void setUp() {
		org.apache.logging.log4j.core.config.Configurator.setLevel("edu.upenn.cis.cis455", Level.DEBUG);
		q = new HttpTaskQueue(4);
		ws = TestHelper.getMockWS("/home/cis455/Documents/555-hw1/555-hw1/www");
		w1 = new HttpWorker(q ,0, ws);
		w2 = new HttpWorker(q, 1, ws);
		l = TestHelper.getMockListener();
		
	}


	@Test
	public void test() {
		Thread t1 = new Thread(w1);
		Thread t2 = new Thread(w2);
		t1.start();
		t2.start();
		
		
		for (int i = 0; i < 10; i++) {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				q.add(new HttpTask(TestHelper.getMockSocket("GET /index.html HTTP/1.1 \nHost: www.host1.com:80 \nFrom:"
						+ " someuser@jmarshall.com\nUser-Agent: HTTPTool/1.1", out)), l);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		w1.shutdown();
		w2.shutdown();
	}
	
	

	@After
	public void tearDown() {};
}
