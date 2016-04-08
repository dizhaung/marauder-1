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
package org.ambud.marauder.event.taxonomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ambud.marauder.configuration.MarauderParserConstants;
import org.ambud.marauder.event.MarauderEventTypes;

public abstract class EventAttributeMap {

	private Map<MarauderEventTypes, Map<String, MarauderEventAttribute>> attributeMap=null;
	
	public EventAttributeMap() {
		this.attributeMap = new HashMap<MarauderEventTypes, Map<String, MarauderEventAttribute>>();
		initBase();
	}
	
	protected void initBase() {
		HashMap<String, MarauderEventAttribute> eventAttributeMap = new HashMap<>();
		attributeMap.put(MarauderEventTypes.BASE, eventAttributeMap);
		eventAttributeMap.put("timestamp", new MarauderEventAttribute(
				UUID.randomUUID().toString(), 
				MarauderParserConstants.MARAUDER_KEY_TIMESTAMP, 
				"timestamp", 
				MarauderEventTypes.BASE, 
				true,
				MarauderAttributeValueType.HEX_INT));
		eventAttributeMap.put("sigID", new MarauderEventAttribute(
				UUID.randomUUID().toString(), 
				MarauderParserConstants.MARAUDER_KEY_EVENTID, 
				"eventID", 
				MarauderEventTypes.BASE, 
				true,
				MarauderAttributeValueType.HEX_INT));
		eventAttributeMap.put("type", new MarauderEventAttribute(
				UUID.randomUUID().toString(), 
				MarauderParserConstants.MARAUDER_KEY_EVENT_TYPE, 
				"type", 
				MarauderEventTypes.BASE, 
				true,
				MarauderAttributeValueType.STRING));
		eventAttributeMap.put("source", new MarauderEventAttribute(
				UUID.randomUUID().toString(), 
				MarauderParserConstants.MARAUDER_KEY_SOURCE, 
				"source", 
				MarauderEventTypes.BASE, 
				true,
				MarauderAttributeValueType.HEX_INT));
	}
	
	protected void initDatabase() {
		MarauderEventTypes[] types = MarauderEventTypes.values();
		for(MarauderEventTypes type:types){
			if(type==MarauderEventTypes.BASE){
				continue;
			}
			List<MarauderEventAttribute> attributeList = new ArrayList<>();
			HashMap<String, MarauderEventAttribute> eventAttributeMap = new HashMap<>();
			attributeMap.put(type, eventAttributeMap);
			for(MarauderEventAttribute attribute:attributeList){
				eventAttributeMap.put(attribute.getAttributeAlias(), attribute);
			}
		}
	}

	/**
	 * @return the attributeMap
	 */
	public Map<MarauderEventTypes, Map<String, MarauderEventAttribute>> getAttributeMap() {
		return attributeMap;
	}
	
}
