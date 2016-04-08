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

import java.io.DataInput;
import java.io.IOException;

/**
 * Snort Unified2 Packet Object representation. 
 * 
 * Refer to Snort Manual/Development section for more details.
 *  
 * @author Ambud Sharma
 */
public class Unified2Packet {

	private int length;	
	private int sensorID,
				eventID,
				eventSecond,
				eventMicrosecond,
				linkType,
				packetLength;
	private byte[] data;
	
	public Unified2Packet(int length, DataInput di) throws IOException {
		this.length = length;
		if(!Unified2IDSFileReader.isPacketExtractionEnabled()){
			this.data = new byte[this.length];
			di.readFully(data);
		}else{
			this.sensorID = di.readInt();
			this.eventID = di.readInt();
			this.eventSecond = di.readInt();
			this.eventMicrosecond = di.readInt();
			this.linkType = di.readInt();
			this.packetLength = di.readInt();
			this.data = new byte[this.packetLength];
			di.readFully(data);
		}
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the sensorID
	 */
	public int getSensorID() {
		return sensorID;
	}

	/**
	 * @return the eventID
	 */
	public int getEventID() {
		return eventID;
	}

	/**
	 * @return the eventSecond
	 */
	public int getEventSecond() {
		return eventSecond;
	}

	/**
	 * @return the eventMicrosecond
	 */
	public int getEventMicrosecond() {
		return eventMicrosecond;
	}

	/**
	 * @return the linkType
	 */
	public int getLinkType() {
		return linkType;
	}

	/**
	 * @return the packetLength
	 */
	public int getPacketLength() {
		return packetLength;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}
	
}
