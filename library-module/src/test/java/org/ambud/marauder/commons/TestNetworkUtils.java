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
package org.ambud.marauder.commons;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestNetworkUtils {

	@Test
	public void testToStringIP() {
		int val = NetworkUtils.stringIPtoInt("192.168.1.1");
		long actual = 3232235777L;
		assertEquals((int)actual, val);
	}

	@Test
	public void testStringIPtoInt() {
		assertEquals("192.168.1.1", NetworkUtils.toStringIP((int) 3232235777L));
	}

}
