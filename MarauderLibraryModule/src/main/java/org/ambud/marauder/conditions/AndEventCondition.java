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
package org.ambud.marauder.conditions;

import org.ambud.marauder.event.MarauderBaseEvent;

public class AndEventCondition implements MarauderEventCondition {

	private MarauderEventCondition conditionOne;
	private MarauderEventCondition conditionTwo;
	
	public AndEventCondition(MarauderEventCondition conditionOne,
			MarauderEventCondition conditionTwo) {
		this.conditionOne = conditionOne;
		this.conditionTwo = conditionTwo;
	}

	@Override
	public boolean evaluate(MarauderBaseEvent event) {
		return conditionOne.evaluate(event) && conditionTwo.evaluate(event);
	}

	@Override
	public String toString() {
		return "\tAND "+conditionOne+"\n\t\t"+conditionTwo;
	}
}
