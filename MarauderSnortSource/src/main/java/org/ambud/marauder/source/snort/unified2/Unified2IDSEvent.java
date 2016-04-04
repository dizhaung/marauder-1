/**
 * 
 */
package org.ambud.marauder.source.snort.unified2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.ambud.marauder.source.ids.MarauderIDSEvent;

/**
 * Snort Unified2 IPv4 IDS Event reader (supports both v1 and v2)
 * 
 * v1
 * sensor id 4 bytes
 * event id 4 bytes
 * event second 4 bytes
 * event microsecond 4 bytes
 * signature id 4 bytes
 * generator id 4 bytes
 * signature revision 4 bytes
 * classification id 4 bytes
 * priority id 4 bytes
 * ip source 4 bytes /16 for v6
 * ip destination 4 bytes/16 for v6
 * source port/icmp type 2 bytes
 * dest. port/icmp code 2 bytes
 * protocol 1 byte
 * impact flag 1 byte
 * impact 1 byte
 * blocked 1 byte
 * 
 * v2
 * mpls label 4 bytes
 * vlan id 2 bytes
 * padding 2 bytes
 * 
 * @author Ambud Sharma
 */
public abstract class Unified2IDSEvent extends MarauderIDSEvent{
	
	private static final String TYPE = "snort";
	protected boolean isIP6;
	protected int length;
	protected int sensorID, eventID, eventSecond, 
				eventMicrosecond, signatureID, generatorID,
				signatureVersion, classificationID, priorityID;
	protected short srcPort, dstPort;
	protected byte protocol, impactFlag, impact, blocked;
	//for v2 events
	protected int mplsLabel;
	protected short vlanID, padding;
	protected Queue<Unified2Packet> packets = new LinkedList<Unified2Packet>();
	protected List<Unified2ExtraData> exData = null;
	protected int hostAddress;
	
	protected Unified2IDSEvent(int length, int hostAddress, boolean isIP6) throws IOException {
		this.length = length;
		this.isIP6 = isIP6;
		this.hostAddress = hostAddress;
	}
	
	/**
	 * @return the src IP
	 */
	public abstract byte[] getSrcIP();
	
	/**
	 * @return the dst IP
	 */
	public abstract byte[] getDstIP();

	/**
	 * @return the sensorID
	 */
	public int getSensorID() {
		return sensorID;
	}

	/**
	 * @return the eventID
	 */
	public int getEventID() {
		return eventID;
	}
	
	/* (non-Javadoc)
	 * @see org.ambud.marauder.source.ids.MarauderIDSEventBucket#getTimestamp()
	 */
	@Override
	public int getTimestamp(){
		return eventSecond;
	}

	/**
	 * @return the eventSecond
	 */
	public int getEventSecond() {
		return eventSecond;
	}

	/**
	 * @return the eventMicrosecond
	 */
	public int getEventMicrosecond() {
		return eventMicrosecond;
	}

	/**
	 * @return the signatureID
	 */
	public int getSignatureID() {
		return signatureID;
	}

	/**
	 * @return the generatorID
	 */
	public int getGeneratorID() {
		return generatorID;
	}

	/**
	 * @return the signatureVersion
	 */
	public int getSignatureVersion() {
		return signatureVersion;
	}

	/**
	 * @return the classificationID
	 */
	public int getClassificationID() {
		return classificationID;
	}

	/**
	 * @return the priorityID
	 */
	public int getPriorityID() {
		return priorityID;
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
	 * @return the protocol
	 */
	public byte getProtocol() {
		return protocol;
	}

	/**
	 * @return the impactFlag
	 */
	public byte getImpactFlag() {
		return impactFlag;
	}

	/**
	 * @return the impact
	 */
	public byte getImpact() {
		return impact;
	}

	/**
	 * @return the blocked
	 */
	public byte getBlocked() {
		return blocked;
	}

	/**
	 * @return the mplsLabel
	 */
	public int getMplsLabel() {
		return mplsLabel;
	}

	/**
	 * @return the vlanID
	 */
	public short getVlanID() {
		return vlanID;
	}

	/**
	 * @return the padding
	 */
	public short getPadding() {
		return padding;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the isI
	 * P6
	 */
	public boolean isIP6() {
		return isIP6;
	}

	/**
	 * @return the packets
	 */
	public Queue<Unified2Packet> getPackets() {
		return packets;
	}

	/**
	 * @param packets the packets to set
	 */
	public void setPackets(Queue<Unified2Packet> packets) {
		this.packets = packets;
	}

	/**
	 * @return the exData
	 */
	public List<Unified2ExtraData> getExData() {
		return exData;
	}
	
	protected byte[] extractPacketsToByteArray() throws IOException{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Iterator<Unified2Packet> iterator = packets.iterator();
		while(iterator.hasNext()){
			output.write(iterator.next().getData());
		}
		return output.toByteArray();
	}

	/**
	 * @param exData the exData to set
	 */
	public void setExData(List<Unified2ExtraData> exData) {
		this.exData = exData;
	}
	
	@Override
	public int getSourceAddress() {
		return hostAddress;
	}
	
	@Override
	public byte[] getBody() {
		byte[] tempAry = null;
		try {
			tempAry = extractPacketsToByteArray();
		} catch (IOException e) {
			// ignore
		}
		return tempAry;
	}
	
	@Override
	public void setBody(byte[] body) {
		// does nothing
	}
	
	@Override
	protected String getIDSType() {
		return TYPE;
	}

	
}
