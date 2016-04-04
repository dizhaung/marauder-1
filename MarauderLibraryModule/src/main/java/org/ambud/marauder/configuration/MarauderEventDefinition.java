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
package org.ambud.marauder.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class defines MarauderEvent which primarily comprises of:<br/>
 * 	1. Event ID<br/>
 * 	2. KeyPriority<br/>
 * 	3. Event specific column definitions<br/>
 * 
 * @author Ambud Sharma
 */
public class MarauderEventDefinition {
	
	/**
	 * Event ID of the Syslog Event received
	 */
	private int eventID;
	/**
	 * Row Key priority of the indexing keys to be used for this Event 
	 */
	private EventKey[] keyPriority;
	/**
	 * Specific column definitions applicable to this Event
	 */
	private HashMap<String, MarauderColumnDefinition> columnDefinitions;
	
	/**
	 * Constructor 
	 * Materializes the keyPriority from a priority queue to an array
	 * @param eventID
	 * @param keyPriority
	 * @param columnDefinitions
	 */
	public MarauderEventDefinition(int eventID, PriorityQueue<EventKey> keyPriority, HashMap<String, MarauderColumnDefinition> columnDefinitions) {
		super();
		this.eventID = eventID;
		this.keyPriority = keyPriority.toArray(new EventKey[1]);
		this.columnDefinitions = columnDefinitions;
	}
	
	/**
	 * Getter for event ID
	 * @return eventID
	 */
	public int getEventID() {
		return eventID;
	}
		
	/**
	 * Getter for keyPriority array
	 * @return keyPriority
	 */
	public EventKey[] getKeyPriority() {
		return keyPriority;
	}
	
	/**
	 * Getter for column definitions as HashMap
	 * @return the columnDefinitions
	 */
	public HashMap<String, MarauderColumnDefinition> getColumnDefinitions() {
		return columnDefinitions;
	}

	/**
	 * Getter for column definitions as List 
	 * Makes it easier for iteration only purpose
	 * @return columnDefinitionsList
	 */
	public List<MarauderColumnDefinition> getColumnDefinitionsList(){
		return new ArrayList<MarauderColumnDefinition>(columnDefinitions.values());
	}
	
	@Override
	public String toString() {		
		return "Id:"+eventID+" Pri:"+Arrays.toString(keyPriority)+" \n\tColDef:"+columnDefinitions;
	}
	
}