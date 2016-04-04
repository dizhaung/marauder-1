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
package org.ambud.marauder.source.configuration;

import org.ambud.marauder.event.MarauderEventTypes;
import org.apache.flume.Context;

public class MarauderSourceBaseConfig {

	public static final String CONF_BASE_CONTEXT_STRING = "base.";
	public static final String CONF_BASE_PATTERN_STRING = "pattern"; //represents the base pattern for logs the system is trying to read
	public static final String CONF_BASE_SOURCE_EVENT_CLASS = "class";
	
	public String pattern = null;
	public MarauderEventTypes sourceEventClass = null;	
	
	public MarauderSourceBaseConfig(Context context) throws InvalidConfigException{
		context = new Context(context.getSubProperties(CONF_BASE_CONTEXT_STRING));
		this.pattern = context.getString(CONF_BASE_PATTERN_STRING);
		try{
			validateStringEmpty(pattern);
		}catch(NullPointerException e){
			throw new InvalidConfigException(CONF_BASE_CONTEXT_STRING+CONF_BASE_PATTERN_STRING+" must be initialized");
		}
		this.sourceEventClass = MarauderEventTypes.valueOf(context.getString(CONF_BASE_SOURCE_EVENT_CLASS));
		
	}
	
	public String getPattern() {
		return pattern;
	}

	public MarauderEventTypes getSourceEventClass() {
		return sourceEventClass;
	}

	private void validateStringEmpty(String input) throws NullPointerException{
		if(input==null || input.trim().isEmpty()){
			throw new NullPointerException();
		}
	}
	
}
