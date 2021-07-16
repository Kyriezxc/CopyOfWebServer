package edu.upenn.cis.cis455.m1.server;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class FileDirectoryTest {

	@Test
	public void test() {
		File file = new File("/home/cis455/Documents/555-hw1/555-hw1/www/index.html");
		assertTrue(file.exists());
	}

}
