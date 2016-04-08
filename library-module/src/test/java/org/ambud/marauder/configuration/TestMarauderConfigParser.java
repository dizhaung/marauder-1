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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Unit test for MarauderConfig Parser
 * 
 * @author Ambud Sharma
 */
public class TestMarauderConfigParser {

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderConfigParser#MarauderConfigParser(org.ambud.marauder.configuration.MarauderGlobalConfig, java.lang.String, java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testMarauderConfigParser() {
		MarauderConfigParser parser = new MarauderConfigParser(new GlobalConfigImplementation("cf1", "cf2"), 
				"EventSchema.xsd", "EventSchema.xml");
		assertEquals(parser.getXmlSchemaFile(), "EventSchema.xsd");
		assertEquals(parser.getXmlConfigFile(), "EventSchema.xml");
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderConfigParser#initHeaderDefinitions(org.w3c.dom.Document)}.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 */
	@Test
	public void testInitHeaderDefinitions() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		MarauderGlobalConfig config = new GlobalConfigImplementation("cf1", "cf2");
		MarauderConfigParser parser = new MarauderConfigParser(config, 
				"./src/test/data/EventSchema.xsd", "./src/test/data/EventSchema.xml");
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(parser.getXmlConfigFile());
		parser.initHeaderDefinitions(doc);
		config.printDefinitions();
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderConfigParser#initColumnDefinitions(org.w3c.dom.Document)}.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 * @throws ParserConfigurationException 
	 */
	@Test
	public void testInitColumnDefinitions() throws SAXException, IOException, XPathExpressionException, ParserConfigurationException {
		MarauderGlobalConfig config = new GlobalConfigImplementation("cf1", "cf2");
		MarauderConfigParser parser = new MarauderConfigParser(config, 
				"./src/test/data/EventSchema.xsd", "./src/test/data/EventSchema.xml");
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(parser.getXmlConfigFile());
		parser.initColumnDefinitions(doc);
		config.printDefinitions();
		assertTrue(config.getColumnDefinitions().size()>0);
	}
	
	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderConfigParser#initContextLookup(org.w3c.dom.Document)}.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 * @throws ParserConfigurationException 
	 */
	@Test
	public void testInitContextLookup() throws SAXException, IOException, XPathExpressionException, ParserConfigurationException {
		MarauderGlobalConfig config = new GlobalConfigImplementation("cf1", "cf2");
		MarauderConfigParser parser = new MarauderConfigParser(config, 
				"./src/test/data/EventSchema.xsd", "./src/test/data/EventSchema.xml");
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(parser.getXmlConfigFile());
		parser.initContextLookup(doc);
		config.printDefinitions();
		assertTrue(config.getContextLookup().size()>0);
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderConfigParser#customizeEventColDefinitions(java.util.HashMap, org.w3c.dom.NodeList)}.
	 * @throws XPathExpressionException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	@Test
	public void testInitEventDefinitions() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		MarauderGlobalConfig config = new GlobalConfigImplementation("cf1", "cf2");
		MarauderConfigParser parser = new MarauderConfigParser(config, 
				"./src/test/data/EventSchea.xsd", "./src/test/data/EventSchema.xml");
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(parser.getXmlConfigFile());
		parser.initEventDefinitions(doc);
		//the supplied file must have some event definitions
		assertTrue(config.getEventDefinitions().size()>0);
	}

	/**
	 * 
	 */
	@Test
	public void testGetEventKeyPriorityComparator() {
		MarauderConfigParser.PriorityQueueComparator compareKeyPriorties = new MarauderConfigParser.PriorityQueueComparator();
		int val = compareKeyPriorties.compare(new EventKey("x", "x1", 1), new EventKey("y", "y1", 2));
		assertTrue(val<0);
	}
	
	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderConfigParser#validate()}.
	 * @throws IOException 
	 * @throws SAXException 
	 */
	@Test
	public void testValidate() throws SAXException, IOException {
		MarauderGlobalConfig config = new GlobalConfigImplementation("cf1", "cf2");
		MarauderConfigParser parser = new MarauderConfigParser(config, 
				"./src/test/data/EventSchema.xsd", "./src/test/data/EventSchema.xml");
		parser.validate();
		assertTrue(parser.isValidated());
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderConfigParser#clone(java.util.HashMap)}.
	 */
	@Test
	public void testCloneHashMapOfStringMarauderColumnDefinition() {
		HashMap<String, MarauderColumnDefinition> originalMap = new HashMap<String, MarauderColumnDefinition>();
		MarauderColumnDefinition defOrg = new MarauderColumnDefinition("2", "21", 2, "12312");
		originalMap.put("test1", defOrg);
		HashMap<String, MarauderColumnDefinition> output = MarauderConfigParser.clone(originalMap);
		assertNotEquals(output.hashCode(), originalMap.hashCode());
		assertEquals(output.values().toArray(new MarauderColumnDefinition[1])[0].getId(), defOrg.getId());
	}

}
