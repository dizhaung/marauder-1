# marauder

You can get the working version of this project from: https://github.com/srotya/marauder

Marauder is an open source SIEM platform available under GNU GPL v2

I had started to work on Marauder about 2 years ago and the project got left out, however there's enough code in Marauder that can be utilized by folks who may be interested in the individual modules of Marauder.

If anyone would like to collaborate with me on completing Marauder, let me know.

If I have some cycles from Veronica then I will surely put some additional commits into Marauder.

The original version of the project from early 2013 was here:https://code.google.com/p/marauder/

History:
I have spent about 3 years of my career as a Security Engineer / Programmer. Information security is increasingly become top priority of all organizations, you can't bury your heads in the sand anymore. A SIEM is a Security Incident and Event Monitoring / Management system. There are several paid as well as open source SIEMs out there and the concept is not new.

In the recent years due to the exponential growth in data consumption and the internet the raw amount of information that needs to be inspected by security systems has become huge therefore security industry has created a hierarchy based approach to threat analysis. Let me explain with an example: you deploy specialist systems like firewalls, intrusion detection systems, DLPs etc. for doing what they do best in their own threat vector space however outcomes from these systems then need to be correlated to visualization an attack pattern or detect annomalies; that's where a SIEM comes in. As the system to perform these sophisticated correlations at an extremely large scale (we are talking double digit terabytes of log data)

So what was so unique about Marauder? Well none of the then and even now market available systems are designed the way Marauder is. The concept of log collection is the fan-in approach of getting logs however it's what you do with the logs is where the magic is. 

Marauder uses Apache Flume as the underlying delivery system for active log transmission. Well I encapsulate everything, events are serialized as raw bytes of the original event as well as parsed by the specialized log analyzer I have written for different event types.

Here's an example: Snort (most popular Network Intrusion Detection system) can produce simple text based alerts however what you want to really output from Snort is Unified2 logs with have the capability of including the packet capture of the window of packets that originally fired the alerts (why? well so that you can analyze them later to better understand if this was a true positive or a false postive)

So here's what Marauder does, it will continuously tail on the directory you have configured to be the place where your Unified2
events are going to end up in and then parse it (yes, all the way to pcap and protocol extraction), once parsed they will get populated into the event header (see Apache Flume) which is essentially a Map (Java).

These events are transmitted to aggregators (fan-in approach) that can further be chained together to even beefier aggregators, the final layer of aggregator send data to two places, the sink but can also on-demand route data to the UI if needed, as in an analyst wants to view the logs in realtime of a certain type, then the aggregator of those event types will now start duplicating and routing those events to the UI.

Let's talk about backend magic a little bit. The real power of Marauder comes from the fact that all this log data is custom sharded to an HBase table. The sharding strategy is done via timewindows that essentially aggregate multiple events into 1 key and then the value is another sequence of those events. All of this is done at a byte level so that the keys are as compressed as practically possible to give best performance.

So why did I stop working on something so cool, I got side tracked by some personal things for a few months, more than enough time to loose touch with this stuff. 
