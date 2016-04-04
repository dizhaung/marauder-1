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
package org.ambud.marauder.source.ids;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.apache.commons.codec.binary.Hex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestIDSEvent {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMarauderIDSEvent() {
		MarauderIDSEvent event = new ConcreteTestIDSEvent((int)(System.currentTimeMillis()/1000), 
										101, 
										23212, 
										2, 
										(short)34212, 
										(short)80, 
										new byte[2], 
										ByteBuffer.allocate(4).putInt(1695609641).array(), 
										ByteBuffer.allocate(4).putInt(1695609642).array());
		assertNotNull(event);
	}

	@Test
	public void testInitHdrs() {
		MarauderIDSEvent event = new ConcreteTestIDSEvent((int)(System.currentTimeMillis()/1000), 
				101, 
				23212, 
				2, 
				(short)34212, 
				(short)80, 
				new byte[2], 
				ByteBuffer.allocate(4).putInt(1695609641).array(), 
				ByteBuffer.allocate(4).putInt(1695609642).array());
		event.initHdrs();
		assertEquals(event.getHeaders().get(MarauderIDSEvent.IDS_EVENT_SRC_ADDR), Hex.encodeHexString(ByteBuffer.allocate(4).putInt(1695609641).array()));
		assertEquals(event.getHeaders().get(MarauderIDSEvent.IDS_EVENT_DST_ADDR), Hex.encodeHexString(ByteBuffer.allocate(4).putInt(1695609642).array()));
		assertEquals(event.getHeaders().get(MarauderIDSEvent.IDS_EVENT_DST_PORT), String.valueOf(80));
		assertEquals(event.getHeaders().size(), 11);
	}

	@Test
	public void testGetHeaders() {
		MarauderIDSEvent event = new ConcreteTestIDSEvent((int)(System.currentTimeMillis()/1000), 
				101, 
				23212, 
				2, 
				(short)34212, 
				(short)80, 
				new byte[2], 
				ByteBuffer.allocate(4).putInt(1695609641).array(), 
				ByteBuffer.allocate(4).putInt(1695609642).array());		
		assertEquals(1, event.getHeaders().size());
		event.initHdrs();
		assertEquals(11, event.getHeaders().size());
	}

	@Test
	public void testSetHeaders() {
		MarauderIDSEvent event = new ConcreteTestIDSEvent((int)(System.currentTimeMillis()/1000), 
				101, 
				23212, 
				2, 
				(short)34212, 
				(short)80, 
				new byte[2], 
				ByteBuffer.allocate(4).putInt(1695609641).array(), 
				ByteBuffer.allocate(4).putInt(1695609642).array());
		assertEquals(1, event.getHeaders().size());
		event.initHdrs();
		assertEquals(11, event.getHeaders().size());
		event.setHeaders(new HashMap<String, String>(){
			private static final long serialVersionUID = 1L;
			{put("a", "b");}});
		assertEquals(event.getHeaders().size(), 12);
	}

}
