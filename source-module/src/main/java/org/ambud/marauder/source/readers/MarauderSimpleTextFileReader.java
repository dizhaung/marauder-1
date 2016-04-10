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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public abstract class MarauderSimpleTextFileReader extends MarauderFileReader {
	
	private BufferedReader reader = null;
	private int counter = 0;

	public MarauderSimpleTextFileReader(File file) throws FileNotFoundException {
		super(file);
		reader = new BufferedReader(new FileReader(getFile()));
	}

	@Override
	public void closeReader() throws IOException {
		reader.close();
	}
	
	@Override
	public void readFile() throws IOException {
		String temp = null;		
		while((temp = reader.readLine())!=null){
			try {
				processReadLine(temp, counter);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			counter++;
		}
	}
	
	/**
	 * Process read line
	 * @param line
	 * @param counter
	 * @throws ParseException 
	 */
	public abstract void processReadLine(String line, int counter) throws ParseException;
	
	/**
	 * @return get reference to the reader initialized by this class
	 */
	public BufferedReader getReader() {
		return reader;
	}
	
	/**
	 * @return line number of file pointer
	 */
	public int getCounter() {
		return counter;
	}
	
	/**
	 * Add +1 to the counter
	 */
	public void incrementCounter() {
		counter++;
	}

}
