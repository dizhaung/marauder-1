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
import java.util.regex.Pattern;

import org.apache.commons.vfs.FileSystemException;

/**
 * Reads a line if it fits the configured regular expression 
 * 
 * @author Ambud Sharma
 *
 */
public abstract class MarauderRegexTextFileReader extends MarauderPollingTextFileReader {

	private Pattern[] patterns = null;
	
	/**
	 * Constructor with the following arguments
	 * @param file
	 * @param pollingInterval
	 * @param patternArray
	 * @throws FileNotFoundException
	 * @throws FileSystemException
	 */
	public MarauderRegexTextFileReader(File file, int pollingInterval, String[] patternArray)
			throws FileNotFoundException, FileSystemException {
		super(file, pollingInterval);
		patterns = new Pattern[patternArray.length];
		for(int i=0;i<patternArray.length;i++){
			patterns[i] = Pattern.compile(patternArray[i]);
		}
	}

	@Override
	public void processReadLine(String line, int counter) {
		for(Pattern inputPattern:patterns){
			if(inputPattern.matcher(line).matches()){
				processValidatedLine(line, counter);
				break;
			}
		}
	}

	/**
	 * Process line validated with the set of input regular expressions
	 * @param line
	 * @param counter
	 */
	protected abstract void processValidatedLine(String line, int counter);

}
