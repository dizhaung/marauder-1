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

import org.ambud.marauder.conditions.MarauderEventCondition;
import org.ambud.marauder.event.MarauderBaseEvent;
import org.ambud.marauder.event.MarauderEventTypes;

public class BaseEventCondition implements MarauderEventCondition {

	protected MarauderEventTypes type;
	protected String attribute;
	private boolean isBase;
	
	public BaseEventCondition(MarauderEventTypes type, String attribute) {
		this.type = type;
		this.attribute = attribute;
		this.isBase = (type==MarauderEventTypes.BASE)?true:false;
	}
	
	@Override
	public boolean evaluate(MarauderBaseEvent event) {
		if(!isBase && event.getEventType()!=type){
			return false;
		}
		if(!event.getHeaders().containsKey(attribute)) {
			return false;
		}else {
			return true;
		}
	}

	@Override
	public String toString() {
		return type+" "+attribute+" ";
	}
}
