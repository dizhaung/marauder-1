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
package org.ambud.marauder.source.ids.pcap;

import java.io.File;
import java.io.IOException;

import org.ambud.marauder.source.ids.IDSFileSource;
import org.ambud.marauder.source.ids.MarauderIDSLogFileReader;
import org.apache.commons.vfs.FileObject;

import com.google.common.base.Throwables;

/**
 * Source to spool and stream libpcap files. Pcap files must be closed before
 * this source picks them up for reading.
 * 
 * @author ambudsharma
 */
public class PcapSource extends IDSFileSource {

	private static final String PROP_DEFAULT_FILENAME = "capture.pcap";
	private static final String PROP_DEFAULT_DIR = "/var/log/pcaps";
	private static final String PROP_PREFIX = "pcap.";

	@Override
	protected MarauderIDSLogFileReader builderFileReader(FileObject file, boolean isContinuous) {
		MarauderIDSLogFileReader temp = null;
		try {
			temp = new LibpcapFileReader(new File(file.getName().getPath()), getHostAddress(), getOutputQueue());
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
