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

import org.ambud.marauder.commons.ByteUtils;
import org.ambud.marauder.commons.TimeUtils;
import org.ambud.marauder.configuration.MarauderParserConstants;
import org.ambud.marauder.sink.MarauderHBaseSink;
import org.apache.flume.Event;
import org.apache.hadoop.hbase.client.Put;

public class MarauderGenericSerializer extends MarauderBaseSerializer{

	public MarauderGenericSerializer(MarauderHBaseSink parentSink, int windowSize) {
		super(parentSink);
	}

	@Override
	public void performExtendedProcessing(Put putReq, Event event) throws EventProcessingException {
		
	}
	
	@Override
	public byte[] getRowKey(byte[] baseKey, Map<String, String> hdrs) {
//		byte[] output = new byte[baseKey.length+4];
//		System.arraycopy(baseKey, 0, output, 0, baseKey.length);
//		System.arraycopy(ByteUtils.intToByteMSB(random.nextInt(100000)), 0, output, baseKey.length, 4);
		return baseKey;
	}

	byte[] cf = "v".getBytes();
	byte[] c = "r".getBytes();

	@Override
	public List<Put> processGraph(Event event) {
		return constructGraphDatabase(rowKey, event);
	}
	
	private String[] headers = new String[]{"si","di","dp"}; 
	private int windowSize = 60;
	
	protected List<Put> constructGraphDatabase(byte[] rowKey, Event event) {
		byte[] window = ByteUtils.intToByteMSB(
				TimeUtils.getWindowFlooredTime(
						Integer.parseInt(event.getHeaders().get(MarauderParserConstants.MARAUDER_KEY_TIMESTAMP), 16),
						windowSize*4));
		List<Put> puts = new ArrayList<>();
		for(String header:headers){
			byte[] entryKey = (event.getHeaders().get(header)).getBytes();
			byte[] key = new byte[window.length+entryKey.length+1];
			System.arraycopy(entryKey, 0, key, 0, entryKey.length);
			key[entryKey.length] = '%';
			System.arraycopy(window, 0, key, entryKey.length+1, window.length);
			Put req = new Put(key);
			req.add(cf, rowKey, header.getBytes());
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
	
	
}
