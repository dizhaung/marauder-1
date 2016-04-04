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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ambud.marauder.configuration.MarauderParserConstants;
import org.ambud.marauder.sink.MarauderHBaseSink;
import org.apache.flume.Event;
import org.apache.hadoop.hbase.client.Put;

/**
 * Generic Indexer Serializer is capable of generating secondary indices for other columns to 
 * make querying possible for all columns. True indexer serializer scenario will demand
 * construction of more sophisticated indexing strategies than the Generic Indexer Serializer.
 * 
 * @author Ambud Sharma
 * 
 */
public class MarauderGenericIndexerSerialzier extends
		MarauderBaseIndexerSerializer {
	
	public MarauderGenericIndexerSerialzier(MarauderHBaseSink parentSink) {
		super(parentSink);
	}

	@Override
	public byte[] getRowKey(byte[] baseKey, Map<String, String> hdrs) {
		return baseKey;
	}

	@Override
	public List<Put> generateIndexPuts(Put basePutReq, Event event) {
		List<Put> putReqs = new ArrayList<Put>();
		Put tempPut = null;
		tempPut = new Put(constructIndexKey(MarauderParserConstants.MARAUDER_KEY_SOURCE, event).getBytes());
		tempPut.add(indexColumnFamily, indexColumn, basePutReq.getRow());
		putReqs.add(tempPut);
		tempPut = new Put(constructIndexKey(MarauderParserConstants.MARAUDER_KEY_EVENTID, event).getBytes());
		tempPut.add(indexColumnFamily, indexColumn, basePutReq.getRow());
		putReqs.add(tempPut);
		return putReqs;
	}
	
	public static String constructIndexKey(String attributeName, Event event){
		StringBuffer tempBuffer = new StringBuffer();
		tempBuffer.append(attributeName);
		tempBuffer.append(MarauderParserConstants.MARAUDER_KEY_DELIMITER);
		tempBuffer.append(event.getHeaders().get(attributeName));
		tempBuffer.append(MarauderParserConstants.MARAUDER_KEY_DELIMITER);
		tempBuffer.append(event.getHeaders().get(MarauderParserConstants.MARAUDER_KEY_TIMESTAMP));
		return tempBuffer.toString();
	}

	@Override
	public List<Put> processGraph(Event event) {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

}
