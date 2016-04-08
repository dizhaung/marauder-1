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

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import org.ambud.marauder.source.ids.MarauderIDSEvent;
import org.ambud.marauder.source.ids.MarauderIDSLogFileReader;
import org.ambud.marauder.source.pcap.layer2.EtherFrame;

import com.google.common.io.LittleEndianDataInputStream;

/**
 * 
 * Format source: Wireshark documentation 
 * 
 * This format is v2.4 as of 9/4/2013
 * @author Ambud Sharma
 *
 */
public class LibpcapFileReader extends MarauderIDSLogFileReader{
	// Format source: Wireshark documentation http://wiki.wireshark.org/Development/LibpcapFileFormat
	private int loopCounter = 1;
	private LibpcapGlobalHeader fileHeader = null;
	private LibpcapRecordHeader tempRecordHeader = null;
	
	public LibpcapFileReader(File file, boolean isContinuous, int hostAddress, BlockingQueue<MarauderIDSEvent> outputQueue) throws IOException {
		super(file, isContinuous, hostAddress, outputQueue); //libpcap files cannot be continuous		
	}
	
	@Override
	public void preProcess() throws IOException {
		fileHeader = new LibpcapGlobalHeader(getIs());
		if(fileHeader.isSwapped()){
			this.reader = new LittleEndianDataInputStream(getIs());
		}else{
			this.reader = new DataInputStream(getIs());
		}
	}
	
	@Override
	protected void read() throws IOException {
		//read stream after the file header has been extracted in the constructor
		tempRecordHeader = new LibpcapRecordHeader(reader);
		byte[] raw = new byte[tempRecordHeader.getInclLen()];
		reader.readFully(raw);
		ByteArrayInputStream bis = new ByteArrayInputStream(raw);
		readPacket(bis, tempRecordHeader);		
		bis.close();
		loopCounter++;
	}
	
	protected void readPacket(InputStream is, LibpcapRecordHeader recordHeader) throws IOException{
		DataInput di = new DataInputStream(is);
		EtherFrame frame = new EtherFrame(recordHeader.getTsSec());
		frame.decode(di);
//		getOutputQueueRef().add(getIDSEventFromPacket(frame));
		System.out.println(frame.getNetworkLayer().toString());
	}

	/**
	 * @return the loopCounter
	 */
	protected int getLoopCounter() {
		return loopCounter;
	}

	protected MarauderIDSEvent getIDSEventFromPacket(final EtherFrame frame){
		MarauderIDSEvent event = new MarauderIDSEvent() {
			
			@Override
			public void setBody(byte[] body) {
				//does nothing
			}
			
			@Override
			public byte[] getBody() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getTimestamp() {
				return frame.getPacketTimestamp();
			}
			
			@Override
			public short getSrcPort() {
				return (frame.getNetworkLayer().getTransportLayer()!=null)?frame.getNetworkLayer().getTransportLayer().getSourcePort():0;
			}
			
			@Override
			public byte[] getSrcIP() {
				return null;//(frame.getNetworkLayer().getVersion()==4)?null:(byte[]) frame.getNetworkLayer().getSourceAddr();
			}
			
			@Override
			public int getSignatureID() {
				return ((int)frame.getNetworkLayer().getVersion())<<4+frame.getNetworkLayer().getNextProto();
			}
			
			@Override
			public int getGeneratorID() {
				return 0;
			}
			
			@Override
			public int getEventID() {
				return ((int)frame.getNetworkLayer().getVersion())<<4+frame.getNetworkLayer().getNextProto();
			}
			
			@Override
			public short getDstPort() {
				return (frame.getNetworkLayer().getTransportLayer()!=null)?frame.getNetworkLayer().getTransportLayer().getDestinationPort():0;
			}
			
			@Override
			public byte[] getDstIP() {
				return null;//(frame.getNetworkLayer().getVersion()==4)?null:(byte[]) frame.getNetworkLayer().getDestinationAddr();
			}

			@Override
			public int getSigID() {
				return -1;
			}
			
			@Override
			public int getSourceAddress() {
				return 0;
			}
			
			@Override
			protected String getIDSType() {
				return "snort";
			}
		};
		return event;
	}
	
	/**
	 * @return the fileHeader
	 */
	protected LibpcapGlobalHeader getFileHeader() {
		return fileHeader;
	}	
	
}
