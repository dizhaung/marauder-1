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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class MarauderFileReader {

	private File file = null;
	
	public MarauderFileReader(File file) throws FileNotFoundException {
		if(!file.exists()){
			throw new FileNotFoundException();
		}
		this.file = file;
	}
	
	/**
	 * Read the file
	 */
	public abstract void readFile() throws IOException;
	
	/**
	 * Close the reader or reference to readers
	 */
	public abstract void closeReader() throws IOException;
	
	/**
	 * @return get file being/to-be read
	 */
	public File getFile() {
		return file;
	}
	
}
