package org.ambud.marauder.source.snort.unified2;

import java.io.DataInput;
import java.io.IOException;

import com.ambud.marauder.source.pcap.layer3.IPv6;

public class Unified2IDSEventv6 extends Unified2IDSEvent {
	
	private byte[] srcIP = new byte[IPv6.IP6_ADDR_LENGTH],
					dstIP = new byte[IPv6.IP6_ADDR_LENGTH];

	protected Unified2IDSEventv6(int hostAddress, int length, DataInput di) throws IOException {
		super(length, hostAddress, true);
		this.sensorID = di.readInt();
		this.eventID = di.readInt();
		this.eventSecond = di.readInt();
		this.eventMicrosecond = di.readInt();
		this.signatureID = di.readInt();
		this.generatorID = di.readInt();
		this.signatureVersion = di.readInt();
		this.classificationID = di.readInt();
		this.priorityID = di.readInt();
		di.readFully(srcIP);
		di.readFully(dstIP);
		this.srcPort = di.readShort();
		this.dstPort = di.readShort();
		this.protocol = di.readByte();
		this.impactFlag = di.readByte();
		this.impact = di.readByte();
		this.blocked = di.readByte();
	}
	
	protected Unified2IDSEventv6(int length, int hostAddress, DataInput di, boolean v2) throws IOException {
		this(length, hostAddress, di);
		//V2 events
		this.mplsLabel = di.readInt();
		this.vlanID = di.readShort();
		this.padding = di.readShort();
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

	@Override
	public int getSigID() {
		return getSignatureID();
	}

}
