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

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ByteUtils {
	
	private ByteUtils() {}
	
	public static final Charset DEF_CHARSET = Charset.forName("US-ASCII"); 

	public static byte[] intToByteMSB(int in){
		byte[] ou = new byte[4];
		ou[0] = (byte)((in >> 24) & 0xff);
		ou[1] = (byte)((in >> 16) & 0xff);
		ou[2] = (byte)((in >> 8) & 0xff);
		ou[3] = (byte)(in & 0xff);
		return ou;
	}
	
	public static byte[] longToBytes(long x) {
	    ByteBuffer buffer = ByteBuffer.allocate(8);
	    buffer.putLong(x);
	    return buffer.array();
	}
	
	public static byte[] shortToByteMSB(short in){
		byte[] ou = new byte[2];
		ou[0] = (byte)((in << 8) & 0xff);
		ou[1] = (byte)((in >> 8) & 0xff);
		return ou;
	}
	
	public static byte[] stringToBytes(String input){
		byte[] ou = new byte[input.length()];
		for(int i=0; i<ou.length; i++){
			ou[i] = (byte) input.charAt(i);
		}
		return ou;
	}
	
	public static String intToHex(int in){
		return Integer.toOctalString(in);
	}
	/**
	 * @param in
	 * @return 
	 */
	public static String byteAryToAscii(byte[] in){
		return new String(in, DEF_CHARSET);
	}
	
}
