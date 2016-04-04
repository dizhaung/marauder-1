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
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * Implementation of how MarauderGlobalConfiguration is stored in memory
 * for quick retrival during event parsing
 * 
 * @author Ambud Sharma
 */
public class GlobalConfigImplementation implements MarauderGlobalConfig{
	
	private static final long serialVersionUID = -8961868958938530962L;
	
	private String cf1, cf2;
	private byte[] cf1B, cf2B;
	private static HashMap<String, MarauderColumnDefinition> headerDefinitionsMap;
	private static HashMap<String, MarauderColumnDefinition> columnDefinitionsMap;
	private static HashMap<Integer, MarauderEventDefinition> eventDefinitionsMap;
	private static SortedMap<String, String> contextLookupMap;
	private final Logger logger = Logger.getAnonymousLogger();
	
	/**
	 * Constructor
	 * @param cf1 column family 1 (HBase)
	 * @param cf2 column family 2 (HBase)
	 */
	public GlobalConfigImplementation(String cf1, String cf2) {
		columnDefinitionsMap = new HashMap<String, MarauderColumnDefinition>();
		eventDefinitionsMap = new HashMap<Integer, MarauderEventDefinition>();
		headerDefinitionsMap = new HashMap<String, MarauderColumnDefinition>();
		contextLookupMap = new TreeMap<String, String>();
		this.cf1 = cf1;
		this.cf2 = cf2;
		this.cf1B = cf1.getBytes();
		this.cf2B = cf2.getBytes();
	}
	
	public List<MarauderColumnDefinition> getColumnDefinitionsList(){
		return new ArrayList<MarauderColumnDefinition>(columnDefinitionsMap.values());
	}
	
	public HashMap<String, MarauderColumnDefinition> getColumnDefinitions() {
		return columnDefinitionsMap;
	}
	
	public void setColumnDefinitions(
			HashMap<String, MarauderColumnDefinition> columnDefinitions) {
		if(columnDefinitions!=null){
			GlobalConfigImplementation.columnDefinitionsMap.putAll(columnDefinitions);
		}
	}
	
	public HashMap<Integer, MarauderEventDefinition> getEventDefinitions() {
		return eventDefinitionsMap;
	}
	
	public void setEventDefinitions(
			HashMap<Integer, MarauderEventDefinition> eventDefinitions) {
		if(eventDefinitions!=null){
			GlobalConfigImplementation.eventDefinitionsMap.putAll(eventDefinitions);
		}
	}
	
	@Override
	public void printDefinitions() {
		logger.info("Default Column Definitions:");
		logger.info("Event Definitions:");
		List<MarauderEventDefinition> evVals = new ArrayList<MarauderEventDefinition>(eventDefinitionsMap.values());
		for(MarauderEventDefinition evDef:evVals){
			logger.info("\t"+evDef);
		}
		for(String ctxKeys:contextLookupMap.keySet()){
			logger.info("CtxKeyVal:"+ctxKeys+" maps to:"+contextLookupMap.get(ctxKeys));
		}
	}

	@Override
	public String getCF1() {		
		return cf1;
	}

	@Override
	public String getCF2() {
		return cf2;
	}

	@Override
	public List<MarauderColumnDefinition> getHeaderDefinitionsList() {
		return new ArrayList<MarauderColumnDefinition>(headerDefinitionsMap.values());
	}

	@Override
	public HashMap<String, MarauderColumnDefinition> getHeaderDefinitions() {		
		return headerDefinitionsMap;
	}
	
	@Override
	public SortedMap<String,String> getContextLookup() {
		return contextLookupMap;
	}

	@Override
	public byte[] getCF1Bytes() {
		return cf1B;
	}

	@Override
	public byte[] getCF2Bytes() {
		return cf2B;
	}
}
