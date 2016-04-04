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
package org.ambud.marauder.source.pcap.layer4;

import java.io.DataInput;
import java.io.IOException;

import org.ambud.marauder.source.pcap.layer2.EtherFrame;
import org.ambud.marauder.source.pcap.layer3.NetworkLayer;

public class TCP implements TransportLayer{

	private short srcPort, dstPort;
	private int seqNumber, ackNumber;
	private byte hlen, reserved, flgs;
	private short winSize, chkSum, urgPtr;
	private NetworkLayer parent;
	
	public TCP() {
	}
	
	@Override
	public void decode(DataInput di, NetworkLayer parent) throws IOException {
		this.parent = parent;
		this.srcPort = EtherFrame.readShort(di);
		this.dstPort = EtherFrame.readShort(di);
		this.seqNumber = di.readInt();
		this.ackNumber = di.readInt();
		byte[] temp = new byte[2];
		this.hlen = (byte) ((temp[0] >> 4) & 0xff);
//		this.reserved = (byte) (((temp[0] << 4) & 0xff) | ((temp[1] >> 6) & 0xff));
		this.flgs = (byte) (((temp[1] << 2) & 0xff) >> 2);
		this.winSize = EtherFrame.readShort(di);
		this.chkSum = EtherFrame.readShort(di);
		this.urgPtr = EtherFrame.readShort(di);
		if(hlen > 5){
			// ignore tcp options
			di.skipBytes((hlen-5)*4);
		}
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
	 * @return the seqNumber
	 */
	public int getSeqNumber() {
		return seqNumber;
	}

	/**
	 * @return the ackNumber
	 */
	public int getAckNumber() {
		return ackNumber;
	}

	/**
	 * @return the hlen
	 */
	public byte getHlen() {
		return hlen;
	}

	/**
	 * @return the reserved
	 */
	public byte getReserved() {
		return reserved;
	}

	/**
	 * @return the flgs
	 */
	public byte getFlgs() {
		return flgs;
	}

	/**
	 * @return the winSize
	 */
	public short getWinSize() {
		return winSize;
	}

	/**
	 * @return the chkSum
	 */
	public short getChkSum() {
		return chkSum;
	}

	/**
	 * @return the urgPtr
	 */
	public short getUrgPtr() {
		return urgPtr;
	}

	@Override
	public short getSourcePort() {
		return srcPort;
	}

	@Override
	public short getDestinationPort() {
		return dstPort;
	}
	
	/**
	 * Return Parent
	 * @return
	 */
	public NetworkLayer getParent() {
		return parent;
	}
	
	@Override
	public String toString() {
		return "SrcPort:"+((int)srcPort & 0xffff)+" DstPort:"+((int)dstPort & 0xffff);
	}
	
}
