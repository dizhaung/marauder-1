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

/**
 * Delimiter separated value File Reader.
 * 
 * Pre-conditions:
 * 	-	File type should be text
 * 	-	Records should be 1 per line separated by a delimiter
 * 
 * Features:
 * 	-	Null values are allowed
 * 	-	Missing values are allowed
 * 	-	Delimiter can be regex and allows for specifying upper limit to resulting values
 * 	-	Can ignore 1st n lines configured via constructor
 *  
 * @author Ambud Sharma
 *
 */
public abstract class MarauderDSVFileReader extends MarauderSimpleTextFileReader implements MarauderDSVReader {
	
	private String delimiter = null;
	private int limit = -1;
	private int ignore = -1;

	public MarauderDSVFileReader(File file, String delimiter, int limit, int ignore) throws FileNotFoundException {
		super(file);
		this.delimiter = delimiter;
		this.limit = limit;
		this.ignore = ignore;
	}

	@Override
	public void processReadLine(String line, int counter) {
		if(ignore>0){
			if(counter<ignore){
				return; // ignore 1st n lines 
			}
		}
		String[] values = line.split(delimiter, limit);
		if(values!=null && values.length>0){
			processSplits(values, counter);
		}else{
			processErroredLine(line, counter);
		}
	}
	
}