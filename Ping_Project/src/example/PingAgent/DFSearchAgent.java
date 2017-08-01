package example.PingAgent;
import java.util.Iterator;

import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.*;

public class DFSearchAgent extends Agent{
	protected void setup() {
	  	// Search for services of type "weather-forecast"
	  	System.out.println("Agent "+getLocalName()+" searching for services of type \"weather-forecast\"");
	  	try {
	  		// Build the description used as template for the search
	  		DFAgentDescription template = new DFAgentDescription();
	  		ServiceDescription templateSd = new ServiceDescription();
	  		templateSd.setType("weather-forecast");
	  		template.addServices(templateSd);
	  		
	  		SearchConstraints sc = new SearchConstraints();
	  		// We want to receive 10 results at most
	  		sc.setMaxResults(new Long(10));
	  		
	  		DFAgentDescription[] results = DFService.search(this, template, sc);
	  		if (results.length > 0) {
	  			System.out.println("Agent "+getLocalName()+" found the following weather-forecast services:");
	  			for (int i = 0; i < results.length; ++i) {
	  				DFAgentDescription dfd = results[i];
	  				AID provider = dfd.getName();
	  				// The same agent may provide several services; we are only interested
	  				// in the weather-forecast one
	  				Iterator it = dfd.getAllServices();
	  				while (it.hasNext()) {
	  					ServiceDescription sd = (ServiceDescription) it.next();
	  					if (sd.getType().equals("weather-forecast")) {
	  						System.out.println("- Service \""+sd.getName()+"\" provided by agent "+provider.getName());
	  					}
	  				}
	  			}
	  		}	
	  		else {
	  			System.out.println("Agent "+getLocalName()+" did not find any weather-forecast service");
	  		}
	  	}
	  	catch (FIPAException fe) {
	  		fe.printStackTrace();
	  	}
	  } 
}
