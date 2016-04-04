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

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.vfs.FileSystemException;

public abstract class MarauderPollingDSVFileReader extends MarauderPollingTextFileReader implements MarauderDSVReader{
	
	private String delimiter = null;
	private int limit = -1;
	private int ignore = -1;
	private boolean isStart = false;

	public MarauderPollingDSVFileReader(File file, int pollingInterval, String delimiter, int limit, int ignore)
			throws FileNotFoundException, FileSystemException {
		super(file, pollingInterval);
		this.delimiter = delimiter;
		this.limit = limit;
		this.ignore = ignore;
		if(ignore>0){
			this.isStart = true;
		}
	}

	@Override
	public void processReadLine(String line, int counter) {
		if(isStart){
			if(counter<ignore){
				return; // ignore 1st n lines 
			}else{
				isStart = false;
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
