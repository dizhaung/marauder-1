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

import org.ambud.marauder.source.pcap.layer3.IPv4;

public class Unified2IDSEventv4 extends Unified2IDSEvent {

	private byte[] srcIP = new byte[IPv4.IP_ADDR_LENGTH],
			dstIP = new byte[IPv4.IP_ADDR_LENGTH];
	
	public Unified2IDSEventv4(int length, int hostAddress, DataInput di) throws IOException {
		super(length, hostAddress, false);
		this.sensorID = di.readInt();
		this.eventID = di.readInt();
		this.eventSecond = di.readInt();
		this.eventMicrosecond = di.readInt();
		this.signatureID = di.readInt();
		this.generatorID = di.readInt();
		this.signatureVersion = di.readInt();
		this.classificationID = di.readInt();
		this.priorityID = di.readInt();
		di.readFully(srcIP);
		di.readFully(dstIP);
		this.srcPort = di.readShort();
		this.dstPort = di.readShort();
		this.protocol = di.readByte();
		this.impactFlag = di.readByte();
		this.impact = di.readByte();
		this.blocked = di.readByte();
	}
	
	public Unified2IDSEventv4(int length, int hostAddress, DataInput di, boolean v2) throws IOException {
		this(length, hostAddress, di);
		//V2 events
		this.mplsLabel = di.readInt();
		this.vlanID = di.readShort();
		this.padding = di.readShort();
	}

	@Override
	public byte[] getSrcIP() {
		return srcIP;
	}

	@Override
	public byte[] getDstIP() {
		return dstIP;
	}

	@Override
	public int getSigID() {
		return getSignatureID();
	}
	
}
