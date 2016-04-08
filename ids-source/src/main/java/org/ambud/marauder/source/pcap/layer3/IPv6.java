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
package org.ambud.marauder.source.pcap.layer3;

import java.io.DataInput;
import java.io.IOException;

import org.ambud.marauder.source.pcap.layer2.EtherFrame;
import org.apache.commons.codec.binary.Hex;

public class IPv6 extends NetworkLayer{

	public static int IP6_HDR_LENGTH = 40;
	public static int IP6_ADDR_LENGTH = 16;
	
	private byte version;			/* 4 bits */
	private byte trafficClass;		/* 8 bits */
	private int flowLbl; 			/* 20 bits */
	private short payloadLength;	/* 16 bits */
	private byte nextHdr, hopLimit;	/* 8 & 8 bits */
	
	private EtherFrame parent;
	
	private byte[] srcIP = new byte[IP6_ADDR_LENGTH],
			dstIP = new byte[IP6_ADDR_LENGTH]; /* 128 bit IPv6 IP addr */

	public IPv6() {
	}
	
	@Override
	public void decode(DataInput di, EtherFrame parent) throws IOException {
		this.parent = parent;
		byte[] tempBytes = new byte[4];
		di.readFully(tempBytes);
		this.version = (byte) ((tempBytes[0] >> 4) & 0xff);
		this.trafficClass = (byte) ((tempBytes[0] << 4) | ((tempBytes[1] >> 4) & 0xff));
		this.flowLbl = (byte) ((((int)(tempBytes[1] << 4) & 0xffff) << 16) | (((short)tempBytes[2] << 8) & 0xff) | (tempBytes[3] & 0xff));
		this.payloadLength = EtherFrame.readShort(di);
		this.nextHdr = di.readByte();
		this.hopLimit = di.readByte();
		di.readFully(this.srcIP);
		di.readFully(this.dstIP);
		decodeNextLayer(di);
	}

	/**
	 * @return the version
	 */
	public byte getVersion() {
		return version;
	}

	/**
	 * @return the trafficClass
	 */
	public byte getTrafficClass() {
		return trafficClass;
	}

	/**
	 * @return the flowLbl
	 */
	public int getFlowLbl() {
		return flowLbl;
	}

	/**
	 * @return the payloadLength
	 */
	public short getPayloadLength() {
		return payloadLength;
	}

	/**
	 * @return the hopLimit
	 */
	public byte getHopLimit() {
		return hopLimit;
	}

	/**
	 * @return the srcIP
	 */
	public byte[] getSrcIP() {
		return srcIP;
	}

	/**
	 * @return the dstIP
	 */
	public byte[] getDstIP() {
		return dstIP;
	}
	
	/**
	 * @return the nextHdr
	 */
	public byte getNextHdr() {
		return nextHdr;
	}
	
	@Override
	public String toString() {
		return "Version:"+version+" Traffic Class:"+trafficClass+" FlowLbl:"+flowLbl+" Src:"+Hex.encodeHexString(srcIP)+" Dst:"+Hex.encodeHexString(dstIP)+" Payload:"+payloadLength+" NxtHDR:"+nextHdr;
	}

	@Override
	public NETWORK_LAYER_TYPE getType() {
		return NETWORK_LAYER_TYPE.IPv6;
	}

	@Override
	public byte[] getSourceAddr() {
		return srcIP;
	}

	@Override
	public byte[] getDestinationAddr() {
		return dstIP;
	}

	@Override
	public byte getNextProto() {
		return nextHdr;
	}
	
	@Override
	public EtherFrame getParent() {
		return parent;
	}

}
