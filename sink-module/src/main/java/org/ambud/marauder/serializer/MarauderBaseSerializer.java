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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.ambud.marauder.commons.ByteUtils;
import org.ambud.marauder.configuration.MarauderGlobalConfig;
import org.ambud.marauder.configuration.MarauderParserConstants;
import org.ambud.marauder.sink.MarauderHBaseSink;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.flume.Event;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;

/**
 * Base serializer abstracts common processing for Cluster Sink serialization
 * and performs the common tasks like putting data into HBase, capability to
 * build default key externalize rowKey computation, extending extra processing
 * to the concrete classes.
 * 
 * 
 * @author Ambud Sharma
 *
 */
public abstract class MarauderBaseSerializer implements MarauderParentSerializer {

	private MarauderHBaseSink parentSink = null;

	public MarauderBaseSerializer() {
	}

	/**
	 * Initialize the parent sink reference used for getting reference to
	 * primary table for performing the puts/commits
	 * 
	 * @param parentSink
	 */
	public MarauderBaseSerializer(MarauderHBaseSink parentSink) {
		this.parentSink = parentSink;
	}

	byte[] rowKey = null;

	// @SuppressWarnings("deprecation")
	@Override
	public Put processEvent(Event event)
			throws EventProcessingException, RetriesExhaustedWithDetailsException, InterruptedIOException {
		Set<Entry<String, String>> kvp = event.getHeaders().entrySet();
		// Iterator<Entry<String, String>> itr = kvp.iterator();
		rowKey = getRowKey(constructDefaultRowKey(event.getHeaders(), 0), event.getHeaders());
		Put putReq = new Put(rowKey);
		// while(itr.hasNext()){
		// Entry<String, String> entry = itr.next();
		// if(entry.getValue()!=null){
		//
		// }
		// }
		putReq.add(MarauderParserConstants.MARAUDER_CF_HEADERS, "v".getBytes(),
				event.getHeaders().toString().getBytes());
		// if(event.getBody()!=null){
		// putReq.add(MarauderParserConstants.MARAUDER_CF_MESSAGE,
		// MarauderParserConstants.MARAUDER_KEY_MESSAGE,
		// compressEventBody(event.getBody()));
		// }
		performExtendedProcessing(putReq, event);
		return putReq;
	}

	public abstract List<Put> processGraph(Event event);

	/**
	 * Compress event body
	 * 
	 * @param body
	 * @return compressedBody
	 */
	protected byte[] compressEventBody(byte[] body) {
		return "".getBytes();
	}

	/**
	 * Delegate additional processing to concrete class
	 * 
	 * @param putReq
	 * @param event
	 */
	public abstract void performExtendedProcessing(Put putReq, Event event) throws EventProcessingException;

	/**
	 * Generates default rowkey with newest 1st logic order for HBase storage
	 * and searching
	 * 
	 * @param hdrs
	 * @param timeValue
	 * @return
	 */
	protected byte[] constructDefaultRowKey(Map<String, String> hdrs, long timeValue) {
		byte[] temp = ByteUtils.stringToBytes(buildBaseString(hdrs));
		byte[] base = new byte[9 + temp.length];
		timeValue = System.currentTimeMillis();
		// TODO fix logic
		byte[] time = ByteBufferUtil.bytes(timeValue).array();
		// base[0] = (byte) random.nextInt(10);
		base[0] = (byte) hdrs.get(MarauderParserConstants.MARAUDER_KEY_EVENT_TYPE).charAt(0);
		System.arraycopy(time, 2, base, 1, time.length - 2);
		System.arraycopy(temp, 0, base, 9, temp.length);
		// System.out.println(Arrays.toString(time)+" "+timeValue+"
		// "+ByteBufferUtil.toLong(ByteBuffer.wrap(time)));
		return base;
	}

	private Random random = new Random();

	/**
	 * @param hdrs
	 * @return base key string with non compressed elements
	 */
	private String buildBaseString(Map<String, String> hdrs) {
		StringBuilder builder = new StringBuilder();
		builder.append(MarauderParserConstants.MARAUDER_KEY_DELIMITER);
		builder.append(hdrs.get(MarauderParserConstants.MARAUDER_KEY_EVENTID));
		builder.append(MarauderParserConstants.MARAUDER_KEY_DELIMITER);
		builder.append(hdrs.get(MarauderParserConstants.MARAUDER_KEY_SOURCE));
		builder.append(MarauderParserConstants.MARAUDER_KEY_DELIMITER);
		builder.append(random.nextInt(10000));
		return builder.toString();
	}

	/**
	 * @return computed row key
	 */
	public abstract byte[] getRowKey(byte[] baseKey, Map<String, String> hdrs);

	/**
	 * @return random UUID to be appended for row indexing
	 */
	protected UUID getUUID() {
		return UUID.randomUUID();
	}

	/**
	 * @return the config
	 */
	public MarauderGlobalConfig getConfig() {
		return parentSink.getConfig();
	}

	/**
	 * @return the table
	 */
	public HTable getTable() {
		return parentSink.getTable();
	}

	/**
	 * @param parentSink
	 */
	public void setParentSink(MarauderHBaseSink parentSink) {
		this.parentSink = parentSink;
	}

	/**
	 * @return the tableWAL
	 */
	public boolean isTableWAL() {
		return parentSink.isTableWAL();
	}

	/**
	 * @return the isDisabledRealtimeIdx
	 */
	public boolean isDisabledRealtimeIdx() {
		return parentSink.isDisabledRealtimeIdx();
	}

	/**
	 * @return the parentSink
	 */
	protected MarauderHBaseSink getParentSink() {
		return parentSink;
	}

}
