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

public class IPv4 extends NetworkLayer{

	public static final int IP_HDR_LENGTH = 20;
	public static final int IP_ADDR_LENGTH = 4;
	
	private byte version, hdrLength;
	private byte typeOfService;
	private short totalLength, ident;
	private byte flgs;
	private short fragOffset;
	private byte ttl, proto;
	private short chkSum;
	private int srcIP, dstIP;
	
	private EtherFrame parent;
	
	public IPv4() {
	}
	
	@Override
	public void decode(DataInput di, EtherFrame parent) throws IOException {
		this.parent = parent;
		byte temp = di.readByte();
		this.version = (byte)((temp >> 4) & 0xff);
		//all below for IPv4
		this.hdrLength = (byte)(((temp << 4) & 0xff) >> 4);
		this.typeOfService = di.readByte();
		this.totalLength = EtherFrame.readShort(di);
		this.ident = EtherFrame.readShort(di);
		short tempFlgOff = EtherFrame.readShort(di);
		this.flgs = (byte) ((tempFlgOff >> 13) & 0xff);
		this.fragOffset = (short) (((tempFlgOff << 3) >> 3) & 0xff);
		this.ttl = di.readByte();
		this.proto = di.readByte();
		this.chkSum = EtherFrame.readShort(di);
		this.srcIP = di.readInt();
		this.dstIP = di.readInt();
		if(hdrLength > 5){
			// options are present, skip options for now
			// options = hdrLength*4 - 5*4 = hdrLength*4 - 20 
			di.skipBytes((hdrLength-5));
		}
		decodeNextLayer(di);
	}

	/**
	 * @return the version
	 */
	public byte getVersion() {
		return version;
	}

	/**
	 * @return the hdrLength
	 */
	public byte getHdrLength() {
		return hdrLength;
	}

	/**
	 * @return the typeOfService
	 */
	public byte getTypeOfService() {
		return typeOfService;
	}

	/**
	 * @return the totalLength
	 */
	public short getTotalLength() {
		return totalLength;
	}

	/**
	 * @return the ident
	 */
	public short getIdent() {
		return ident;
	}

	/**
	 * @return the flgs
	 */
	public byte getFlgs() {
		return flgs;
	}

	/**
	 * @return the fragOffset
	 */
	public short getFragOffset() {
		return fragOffset;
	}

	/**
	 * @return the ttl
	 */
	public byte getTtl() {
		return ttl;
	}
	
	public byte getNxtProto() {		
		return proto;
	}

	/**
	 * @return the chkSum
	 */
	public short getChkSum() {
		return chkSum;
	}

	/**
	 * @return the srcIP
	 */
	public int getSrcIP() {
		return srcIP;
	}

	/**
	 * @return the dstIP
	 */
	public int getDstIP() {
		return dstIP;
	}

	@Override
	public String toString() {
		return "Version:"+version+" Hdr:"+hdrLength+" Src:"+intToStringIP(srcIP)+" Dst:"+intToStringIP(dstIP)+" Proto:"+proto;
	}
	
	public static String intToStringIP(int ip){
		return ((ip >> 24) & 0xff) + "."+
				((ip >> 16) & 0xff) + "."+
				((ip >> 8) & 0xff) + "."+
				((ip >> 0) & 0xff);
	}

	@Override
	public NETWORK_LAYER_TYPE getType() {
		return NETWORK_LAYER_TYPE.IPv4;
	}

	@Override
	public Integer getSourceAddr() {
		return srcIP;
	}

	@Override
	public Integer getDestinationAddr() {
		return dstIP;
	}

	@Override
	public byte getNextProto() {
		return proto;
	}

	@Override
	public EtherFrame getParent() {
		return parent;
	}

}
