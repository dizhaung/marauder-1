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
