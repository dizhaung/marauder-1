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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

/**
 * Interface for GlobalConfiguration of Events and Column Definitions
 * 
 * @author Ambud Sharma
 */
public interface MarauderGlobalConfig extends Serializable{

	/**
	 * Getter for MarauderEventDefinition as hash
	 * @return marauderEventDefinition
	 */
	public HashMap<Integer, MarauderEventDefinition> getEventDefinitions();
	
	/**
	 * Getter for MarauderColumnDefinition as hash
	 * @return marauderColumnDefinitions
	 */
	public HashMap<String, MarauderColumnDefinition> getColumnDefinitions();
	
	/**
	 * Getter for Global Column Definitions as list
	 * @return marauderColumnDefinitions
	 */
	public List<MarauderColumnDefinition> getColumnDefinitionsList();
	
	/**
	 * Getter for Marauder Header Definition as hash
	 * @return marauderHeaderDefinitions
	 */
	public HashMap<String, MarauderColumnDefinition> getHeaderDefinitions();
	
	/**
	 * Getter for Header Column Definitions as list
	 * @return marauderHeaderDefinitions
	 */
	public List<MarauderColumnDefinition> getHeaderDefinitionsList();
	
	/**
	 * Getter for event context lookup hash
	 * @return
	 */
	public SortedMap<String, String> getContextLookup();
	
	/**
	 * Print all configuration definitions (for debug/test purposes)
	 */
	public void printDefinitions();
	
	/**
	 * Getter for name of 1st column family
	 * @return cf1
	 */
	public String getCF1();
	
	/**
	 * Get as bytes to reduce conversion everytime column family is needed
	 * @return cf1 bytes
	 */
	public byte[] getCF1Bytes();
	
	/**
	 * Getter for name of 2nd column family
	 * @return cf2
	 */
	public String getCF2();
	
	/**
	 * Get as bytes to reduce conversion everytime column family is needed
	 * @return cf2 bytes
	 */
	public byte[] getCF2Bytes();
}
