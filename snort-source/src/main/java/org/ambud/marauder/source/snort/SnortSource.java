/*
 * Copyright 2015 Ambud Sharma
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
package org.ambud.marauder.source.snort;

import java.io.File;
import java.io.IOException;

import org.ambud.marauder.source.ids.IDSFileSource;
import org.ambud.marauder.source.ids.MarauderIDSLogFileReader;
import org.ambud.marauder.source.snort.unified2.Unified2IDSFileReader;
import org.apache.commons.vfs.FileObject;

import com.google.common.base.Throwables;

/**
 * Marauder Snort Source is capable of parsing all of Snort logs including:
 * <ul>
 * 	<li>Unified2 logs
 * 	<li>Unified logs
 * 	<li>Snort alert logs
 * </ul>
 * 
 * Marauder Snort can be configured in the standard way of configuring an IDS 
 * sink. Each sink can be configured to use just one type of Snort output format.
 * 
 * To configure multiple outputs either from single sensors or multiple Snort sensors
 * multiple source configurations can be used.
 * 
 * @author Ambud Sharma
 */
public class SnortSource extends IDSFileSource {

	private static final String PROP_DEFAULT_FILENAME = "snort.log";
	private static final String PROP_DEFAULT_DIR = "/var/log/snort";
	private static final String PROP_PREFIX = "snort.";

	@Override
	protected MarauderIDSLogFileReader builderFileReader(FileObject file, boolean isContinuous) {
		MarauderIDSLogFileReader temp = null; 
		try {
			temp = new Unified2IDSFileReader(new File(file.getName().getPath()), isContinuous, getHostAddress(), getOutputQueue());
		} catch (IOException e) {				
			Throwables.propagate(e);
		}
		return temp;
	}


	@Override
	public String getDefaultFilename() {
		return PROP_DEFAULT_FILENAME;
	}


	@Override
	public String getDefaultDirectory() {
		return PROP_DEFAULT_DIR;
	}


	@Override
	public String getPrefix() {
		return PROP_PREFIX;
	}

	
}
