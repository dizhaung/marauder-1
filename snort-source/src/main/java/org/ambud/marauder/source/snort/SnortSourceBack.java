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
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.ambud.marauder.source.ids.MarauderIDSEvent;
import org.ambud.marauder.source.ids.MarauderIDSLogFileReader;
import org.ambud.marauder.source.ids.MarauderIDSSource;
import org.ambud.marauder.source.snort.unified2.Unified2IDSFileReader;
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
public class SnortSourceBack extends MarauderIDSSource{

	private static final String PROP_IS_SEQUENTIAL = "sequential";
	private static final String PROP_BASE_NAME = "base.name";
	private static final String PROP_DEFAULT_FILENAME = "snort.log";
	private static final String PROP_DEFAULT_DIR = "/var/log/snort";
	private static final String PROP_DIRECTORY = "directory";
	private static final String PROP_PREFIX = "snort.";
	private Logger logger = Logger.getLogger(SnortSourceBack.class.getCanonicalName());
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
		context = new Context(context.getSubProperties(PROP_PREFIX));
		this.watchDirectory = new File(context.getString(PROP_DIRECTORY, PROP_DEFAULT_DIR));
		this.logBaseName = context.getString(PROP_BASE_NAME, PROP_DEFAULT_FILENAME);
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
					processFile(fileEvent.getFile(), true);
				}
			}

			@Override
			public void fileDeleted(FileChangeEvent arg0) throws Exception {
				// acknowledge these
			}
			
		});
		int bufferSize = context.getInteger("buffer.size", 500);
		this.outputQueue = new ArrayBlockingQueue<MarauderIDSEvent>(bufferSize);
	}

	@Override
	public MarauderIDSEvent getEvent() throws InterruptedException{
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
		MarauderIDSLogFileReader temp = null; 
		try {
			temp = new Unified2IDSFileReader(new File(file.getName().getPath()), isContinuous, getHostAddress(), outputQueue);
		} catch (IOException e) {				
			Throwables.propagate(e);
		}
		HandlerRunTask runTempTask = new HandlerRunTask(temp);
		HandlerCloseTask closeTempTask = new HandlerCloseTask(temp);
		
		logger.info("Now processing:"+file.getName().getPath());
		threadPool.execute(runTempTask);
		threadPool.execute(closeTempTask);
		
		return temp;
	}
	
	/**
	 * To be used for debugging only
	 * @return output queue
	 */
	public BlockingQueue<MarauderIDSEvent> getOutputQueue() {
		return outputQueue;
	}
	
	private class HandlerCloseTask implements Runnable {

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
	
	private class HandlerRunTask implements Runnable {
		
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
