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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InterruptedIOException;
import java.util.Date;
import java.util.Map;

import org.ambud.marauder.commons.NetworkUtils;
import org.ambud.marauder.event.MarauderBaseEvent;
import org.ambud.marauder.event.MarauderEventTypes;
import org.ambud.marauder.serializer.EventProcessingException;
import org.ambud.marauder.serializer.MarauderBaseAggregatedSerializer;
import org.apache.commons.codec.binary.Hex;
import org.apache.flume.Event;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ambud Sharma
 *
 */
public class TestMarauderBaseAggregatedSerializer {

	private MarauderBaseEvent testEvent = null;
	private MarauderBaseAggregatedSerializer serializer = null;
	private int time = (int) ((new Date("10/10/2013 5:23:10 EDT")).getTime()/1000);
	
	/**
	 * Setup method
	 */
	@Before
	public void setUp() {
		serializer = new MarauderBaseAggregatedSerializer(null, 60) {
			
			@Override
			public void performExtendedProcessing(Put putReq, Event event)
					throws EventProcessingException {
				//do nothing
			}
			
			@Override
			public byte[] getRowKey(byte[] baseKey, Map<String, String> hdrs) {
				return baseKey;
			}
		};
		testEvent = new MarauderBaseEvent() {
			
			@Override
			public void setBody(byte[] body) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public byte[] getBody() {
				return "Event from xyz ip address headed to abc".getBytes();
			}
			
			@Override
			public int getTimestamp() {
				return time;
			}
			
			@Override
			public int getSourceAddress() {
				return NetworkUtils.stringIPtoInt("192.168.1.1");
			}
			
			@Override
			public int getSigID() {
				return 21312;
			}
			
			@Override
			public MarauderEventTypes getEventType() {
				return MarauderEventTypes.IDS;
			}
		};
		testEvent.initHdrs();
	}
	
	/**
	 * Test method for {@link org.ambud.marauder.serializer.MarauderBaseSerializer#processEvent(org.apache.flume.Event)}.
	 * @throws InterruptedIOException 
	 * @throws EventProcessingException 
	 * @throws RetriesExhaustedWithDetailsException 
	 */
	@Test
	public void testProcessEvent() throws EventProcessingException, InterruptedIOException {
//		serializer.processEvent(testEvent);
	}

	/**
	 * Test method for {@link org.ambud.marauder.serializer.MarauderBaseSerializer#performExtendedProcessing(org.apache.hadoop.hbase.client.Put, org.apache.flume.Event)}.
	 */
	@Test
	public void testPerformExtendedProcessing() {
	}

	/**
	 * Test method for {@link org.ambud.marauder.serializer.MarauderBaseSerializer#constructDefaultRowKey(java.util.Map)}.
	 */
	@Test
	public void testConstructDefaultRowKey() {
		byte[] output = serializer.constructDefaultRowKey(testEvent.getHeaders(), time);
		assertEquals("40525671f4c0a8010100005340", Hex.encodeHexString(output));
	}

	/**
	 * Test method for {@link org.ambud.marauder.serializer.MarauderBaseSerializer#getRowKey(java.lang.String, java.util.Map)}.
	 */
	@Test
	public void testGetRowKey() {
		byte[] output = serializer.getRowKey(serializer.constructDefaultRowKey(testEvent.getHeaders(), time), testEvent.getHeaders());
		assertEquals("40525671f4c0a8010100005340", Hex.encodeHexString(output));
	}

	/**
	 * Test method for {@link org.ambud.marauder.serializer.MarauderBaseSerializer#getUUID()}.
	 */
	@Test
	public void testGetUUID() {
		String uuid = serializer.getUUID().toString();
		assertTrue(uuid.matches("[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}"));
	}

	/**
	 * Test method for {@link org.ambud.marauder.serializer.MarauderBaseSerializer#getParentSink()}.
	 */
	@Test
	public void testGetParentSink() {
	}

}
