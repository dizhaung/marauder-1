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
package org.ambud.marauder.source.ids.pcap;

import java.io.DataInput;
import java.io.IOException;
import java.sql.Date;

public class LibpcapRecordHeader {

	private int tsSec,		/* timestamp seconds (unsigned int32)*/
				tsUsec,		/* timestamp microseconds (unsigned int32)*/
				inclLen,	/* number of octets of packet saved in file (unsigned int32)*/
				origLen;	/* actual length of packet (unsigned int32)*/
	
	public LibpcapRecordHeader(DataInput input) throws IOException {
		this.tsSec = input.readInt();
		this.tsUsec = input.readInt();
		this.inclLen = input.readInt();
		this.origLen = input.readInt();
	}

	/**
	 * @return the tsSec
	 */
	public int getTsSec() {
		return tsSec;
	}

	/**
	 * @return the tsUsec
	 */
	public int getTsUsec() {
		return tsUsec;
	}

	/**
	 * @return the inclLen
	 */
	public int getInclLen() {
		return inclLen;
	}

	/**
	 * @return the origLen
	 */
	public int getOrigLen() {
		return origLen;
	}
	
	@Override
	public String toString() {		
		return "Timestamp(s):"+new Date(1000L*(long)(0x00000000ffffffffL &(long)tsSec)).toString()+" timestamp(mu s):"+tsUsec+" packet saved bytes:"+inclLen+" orig length:"+origLen;
	}
}
