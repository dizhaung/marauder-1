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

import java.util.Date;

import org.junit.Test;

public class TestByteUtils {

	@Test
	public void testIntToByteMSB() {
//		fail("Not yet implemented");
	}

	@Test
	public void testByteAryToAscii() {
		int time = 1380327876;
		System.out.println("time:"+new Date((long)time*1000));
		time = Integer.MAX_VALUE - time;
		System.out.println("inverted time:"+time);
		System.out.println("inverted time:"+new Date((long)time*1000));
		System.out.println("inverted time:"+ByteUtils.byteAryToAscii(ByteUtils.intToByteMSB(time)));
		System.out.println("max time:"+new Date((long)Integer.MAX_VALUE*1000));
		System.out.println("max time:"+ByteUtils.byteAryToAscii(ByteUtils.intToByteMSB(Integer.MAX_VALUE)));		
	}

}
