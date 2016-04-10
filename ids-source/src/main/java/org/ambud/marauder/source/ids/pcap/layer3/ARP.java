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
package org.ambud.marauder.source.ids.pcap.layer3;

import java.io.DataInput;
import java.io.IOException;

import org.ambud.marauder.source.ids.pcap.layer2.EtherFrame;
import org.apache.commons.codec.binary.Hex;

public class ARP extends NetworkLayer{
	
	private short hardType, protoType;
	private byte hardSize, protoAddrSize;
	private short opCode;
	private byte[] senderEthAddr = new byte[EtherFrame.ETHER_ADDR_SIZE];
	private int senderIP;
	private byte[] targetEthAddr = new byte[EtherFrame.ETHER_ADDR_SIZE];
	private int targetIP;
	
	private EtherFrame parent;
	
	public ARP() {
	}

	@Override
	public void decode(DataInput di, EtherFrame parent) throws IOException {
		this.parent = parent;
		this.hardType = di.readShort();
		this.protoType = di.readShort();
		this.hardSize = di.readByte();
		this.protoAddrSize = di.readByte();
		this.opCode = di.readShort();
		di.readFully(senderEthAddr);
		this.senderIP = di.readInt();
		di.readFully(targetEthAddr);
		this.targetIP = di.readInt();
	}
	
	/**
	 * @return the hardType
	 */
	public short getHardType() {
		return hardType;
	}

	/**
	 * @param hardType the hardType to set
	 */
	public void setHardType(short hardType) {
		this.hardType = hardType;
	}

	/**
	 * @return the protoType
	 */
	public short getProtoType() {
		return protoType;
	}

	/**
	 * @param protoType the protoType to set
	 */
	public void setProtoType(short protoType) {
		this.protoType = protoType;
	}

	/**
	 * @return the hardSize
	 */
	public byte getHardSize() {
		return hardSize;
	}

	/**
	 * @param hardSize the hardSize to set
	 */
	public void setHardSize(byte hardSize) {
		this.hardSize = hardSize;
	}

	/**
	 * @return the protoAddrSize
	 */
	public byte getProtoAddrSize() {
		return protoAddrSize;
	}

	/**
	 * @param protoAddrSize the protoAddrSize to set
	 */
	public void setProtoAddrSize(byte protoAddrSize) {
		this.protoAddrSize = protoAddrSize;
	}

	/**
	 * @return the opCode
	 */
	public short getOpCode() {
		return opCode;
	}

	/**
	 * @param opCode the opCode to set
	 */
	public void setOpCode(short opCode) {
		this.opCode = opCode;
	}

	/**
	 * @return the senderEthAddr
	 */
	public byte[] getSenderEthAddr() {
		return senderEthAddr;
	}

	/**
	 * @param senderEthAddr the senderEthAddr to set
	 */
	public void setSenderEthAddr(byte[] senderEthAddr) {
		this.senderEthAddr = senderEthAddr;
	}

	/**
	 * @return the senderIP
	 */
	public int getSenderIP() {
		return senderIP;
	}

	/**
	 * @param senderIP the senderIP to set
	 */
	public void setSenderIP(int senderIP) {
		this.senderIP = senderIP;
	}

	/**
	 * @return the targetEthAddr
	 */
	public byte[] getTargetEthAddr() {
		return targetEthAddr;
	}

	/**
	 * @param targetEthAddr the targetEthAddr to set
	 */
	public void setTargetEthAddr(byte[] targetEthAddr) {
		this.targetEthAddr = targetEthAddr;
	}

	/**
	 * @return the targetIP
	 */
	public int getTargetIP() {
		return targetIP;
	}

	/**
	 * @param targetIP the targetIP to set
	 */
	public void setTargetIP(int targetIP) {
		this.targetIP = targetIP;
	}

	/**
	 * @return the ethernetAddrSize
	 */
	public static int getEthernetAddrSize() {
		return EtherFrame.ETHER_ADDR_SIZE;
	}
	
	@Override
	public String toString() {
		return "Sender:"+IPv4.intToStringIP(senderIP)+"/"+Hex.encodeHexString(senderEthAddr)
				+" Target:"+IPv4.intToStringIP(targetIP)+"/"+Hex.encodeHexString(targetEthAddr)+" Proto:"+protoType;
	}

	@Override
	public NETWORK_LAYER_TYPE getType() {
		return NETWORK_LAYER_TYPE.ARP;
	}

	@Override
	public Integer getSourceAddr() {
		return senderIP;
	}

	@Override
	public Integer getDestinationAddr() {
		return targetIP;
	}

	@Override
	public byte getVersion() {
		return 0;// not supported
	}

	@Override
	public byte getNextProto() {
		return 0;// not supported
	}

	@Override
	public EtherFrame getParent() {
		return parent;
	}

}
