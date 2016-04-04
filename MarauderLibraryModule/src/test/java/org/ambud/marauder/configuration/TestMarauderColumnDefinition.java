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

import org.junit.Test;

/**
 * Unit test for MarauderColumnDefinition class
 * 
 * @author Ambud Sharma
 */
public class TestMarauderColumnDefinition {

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderColumnDefinition#MarauderColumnDefinition(java.lang.String, java.lang.String, int, java.lang.String)}.
	 */
	@Test
	public void testMarauderColumnDefinitionParameterized() {
		MarauderColumnDefinition colDef = new MarauderColumnDefinition("an", "msg", 2, "id_sd");
		assertNotNull(colDef);
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderColumnDefinition#MarauderColumnDefinition(org.ambud.marauder.configuration.MarauderColumnDefinition)}.
	 */
	@Test
	public void testMarauderColumnDefinitionCloneTest() {
		MarauderColumnDefinition def1 = new MarauderColumnDefinition("a1", "a2", 3, "asd");		
		MarauderColumnDefinition def2 = new MarauderColumnDefinition(def1);
		assertEquals(def1.getId(), def2.getId());
		assertEquals(def1.getPatternGrp(), def2.getPatternGrp());
		assertEquals(def1.getPattern().pattern(), def2.getPattern().pattern());
		assertEquals(def1.getType(), def2.getType());
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderColumnDefinition#getId()}.
	 */
	@Test
	public void testGetId() {
		MarauderColumnDefinition colDef = new MarauderColumnDefinition("a1", "a2", 3, "asd");
		assertEquals(colDef.getId(), "a1");		
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderColumnDefinition#getType()}.
	 */
	@Test
	public void testGetType() {
		MarauderColumnDefinition colDef = new MarauderColumnDefinition("a1", "a2", 3, "asd");
		assertEquals(colDef.getType(), "a2");
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderColumnDefinition#getPatternGrp()}.
	 */
	@Test
	public void testGetGrpId() {
		MarauderColumnDefinition colDef = new MarauderColumnDefinition("a1", "a2", 3, "asd");
		assertEquals(colDef.getPatternGrp(), 3);
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderColumnDefinition#getPattern()}.
	 */
	@Test
	public void testGetPattern() {
		MarauderColumnDefinition colDef = new MarauderColumnDefinition("a1", "a2", 3, "asd");
		assertEquals(colDef.getPattern().pattern(), "asd");
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderColumnDefinition#isIndex()}.
	 */
	@Test
	public void testIsIndex() {
		MarauderColumnDefinition colDef = new MarauderColumnDefinition("a1", "a2", 3, "asd");
		assertFalse(colDef.isIndex());
	}

	/**
	 * Test method for {@link org.ambud.marauder.configuration.MarauderColumnDefinition#setIndex(boolean)}.
	 */
	@Test
	public void testSetIndex() {
		MarauderColumnDefinition colDef = new MarauderColumnDefinition("a1", "a2", 3, "asd");
		colDef.setIndex(true);
		assertTrue(colDef.isIndex());
	}

}
