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

public class NumericIntEventCondition extends BaseEventCondition {
	
	private ConditionOperator operator;
	private int condition;

	public NumericIntEventCondition(MarauderEventTypes type, ConditionOperator operator, String attribute, int condition) {
		super(type, attribute);
		this.operator = operator;
		this.condition = condition;
	}
	
	@Override
	public boolean evaluate(MarauderBaseEvent event) {
		if(super.evaluate(event)) {
			int value = 0;
			try{
				value = Integer.parseInt(event.getHeaders().get(attribute));
			}catch(Exception e) {
				return false;
			}
			switch(operator) {
			case EQUALS:return value==condition;
			case GREATERTHAN:return value>condition;
			case GREATERTHANEQUALS:return value>=condition;
			case LESSTHAN:return value<condition;
			case LESSTHANEQUALS:return value<=condition;
			case NOTEQUALS:return value!=condition;
			}
			return false;
		}else {
			return false;
		}
	}

	@Override
	public String toString() {
		return super.toString()+operator+" "+condition;
	}

}
