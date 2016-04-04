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

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Config parser is responsible for generating MarauderGlobalConfiguration
 * 
 * @author Ambud Sharma
 */
public class MarauderConfigParser {
		
	private MarauderGlobalConfig configHolder;
	private XPath xpath = XPathFactory.newInstance().newXPath();
	private final Logger logger = Logger.getLogger(MarauderConfigParser.class.getCanonicalName());
	private String xmlConfigFile = null, xmlSchemaFile = null;
	private boolean isValidated = false;

	/**
	 * @param configHolderRef
	 * @throws Exception
	 */
	public MarauderConfigParser(MarauderGlobalConfig configHolderRef, String xmlSchemaPath, String xmlConfigPath) {
		this.configHolder = configHolderRef;
		this.xmlConfigFile = xmlConfigPath;
		this.xmlSchemaFile = xmlSchemaPath;
	}

	/**
	 * @return the xmlConfigFile
	 */
	public String getXmlConfigFile() {
		return xmlConfigFile;
	}

	/**
	 * @param xmlConfigFile the xmlConfigFile to set
	 */
	public void setXmlConfigFile(String xmlConfigFile) {
		this.xmlConfigFile = xmlConfigFile;
	}

	/**
	 * @return the xmlSchemaFile
	 */
	public String getXmlSchemaFile() {
		return xmlSchemaFile;
	}

	/**
	 * @param xmlSchemaFile the xmlSchemaFile to set
	 */
	public void setXmlSchemaFile(String xmlSchemaFile) {
		this.xmlSchemaFile = xmlSchemaFile;
	}

	/**
	 * Load the XML schema as a document and extract
	 * 	ColumnDefinitions
	 * &EventDefinitions from it
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws UnvalidatedXMLConfigException 
	 */
	public void init() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, UnvalidatedXMLConfigException{
		if(!isValidated){
			throw new UnvalidatedXMLConfigException();
		}
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		logger.log(Level.INFO, "Loading configuration from:"+xmlConfigFile);
		Document marauderSchema = docBuilder.parse(new File(xmlConfigFile));
		initHeaderDefinitions(marauderSchema);
		initColumnDefinitions(marauderSchema);
		initEventDefinitions(marauderSchema);
		initContextLookup(marauderSchema);
		logger.log(Level.INFO, "Completed configuration initialization for MarauderSink");
	}	
	
	/**
	 * Initialize Header Definitions
	 * @param marauderSchema
	 * @throws XPathExpressionException
	 */
	protected void initHeaderDefinitions(Document marauderSchema) throws XPathExpressionException {
		NodeList columnNodes = null;
		try {
			columnNodes = (NodeList) xpath.compile(MarauderParserConstants.XML_MARAUDER_BASE_XPATH+MarauderParserConstants.XML_MARAUDER_HDR_COL_XPATH)
					.evaluate(marauderSchema, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			logger.log(Level.SEVERE, "XPath exception occured while trying to initialize Header Definitions");
			throw e;
		}
		extractColumnInformation(configHolder.getHeaderDefinitions(), columnNodes);
	}

	/**
	 * Initialize column definitions from document
	 * @param marauderSchema
	 * @throws XPathExpressionException
	 */
	protected void initColumnDefinitions(Document marauderSchema) throws XPathExpressionException{				
		NodeList columnNodes = null;
		try {
			columnNodes = (NodeList) xpath.compile(MarauderParserConstants.XML_MARAUDER_BASE_XPATH+MarauderParserConstants.XML_MARAUDER_COLS_COL_XPATH)
					.evaluate(marauderSchema, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			logger.log(Level.SEVERE, "XPath exception occured while trying to initialize Column Definitions");
			throw e;
		}
		extractColumnInformation(configHolder.getColumnDefinitions(), columnNodes);
	}
	
	/**
	 * Extract column definitions from document
	 * @param colDefs
	 * @param columnNodes
	 */
	protected void extractColumnInformation(HashMap<String, MarauderColumnDefinition> colDefs, 
			NodeList columnNodes){
		MarauderColumnDefinition tempDefinition = null;		
		for(int i = 0;i<columnNodes.getLength();i++){			
			Node colNode = columnNodes.item(i);
			if(!colNode.getNodeName().equalsIgnoreCase(MarauderParserConstants.MARAUDER_COL_TAG)){
				continue;
			}			
			NamedNodeMap colAttributes = colNode.getAttributes();			
			String colId = colAttributes.getNamedItem(MarauderParserConstants.MARAUDER_COL_ID_ATTRIBUTE)
					.getNodeValue();
			tempDefinition = new MarauderColumnDefinition(
					colId, 
					colAttributes.getNamedItem(MarauderParserConstants.MARAUDER_COL_LOC_ATTRIBUTE)
					.getNodeValue(),
					Integer.parseInt(colAttributes.getNamedItem(MarauderParserConstants
							.MARAUDER_COL_GROUP_ATTRIBUTE).getNodeValue()),
					colNode.getTextContent());
			Node indexEnabled = null;
			if((indexEnabled = colAttributes.getNamedItem(MarauderParserConstants.MARAUDER_COL_INDEX_ATTRIBUTE)) != null){
				tempDefinition.setIndex(
						Boolean.parseBoolean(
								indexEnabled
								.getNodeValue()));
			}
			colDefs.put(colId, tempDefinition);
		}
	}
	
	/**
	 * Initialize event definitions from event schema
	 * @param marauderSchema
	 * @throws XPathExpressionException
	 */
	protected void initEventDefinitions(Document marauderSchema) throws XPathExpressionException{
		MarauderEventDefinition tempDefinition = null;
		XPath xpath = XPathFactory.newInstance().newXPath();		
		NodeList eventNodes = null;
		try {
			eventNodes = (NodeList) xpath.compile(MarauderParserConstants.XML_MARAUDER_BASE_XPATH
					+MarauderParserConstants.XML_MARAUDER_WEVENTS_XPATH).evaluate(marauderSchema, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			logger.log(Level.SEVERE, "XPath exception occured while trying to initialize Event Definitions");
			throw e;
		}
		for(int j = 0;j<eventNodes.getLength();j++){
			Node evNode = eventNodes.item(j);
			NodeList eventSubNodes = evNode.getChildNodes();
			NamedNodeMap colAttributes = evNode.getAttributes();
			if(colAttributes==null){
				continue;
			}
			String eventID = null;
			try{
				eventID = colAttributes.getNamedItem(
					MarauderParserConstants.MARAUDER_COL_ID_ATTRIBUTE).
					getNodeValue();
			}catch(NullPointerException nx){
				logger.severe("Exception while trying to fetch event id attribute from event definitions");
				throw nx;
			}
			PriorityQueue<EventKey> keyList = getEventKeyPriorityQueue(eventSubNodes);
			HashMap<String, MarauderColumnDefinition> eventSpecificDef = clone(configHolder
					.getColumnDefinitions());
			customizeEventColDefinitions(eventSpecificDef, eventSubNodes);
			if(eventID.equalsIgnoreCase(MarauderParserConstants.MARAUDER_EVENT_DEFAULT_ID)){
				tempDefinition = new MarauderEventDefinition(-1, keyList, eventSpecificDef);
				configHolder.getEventDefinitions().put(-1, tempDefinition);
				continue;
			}
			int colId=Integer.parseInt(eventID);
			tempDefinition = new MarauderEventDefinition(colId, keyList, eventSpecificDef);
			configHolder.getEventDefinitions().put(colId, tempDefinition);
		}
	}
	
	/**
	 * @param marauderSchema
	 * @throws XPathExpressionException
	 */
	protected void initContextLookup(Document marauderSchema) throws XPathExpressionException{
		String xpathQuery = MarauderParserConstants.XML_MARAUDER_BASE_XPATH + MarauderParserConstants.XML_MARAUDER_CONTEXT_DEF_XPATH;
		XPath xpath = XPathFactory.newInstance().newXPath();		
		NodeList eventNodes = null;
		try{
			eventNodes = (NodeList) xpath.compile(xpathQuery).evaluate(marauderSchema, XPathConstants.NODESET);
		}catch(XPathExpressionException e){
			logger.log(Level.SEVERE, "XPath exception occured while trying to initialize Context Lookup");
			throw e;
		}
		for(int i = 0;i<eventNodes.getLength();i++){
			Node ctxDef = eventNodes.item(i);
			NamedNodeMap ctxAttrs = ctxDef.getAttributes();
			if(ctxAttrs==null){
				continue;
			}
			String ctxName = ctxDef.getParentNode().getAttributes().getNamedItem(MarauderParserConstants.MARAUDER_CONTEXT_DEF_NAME).getNodeValue();
			String ctxKey = ctxAttrs.getNamedItem(MarauderParserConstants.MARAUDER_CONTEXT_DEF_KEY).getNodeValue();
			String ctxKVal = ctxAttrs.getNamedItem(MarauderParserConstants.MARAUDER_CONTEXT_DEF_VALUE).getNodeValue();
			configHolder.getContextLookup().put(ctxKey+MarauderParserConstants.MARAUDER_KEY_DELIMITER+ctxKVal, ctxName);
		}
	}
	
	/**
	 * Customize column definitions for specific events
	 * @param eventSpecificDef
	 * @param eventSubNodes
	 */
	protected void customizeEventColDefinitions(
			HashMap<String, MarauderColumnDefinition> eventSpecificDef, NodeList eventSubNodes) {		
		if(eventSpecificDef!=null){
			NodeList colList = null;
			try {
				colList = (NodeList) xpath.compile(MarauderParserConstants.XML_MARAUDER_COLS_COL_XPATH)
						.evaluate(eventSubNodes, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}			
			if(colList!=null && colList.getLength()>0){
				extractColumnInformation(eventSpecificDef, colList);				
			}
		}
	}

	/**
	 * Generate priority queues for row key/key priority for
	 * specific event ID/type
	 * @param eventKeys
	 * @return eventKeyQueue
	 */
	public PriorityQueue<EventKey> getEventKeyPriorityQueue(NodeList eventKeys){
		EventKey eventKey = null;
		PriorityQueue<EventKey> eventKeyQueue = new PriorityQueue<EventKey>(5, new PriorityQueueComparator());					
		NodeList keysList = null;
		try {
			keysList = (NodeList) xpath.compile(MarauderParserConstants.XML_MARAUDER_KEYS_XPATH)
					.evaluate(eventKeys, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		if(keysList!=null){
			for(int j = 0;j<keysList.getLength();j++){
				Node key = keysList.item(j);
				String id = key.getAttributes().getNamedItem(MarauderParserConstants.MARAUDER_COL_ID_ATTRIBUTE)
						.getNodeValue();
				eventKey = new EventKey(id, key.getTextContent(), 
						Integer.parseInt(key.getAttributes().getNamedItem(MarauderParserConstants
								.MARAUDER_COL_PRIORITY_ATTRIBUTE).getNodeValue()));
				eventKeyQueue.add(eventKey);
			}		
		}
		return eventKeyQueue;
	}
	
	/**
	 * Comparator for event index key priority
	 * 
	 * @author Ambud Sharma
	 */
	public static class PriorityQueueComparator implements Comparator<EventKey>{
		
		@Override
		public int compare(EventKey k1, EventKey k2) {
			return Integer.compare(k1.getPriority(), k2.getPriority());
		}
		
	}
		
	/**
	 * Validation of XML against XSD Schema
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void validate() throws SAXException, IOException{
		Source xmlFile = new StreamSource(new File(xmlConfigFile));
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);		
		Schema schema = schemaFactory.newSchema(new File(xmlSchemaFile));		
		Validator validator = schema.newValidator();
//		Validate XML Config against Schema to make sure it is correct
		validator.validate(xmlFile);
		isValidated = true;
		logger.log(Level.INFO, "Marauder EventSchema.xml successfully validated!");
	}
	
	/**
	 * Clone the column definitions hash for individual events
	 * @param clonableMap
	 * @return clone
	 */
	protected static HashMap<String, MarauderColumnDefinition> clone(HashMap<String, MarauderColumnDefinition> clonableMap){
		HashMap<String, MarauderColumnDefinition> clone = new HashMap<String, MarauderColumnDefinition>(clonableMap.size());
		for(Entry<String, MarauderColumnDefinition> entry:clonableMap.entrySet()){
			clone.put(entry.getKey().toString(), new MarauderColumnDefinition(entry.getValue()));
		}
		return clone;
	}
	
	/**
	 * Has the XML Config been validated against schema?
	 * @return isValidated
	 */
	public boolean isValidated() {
		return isValidated;
	}
	
}
