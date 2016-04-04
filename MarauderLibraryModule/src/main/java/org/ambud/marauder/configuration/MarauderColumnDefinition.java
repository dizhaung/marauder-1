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

import java.util.regex.Pattern;

/**
 * Defines Column/Field all/an event. Stores the regex pattern
 * used to extract this field from the body of syslog message
 * 
 * @author Ambud Sharma 
 */
public class MarauderColumnDefinition {
	
	private String id;
	private String type;
	private int patternGrp;
	private Pattern pattern;
	private boolean index;
	
	/**
	 * Default Constructor
	 */
	public MarauderColumnDefinition() {		
	}
	
	/**
	 * Constructor
	 * @param id
	 * @param type
	 * @param patternGrp
	 * @param pattern
	 */
	public MarauderColumnDefinition(String id, String type, int patternGrp,
			String pattern) {
		this.id = id;
		this.type = type;
		this.patternGrp = patternGrp;
		this.pattern = Pattern.compile(pattern);
	}
	
	/**
	 * Constructor for cloning object
	 * @param clonable
	 */
	public MarauderColumnDefinition(MarauderColumnDefinition clonable){
		this.id = clonable.id;
		this.type = clonable.type;
		this.patternGrp = clonable.patternGrp;
		this.pattern = Pattern.compile(clonable.pattern.pattern());
	}

	/**
	 * Getter for column ID used for key mapping and column name in datastore
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter for column value type
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Getter for regex pattern group that contains field value
	 * @return grpId
	 */
	public int getPatternGrp() {
		return patternGrp;
	}

	/**
	 * Getter for regex pattern
	 * @return pattern
	 */
	public Pattern getPattern() {
		return pattern;
	}
	
	/**
	 * @return the index
	 */
	public boolean isIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(boolean index) {
		this.index = index;
	}

	@Override
	public String toString() {		
		return "Col:"+id+" Type:"+type+" Grp:"+patternGrp+" Pattern:"+pattern;
	}	
}
