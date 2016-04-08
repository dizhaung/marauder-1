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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * Unit tests for GlobalConfigImplementationClass
 * 
 * @author Ambud Sharma
 */
public class TestGlobalConfigImplementation {

	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#getColumnDefinitionsList()}.
	 */
	@Test
	public void testGetColumnDefinitionsList() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		assertNotNull(configImpl.getColumnDefinitionsList());
		assertEquals(configImpl.getColumnDefinitionsList().size(), 0);
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#getColumnDefinitions()}.
	 */
	@Test
	public void testGetColumnDefinitions() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		assertNotNull(configImpl.getColumnDefinitions());
		assertEquals(configImpl.getColumnDefinitions().size(), 0);
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#setColumnDefinitions(java.util.HashMap)}.
	 */
	@Test
	public void testSetColumnDefinitions() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		int origSize = configImpl.getColumnDefinitions().size();
		configImpl.setColumnDefinitions(null);
		assertEquals(configImpl.getColumnDefinitions().size(), origSize);
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#getEventDefinitions()}.
	 */
	@Test
	public void testGetEventDefinitions() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		assertNotNull(configImpl.getEventDefinitions());
		assertEquals(configImpl.getEventDefinitions().size(), 0);
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#setEventDefinitions(java.util.HashMap)}.
	 */
	@Test
	public void testSetEventDefinitions() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		int origSize = configImpl.getEventDefinitions().size();
		configImpl.setEventDefinitions(null);
		assertEquals(configImpl.getColumnDefinitions().size(), origSize);
	}
	
	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#getCF1()}.
	 */
	@Test
	public void testGetCF1() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		assertEquals(configImpl.getCF1(), "cf1");
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#getCF2()}.
	 */
	@Test
	public void testGetCF2() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		assertEquals(configImpl.getCF2(), "cf2");
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#getHeaderDefinitionsList()}.
	 */
	@Test
	public void testGetHeaderDefinitionsList() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		assertNotNull(configImpl.getHeaderDefinitionsList());
		assertEquals(configImpl.getHeaderDefinitionsList().size(), 0);
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#getHeaderDefinitions()}.
	 */
	@Test
	public void testGetHeaderDefinitions() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		assertNotNull(configImpl.getHeaderDefinitions());
		assertEquals(configImpl.getHeaderDefinitions().size(), 0);
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#getCF1Bytes()}.
	 */
	@Test
	public void testGetCF1Bytes() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		assertTrue(Arrays.equals(configImpl.getCF1().getBytes(), configImpl.getCF1Bytes()));
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#getCF2Bytes()}.
	 */
	@Test
	public void testGetCF2Bytes() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		assertTrue(Arrays.equals(configImpl.getCF2().getBytes(), configImpl.getCF2Bytes()));
	}
	
	/**
	 * Test method for {@link org.ambud.marauder.configuration.GlobalConfigImplementation#GlobalConfigImplementation(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGlobalConfigImplementation() {
		GlobalConfigImplementation configImpl = new GlobalConfigImplementation("cf1", "cf2");
		assertNotNull(configImpl);
		assertNotNull(configImpl.getCF1());
		assertNotNull(configImpl.getCF2());
		assertNotNull(configImpl.getColumnDefinitions());
		assertNotNull(configImpl.getColumnDefinitionsList());
		assertNotNull(configImpl.getEventDefinitions());
		assertNotNull(configImpl.getHeaderDefinitions());
		assertNotNull(configImpl.getHeaderDefinitionsList());
		assertTrue(Arrays.equals(configImpl.getCF1().getBytes(), configImpl.getCF1Bytes()));
		assertTrue(Arrays.equals(configImpl.getCF2().getBytes(), configImpl.getCF2Bytes()));
	}

}
