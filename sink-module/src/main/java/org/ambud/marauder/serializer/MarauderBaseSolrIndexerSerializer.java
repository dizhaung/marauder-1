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

import java.io.IOException;
import java.util.Map;

import org.ambud.marauder.configuration.MarauderParserConstants;
import org.ambud.marauder.sink.MarauderHBaseSink;
import org.apache.flume.Event;
import org.apache.hadoop.hbase.client.Put;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.common.SolrInputDocument;

public abstract class MarauderBaseSolrIndexerSerializer extends MarauderBaseAggregatedSerializer {

	private ConcurrentUpdateSolrServer solrServer = null;
	
	public MarauderBaseSolrIndexerSerializer(MarauderHBaseSink parentSink, int windowSize) {
		super(parentSink, windowSize);
		solrServer = new ConcurrentUpdateSolrServer(parentSink.getSolrServerURL(), 
													parentSink.getBatchSize(), 2);
		solrServer.setConnectionTimeout(10000);
	}

	@Override
	public void performExtendedProcessing(Put putReq, Event event)
			throws EventProcessingException {
		String rowKey = new String(putReq.getRow());
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", rowKey);
		doc.addField(MarauderParserConstants.MARAUDER_KEY_SOURCE, event.getHeaders().get(MarauderParserConstants.MARAUDER_KEY_SOURCE));
		doc.addField(MarauderParserConstants.MARAUDER_KEY_EVENTID, event.getHeaders().get(MarauderParserConstants.MARAUDER_KEY_EVENTID));
		addAdditionalIndices(doc, event);
		try {
			solrServer.add(doc);
		} catch (SolrServerException | IOException e) {
			throw new EventProcessingException(e);
		}
	}
	
	protected abstract void addAdditionalIndices(final SolrInputDocument doc, final Event event) 
			throws EventProcessingException;

	@Override
	public byte[] getRowKey(byte[] baseKey, Map<String, String> hdrs) {
		return baseKey;
	}
	
	@Override
	protected void finalize() throws Throwable {
		solrServer.commit();
		super.finalize();
	}
	
}
