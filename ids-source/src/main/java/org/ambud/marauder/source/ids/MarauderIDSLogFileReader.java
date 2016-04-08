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
package org.ambud.marauder.source.ids;

import java.io.DataInput;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

public abstract class MarauderIDSLogFileReader {

//	private Logger logger = Logger.getLogger(MarauderIDSLogFileReader.class.getCanonicalName());
	protected File file = null;
	private InputStream is = null;
	protected DataInput reader = null;
	private boolean isContinuous = false;
	private BlockingQueue<MarauderIDSEvent> outputQueueRef = null;
	private long idleTime = 0;
	private int pollInterval = 10000;
	private int hostAddress = 0;
	
	public MarauderIDSLogFileReader(File file, boolean isContinuous, int hostAddress, BlockingQueue<MarauderIDSEvent> outputQueue) throws IOException {
		this.file = file;
		this.outputQueueRef = outputQueue;
		this.is = new FileInputStream(file);		
		this.isContinuous = isContinuous;
		this.hostAddress = hostAddress;
		if(isContinuous){
			this.pollInterval = 1000;
		}
	}
	
	public abstract void preProcess() throws IOException;
	
	public void readFile() throws IOException{		
		if(!isContinuous){
			readLoop();
		}else{
			while(isContinuous){
				readLoop();						
				try {
					Thread.sleep(pollInterval);
					idleTime += pollInterval;
				} catch (InterruptedException e) {
					break;
				}
			}			
		}
		is.close();
	}

	/**
	 * @return the isContinuous
	 */
	public boolean isContinuous() {
		return isContinuous;
	}

	/**
	 * @param isContinuous the isContinuous to set
	 */
	public void setContinuous(boolean isContinuous) {
		this.isContinuous = isContinuous;
	}

	private void readLoop() throws IOException {
		try{
			preProcess();
			while(true){
				read();
			}
		}catch(EOFException e){
//			logger.info("EOF reached!");
		}
	}
	
	/**
	 * Method called in every loop cycle
	 * @param dis
	 * @throws EOFException
	 * @throws IOException 
	 */
	protected abstract void read() throws IOException;
	
	/**
	 * Close the input stream (will cause an IOException to be thrown by the readFile)
	 * @throws IOException
	 */
	public void closeStream() throws IOException{
		is.close();
	}
	
	/**
	 * @return the file
	 */
	protected File getFile() {
		return file;
	}

	/**
	 * @return the is
	 */
	protected InputStream getIs() {
		return is;
	}

	/**
	 * @return the idleTime
	 */
	public long getIdleTime() {
		return idleTime;
	}

	/**
	 * @return the outputQueueRef
	 */
	public BlockingQueue<MarauderIDSEvent> getOutputQueueRef() {
		return outputQueueRef;
	}
	
	/**
	 * @return host ipaddress
	 */
	public int getHostAddress() {
		return hostAddress;
	}
	
}
