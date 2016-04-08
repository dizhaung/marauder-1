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

import static org.junit.Assert.*;

import java.io.InterruptedIOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ambud.marauder.configuration.MarauderParserConstants;
import org.ambud.marauder.event.MarauderEventTypes;
import org.ambud.marauder.serializer.EventProcessingException;
import org.ambud.marauder.serializer.MarauderBaseSerializer;
import org.apache.flume.Event;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Windows
 *
 */
public class TestMarauderBaseSerializer {

	private Event testEvent = null;
	private MarauderBaseSerializer serializer = null;
	
	/**
	 * Setup method
	 */
	@Before
	public void setUp() {
		serializer = new MarauderBaseSerializer(null) {
			
			@Override
			public void performExtendedProcessing(Put putReq, Event event)
					throws EventProcessingException {
				//do nothing
			}
			
			@Override
			public byte[] getRowKey(byte[] baseKey, Map<String, String> hdrs) {
				return baseKey;
			}

			@Override
			public List<Put> processGraph(Event event) {
				// TODO Auto-generated method stub
				return new ArrayList<>();
			}
		};
		testEvent = new Event() {
			
			private Map<String, String> headers = new HashMap<String, String>() {
				{
					put(MarauderParserConstants.MARAUDER_KEY_EVENT_TYPE, MarauderEventTypes.IDS.getTypeName());
					put(MarauderParserConstants.MARAUDER_KEY_EVENTID, "21312");
					put(MarauderParserConstants.MARAUDER_KEY_SOURCE, "test.marauder.local");
					put(MarauderParserConstants.MARAUDER_KEY_TIMESTAMP, "53b4133a");
				}
			};
			
			private byte[] testBody = "Event from xyz ip address headed to abc".getBytes();
			
			@Override
			public void setHeaders(Map<String, String> headers) {
				headers.putAll(headers);
			}
			
			@Override
			public void setBody(byte[] body) {
				// does nothing
			}
			
			@Override
			public Map<String, String> getHeaders() {
				return headers;
			}
			
			@Override
			public byte[] getBody() {
				return testBody;
			}
		};
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
		String output = new String(serializer.constructDefaultRowKey(testEvent.getHeaders(), 0));
		System.out.println("default key:"+output);
//		assertEquals("@"+",K��"+"21312#test.marauder.local#", output);
	}

	/**
	 * Test method for {@link org.ambud.marauder.serializer.MarauderBaseSerializer#getRowKey(java.lang.String, java.util.Map)}.
	 */
	@Test
	public void testGetRowKey() {
		String output = new String(serializer.getRowKey(serializer.constructDefaultRowKey(testEvent.getHeaders(), 0), testEvent.getHeaders()), Charset.forName("ISO-8859-1"));
//		assertEquals("@"+",K��"+"21312#test.marauder.local#", output);
	}

	/**
	 * Test method for {@link org.ambud.marauder.serializer.MarauderBaseSerializer#getUUID()}.
	 */
	@Test
	public void testGetUUID() {
		String uuid = serializer.getUUID().toString();
		System.out.println("UUID:"+uuid);
		assertTrue(uuid.matches("[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}"));
	}

	/**
	 * Test method for {@link org.ambud.marauder.serializer.MarauderBaseSerializer#getParentSink()}.
	 */
	@Test
	public void testGetParentSink() {
	}

}
