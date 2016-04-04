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

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.ambud.marauder.commons.ByteUtils;
import org.ambud.marauder.commons.TimeUtils;
import org.ambud.marauder.configuration.MarauderParserConstants;
import org.ambud.marauder.sink.MarauderHBaseSink;
import org.apache.flume.Event;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;

/**
 * Base serializer abstracts common processing for Cluster Sink 
 * serialization and performs the common tasks like putting data into HBase,
 * capability to build default key externalize rowKey computation, 
 * extending extra processing to the concrete classes. 
 * 
 * The aggregated serializer groups similar events together in HBase rows thereby maximizing
 * throughput performance by reducing the number of individual rows that have to be traversed each time
 * a query is executed. Since Columns in HBase are sorted lexicographically. We leverage that feature to
 * maximize throughput by bucketing events together in row buckets.
 * 
 * We create time window buckets for similar events / events of same type e.g. IDS events
 * and add then to a time window bucket. The time window is configurable between a range of 30 seconds
 * to 5 minutes in an increment of 10 seconds. We do not allow a window bigger than 5 minutes primarily
 * because of the amount of events that could potentially get clogged in the system.
 * 
 * @author Ambud Sharma
 *
 */
public abstract class MarauderBaseAggregatedSerializer extends MarauderBaseSerializer{

	private int windowSize;
		
	public MarauderBaseAggregatedSerializer() {
	}
	/**
	 * Initialize the parent sink reference used for getting reference to 
	 * primary table for performing the puts/commits
	 * @param parentSink
	 */
	public MarauderBaseAggregatedSerializer(MarauderHBaseSink parentSink, int windowSize) {
		super(parentSink);
		this.windowSize = windowSize;
		
	}

	byte[] rowKey = null;
	byte[] column = null;
	int time = 0;
	short offset = 0;
	
	@Override
	public Put processEvent(Event event) throws EventProcessingException, RetriesExhaustedWithDetailsException, InterruptedIOException {
		time = Integer.parseInt(event.getHeaders().get(MarauderParserConstants.MARAUDER_KEY_TIMESTAMP), 16);
		rowKey = getRowKey(constructDefaultRowKey(event.getHeaders(), time), 
				event.getHeaders());		
//		System.out.println(new Date((long)time*1000));
		Put putReq = new Put(rowKey);
		offset = TimeUtils.getWindowOffsetTime(time, windowSize);
		column = new byte[6];
		System.arraycopy(ByteUtils.shortToByteMSB(offset), 0, column, 0, 2);
		System.arraycopy(ByteUtils.intToByteMSB(random.nextInt(1000000)), 0, column, 2, 4);
		putReq.add(MarauderParserConstants.MARAUDER_CF_HEADERS, column, event.getHeaders().toString().getBytes());
		performExtendedProcessing(putReq, event);
		return putReq;
	}
	
	private Random random = new Random();
	/**
	 * Generates default rowkey with newest 1st logic order for HBase
	 * storage and searching
	 * 
	 * @param hdrs
	 * @param eventId
	 * @return rowKey
	 */
//	@Override
	protected byte[] constructDefaultRowKey(Map<String, String> hdrs, int timestamp) {
		byte[] extendedKey = buildExtendedKey(hdrs);
		byte[] base = new byte[5 + extendedKey.length];		
		base[0] = (byte)hdrs.get(MarauderParserConstants.MARAUDER_KEY_EVENT_TYPE).charAt(0);
		byte[] time = ByteUtils.intToByteMSB(
				TimeUtils.getWindowFlooredTime(
						timestamp, windowSize));
		System.arraycopy(
				time, 
				0, base, 1, 4);
		System.arraycopy(extendedKey, 0, base, 5, extendedKey.length);
		return base;
	}
	
	byte[] cf = "v".getBytes();
	byte[] c = "r".getBytes();

	@Override
	public List<Put> processGraph(Event event) {
		return constructGraphDatabase(rowKey, column, event);
	}
	
	private String[] headers = new String[]{"si","di","dp"}; 
	
	protected List<Put> constructGraphDatabase(byte[] rowKey, byte[] column, Event event) {
		byte[] combinedKey = new byte[rowKey.length+column.length+1];
		byte[] window = ByteUtils.intToByteMSB(
				TimeUtils.getWindowFlooredTime(
						time, windowSize*4));
		System.arraycopy(rowKey, 0, combinedKey, 0, rowKey.length);
		combinedKey[rowKey.length] = '%';
		System.arraycopy(column, 0, combinedKey, rowKey.length+1, column.length);
		List<Put> puts = new ArrayList<>();
		for(String header:headers){
			byte[] entryKey = (event.getHeaders().get(header)).getBytes();
			byte[] key = new byte[window.length+entryKey.length+1];
			System.arraycopy(entryKey, 0, key, 0, entryKey.length);
			key[entryKey.length] = '%';
			System.arraycopy(window, 0, key, entryKey.length+1, window.length);
			Put req = new Put(key);
			req.add(cf, combinedKey, header.getBytes());
			puts.add(req);
		}
		
		/*while(headers.hasNext()){
			Entry<String, String> entry = headers.next();
			byte[] entryKey = (entry.getValue()).getBytes();
			byte[] key = new byte[window.length+entryKey.length+1];
			System.arraycopy(entryKey, 0, key, 0, entryKey.length);
			key[entryKey.length] = '%';
			System.arraycopy(window, 0, key, entryKey.length+1, window.length);
			Put req = new Put(key);
			req.setWriteToWAL(false);
			req.add(cf, combinedKey, entry.getKey().getBytes());
			puts.add(req);
		}*/
		return puts;
	}
	
	/**
	 * @param hdrs
	 * @return base key string with non compressed elements
	 */
	private byte[] buildExtendedKey(Map<String, String> hdrs){
		byte[] extentedKey = new byte[8];		
		byte[] sourceAddress = ByteUtils.intToByteMSB((int) Long.parseLong(hdrs.get(MarauderParserConstants.MARAUDER_KEY_SOURCE), 16));
		System.arraycopy(sourceAddress, 0, extentedKey, 0, sourceAddress.length);
		byte[] eventID = ByteUtils.intToByteMSB(Integer.parseInt(hdrs.get(MarauderParserConstants.MARAUDER_KEY_EVENTID), 16));
		System.arraycopy(eventID, 0, extentedKey, sourceAddress.length, eventID.length);
        return extentedKey;
    }	
	
}
