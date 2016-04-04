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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.ambud.marauder.source.readers.MarauderDSVReader;
import org.ambud.marauder.source.readers.MarauderPollingDSVFileReader;
import org.apache.commons.vfs.FileSystemException;
import org.junit.Test;

public class TestPollingDSVFileReader {
	
	private String path = "./src/test/data/polling_dsv.txt";

	@Test
	public void testProcessReadLine() throws IOException {
		TestDSVReader reader = new TestDSVReader(new File(path), 10, ",", 4, 3);
		reader.readFile();
		String[] temp = reader.getVals();
		assertEquals("line4", temp[0]);
		assertEquals("ele2", temp[2].trim());
	}
	
	@Test
	public void testProcessReadLine_MaxLimit() throws IOException {
		TestDSVReader reader = new TestDSVReader(new File(path), 10, ",", 2, 3);
		reader.readFile();
		String[] temp = reader.getVals();
		assertEquals("line4", temp[0]);
		assertEquals("ele1, ele2, ele3", temp[1].trim());
		assertEquals(2, temp.length);
	}
	
	private class TestDSVReader extends MarauderPollingDSVFileReader implements MarauderDSVReader {
		
		private String[] vals = null;
		
		public TestDSVReader(File file, int pollingInterval, String delimiter,
				int limit, int ignore) throws FileNotFoundException,
				FileSystemException {
			super(file, pollingInterval, delimiter, limit, ignore);			
		}

		@Override
		public void processSplits(String[] splits, int counter) {
			this.vals = splits; 
		}

		@Override
		public void processErroredLine(String line, int counter) {
		}
		
		public String[] getVals() {
			return vals;
		}
	}

}
