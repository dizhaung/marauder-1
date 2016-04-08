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
package org.ambud.marauder.event;

public enum MarauderEventTypes {
	
	BASE(""),
	IDS("@"),		// IDS/IPS					e.g (Snort, Bro, Surricata)	
	FIREWALL("$"),	// Firewall					e.g (Cisco, Juniper, IPTables) 
	HIDS("%"),	// Host IDS					e.g (Host based intrusion detection systems Tripwire, OSSEC)
	LDAP("^"),	// LDAP						e.g (LDAP events)
	WIN("&"),		// Windows event			e.g (Generic Windows Events)
	APP("*"),		// Application event source	e.g (Application event logs)
	AV("|"),		// Anti-virus systems		e.g (ClamAV, Trend Micro, AVG)
	DB("?"),		// DB events				e.g (DB event logs + DB based events)
	PENTEST(":"),// Pentest events			e.g (Penetration testing systems Nessus, nmap)
	DLP("'"),		// DLP events				e.g (Data loss prevention systems)
	GATEWAY("<"),// Gatway events			e.g (Application gateway: web application firewalls, network gateways, load balancers)
	OS(">"),		// Operating system			e.g (Windows, Linux, Unix etc.)
	NETSYS(";"),	// Network systems			e.g (Generic Network Systems i.e. networking hardware with reporting capabilities)
	SWITCH("["),	// Network systems Switch	e.g (HP, Juniper)
	ROUTER("]"),	// Network systems Router	e.g (Cisco, HP, Juniper)
	VIRT("{"),	// Virtualization 			e.g (VMWare, Citrix, HyperV)
	AUTH("}"),	// Authentication services 	e.g (NTLM, Kerberos)
	WEB("+"),		// Web server events		e.g (Apache, IIS)
	NETSRVC("="),	// Network service			e.g (Generic Network Services)
	DHCP("-"),	// Network service DHCP		e.g (MS DHCP, ISC-DHCP, DHCPD)
	RADIUS("_"),	// Network service RADIUS	e.g (MS RADIUS)
	VPN("~"),	// Network service VPN		e.g (Cisco, Juniper)
	DNS("`");	// Network service DNS		e.g (DNS)
	
	private String eventType;
	
	MarauderEventTypes(String eventType){
		this.eventType = eventType;
	}
	
	public String getTypeName(){
		return eventType;
	}
}
