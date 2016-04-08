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

public class Unified2ExtraData {

	private int sensorID, eventID, eventSecond, type, dataType, dataLength;
	private byte[] data;
	
	public Unified2ExtraData(DataInput di) throws IOException {
		this.sensorID = di.readInt();
		this.eventID = di.readInt();
		this.eventSecond = di.readInt();
		this.type = di.readInt();
		this.dataType = di.readInt();
		this.dataLength = di.readInt();
		this.data = new byte[dataLength];
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
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the dataType
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * @return the dataLength
	 */
	public int getDataLength() {
		return dataLength;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}
	
}
