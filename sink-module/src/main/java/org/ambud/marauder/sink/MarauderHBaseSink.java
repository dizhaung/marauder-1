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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.ambud.marauder.configuration.MarauderConstants;
import org.ambud.marauder.configuration.MarauderGlobalConfig;
import org.ambud.marauder.configuration.MarauderParserConstants;
import org.ambud.marauder.event.MarauderEventTypes;
import org.ambud.marauder.serializer.MarauderBaseSerializer;
import org.ambud.marauder.serializer.MarauderGenericSerializer;
import org.ambud.marauder.serializer.MarauderGenericSolrIndexerSerializer;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;

/**
 * Cluster sink runs on the final tier Aggregators and is responsible for 
 * publishing data to the cluster. 
 * 
 * The cluster sink uses an implementation of the MarauderGenericSerializer 
 * to create baseline indexes for the input and perform compressions for 
 * event body.
 * 
 * @author Ambud Sharma
 * 
 */
public class MarauderHBaseSink extends AbstractSink implements Configurable {

	private static final String INDEXER_URL = "index.solr.url";
	public static final String CONF_SINK_BASE = "marauder.csink.";
	public static final String CONF_SERIALIZER_LIST = "serializer.fqcn.";
	public PrintWriter pr = null;
	private AtomicInteger counter = new AtomicInteger(0);
	
	public AtomicInteger getCounter() {
		return counter;
	}

	protected Logger logger = LoggerFactory.getLogger("Sink");
	private int batchSize = 0; // to enable any batch processing
	private String solrServerURL = null;
	private HTable mainTable = null;
	private HBaseSchema schema = null;
	private Configuration hconf = null;
	private MarauderGlobalConfig config = null;
	private Map<String, MarauderBaseSerializer> serializerLookupTable = null;
	private int bufferSize = 0;
	private boolean tableWAL;
	private boolean isDisabledRealtimeIdx;
	private int aggregatorTimeWindow;
	private boolean modeGraph = false;
	
	public MarauderHBaseSink() {
		hconf = HBaseConfiguration.create();
	}
	
	@Override
	public void configure(Context context) {
		context = new Context(context.getSubProperties(CONF_SINK_BASE));
		logger.info("Attempting configuration initialization for MarauderEventSink");
		schema = new HBaseSchema();
		schema.setCf1(context.getString(MarauderConstants.CONF_CF1_NAME, MarauderConstants.DEFAULT_COLUMN_FAMILY_1));
		schema.setCf2(context.getString(MarauderConstants.CONF_CF2_NAME, MarauderConstants.DEFAULT_COLUMN_FAMILY_2));
		schema.setTableName(context.getString(MarauderConstants.CONF_TABLE_NAME, MarauderConstants.DEFAULT_TABLE_NAME));
		isDisabledRealtimeIdx = context.getBoolean(MarauderConstants.CONF_MARAUDER_SECONDARY_INDEXING, !MarauderConstants.DEFAULT_MARAUDER_SECONDARY_INDEXING);
		if(!isDisabledRealtimeIdx){
			solrServerURL = context.getString(INDEXER_URL);
			if(solrServerURL == null || solrServerURL.trim().isEmpty()){
				try {
					throw new Exception("Indexing configuration: Solr server URL missing");
				} catch (Exception e) {
					Throwables.propagate(e);
				}
			}
		}
		if(context.getBoolean("aggregator.isActive", true)){
			this.aggregatorTimeWindow = context.getInteger("aggregator.timeWindow", 60); //default window of 60 seconds
		}
		modeGraph = context.getBoolean("graph", false);
		batchSize = context.getInteger(MarauderConstants.CONF_BATCH_SIZE, MarauderConstants.DEFAULT_MARAUDER_EVENT_BATCH);	
		tableWAL = context.getBoolean(MarauderConstants.CONF_TABLE_WAL, MarauderConstants.DEFAULT_TABLE_WAL);
		this.bufferSize = context.getInteger(MarauderConstants.CONF_TABLE_BUFFER_SIZE, MarauderConstants.DEFAULT_TABLE_BUFFER_SIZE);
		String hbasePath = context.getString(MarauderConstants.CONF_HBASE_DIR_PROP, 
				MarauderConstants.DEFAULT_HBASE_DIR);
		String hadoopPath = context.getString(MarauderConstants.CONF_HADOOP_DIR_PROP, 
				MarauderConstants.DEFAULT_HADOOP_DIR);
		logger.info("Configured HBase path:"+hbasePath);
		logger.info("Configured Hadoop path:"+hadoopPath);
		hconf.addResource(new Path(hadoopPath+MarauderConstants.DEFAULT_CONF_DIR_NAME+"/"+"core-site.xml"));
		hconf.addResource(new Path(hadoopPath+MarauderConstants.DEFAULT_CONF_DIR_NAME+"/"+"hdfs-site.xml"));
		hconf.addResource(new Path(hadoopPath+MarauderConstants.DEFAULT_CONF_DIR_NAME+"/"+"mapred-site.xml"));
		hconf.addResource(new Path(hbasePath+MarauderConstants.DEFAULT_CONF_DIR_NAME+"/"+"hbase-site.xml"));
		try {
			schema.validateSchemaValues();
			logger.info("Table validation complete for Primary Table:"+schema.getTableName());			
		} catch (Exception e) {
			logger.error("Schema initialization values(table name, column fam1/2) cannot be empty", e);
			Throwables.propagate(e);
		}
		this.serializerLookupTable = new HashMap<String, MarauderBaseSerializer>();		
		try {
			configureSerializerLookupTable(context.getSubProperties(CONF_SERIALIZER_LIST));
			logger.info("Serializers loaded in memory");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			if(e instanceof ClassNotFoundException){
				logger.error("Serializer class not found.", e);
			}else{
				logger.error("Exception initializing/loading serializer", e);
			}
			Throwables.propagate(e);
		}
		logger.info("Sink indexing is "+(isDisabledRealtimeIdx?"NOT":"")+" enabled.");
		logger.info("Sink initialization complete");
		try {
			pr = new PrintWriter("./eps"+getName()+".log");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Configuration getHconf() {
		return hconf;
	}

	/**
	 * Configure lookup table for serializers supported by the Cluster Sink. A Cluster sink should be able to serialize
	 * anything and everything that comes it's way without any hickups
	 * 
	 * @param immutableMap
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected void configureSerializerLookupTable(ImmutableMap<String, String> immutableMap) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		MarauderEventTypes[] eventTypes = MarauderEventTypes.values();
		MarauderBaseSerializer defaultSerializer = (isDisabledRealtimeIdx?new MarauderGenericSerializer(this, aggregatorTimeWindow):new MarauderGenericSolrIndexerSerializer(this, aggregatorTimeWindow));
		for(MarauderEventTypes eventType:eventTypes){
			logger.info("Checking serializer for:"+eventType.name()+" events");
			if(immutableMap.containsKey(eventType.getTypeName())){				
				@SuppressWarnings("unchecked")
				Class<MarauderBaseSerializer> serializer = (Class<MarauderBaseSerializer>) Class.forName(immutableMap.get(eventType.getTypeName()));
				MarauderBaseSerializer temp = serializer.newInstance();
				temp.setParentSink(this);
				serializerLookupTable.put(eventType.getTypeName(), temp);
				logger.info("Found dedicated serializer for:"+eventType.name()+" events");
			}else{
				serializerLookupTable.put(eventType.getTypeName(), defaultSerializer);
				logger.info("Using generic serializer for:"+eventType.name()+" events");
			}
		}
	}
	
	@Override
	public Status process() throws EventDeliveryException {
		Status status = Status.READY;
		Transaction transaction = getChannel().getTransaction();
		transaction.begin();
		int i = 0;
		long time = System.currentTimeMillis();
		
		List<Put> putBatch = new ArrayList<Put>(getBatchSize());
		List<Put> putGraph = new ArrayList<>();
		for (; i < getBatchSize(); i++) {
			Event event = getChannel().take();
			if(event == null || !event.getHeaders().containsKey(MarauderParserConstants.MARAUDER_KEY_EVENT_TYPE)){
				if(event!=null){
					logger.error("Non-Marauder event must be piped through an appropriate source to get properly normalized");
				}
				status = Status.BACKOFF;
			}else{
				try {
					MarauderBaseSerializer serializer = null;
					if((serializer=serializerLookupTable.get(
							event.getHeaders().get(MarauderParserConstants.MARAUDER_KEY_EVENT_TYPE)))!=null){
						if(!modeGraph){
							putBatch.add(serializer.processEvent(event));//putBatch.add();
						}else{
							serializer.processEvent(event);
							putGraph.addAll(serializer.processGraph(event));
						}
						counter.incrementAndGet();
					}else{
						logger.error("Serializer is null");
					}
				}
				 catch (Exception e) {
					e.printStackTrace();
					handleTransactionFailure(transaction);	
					status = Status.BACKOFF;
					return status;
				}
			}
		}
		try {
			if(modeGraph){
				graphT.batch(putGraph);
				graphT.flushCommits();
			}else{
				getTable().batch(putBatch);
				getTable().flushCommits();
			}
		} catch (Exception e) {//InterruptedException | IO
			e.printStackTrace();
		}			
		transaction.commit();
		transaction.close();
		time = System.currentTimeMillis() - time;
//		logger.info("Completed:"+counter+" events in "+time+" milliseconds");
		return status;
	}
	
	@Override
	public synchronized void start() {		
		try {
			mainTable = new HTable(hconf, schema.getTableName());
			logger.info("Connected to primary table");
			mainTable.setWriteBufferSize(bufferSize);
			mainTable.setAutoFlush(false, true);
		} catch (IOException e) {
			Throwables.propagate(e);
		}		
		mainTable.setAutoFlush(false);		
		try {
			graphT = new HTable(getHconf(), "mGraph");
			graphT.setAutoFlush(false, true);
			graphT.setWriteBufferSize(1024*1024*300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		status.set(true);
		final Thread monitor = new Thread() {
			@Override
			public void run() {
				while(status.get()){
					logger.info("G:"+modeGraph+" EPS:"+counter.get());
					counter.set(0);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						status.set(false);
					}
				}
			}
		};
		monitor.start();
		super.start();
	}
	
	private HTable graphT = null;
	
	private AtomicBoolean status = new AtomicBoolean(false);
	
	@Override
	public synchronized void stop() {		
		try {
			if(mainTable!=null){
				mainTable.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		status.set(false);
		super.stop();		
	}
	
	/**
	 * @return batch size
	 */
	public int getBatchSize() {
		return batchSize;
	}
	
	/**
	 * @return the table
	 */
	public HTable getTable() {
		return mainTable;
	}

	/**
	 * @return the tableWAL
	 */
	public boolean isTableWAL() {
		return tableWAL;
	}

	/**
	 * @return the isDisabledRealtimeIdx
	 */
	public boolean isDisabledRealtimeIdx() {
		return isDisabledRealtimeIdx;
	}

	/**
	 * @return the config
	 */
	public MarauderGlobalConfig getConfig() {
		return config;
	}

	/**
	 * Gracefully handle failures
	 * @param txn
	 * @throws EventDeliveryException
	 */
	protected void handleTransactionFailure(Transaction txn)
		      throws EventDeliveryException {
		try {
		  txn.rollback();
		} catch (Throwable e) {
			logger.error("Failed to commit transaction." +
					"Transaction rolled back.", e);
			if(e instanceof Error || e instanceof RuntimeException){
		    logger.error("Failed to commit transaction." +
		    "Transaction rolled back.", e);
		    Throwables.propagate(e);
		  } else {
		    logger.error("Failed to commit transaction." +
		    "Transaction rolled back.", e);
		throw new EventDeliveryException("Failed to commit transaction." +
		    "Transaction rolled back.", e);
		    }
		  } finally {
		    txn.close();
		  }
	}

	/**
	 * @return the solrServerURL
	 */
	public String getSolrServerURL() {
		return solrServerURL;
	}

	/**
	 * @return the schema
	 */
	public HBaseSchema getSchema() {
		return schema;
	}
}
