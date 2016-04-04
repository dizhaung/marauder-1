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
package com.ambud.marauder.services;

import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.conf.Configurable;

public class MarauderServiceRegistrar implements Configurable{

	private List<MarauderService> services = null;
	private List<MarauderService> alertingService = null;
	private List<MarauderService> reportingService = null;
	private List<MarauderService> analyticsService = null;
	private List<MarauderService> monitoringServices = null;
	
	public MarauderServiceRegistrar() {
	}
	
	public List<MarauderService> getServices() {
//		return Arrays.asList(alertingService, reportingService, analyticsService, monitoringSerives);
		return null;
	}

	@Override
	public void configure(Context context) {
		//perform initialization and configuration
		//get implementations
		//configuration
		for(MarauderService service:getServices()){
			if(service!=null) {
				service.configure(context);
			}
		}
	}
	
	public void startServices() {
		for(MarauderService service:getServices()){
			if(service!=null) {
				service.start();
			}
		}
	}
	
}
