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
package org.ambud.marauder.configuration;

/**
 * Holder for Marauder Constants for default configurations/configuration lookup
 * property names
 * 
 * @author Ambud Sharma
 */
public class MarauderConstants {

	public static final String DEFAULT_TABLE_NAME = "syslogTestW";
	
	public static final String DEFAULT_COLUMN_FAMILY_1 = "l";
	
	public static final String DEFAULT_COLUMN_FAMILY_2 = "m";
	
	public static final String MARAUDER_INDEX_CF_BYTES = "r";
	
	public static final String DEFAULT_HADOOP_DIR = "/marauder/hadoop/";
	
	public static final String DEFAULT_HBASE_DIR = "/marauder/hbase/";
	
	public static final String DEFAULT_CONF_DIR_NAME = "conf";
	
	public static final String CONF_TABLE_NAME = "table.tableName";
	
	public static final String CONF_CF1_NAME = "table.cf1";
	
	public static final String CONF_CF2_NAME = "table.cf2";
	
	public static final String CONF_TABLE_WAL = "table.enableWAL";
	
	public static final boolean DEFAULT_TABLE_WAL = false;
	
	public static final String CONF_TABLE_BUFFER_SIZE = "table.bufferSize";
	
	public static final int DEFAULT_TABLE_BUFFER_SIZE = 10*1024*1024;
	
	public static final String CONF_HADOOP_DIR_PROP = "hadoopConf";
	
	public static final String CONF_HBASE_DIR_PROP = "hbaseConf";
	
	public static final String DEFAULT_EVENT_SCHEMA_FILENAME = "EventSchema.xml";
	
	public static final String DEFAULT_EVENT_SCHEMA_XSD_FILENAME = "EventSchema.xsd";
	
	public static final String MARAUDER_CONFIG_DIR = "MARAUDER_CONFIG";
	
	public static final String CONF_BATCH_SIZE = "batchSize";
	
	public static final int DEFAULT_MARAUDER_EVENT_BATCH = 5000;
	
	public static final String CONF_MARAUDER_SECONDARY_INDEXING = "secondaryIndex.enable";
	
	public static final boolean DEFAULT_MARAUDER_SECONDARY_INDEXING = false;
	
	public static final boolean DEFAULT_MARAUDER_MARAUDER_WAL = false;
	
	public static final String CONF_MARAUDER_SECONDARY_INDEXING_TYPE = "secondaryIndex.type";
	
	public static final String DEFAULT_MARAUDER_SECONDARY_INDEXING_TYPE = SecondaryIndexTypes.REALTIME_FLUME.name();
	
	public enum SecondaryIndexTypes{
		REALTIME_FLUME, REALTIME_HBASE, MAPREDUCE
	}
}
