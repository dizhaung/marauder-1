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
package org.ambud.marauder.serializer;

import org.ambud.marauder.sink.MarauderHBaseSink;
import org.apache.flume.EventDeliveryException;

public class MarauderTestSink extends MarauderHBaseSink {

	public MarauderTestSink() {
		super();
	}

	@Override
	public Status process() throws EventDeliveryException {
		// TODO Auto-generated method stub
		return null;
	}

}
