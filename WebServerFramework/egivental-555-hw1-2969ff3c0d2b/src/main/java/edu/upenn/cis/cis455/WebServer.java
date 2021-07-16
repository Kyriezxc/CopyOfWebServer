package edu.upenn.cis.cis455;

import static edu.upenn.cis.cis455.SparkController.*;

import org.apache.logging.log4j.Level;

import edu.upenn.cis.cis455.m1.server.*;

/**
 * Initialization / skeleton class.
 * Note that this should set up a basic web server for Milestone 1.
 * For Milestone 2 you can use this to set up a basic server.
 * 
 * CAUTION - ASSUME WE WILL REPLACE THIS WHEN WE TEST MILESTONE 2,
 * SO ALL OF YOUR METHODS SHOULD USE THE STANDARD INTERFACES.
 * 
 * @author zives
 *
 */
public class WebServer {
    public static void main(String[] args) {
        org.apache.logging.log4j.core.config.Configurator.setLevel("edu.upenn.cis.cis455", Level.DEBUG);
        
        int port = 45555;
        String home_folder = "./www";
        
        if (args.length > 1) {
        	port = Integer.parseInt(args[0]);
        	if (args.length > 2) {
        		home_folder = args[1];
        	}
        }
        
        WebService ws = new WebService(4, port, home_folder); 
        ws.awaitInitialization();
        
        System.out.println("Waiting to handle requests!");
        //awaitInitialization();
    }
}
