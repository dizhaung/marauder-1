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
import java.text.ParseException;

import org.apache.commons.vfs.FileChangeEvent;
import org.apache.commons.vfs.FileListener;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.impl.DefaultFileMonitor;

/**
 * Polls text file using Apache Commons VFS API and uses fires read events 
 * when changes are made to the file.
 * 
 * Such reader can be concrete implemented for DSV or other types of
 * text files and have pre-existing content. If a file doesn't exist a reader
 * can't be deployed to read it. 
 * 
 * @author Ambud Sharma
 *
 */
public abstract class MarauderPollingTextFileReader extends MarauderSimpleTextFileReader {
	
	private DefaultFileMonitor monitor = null;
	
	public MarauderPollingTextFileReader(File file, int pollingInterval) throws FileNotFoundException, FileSystemException {
		super(file);
		FileSystemManager fsMgr = VFS.getManager();
		FileObject fo = fsMgr.resolveFile(getFile().getAbsolutePath());
		this.monitor = new DefaultFileMonitor(new FileListener() {
			
			@Override
			public void fileDeleted(FileChangeEvent arg0) throws Exception {
				// can't do anything since the file is closed
				closeReader();
			}
			
			@Override
			public void fileCreated(FileChangeEvent arg0) throws Exception {
				// shouldn't happen if file already exist
			}
			
			@Override
			public void fileChanged(FileChangeEvent arg0) throws Exception {
				readFile();
			}
		});
		monitor.addFile(fo);
		monitor.setRecursive(false);
		monitor.setDelay(pollingInterval);
	}

	@Override
	public synchronized void readFile() throws IOException {
		String temp = null;		
		while((temp = getReader().readLine())!=null){
			try {
				processReadLine(temp, getCounter());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			incrementCounter();
		}
	}

}
