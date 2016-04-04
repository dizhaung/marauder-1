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
package org.ambud.marauder.source.liveFeed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flume.ChannelException;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.instrumentation.SourceCounter;
import org.apache.flume.source.AvroSource;
import org.apache.flume.source.avro.AvroFlumeEvent;
import org.apache.flume.source.avro.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarauderLFSource extends AvroSource {

	private static final Logger logger = LoggerFactory
		      .getLogger(AvroSource.class);
	private SourceCounter sourceCounter;
	
	@Override
	public void configure(Context context) {
		
		super.configure(context);
	}
	
	/**
	  * Helper function to convert a map of CharSequence to a map of String.
	  */
	private static Map<String, String> toStringMap(
		Map<CharSequence, CharSequence> charSeqMap) {
	    Map<String, String> stringMap =
	        new HashMap<String, String>();
	    for (Map.Entry<CharSequence, CharSequence> entry : charSeqMap.entrySet()) {
	      stringMap.put(entry.getKey().toString(), entry.getValue().toString());
	    }
	    return stringMap;
	}
	
	@Override
	  public Status append(AvroFlumeEvent avroEvent) {
	    logger.debug("Avro source {}: Received avro event: {}", getName(),
	        avroEvent);
	    sourceCounter.incrementAppendReceivedCount();
	    sourceCounter.incrementEventReceivedCount();

	    Event event = EventBuilder.withBody(avroEvent.getBody().array(),
	        toStringMap(avroEvent.getHeaders()));

	    try {
	      getChannelProcessor().processEvent(event);
	    } catch (ChannelException ex) {
	      logger.warn("Avro source " + getName() + ": Unable to process event. " +
	          "Exception follows.", ex);
	      return Status.FAILED;
	    }

	    sourceCounter.incrementAppendAcceptedCount();
	    sourceCounter.incrementEventAcceptedCount();

	    return Status.OK;
	  }

	  @Override
	  public Status appendBatch(List<AvroFlumeEvent> events) {
	    logger.debug("Avro source {}: Received avro event batch of {} events.",
	        getName(), events.size());
	    sourceCounter.incrementAppendBatchReceivedCount();
	    sourceCounter.addToEventReceivedCount(events.size());

	    List<Event> batch = new ArrayList<Event>();

	    for (AvroFlumeEvent avroEvent : events) {
	      Event event = EventBuilder.withBody(avroEvent.getBody().array(),
	          toStringMap(avroEvent.getHeaders()));

	      batch.add(event);
	    }

	    try {
	      getChannelProcessor().processEventBatch(batch);
	    } catch (Throwable t) {
	      logger.error("Avro source " + getName() + ": Unable to process event " +
	          "batch. Exception follows.", t);
	      if (t instanceof Error) {
	        throw (Error) t;
	      }
	      return Status.FAILED;
	    }
	    sourceCounter.incrementAppendBatchAcceptedCount();
	    sourceCounter.addToEventAcceptedCount(events.size());
	    return Status.OK;
	  }
	
}
