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
package org.ambud.marauder.source.ids.pcap.layer2;

import java.io.DataInput;
import java.io.IOException;

import org.ambud.marauder.source.ids.pcap.layer3.ARP;
import org.ambud.marauder.source.ids.pcap.layer3.IPv4;
import org.ambud.marauder.source.ids.pcap.layer3.IPv6;
import org.ambud.marauder.source.ids.pcap.layer3.NetworkLayer;
import org.apache.commons.codec.binary.Hex;

public class EtherFrame {
	
	public static final int ETHER_HDR_SIZE = 6+6+2;
	public static final int ETHER_HDR_VLAN_SIZE = 6+6+4+2;
	public static final int ETHER_CRC_SIZE = 4;
	
	public static final int ETHER_ADDR_SIZE = 6;	

	private byte[] dstMac = new byte[ETHER_ADDR_SIZE],
					srcMac = new byte[ETHER_ADDR_SIZE];	
	private FRAME_TYPE frameType;
	
	private short frameTyp;
	private int packetTimestamp;
	
	public short getFrameTyp() {
		return frameTyp;
	}
	
	// VLAN fields start
	// for VLAN / 802.1Q packets only	
	private byte priority, cfi;
	private short vlanID = -1;
	// VLAN fields end	
	
	private NetworkLayer networkLayer;
	
	public EtherFrame(int packetTimestamp) {
		this.packetTimestamp = packetTimestamp;
	}
		
	public void decode(DataInput di) throws IOException{
		di.readFully(this.dstMac);
		di.readFully(this.srcMac);
		this.frameTyp = extractTagType(di);
		//Data follows here
		readNetworkLayer(di);
	}

	protected short extractTagType(DataInput di) throws IOException {
		short frameType = readShort(di);
		switch(frameType){
		case (short) 0x8100: // VLAN tagged packet (most likely for an enterprise network)
			this.frameType = FRAME_TYPE.VLAN;
			short temp = readShort(di); //read the rest 2 bytes of the 802.1Q tag field
			this.priority = (byte) ((temp >> 13) & 0xff);
			this.cfi = (byte) (((temp << 3) >> 15) & 0xff);
			this.vlanID = (short) (((temp << 4) >> 4) & 0xff);
			extractTagType(di); // recurse to extract type/length info
			break;
		case 0x0800: // regular IP packet
			this.frameType = FRAME_TYPE.IPv4;
			break;
		case 0x0806: // ARP
			this.frameType = FRAME_TYPE.ARP;
			break;
		case (short) 0x86dd: // Ipv6
			this.frameType = FRAME_TYPE.IPv6;
			break;
		case (short) 0x8137: // IPeX (Novell)
			break;
		default: // 0-1500 denotes length
			this.frameType = FRAME_TYPE.LENGTH;
		}
		return frameType;
	}
	
	protected void readNetworkLayer(DataInput di) throws IOException{
		switch(frameType){
		case IPv4:networkLayer = new IPv4();
			break;
		case IPv6:networkLayer = new IPv6();
			break;
		case ARP:networkLayer = new ARP();
			break;
		case LENGTH: //802.3 packet
		case VLAN: // unsupported packet
		default:
		}
		if(networkLayer!=null){
			networkLayer.decode(di, this);
		}
	}

	/**
	 * @return the dstMac
	 */
	public byte[] getDstMac() {
		return dstMac;
	}

	/**
	 * @return the srcMac
	 */
	public byte[] getSrcMac() {
		return srcMac;
	}
	
	/**
	 * @return the frameType
	 */
	public FRAME_TYPE getFrameType() {
		return frameType;
	}

	/**
	 * @return the priority
	 */
	public byte getPriority() {
		return priority;
	}

	/**
	 * @return the cfi
	 */
	public byte getCfi() {
		return cfi;
	}

	/**
	 * @return the vlanID
	 */
	public short getVlanID() {
		return vlanID;
	}

	/**
	 * @return the nextLayer
	 */
	public NetworkLayer getNetworkLayer() {
		return networkLayer;
	}

	@Override
	public String toString() {		
		return "DstAddr:"+Hex.encodeHexString(dstMac)+" SrcAddr:"+Hex.encodeHexString(srcMac)+" FrameType:"+frameType.name()+" vlan:"+vlanID;
	}
	
	/**
	 * @return the packetTimestamp
	 */
	public int getPacketTimestamp() {
		return packetTimestamp;
	}

	/**
	 * Had to Re-define/override because of errors with LittleEndianInputStream 
	 * @param di
	 * @return short
	 * @throws IOException
	 */
	public static short readShort(DataInput di) throws IOException{
		byte[] temp = new byte[2];
		di.readFully(temp);		
		return (short) ((temp[0] << 8) | temp[1] & 0xff);
	}
	
	public static enum FRAME_TYPE{
		LENGTH,
		VLAN,
		IPv4,
		IPv6,
		ARP;
	}

}
