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

public class ICMP implements TransportLayer {
	
	private byte type, code;
	private short chkSum;
	private NetworkLayer parent;
	
	public ICMP() {
	}
	
	@Override
	public void decode(DataInput di, NetworkLayer parent) throws IOException {
		this.parent = parent;
		this.type = di.readByte();
		this.code = di.readByte();
		this.chkSum = EtherFrame.readShort(di);
	}

	@Override
	public short getSourcePort() {
		return 0; //ICMP message doesn't have port
	}

	@Override
	public short getDestinationPort() {
		return 0; //ICMP message doesn't have port
	}

	@Override
	public NetworkLayer getParent() {
		return parent;
	}

	/**
	 * @return the type
	 */
	public byte getType() {
		return type;
	}

	/**
	 * @return the code
	 */
	public byte getCode() {
		return code;
	}

	/**
	 * @return the chkSum
	 */
	public short getChkSum() {
		return chkSum;
	}

}
