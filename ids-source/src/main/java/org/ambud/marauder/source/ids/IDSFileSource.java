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
package org.ambud.marauder.source.ids;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.apache.commons.vfs.FileChangeEvent;
import org.apache.commons.vfs.FileListener;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.impl.DefaultFileMonitor;
import org.apache.flume.Context;

import com.google.common.base.Throwables;

/**
 * Source to spool and stream libpcap files. Pcap files must be closed before this source
 * picks them up for reading.
 * 
 * @author ambudsharma
 */
public abstract class IDSFileSource extends MarauderIDSSource {
	
	private static final String PROP_BUFFER_SIZE = "buffer.size";
	private static final String PROP_IS_SEQUENTIAL = "sequential";
	private static final String PROP_BASE_NAME = "base.name";
	private static final String PROP_DIRECTORY = "directory";
	private Logger logger = Logger.getLogger(IDSFileSource.class.getCanonicalName());
	private File watchDirectory = null;
	private BlockingQueue<MarauderIDSEvent> outputQueue = null;
	private ExecutorService threadPool = null;
	private FileObject watchObject = null;
	private String logBaseName = null;
	private DefaultFileMonitor monitor = null;
	private boolean isSequential = true;
	
	@Override
	public void configure(Context context) {
		super.configure(context);
		context = new Context(context.getSubProperties(getPrefix()));
		this.watchDirectory = new File(context.getString(PROP_DIRECTORY, getDefaultDirectory()));
		this.logBaseName = context.getString(PROP_BASE_NAME, getDefaultFilename());
		this.isSequential = context.getBoolean(PROP_IS_SEQUENTIAL, true);
		logger.info("Snort Source will spool/watch - "+this.watchDirectory.getAbsolutePath()+" for Snort log files whose names start with:"+this.logBaseName);
		FileSystemManager fsMgr = null;
		try {
			fsMgr = VFS.getManager();
		} catch (FileSystemException e) {
			Throwables.propagate(e);
		}
		try {
			this.watchObject = fsMgr.resolveFile(watchDirectory.getAbsolutePath());
		} catch (FileSystemException e) {
			Throwables.propagate(e);
		}
		this.monitor = new DefaultFileMonitor(new FileListener() {

			@Override
			public void fileChanged(FileChangeEvent arg0) throws Exception {
				// ignore these
			}

			@Override
			public void fileCreated(FileChangeEvent fileEvent) throws Exception {
				if(acceptFile(fileEvent.getFile().getName().getBaseName())){					
					logger.info("Acknowledged new file:"+fileEvent.getFile().getName().getPath());
					builderFileReader(fileEvent.getFile(), true);
				}
			}

			@Override
			public void fileDeleted(FileChangeEvent arg0) throws Exception {
				// acknowledge these
			}
			
		});
		int bufferSize = context.getInteger(PROP_BUFFER_SIZE, 500);
		this.outputQueue = new ArrayBlockingQueue<MarauderIDSEvent>(bufferSize);
	}

	@Override
	public MarauderIDSEvent getEvent() throws InterruptedException {
		return outputQueue.take();
	}

	protected boolean acceptFile(String fileName) {
		if(fileName.startsWith(logBaseName)){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public synchronized void start() {
		super.start();
		if(isSequential){
			this.threadPool = Executors.newFixedThreadPool(2);
		}else{
			this.threadPool = Executors.newCachedThreadPool();
		}
		monitor.addFile(watchObject);
		monitor.setDelay(1000);
		monitor.setRecursive(false);
		monitor.start();
		logger.info("Listener activated");
		FileObject[] tempList = null;
		try {
			tempList = watchObject.getChildren();
		} catch (FileSystemException e) {
			Throwables.propagate(e);
		}		
		Arrays.sort(tempList, new Comparator<FileObject>() {
			
			@Override
			public int compare(FileObject o1, FileObject o2) {
				return o1.getName().getBaseName().compareTo(o2.getName().getBaseName());
			}
			
		});
		if(tempList.length!=0){
			logger.info("Now Spooling:"+Arrays.toString(tempList));
			processOldFiles(tempList);
			processFile(tempList[tempList.length-1], true);			
		}else{
			logger.info("No suitable files to process. A listener will be activated to watch for new Snort log files in the directory");
		}		

	}
	
	protected void processOldFiles(FileObject[] tempList) {
		for(int i=0;i<tempList.length-1;i++){
			processFile(tempList[i], false);
		}
	}
	
	protected MarauderIDSLogFileReader processFile(FileObject file, boolean isContinuous) {
		MarauderIDSLogFileReader temp = builderFileReader(file, isContinuous);
		HandlerRunTask runTempTask = new HandlerRunTask(temp);
		HandlerCloseTask closeTempTask = new HandlerCloseTask(temp);
		
		logger.info("Now processing:"+file.getName().getPath());
		getThreadPool().execute(runTempTask);
		getThreadPool().execute(closeTempTask);
		return temp;
	}
	
	protected abstract MarauderIDSLogFileReader builderFileReader(FileObject file, boolean isContinuous);
	
	/**
	 * To be used for debugging only
	 * @return output queue
	 */
	public BlockingQueue<MarauderIDSEvent> getOutputQueue() {
		return outputQueue;
	}
	
	/**
	 * @return the threadPool
	 */
	public ExecutorService getThreadPool() {
		return threadPool;
	}
	
	public abstract String getDefaultFilename();
	
	public abstract String getDefaultDirectory();
	
	public abstract String getPrefix();

	public static class HandlerCloseTask implements Runnable {

		private MarauderIDSLogFileReader reader = null;
		
		public HandlerCloseTask(MarauderIDSLogFileReader reader) {
			this.reader = reader;
		}
		
		@Override
		public void run() {			
			if(reader!=null){
				while(reader.getIdleTime()<1000*60){
					try {
						Thread.sleep(1000*10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				reader.setContinuous(false);
			}
		}
	}
	
	public static class HandlerRunTask implements Runnable {
		
		private MarauderIDSLogFileReader reader = null;
		
		public HandlerRunTask(MarauderIDSLogFileReader reader) {
			this.reader = reader;
		}
		
		@Override
		public void run() {
			try {
				reader.readFile();
			} catch (IOException e1) {
				Throwables.propagate(e1);
			}
		}
	}
}
