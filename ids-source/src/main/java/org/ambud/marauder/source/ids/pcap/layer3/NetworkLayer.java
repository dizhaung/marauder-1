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
import org.ambud.marauder.source.ids.pcap.layer4.ICMP;
import org.ambud.marauder.source.ids.pcap.layer4.TCP;
import org.ambud.marauder.source.ids.pcap.layer4.TransportLayer;
import org.ambud.marauder.source.ids.pcap.layer4.UDP;

public abstract class NetworkLayer {

	public enum NETWORK_LAYER_TYPE{
		IPv4,
		IPv6,
		ARP;
	}
	
	private TransportLayer transportLayer = null;
	
	public abstract NETWORK_LAYER_TYPE getType();
	public abstract Object getSourceAddr();
	public abstract Object getDestinationAddr();
	public abstract byte getVersion();
	public abstract byte getNextProto();
	public abstract EtherFrame getParent();
	public abstract void decode(DataInput di, EtherFrame parent) throws IOException;
	
	protected void decodeNextLayer(DataInput di) throws IOException {		
		switch(getNextProto()){
		case 6://TCP
			transportLayer = new TCP();
			break;
		case 17://UDP
			transportLayer = new UDP();
			break;
		case 1://ICMP
			transportLayer = new ICMP();
			break;
		default: //unsupported proto type
		}
		if(transportLayer!=null){
			transportLayer.decode(di, this);
		}
	}
	
	public TransportLayer getTransportLayer() {
		return transportLayer;
	}
	
}