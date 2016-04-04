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
package org.ambud.marauder.sink;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.ambud.marauder.commons.ByteUtils;
import org.ambud.marauder.configuration.MarauderParserConstants;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;

public class MarauderTitanSink extends AbstractSink implements Configurable {

	private TitanGraph g = null;
	private int batchSize = 100;
	private int total = 0;
	
	@Override
	public Status process() throws EventDeliveryException {
		Status status = Status.READY;
		Transaction transaction = getChannel().getTransaction();
		transaction.begin();
		for(int i=0;i<batchSize;i++){
			Event event = getChannel().take();
			if(event!=null){
				storeEvent(event);
				total++;
			}
		}
		Logger.getAnonymousLogger().info("committed:"+total);
		g.commit();
		transaction.commit();
		transaction.close();
		return status;
	}

	protected void storeEvent(Event event) {
		Set<Entry<String, String>> kvp = event.getHeaders().entrySet();
		int time = Integer.parseInt(event.getHeaders().get(MarauderParserConstants.MARAUDER_KEY_TIMESTAMP), 16);
		event.getHeaders().remove(MarauderParserConstants.MARAUDER_KEY_TIMESTAMP);
		Iterator<Entry<String, String>> itr = kvp.iterator();
		byte[] rowKey = constructDefaultRowKey(event.getHeaders(), time);
		Vertex ev = g.addVertex(rowKey);
		ev.setProperty("o", time);
		ev.setProperty("s", System.currentTimeMillis());
		while(itr.hasNext()){
			Entry<String, String> entry = itr.next();
//			ev.setProperty(entry.getKey(), entry.getValue());
			Vertex v = g.addVertex(entry.getValue());
			v.setProperty("t", entry.getKey());
			v.setProperty("n", entry.getValue());
			g.addEdge(null, ev, v, entry.getKey()+"l");
//			Iterator<Vertex> i = g.query().has("t", entry.getKey()).has("n", entry.getValue()).vertices().iterator();
//			if(i.hasNext()){
//				g.addEdge(null, ev, i.next(), entry.getKey()+"l");
//			}else{
//				
//			}
		}
		
	}
	
	protected byte[] constructDefaultRowKey(Map<String, String> hdrs, int timeValue) {
		byte[] temp = ByteUtils.stringToBytes(buildBaseString(hdrs));
		byte[] base = new byte[5 + temp.length];		
		base[0] = (byte)hdrs.get(MarauderParserConstants.MARAUDER_KEY_EVENT_TYPE).charAt(0);
		System.arraycopy(
				ByteUtils.intToByteMSB(timeValue), 
				0, base, 1, 4);
		System.arraycopy(temp, 0, base, 5, temp.length);
		return base;
	}
	
	/**
	 * @param hdrs
	 * @return base key string with non compressed elements
	 */
	private String buildBaseString(Map<String, String> hdrs){
        StringBuilder builder = new StringBuilder();
		builder.append(hdrs.get(MarauderParserConstants.MARAUDER_KEY_EVENTID));
		builder.append(MarauderParserConstants.MARAUDER_KEY_DELIMITER);
		builder.append(hdrs.get(MarauderParserConstants.MARAUDER_KEY_SOURCE));
		builder.append(MarauderParserConstants.MARAUDER_KEY_DELIMITER);
        return builder.toString();
    }
	
	@Override
	public void configure(Context context) {
		Configuration conf = new BaseConfiguration();
		conf.setProperty("storage.backend","hbase");
		conf.setProperty("storage.hostname",
		context.getString("servers", "192.168.1.20"));
//		conf.setProperty("storage.index.t.backend", "lucene");
//		conf.setProperty("storage.index.t.directory", "/tmp/index");
		conf.setProperty("storage.batch-loading", "true");
		batchSize = context.getInteger("batchSize", 1000);
		g = TitanFactory.open(conf);
		g.makeKey("t").dataType(String.class).indexed(Vertex.class).make();
		g.makeKey("o").dataType(Integer.class).indexed(Vertex.class).make();
		g.makeKey("s").dataType(Long.class).indexed(Vertex.class).make();
		g.makeKey("n").dataType(String.class).indexed(Vertex.class).make();
		g.makeLabel("dil").unidirected().manyToMany().make();
		g.makeLabel("sil").unidirected().manyToMany().make();
		g.makeLabel("dpl").unidirected().manyToMany().make();
		g.makeLabel("spl").unidirected().manyToMany().make();
		g.makeLabel("isl").unidirected().manyToMany().make();
		g.makeLabel("hl").unidirected().manyToMany().make();
		g.makeLabel("evl").unidirected().manyToMany().make();
		g.makeLabel("il").unidirected().manyToMany().make();
		g.makeLabel("tl").unidirected().manyToMany().make();
		g.makeLabel("gil").unidirected().manyToMany().make();
		g.commit();
	}
	
}
