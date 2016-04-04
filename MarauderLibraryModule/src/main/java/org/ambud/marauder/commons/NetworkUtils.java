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

public class NetworkUtils {

	private NetworkUtils(){}
	
	public static String toStringIP(int ip){
		StringBuilder builder = new StringBuilder();
		builder.append(((ip >> 24) & 0xff)+".");
		builder.append(((ip >> 16) & 0xff)+".");
		builder.append(((ip >> 8) & 0xff)+".");
		builder.append(((ip >> 0) & 0xff));
		return builder.toString();
	}
	
	public static int stringIPtoInt(String ip){
		String[] ipParts = ip.split("\\.");
		int intIP = 0;
		for (int i = 0; i < 4; i++) {
			intIP += Integer.parseInt(ipParts[i]) << (24 - (8 * i));
		}
		return intIP;
	}
}
