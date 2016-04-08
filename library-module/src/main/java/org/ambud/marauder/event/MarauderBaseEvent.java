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
package org.ambud.marauder.event;

import java.util.HashMap;
import java.util.Map;

import org.ambud.marauder.configuration.MarauderParserConstants;
import org.apache.flume.Event;

public abstract class MarauderBaseEvent implements Event {

	private Map<String, String> eventHdrs = null;
	
	public MarauderBaseEvent() {
		this.eventHdrs = new HashMap<String, String>();		
		eventHdrs.put(MarauderParserConstants.MARAUDER_KEY_EVENT_TYPE, getEventType().getTypeName());
	}
	
	public MarauderBaseEvent(Event event) {
		this.eventHdrs = event.getHeaders();
	}
	
	/**
	 * Initialize headers with critical common headers
	 */
	public void initHdrs(){
		getHeaders().put(MarauderParserConstants.MARAUDER_KEY_EVENTID, Integer.toHexString(getSigID()));
		getHeaders().put(MarauderParserConstants.MARAUDER_KEY_SOURCE, Integer.toHexString(getSourceAddress()));
		getHeaders().put(MarauderParserConstants.MARAUDER_KEY_TIMESTAMP, Integer.toHexString(getTimestamp()));
	}
	
	/**
	 * @return event ID / signature ID (doesn't refer to event serial number but a unique identifier for this event signature)
	 */
	public abstract int getSigID();

	/**
	 * @return type of event
	 */
	public abstract MarauderEventTypes getEventType();
	
	/**
	 * @return hostname of event source
	 */
	public abstract int getSourceAddress();

	/**
	 * @return timestamp of the event in Epoch seconds
	 */
	public abstract int getTimestamp();
	
	@Override
	public Map<String, String> getHeaders() {		
		return eventHdrs;
	}
	
	@Override
	public void setHeaders(Map<String, String> headers) {
		if(headers!=null){
			this.eventHdrs.putAll(headers);
		}
	}
	
	@Override
	public String toString() {		
		return "Columns:"+getHeaders();
	}
}
