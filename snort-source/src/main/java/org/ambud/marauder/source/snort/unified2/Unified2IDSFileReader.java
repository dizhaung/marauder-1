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
package org.ambud.marauder.source.snort.unified2;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ambud.marauder.source.ids.MarauderIDSEvent;
import org.ambud.marauder.source.ids.MarauderIDSLogFileReader;

/**
 * Reader for Unified2 Log file usually generated from Snort or Surricata IDS. <br>
 * 
 * Supports all of:
 * <ul>
 * 	<li>-Unified2 IPv4 Event v1
 * 	<li>-Unified2 IPv4 Event v2
 * 	<li>-Unified2 IPv6 Event v1
 * 	<li>-Unified2 IPv6 Event v2
 * 	<li>-Unified2 Packet
 * 	<li>-Unified2 Extra Data
 * </ul>
 * 
 * Compliant with Snort 2.9.5 tested as of 9/23/2013
 * 
 * @author Ambud Sharma
 */
public class Unified2IDSFileReader extends MarauderIDSLogFileReader{

	private Logger logger = Logger.getLogger(Unified2IDSFileReader.class.getCanonicalName());
	private Unified2IDSEvent currentEvent = null;
	private static boolean isPacketExtractionEnabled = false;
	private int hostAddress;
	
	public Unified2IDSFileReader(File file, boolean isContinuous, int hostAddress, BlockingQueue<MarauderIDSEvent> outputQueue) throws IOException {
		super(file, isContinuous, hostAddress, outputQueue);
		logger.fine("Is file continuous:"+isContinuous);
		this.reader = new DataInputStream(getStream());
		this.hostAddress = hostAddress;
	}
	
	@Override
	protected void read() throws IOException {		
		Unified2IDSEvent tempEvent = extractEvent();		
		if(tempEvent!=null && tempEvent!=currentEvent){
			logger.log(Level.FINEST, tempEvent.toString());
			if(currentEvent!=null){				
				try {
					getOutputQueueRef().put(currentEvent);
				} catch (InterruptedException e) {
					throw new IOException(e);
				}
			}				
			currentEvent = tempEvent;
		}
	}
	
	/**
	 * Extract and construct Unified2IDSEvent.
	 * 
	 * @return Unified2IDSEvent in case of an event or
	 * @return null in case of packet or extra-data
	 * @throws IOException
	 */
	protected Unified2IDSEvent extractEvent() throws IOException{
		Unified2IDSEvent tempEvent = null;
		int hdrType = reader.readInt();
		int length = reader.readInt();
		switch (hdrType) {
		case Unified2Constants.UNIFIED2_IDS_EVENT:tempEvent = new Unified2IDSEventv4(length, hostAddress, reader);
			break;
		case Unified2Constants.UNIFIED2_IDS_EVENT_2:tempEvent = new Unified2IDSEventv4(length, hostAddress, reader, true);
			break;
		case Unified2Constants.UNIFIED2_IDS_EVENT_V6:tempEvent = new Unified2IDSEventv6(length, hostAddress, reader);
			break;
		case Unified2Constants.UNIFIED2_IDS_EVENT_V6_2:tempEvent = new Unified2IDSEventv6(length, hostAddress, reader, true);
			break;
		case Unified2Constants.UNIFIED2_IDS_PACKET:
			if(isPacketExtractionEnabled){
				Unified2Packet pkt = new Unified2Packet(length, reader);
				if(currentEvent!=null){
					currentEvent.getPackets().add(pkt);
				}
			}else{
				reader.skipBytes(length);
			}
			break;
		case Unified2Constants.UNIFIED2_IDS_EXTRA_DATA:
			
			Unified2ExtraData exData = new Unified2ExtraData(reader);
			if(currentEvent!=null){
				if(currentEvent.getExData()==null){
					currentEvent.setExData(new LinkedList<Unified2ExtraData>());
				}
				currentEvent.getExData().add(exData);
			}
			break;
		default://invalid code, input corrupt exception
			break;
		}
		return tempEvent;
	}

	/**
	 * @return the isPacketExtractionEnabled
	 */
	public static boolean isPacketExtractionEnabled() {
		return isPacketExtractionEnabled;
	}

	@Override
	public void preProcess() throws IOException {
		currentEvent = extractEvent();
	}
	
}
