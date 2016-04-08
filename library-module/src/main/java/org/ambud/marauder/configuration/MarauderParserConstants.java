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
 * Holder for all constants for the parser
 * 
 * @author Ambud Sharma
 */
public interface MarauderParserConstants {

	public static final String MARAUDER_EVENTS_TAG = "wevents";	
	public static final String MARAUDER_EVENT_TAG = "wevent";
	public static final String MARAUDER_EVENT_DEFAULT_ID = "default";
	
	public static final String MARAUDER_COL_TAG = "col";
	public static final String MARAUDER_COL_ID_ATTRIBUTE = "id";
	public static final String MARAUDER_COL_CONTEXT_ATTRIBUTE = "context";
	public static final String MARAUDER_COL_PRIORITY_ATTRIBUTE = "priority";
	public static final String MARAUDER_COL_TYPE_ATTRIBUTE = "type";
	public static final String MARAUDER_COL_LOC_ATTRIBUTE = "loc";
	public static final String MARAUDER_COL_INDEX_ATTRIBUTE = "index";
	public static final String MARAUDER_COL_GROUP_ATTRIBUTE = "grp";
	public static final String FLUME_COL_TIMESTAMP = "timestamp";
	
	public static final int MARAUDER_DEFAULT_RANDOM_LENGTH = 5;
	
	public static final String MARAUDER_KEY_EVENT_TYPE = "t";
	public static final String MARAUDER_KEY_DELIMITER = "#";	
	public static final String MARAUDER_KEY_RAND = "r";
	public static final String MARAUDER_KEY_SOURCE = "h";
	public static final String MARAUDER_KEY_EVENTID = "i";
	public static final String MARAUDER_KEY_TIMESTAMP = "s";
	public static final String MARAUDER_KEY_FACILITY = "f";
	public static final String MARAUDER_KEY_SEVERITY = "v";
	public static final String MARAUDER_KEY_AUDITSTATUS = "a";
	
	@Deprecated
	public static final String MARAUDER_KEY_ORIGTIMESTAMP = "o";
	public static final byte[] MARAUDER_KEY_MESSAGE = "m".getBytes();
	public static final byte[] MARAUDER_CF_HEADERS = "h".getBytes();
	public static final byte[] MARAUDER_CF_MESSAGE = "m".getBytes();
	public static final String MARAUDER_KEY_HEADER_DATEFORMAT = "EEE MMM d HH:mm:ss zzz yyyy";
	
	public static final int MARAUDER_EVENT_DEFAULT = -1;
	
	public static final String MARAUDER_CONTEXT_DEF_NAME = "name";
	public static final String MARAUDER_CONTEXT_DEF_KEY = "key";
	public static final String MARAUDER_CONTEXT_DEF_VALUE = "value";
	
	public static final String MICROSOFT_AUDIT = "audit";
	public static final String MICROSOFT_AUDIT_SUCCESS = "Audit Success";
	public static final String MICROSOFT_EVENT_HEADER_DELIMETER = "\t";
	public static final String MICROSOFT_EVENT_HEADER_TERMINATOR = "\r\n";
	
	public static boolean DEBUG = System.getenv("MARAUDER_DEBUG") != null;
	/*header array index info card
	0	-	MSWinEventLog
	1	-	SEVERITY
	2	-	EVENT TYPE
	3	-	SEQUENCE / INCREMENTAL NUMBER
	4	-	DATE
	5	-	EVENT ID
	6	-	MICROSOFT-WINDOWS-SECURITY-AUDITING			
	7	-	N/A
	8	-	AUDIT STATUS
	9	-	UNKNOWN
	10	-	EVENT SOURCE
	11	-	<NUMBER>
	12	-	ABOUT EVENT/EVENT INFO
	 */
	@Deprecated // not used since upgrade of header parsing system which is now inline with body parser. 
	//This could be used in future for syslog-ng based csv file support
	public static enum HeaderMapping{
		KEY_HOSTNAME(MARAUDER_KEY_SOURCE, 10), KEY_ORIGTIMESTAMP(MARAUDER_KEY_ORIGTIMESTAMP, 4), 
		KEY_EVENTID(MARAUDER_KEY_EVENTID, 5), KEY_AUDITSTATUS(MARAUDER_KEY_AUDITSTATUS, 9);
		
		public String key;
		public int pos;
		private HeaderMapping(String key, int pos) {
			this.key = key;
			this.pos = pos;
		}
	}

	//XPath queries
	public static final String XML_MARAUDER_BASE_XPATH = "//configuration/marauder/";
	public static final String XML_MARAUDER_COLS_COL_XPATH = "cols/col[@loc='msg']";
	public static final String XML_MARAUDER_HDR_COL_XPATH = "cols/col[@loc='hdr']";
	public static final String XML_MARAUDER_KEYS_XPATH = "keys/key";	
	public static final String XML_MARAUDER_WEVENTS_XPATH = MarauderParserConstants.MARAUDER_EVENTS_TAG+"/"
			+MarauderParserConstants.MARAUDER_EVENT_TAG;
	public static final String XML_MARAUDER_CONTEXT_DEF_XPATH = "contexts/context/def";


	//@Deprecated target event ID for critical security events
	//private int[] events = {4776, 4624, 4625, 4771, 4768, 4634, 4655, 4611};

}
