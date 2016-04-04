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
package org.ambud.marauder.analytics.indexing;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.ambud.marauder.analytics.indexing.SecondaryIndexer;
import org.ambud.marauder.analytics.indexing.org;
import org.ambud.marauder.configuration.MarauderParserConstants;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Ambud Sharma
 *
 */
public class TestSecondaryIndexer {
	
	private Logger logger = Logger.getLogger(TestSecondaryIndexer.class.getCanonicalName());

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.ambud.marauder.analytics.indexing.flume.sink.indexing.SecondaryIndexer#SecondaryIndexer(byte[])}.
	 */
	@Test
	public void testSecondaryIndexer() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.ambud.marauder.analytics.indexing.flume.sink.indexing.SecondaryIndexer#generateIndexPutRequests(org.ambud.marauder.flume.sink.WinSyslogEvent, java.lang.String)}.
	 */
	@Test
	public void testGenerateIndexPutRequests() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.ambud.marauder.analytics.indexing.flume.sink.indexing.SecondaryIndexer#generateIndexKey(org.ambud.marauder.flume.sink.WinSyslogEvent, java.lang.String)}.
	 */
	@Test
	public void testGenerateIndexKeyWinSyslogEventString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.ambud.marauder.analytics.indexing.flume.sink.indexing.SecondaryIndexer#generateIndexKey(org.apache.hadoop.hbase.client.Result, java.lang.String)}.
	 */
	@Test
	public void testGenerateIndexKeyResultString() {
		String at = "an", av = "ashg123", tsString="6/6/2013 10:12:11", randKey="3ad24";
		String result = at+MarauderParserConstants.MARAUDER_KEY_DELIMITER+
						av+MarauderParserConstants.MARAUDER_KEY_DELIMITER+
						tsString+MarauderParserConstants.MARAUDER_KEY_DELIMITER+
						randKey;		
		logger.info(result);
		assertEquals(result, new String(SecondaryIndexer.composeIndexKey(at, av, tsString, randKey)));		
	}

	/**
	 * Test method for {@link org.ambud.marauder.analytics.indexing.flume.sink.indexing.SecondaryIndexer#compostIndexKey(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCompostIndexKey() {
		fail("Not yet implemented"); // TODO
	}

}
