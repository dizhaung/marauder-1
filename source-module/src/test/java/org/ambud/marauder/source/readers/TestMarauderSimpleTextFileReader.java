/**
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.ambud.marauder.source.readers.MarauderSimpleTextFileReader;
import org.junit.Test;

public class TestMarauderSimpleTextFileReader {
	
	@Test
	public void testMarauderSimpleTextFileReader() throws IOException {
		MarauderSimpleTextFileReader reader = new TestSimpleTextFileReader(new File("./src/test/data/hello.txt"));
		assertNotNull(reader);
		reader.closeReader();
	}
	
	@Test
	public void testReadFile() throws IOException {
		TestSimpleTextFileReader reader = new TestSimpleTextFileReader(new File("./src/test/data/hello.txt"));
		reader.readFile();
		assertEquals("hello world", reader.getReadLine());
		assertEquals(1, reader.getCounter());
		reader.closeReader();
	}

	@Test
	public void testProcessReadLine() throws IOException {
		TestSimpleTextFileReader reader = new TestSimpleTextFileReader(new File("./src/test/data/hello.txt"));
		reader.processReadLine("test", 2);
		assertEquals("test", reader.getReadLine());
		reader.closeReader();
	}

	private class TestSimpleTextFileReader extends MarauderSimpleTextFileReader {
		
		private String line = null;
		
		public TestSimpleTextFileReader(File file) throws FileNotFoundException {
			super(file);
		}

		@Override
		public void processReadLine(String line, int counter) {
			this.line = line;
		}
		
		public String getReadLine(){
			return line;
		}
	}
}
