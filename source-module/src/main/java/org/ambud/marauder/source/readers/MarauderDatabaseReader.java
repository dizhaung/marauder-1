/**
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
package org.ambud.marauder.source.readers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class MarauderDatabaseReader {

	private Connection dbConnection = null;
	private PreparedStatement pollingStatement = null;
	
	public MarauderDatabaseReader(Connection dbConnection,
			String queryStatement) throws Exception {
		this.dbConnection = dbConnection;
		validate();
		this.pollingStatement = dbConnection.prepareStatement(queryStatement); 
	}
	
	protected void validate() throws Exception {
		if(dbConnection == null){
			throw new Exception("DB Connection must be initialized");
		}
	}
	
	/**
	 * @return the dbConnection
	 */
	public Connection getDbConnection() {
		return dbConnection;
	}

	/**
	 * @param dbConnection the dbConnection to set
	 */
	public void setDbConnection(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}

	/**
	 * @return the pollingStatement
	 */
	public PreparedStatement getPollingStatement() {
		return pollingStatement;
	}
	
	public abstract void readDatabase() throws Exception;
	
	public abstract ResultSet getResult();
}
