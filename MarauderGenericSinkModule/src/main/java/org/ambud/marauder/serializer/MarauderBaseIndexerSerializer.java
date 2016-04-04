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

import java.util.List;

import org.ambud.marauder.sink.MarauderHBaseSink;
import org.apache.flume.Event;
import org.apache.hadoop.hbase.client.Put;

/**
 * This serializer is capable of performing Secondary indexing for incoming events
 * in real-time.
 * 
 * @author Ambud Sharma
 *
 */
public abstract class MarauderBaseIndexerSerializer extends	MarauderBaseSerializer {

	public static byte[] indexColumnFamily = "i".getBytes();
	public static byte[] indexColumn = "i".getBytes();
	/**
	 * Reference to Parent sink is needed
	 * @param parentSink
	 */
	public MarauderBaseIndexerSerializer(MarauderHBaseSink parentSink) {
		super(parentSink);
	}
	
	@Override
	public void performExtendedProcessing(Put putReq, Event event)
			throws EventProcessingException {
		/*try {
			getParentSink().getIndexTable().put(generateIndexPuts(putReq, event));
		} catch (RetriesExhaustedWithDetailsException | InterruptedIOException e) {
			
		}*/
	}
	
	/**
	 * Generates puts for index table for complete indexing
	 * @param basePutReq
	 * @param event
	 * @return list of index puts
	 */
	public abstract List<Put> generateIndexPuts(Put basePutReq, Event event);
	
}
