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
import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.source.AbstractSource;

import com.google.common.base.Throwables;

public abstract class MarauderIDSSource extends AbstractSource implements Configurable, PollableSource {	
	
	private ChannelProcessor processor = null;
	private int hostAddress;
	
	@Override
	public void configure(Context context) {
		try {
			String ip = context.getString("host.address", InetAddress.getLocalHost().getHostAddress());
			/*if(ip.startsWith("127.")){
				throw new UnknownHostException("Localhost address/range is not a valid source address in Marauder");
			}*/
			//TODO disabled IP Address Validation
			this.hostAddress = NetworkUtils.stringIPtoInt(ip);
		} catch (UnknownHostException e) {
			Throwables.propagate(e);
		}
	}
	
	public abstract MarauderIDSEvent getEvent() throws InterruptedException;
	
	@Override
	public void setChannelProcessor(ChannelProcessor channelProcessor) {
		super.setChannelProcessor(channelProcessor);
		this.processor = channelProcessor;
	};
	
	@Override
	public ChannelProcessor getChannelProcessor() {
		return processor;
	};
	
	@Override
	public Status process() throws EventDeliveryException {		
		Status status = null;
	    // Start transaction
	    try {	      
	      // Receive new data
	      MarauderIDSEvent e = getEvent();
	      // Initialize headers
	      e.initHdrs();
	      // Store the Event into this Source's associated Channel(s)
	      processor.processEvent(e);
	      status = Status.READY;
	    } catch (Throwable t) {
	      // Log exception, handle individual exceptions as needed
	      status = Status.BACKOFF;
	      // re-throw all Errors
	      if (t instanceof Error) {
	        throw (Error)t;
	      }
	    } finally {
	    }
	    return status;
	}
	
	@Override
	public synchronized void start() {
		super.start();
	}
	
	@Override
	public synchronized void stop() {
		super.stop();
	}

	/**
	 * IP of the host where IPS is running
	 * @return integerIP
	 */
	protected int getHostAddress() {
		return hostAddress;
	}
}
