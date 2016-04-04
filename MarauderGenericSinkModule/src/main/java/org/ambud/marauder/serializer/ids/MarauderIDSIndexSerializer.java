package org.ambud.marauder.serializer.ids;

import java.util.ArrayList;
import java.util.List;

import org.ambud.marauder.serializer.EventProcessingException;
import org.ambud.marauder.serializer.MarauderBaseSolrIndexerSerializer;
import org.ambud.marauder.sink.MarauderHBaseSink;
import org.apache.flume.Event;
import org.apache.hadoop.hbase.client.Put;
import org.apache.solr.common.SolrInputDocument;

public class MarauderIDSIndexSerializer extends
		MarauderBaseSolrIndexerSerializer {

	public MarauderIDSIndexSerializer(MarauderHBaseSink parentSink, int windowSize) {
		super(parentSink, windowSize);
	}

	@Override
	protected void addAdditionalIndices(SolrInputDocument doc, Event event)
			throws EventProcessingException {
		String field = null;
		doc.addField(field = "di", event.getHeaders().get(field));
		doc.addField(field = "dp", inflatePortNumber(event.getHeaders().get(field)));
		doc.addField(field = "si", event.getHeaders().get(field));
		doc.addField(field = "sp", inflatePortNumber(event.getHeaders().get(field)));
		doc.addField(field = "ev", event.getHeaders().get(field));
		doc.addField(field = "gi", event.getHeaders().get(field));
		doc.addField(field = "is", event.getHeaders().get(field));
	}

	/**
	 * @param port
	 * @return inflated port Number
	 */
	private String inflatePortNumber(String port){
		int tempPort = ((int)Short.valueOf(port, 16)) & 0xffff;
		return String.valueOf(tempPort);
	}

	@Override
	public List<Put> processGraph(Event event) {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}
}
