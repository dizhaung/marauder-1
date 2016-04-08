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
package org.ambud.marauder.source.pcap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

import org.ambud.marauder.commons.NetworkUtils;
import org.ambud.marauder.source.ids.MarauderIDSEvent;
import org.ambud.marauder.source.pcap.LibpcapFileReader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLibpcapFileReader {
	
	private String testPcapFileName = "SampleCaptures/test2.pcap";
	private int ipAddress = 0;

	@Before
	public void setUp() throws Exception {
		String hostAddress = InetAddress.getLocalHost().getHostAddress();
		int ipAddress = NetworkUtils.stringIPtoInt(hostAddress);
	}

//	@Test
	public void testReadLoop() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testLibpcapFileReader() throws IOException {
		LibpcapFileReader reader = new LibpcapFileReader(new File(testPcapFileName), false, ipAddress, new ArrayBlockingQueue<MarauderIDSEvent>(5000));
		assertNotNull(reader);
		reader.closeStream();
	}

	@Test
	public void testReadFile() throws IOException {
		LibpcapFileReader reader = new LibpcapFileReader(new File(testPcapFileName), false, ipAddress, new ArrayBlockingQueue<MarauderIDSEvent>(5000));
		reader.readFile();
		assertNotNull(reader.getFileHeader());
		assertEquals(reader.getFileHeader().getVersionMajor(), 2);
		assertEquals(reader.getFileHeader().getVersionMinor(), 4);
		assertTrue(reader.getFileHeader().getSnaplen()>=65535);
		reader.closeStream();
	}

}
