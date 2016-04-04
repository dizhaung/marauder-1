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
package org.ambud.marauder.conditions.expressions;

import org.ambud.marauder.conditions.ConditionOperator;
import org.ambud.marauder.event.MarauderBaseEvent;
import org.ambud.marauder.event.MarauderEventTypes;

public class StringEventCondition extends BaseEventCondition {
	
	private ConditionOperator operator;
	private String regex;
	
	public StringEventCondition(MarauderEventTypes type, ConditionOperator operator, String attribute, String pattern) {
		super(type, attribute);
		this.operator = operator;
		this.regex = pattern;
	}

	@Override
	public boolean evaluate(MarauderBaseEvent event) {
		if(!event.getHeaders().containsKey(attribute)) {
			return false;
		}else {
			String val = (String)event.getHeaders().get(attribute);
			switch(operator){
			case EQUALS:return val.equalsIgnoreCase(regex);
			case NOTEQUALS:return !val.equalsIgnoreCase(regex);
			case GREATERTHAN:return val.compareTo(regex)>0;
			case GREATERTHANEQUALS:return val.compareTo(regex)>=0;
			case LESSTHAN:return val.compareTo(regex)<0;
			case LESSTHANEQUALS:return val.compareTo(regex)<=0;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return attribute+" "+operator.name()+" "+regex;
	}

}
