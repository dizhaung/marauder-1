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
import java.sql.ResultSet;

public class MarauderPollingDatabaseReader extends MarauderDatabaseReader {
	
	private long pollingTimer = 0;
	private ResultSet outputResultSet = null;

	public MarauderPollingDatabaseReader(Connection dbConnection,
			String queryStatement) throws Exception {
		super(dbConnection, queryStatement);
	}

	@Override
	public void readDatabase() throws Exception {
		while(true){
			executeQuery();
			try {
				Thread.sleep(pollingTimer);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	protected void executeQuery() throws Exception {
		if((outputResultSet = getPollingStatement().executeQuery())==null){
			throw new Exception("Query execution wasn't successful");
		}
	}

	@Override
	public synchronized ResultSet getResult() {
		return outputResultSet;
	}

}
