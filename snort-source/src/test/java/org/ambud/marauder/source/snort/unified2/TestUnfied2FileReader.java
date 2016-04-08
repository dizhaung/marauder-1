package org.ambud.marauder.source.snort.unified2;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import org.ambud.marauder.commons.NetworkUtils;
import org.ambud.marauder.source.ids.MarauderIDSEvent;
import org.ambud.marauder.source.snort.unified2.Unified2IDSFileReader;
import org.junit.Before;
import org.junit.Test;

public class TestUnfied2FileReader {

	private static final Logger logger = Logger.getLogger(TestUnfied2FileReader.class.getCanonicalName());
	private String testFileName = "./src/test/data/merged.log.1379730311";
	private int hostAddress = 0;
	
	@Before
	public void setUp() {
		hostAddress = NetworkUtils.stringIPtoInt("127.0.1.1");
	}
	
	@Test
	public void testRead() {
//		fail("Not yet implemented");
	}

	@Test
	public void testUnified2IDSFileReader() throws IOException {
		Unified2IDSFileReader reader = new Unified2IDSFileReader(new File(testFileName), false, hostAddress, new LinkedBlockingQueue<MarauderIDSEvent>());
		reader.closeStream();
	}

	@Test
	public void testReadFile() throws IOException {
		logger.info("Now reading:"+testFileName);
		ArrayBlockingQueue<MarauderIDSEvent> events = new ArrayBlockingQueue<MarauderIDSEvent>(100);
		Unified2IDSFileReader reader = new Unified2IDSFileReader(new File(testFileName), false, hostAddress, events);
		reader.readFile();
		for(MarauderIDSEvent event:events){
			event.initHdrs();
			logger.info("Event list:"+event.toString());
		}
		reader.closeStream();
	}

	@Test
	public void testGetOutputQueueRef() {
//		fail("Not yet implemented");
	}

}
