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
package org.ambud.marauder.source.ids.pcap.layer4;

import java.io.DataInput;
import java.io.IOException;

import org.ambud.marauder.source.ids.pcap.layer2.EtherFrame;
import org.ambud.marauder.source.ids.pcap.layer3.NetworkLayer;

public class UDP implements TransportLayer{

	private short srcPort, dstPort;
	private short length, chkSum;
	private NetworkLayer parent;
	
	public UDP() {
	}
	
	@Override
	public void decode(DataInput di, NetworkLayer parent) throws IOException {
		this.parent = parent;
		this.srcPort = EtherFrame.readShort(di);
		this.dstPort = EtherFrame.readShort(di);
		this.length = EtherFrame.readShort(di);
		this.chkSum = EtherFrame.readShort(di);
	}

	/**
	 * @return the srcPort
	 */
	public short getSrcPort() {
		return srcPort;
	}

	/**
	 * @return the dstPort
	 */
	public short getDstPort() {
		return dstPort;
	}

	/**
	 * @return the length
	 */
	public short getLength() {
		return length;
	}

	/**
	 * @return the chkSum
	 */
	public short getChkSum() {
		return chkSum;
	}

	@Override
	public short getSourcePort() {
		return srcPort;
	}

	@Override
	public short getDestinationPort() {
		return dstPort;
	}

	@Override
	public NetworkLayer getParent() {
		return parent;
	}
	
}
