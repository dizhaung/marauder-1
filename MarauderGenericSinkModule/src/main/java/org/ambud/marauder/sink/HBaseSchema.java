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

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;

/**
 * This class is intended to store the HBase Schema that is loaded from
 * the Flume Sink configuration (a part of Flume's configuration) and allows
 * Marauder to be able to create your HBase Tables for you. This paves the 
 * foundation for Marauder to create secondary indices/alternate indices by creating
 * additional index tables for individual indexing.
 * 
 * @author Ambud Sharma
 *
 */
public class HBaseSchema {

	private String cf1 = null, cf2 = null;
	private String tableName = null;
	private String indexTableName = null;
	private HTable primaryTable = null;
	private HTable indexTable = null;
	
	/**
	 * Default constructor
	 */
	public HBaseSchema(){		
	}
	
	/**
	 * Validates Schema Values for null pointer/empty string and throws exception if detected
	 * @throws Exception
	 */
	public void validateSchemaValues() throws Exception{
		if((tableName==null || cf1==null || cf2==null) && (tableName.isEmpty() || cf1.isEmpty() || cf2.isEmpty())){
			throw new Exception("One of table schema key attributes is not valid");
		}
	}
	
	public void validateConnectingToTable(Configuration hconf, boolean isDisabledRealtimeIdx) throws IOException{
		HTable temp = new HTable(hconf, getTableName());		
		if(!isDisabledRealtimeIdx){
			temp = new HTable(hconf, getIndexTableName());
		}
	}
	
	/**
	 * Construct the HBase table if not present in the cluster. Also triggers 
	 * column family validation
	 * @param conf
	 * @throws IOException
	 */
	public void constructTableIfMissing(Configuration conf) throws IOException{
		if(!isTablePresent(conf)){
			HBaseAdmin admin = new HBaseAdmin(conf);
			HTableDescriptor desc = new HTableDescriptor(tableName);
			admin.createTable(desc);
			admin.close();
			primaryTable = new HTable(conf, tableName);
		}
		validateAndCreateColumnSchema();
		primaryTable.close();
	}
	
	/**
	 * Validate existence of configuration specified Column Families; if not
	 * found then construct those column families in the table
	 * @throws IOException
	 */
	protected void validateAndCreateColumnSchema() throws IOException {
		HTableDescriptor desc = primaryTable.getTableDescriptor();
		for(HColumnDescriptor colDesc:desc.getFamilies()){
			if(!colDesc.getNameAsString().equals(cf1) && !colDesc.getNameAsString().equals(cf2)){
				String columnToCreate = cf1;
				if(!colDesc.getNameAsString().equals(cf1)){
					columnToCreate = cf2;
				}
				colDesc = new HColumnDescriptor(columnToCreate);
				desc.addFamily(colDesc);
			}
		}
	}

	/**
	 * Check if table is present and return true if so else return false
	 * @param conf
	 * @return isTablePresent
	 */
	protected boolean isTablePresent(Configuration conf){
		try{
			primaryTable = new HTable(conf, tableName);
			if(primaryTable !=null){
				return true;
			}
		}catch(Exception e){
			if(e instanceof TableNotFoundException){
				return false;
			}
		}
		return false;
	}

	/**
	 * @return the primaryTable
	 */
	protected HTable getPrimaryTable() {
		return primaryTable;
	}

	/**
	 * @return the indexTable
	 */
	protected HTable getIndexTable() {
		return indexTable;
	}

	/**
	 * @return the cf1
	 */
	public String getCf1() {
		return cf1;
	}

	/**
	 * @param cf1 the cf1 to set
	 */
	public void setCf1(String cf1) {
		this.cf1 = cf1;
	}

	/**
	 * @return the cf2
	 */
	public String getCf2() {
		return cf2;
	}

	/**
	 * @param cf2 the cf2 to set
	 */
	public void setCf2(String cf2) {
		this.cf2 = cf2;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @return the indexTableName
	 */
	public String getIndexTableName() {
		return indexTableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
		this.indexTableName = tableName+"IDX";
	}
	
}
