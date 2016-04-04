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
package org.ambud.marauder.source.pcap;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.LittleEndianDataInputStream;

/**
 * Object representation of libpcap file header. 
 * 
 * Format source: Wireshark documentation 
 * {@link http://wiki.wireshark.org/Development/LibpcapFileFormat}
 * 
 * This format is v2.4 as of 9/4/2013
 * 
 * @author Ambud Sharma
 */
public class LibpcapGlobalHeader {

	private int magicNumber;					/* magic number (unsigned long32)*/
	private int versionMajor, 					/* major version number (unsigned long16)*/
					versionMinor;				/* minor version number (unsigned long16)*/
	private int thisZone;						/* GMT to local correction (signed long32)*/
	private int sigFigs,						/* accuracy of timestamps (unsigned long32)*/
					snaplen,					/* max length of captured packets, in octets (unsigned long32)*/
					network;					/* data link type (unsigned long32)*/
	private boolean isSwapped;					/* computed from magic number and check endianess of the file-data */
	
	public LibpcapGlobalHeader(InputStream inputStream) throws IOException {
		this.magicNumber |= inputStream.read() << 24;
		this.magicNumber |= inputStream.read() << 16;
		this.magicNumber |= inputStream.read() << 8;
		this.magicNumber |= inputStream.read();
		this.isSwapped = (this.magicNumber == 0xd4c3b2a1)?true:false;
		DataInput temp = null;
		if(isSwapped){
			temp = new LittleEndianDataInputStream(inputStream);
			this.versionMajor = temp.readUnsignedShort();
			this.versionMinor = temp.readUnsignedShort();
			this.thisZone = temp.readInt();
			this.sigFigs = temp.readInt();
			this.snaplen = temp.readInt();
			this.network = temp.readInt();
		}else{
			temp = new DataInputStream(inputStream);
			this.versionMajor = temp.readUnsignedShort();
			this.versionMinor = temp.readUnsignedShort();
			this.thisZone = temp.readInt();
			this.sigFigs = temp.readInt();
			this.snaplen = temp.readInt();
			this.network = temp.readInt();
		}
	}
	
	/**
	 * @return the isSwapped
	 */
	protected boolean isSwapped() {
		return isSwapped;
	}

	/**
	 * @return the magicNumber
	 */
	protected int getMagicNumber() {
		return magicNumber;
	}

	/**
	 * @return the versionMajor
	 */
	protected int getVersionMajor() {
		return versionMajor;
	}

	/**
	 * @return the versionMinor
	 */
	protected int getVersionMinor() {
		return versionMinor;
	}

	/**
	 * @return the thisZone
	 */
	protected int getThisZone() {
		return thisZone;
	}

	/**
	 * @return the sigFigs
	 */
	protected int getSigFigs() {
		return sigFigs;
	}

	/**
	 * @return the snaplen
	 */
	protected int getSnaplen() {
		return snaplen;
	}

	/**
	 * @return the network
	 */
	protected int getNetwork() {
		return network;
	}
	
	@Override
	public String toString() {
		return "Magic Number:"+Integer.toHexString(magicNumber)+" isSwapped:"+isSwapped+" Major_V:"+versionMajor+" Minor_V:"+versionMinor+" Zone:"+thisZone+" Time Resolution:"+sigFigs+" Snap Length:"+snaplen+" Network Link Type:"+network;
	}
	
}
