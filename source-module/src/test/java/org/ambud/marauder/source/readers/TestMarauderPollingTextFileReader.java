/*
 * Copyright 2013 Ambud Sharma
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.2
 */
package org.ambud.marauder.source.readers;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.ambud.marauder.source.readers.MarauderPollingTextFileReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs.FileSystemException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMarauderPollingTextFileReader {

	private String filePath = "./src/test/data/polling.txt";
	private PrintWriter pr = null;
	private File temp = null;
	private TestPollingReader reader = null;
	
	@Before
	public void setUp() throws Exception {		
		temp = new File(filePath+"_1");
		FileUtils.copyFile(new File(filePath), temp);
		pr = new PrintWriter(new BufferedWriter(new FileWriter(temp, true)));
		reader = new TestPollingReader(temp, 1);				
		temp.deleteOnExit();		
	}

	@After
	public void tearDown() throws Exception {				
		pr.close();
	}

	@Test
	public void testReadFile() throws IOException, InterruptedException {
		reader = new TestPollingReader(temp, 1);
		reader.readFile();
		assertEquals("line1line2line3", reader.getContents());
		reader.closeReader();
	}

	@Test
	public void testGetReader() {
		assertNotNull(reader.getReader());
	}

	@Test
	public void testGetCounter() throws IOException {
		reader = new TestPollingReader(temp, 1);
		reader.readFile();
		assertEquals(3, reader.getCounter());
		reader.closeReader();
	}

	@Test
	public void testIncrementCounter() throws IOException {
		reader = new TestPollingReader(temp, 1);
		reader.readFile();
		int counter = reader.getCounter();
		reader.incrementCounter();
		assertEquals(counter+1, reader.getCounter());
		reader.closeReader();
	}
	
	private class TestPollingReader extends MarauderPollingTextFileReader {
		
		private StringBuilder builder = null;

		public TestPollingReader(File file, int pollingInterval)
				throws FileNotFoundException, FileSystemException {
			super(file, pollingInterval);
			builder = new StringBuilder();
		}

		@Override
		public void processReadLine(String line, int counter) {
			builder.append(line);
		}
		
		public String getContents() {
			return builder.toString();
		}
		
	}

}
