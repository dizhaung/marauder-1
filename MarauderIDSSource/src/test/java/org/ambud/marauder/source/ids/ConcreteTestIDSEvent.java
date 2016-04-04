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
package org.ambud.marauder.source.ids;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.ambud.marauder.commons.NetworkUtils;

public class ConcreteTestIDSEvent extends MarauderIDSEvent {

	private int ts, evID, sigID, gID;
	private short srcPrt, dstPrt;
	private byte[] body, srcIP, dstIP;
	
	public ConcreteTestIDSEvent(int ts, int evID, int sigID, int gID,
			short srcPrt, short dstPrt, byte[] body, byte[] srcIP, byte[] dstIP) {
		super();
		this.ts = ts;
		this.evID = evID;
		this.sigID = sigID;
		this.gID = gID;
		this.srcPrt = srcPrt;
		this.dstPrt = dstPrt;
		this.body = body;
		this.srcIP = srcIP;
		this.dstIP = dstIP;
	}

	@Override
	public byte[] getBody() {
		return body;
	}

	@Override
	public void setBody(byte[] body) {
		this.body = body;
	}

	@Override
	public int getTimestamp() {
		return ts;
	}

	@Override
	public byte[] getSrcIP() {
		return srcIP;
	}

	@Override
	public byte[] getDstIP() {
		return dstIP;
	}

	@Override
	public short getSrcPort() {
		return srcPrt;
	}

	@Override
	public short getDstPort() {
		return dstPrt;
	}

	@Override
	public int getEventID() {
		return evID;
	}

	@Override
	public int getSignatureID() {
		return sigID;
	}

	@Override
	public int getGeneratorID() {
		return gID;
	}

	@Override
	public int getSigID() {
		return 234;
	}

	@Override
	public int getSourceAddress() {
		try {
			return NetworkUtils.stringIPtoInt(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	protected String getIDSType() {
		return "snort";
	}

}
