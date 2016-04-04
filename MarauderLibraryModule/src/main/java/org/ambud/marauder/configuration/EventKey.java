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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class describing EventKey structure in Event Schema 
 * 
 * @author Ambud Sharma
 */
public class EventKey implements Serializable{
	
	private static final long serialVersionUID = -7657417137623159563L;
	
	private String name;
	private String key;
	private int priority;
	private List<EventKeyPattern> keyParts;	
	
	/**
	 * Constructor
	 * @param name
	 * @param key
	 * @param priority
	 */
	public EventKey(String name, String key, int priority) {
		super();
		this.name = name;
		this.key = key;
		this.priority = priority;
		this.keyParts = new ArrayList<EventKeyPattern>();		
		String[] keyParts = key.split(String.valueOf(MarauderParserConstants.MARAUDER_KEY_DELIMITER));
		for(String keyPart:keyParts){
			String temp = "\\b"+keyPart+"\\b";
			this.keyParts.add(new EventKeyPattern(keyPart, Pattern.compile(temp)));
		}
	}
	
	/**
	 * Getter for keyname
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for this key
	 * @return key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Getter for priority of this key
	 * @return priority
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * @return the keyParts
	 */
	public List<EventKeyPattern> getKeyParts() {
		return keyParts;
	}
	
	@Override
	public String toString() {	
		return "Key:"+key+" Prio:"+priority;
	}
	
	/**
	 * Defines EventKeyPatter
	 *   
	 * @author Ambud Sharma
	 */
	public static class EventKeyPattern{
		
		private String colID;
		private Pattern compiledRegex;
		
		/**
		 * Constructor
		 * @param colID
		 * @param compiledRegex
		 */
		public EventKeyPattern(String colID, Pattern compiledRegex) {
			super();
			this.colID = colID;
			this.compiledRegex = compiledRegex;
		}
		/**
		 * Getter for Column ID used for identifying key keypart
		 * @return the colID
		 */
		public String getColID() {
			return colID;
		}
		
		/**
		 * Getter for compiled regex during initialization
		 * @return the compiledRegex
		 */
		public Pattern getCompiledRegex() {
			return compiledRegex;
		}
		
	}
}