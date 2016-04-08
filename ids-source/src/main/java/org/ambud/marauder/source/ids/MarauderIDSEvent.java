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
package org.ambud.marauder.source.ids;

import java.util.Map;

import org.ambud.marauder.event.MarauderBaseEvent;
import org.ambud.marauder.event.MarauderEventTypes;
import org.apache.commons.codec.binary.Hex;

/**
 * An abstract representation of an IDS event and an extension of MarauderBaseEvent. 
 * Network IDS events have common characteristics:
 * <ul>
 * 	<li>Event timestamp
 * 	<li>Source IP Address
 * 	<li>Source Port
 * 	<li>Destination IP Address
 * 	<li>Destination Port
 * 	<li>IDS Event ID
 * 	<li>Signature ID
 * 	<li>Generator ID (refers to IDS module generating this event)
 * </ul>
 * 
 * In addition to this, Marauder IDS event also contain information about
 * IDS Type, IDS Hostname etc. mapping which helps identifying the event
 * in the table.
 * 
 * @author Ambud Sharma
 */
public abstract class MarauderIDSEvent extends MarauderBaseEvent {
	
	public static String IDS_EVENT_SRC_ADDR = "si",
						 IDS_EVENT_SRC_PRT = "sp",
						 IDS_EVENT_DST_ADDR = "di",
						 IDS_EVENT_DST_PORT = "dp",
						 IDS_EVENT_EVD_ID = "ev",
						 IDS_EVENT_GEN_ID = "gi",
						 IDS_EVENT_IDS_TYPE = "is";
	
	/**
	 * Default constructor
	 */
	public MarauderIDSEvent() {
		super();
	}
	
	/**
	 * Explicit method call needed to construct MarauderIDSEvent headers
	 * from the concrete class. 
	 * @param ids hostname/name of the IDS sensor
	 * @param vid IDS type
	 */
	public void initHdrs(){
		super.initHdrs();
		getHeaders().put(IDS_EVENT_SRC_ADDR, Hex.encodeHexString(getSrcIP()));
		getHeaders().put(IDS_EVENT_DST_ADDR, Hex.encodeHexString(getDstIP()));
		getHeaders().put(IDS_EVENT_SRC_PRT, String.valueOf(getSrcPort()));
		getHeaders().put(IDS_EVENT_DST_PORT, String.valueOf(getDstPort()));
		getHeaders().put(IDS_EVENT_EVD_ID, String.valueOf(getEventID()));
		getHeaders().put(IDS_EVENT_GEN_ID, String.valueOf(getGeneratorID()));
		getHeaders().put(IDS_EVENT_IDS_TYPE, getIDSType());
	}
	
	/**
	 * IDS Type that is generating the event logs
	 * @return idsType
	 */
	protected abstract String getIDSType();

	@Override
	public void setHeaders(Map<String, String> headers) {
		getHeaders().putAll(headers);
	}
	
	@Override
	public MarauderEventTypes getEventType() {
		return MarauderEventTypes.IDS;
	}

	/**
	 * Byte array containing the Source IP address.
	 * Could represent IPv4/IPv6 address
	 * @return srcIP
	 */
	public abstract byte[] getSrcIP();
	/**
	 * Byte array containing the Destination IP address.
	 * Could represent IPv4/IPv6 address
	 * @return dstIP
	 */
	public abstract byte[] getDstIP();
	/**
	 * @return Source Port number 
	 */
	public abstract short getSrcPort();
	/**
	 * @return Destination Port number
	 */
	public abstract short getDstPort();
	/**
	 * @return IDS event ID
	 */
	public abstract int getEventID();
	/**
	 * @return SignatureID generating this event
	 */
	public abstract int getSignatureID();
	/**
	 * @return Module ID generating this event
	 */
	public abstract int getGeneratorID();
	
	@Override
	public String toString() {
		// for testing only; not to be used for any production computation
		return super.toString()+new String(getBody());
	}	
}
