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
package org.ambud.marauder.source.pcap;

import java.util.ArrayList;

import org.ambud.marauder.source.pcap.layer3.IPv4;
import org.junit.Test;

public class TestHexIP {

	@Test
	public void testHexIP(){
		ArrayList<Integer> ints = new ArrayList<>();
		String range = "192.168.1.";
		for(int i=0;i<256;i++){
			int ip = stringToIntIP(range+i);
			ints.add(ip);
			IPv4.intToStringIP(ip);
		}
		long ips = 2923381313L;
		System.out.println(IPv4.intToStringIP((int) ips));
	}
	
	public static int stringToIntIP(String ip){
		String[] ary = ip.split("\\.");
		return (Integer.valueOf(ary[0]) << 24)	|
				(Integer.valueOf(ary[1]) << 16)	|
				(Integer.valueOf(ary[2]) << 8)	|
				(Integer.valueOf(ary[3]) << 0);
				
	}
	
}
