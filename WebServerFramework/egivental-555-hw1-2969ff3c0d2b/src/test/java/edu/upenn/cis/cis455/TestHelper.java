package edu.upenn.cis.cis455;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import edu.upenn.cis.cis455.m1.server.HttpListener;
import edu.upenn.cis.cis455.m1.server.HttpTask;
import edu.upenn.cis.cis455.m1.server.HttpTaskQueue;
import edu.upenn.cis.cis455.m1.server.WebService;

import static org.mockito.Mockito.*;

public class TestHelper {
    
	public static Socket getMockSocket() throws IOException{
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
    	
    	return getMockSocket("GET /path/file.html HTTP/1.0 \nHost: www.host1.com:80 \nFrom:"
				+ " someuser@jmarshall.com\nUser-Agent: HTTPTool/1.0", out);
	}
	
    public static Socket getMockSocket(String socketContent, ByteArrayOutputStream output) throws IOException {
        Socket s = mock(Socket.class);
        byte[] arr = socketContent.getBytes();
        final ByteArrayInputStream bis = new ByteArrayInputStream(arr);

        when(s.getInputStream()).thenReturn(bis);
        when(s.getOutputStream()).thenReturn(output);
        when(s.getLocalAddress()).thenReturn(InetAddress.getLocalHost());
        when(s.getRemoteSocketAddress()).thenReturn(InetSocketAddress.createUnresolved("host", 8080));
        
        
        return s;
    }
    
    public static WebService getMockWS(String homeDirec) {
    	WebService s = mock(WebService.class);
    	when(s.getStaticFileLocation()).thenReturn(homeDirec);
    	
    	return s;
    }
    
    public static HttpListener getMockListener() {
    	HttpListener s = mock(HttpListener.class);
    	
    	when(s.isShutdown()).thenReturn(false);
		return s;
    }
    
//    public static HttpRequest getMockHttpRequest() {
//    	HttpRequest h = mock(HttpRequest.class);
//    	
//    	when(h.requestMethod()).thenReturn("GET");
//    	when(h.userAgent()).thenReturn("HTTPTool/1.0");
//    	when(h.protocol()).thenReturn("HTTTP/1.0");
//    	when(h.pathInfo()).thenReturn("/path/file.html");
//		return h;
//    }
    
    
    
}



