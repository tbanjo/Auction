package example.PingAgent;

import jade.core.Agent;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.proto.SubscriptionInitiator;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;

public class DFSubsribeAgent extends Agent{
	protected void setup() {
		// Build the description used as template for the subscription
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription templateSd = new ServiceDescription();
		templateSd.setType("weather-forecast");
		templateSd.addProperties(new Property("country", "Italy"));
		template.addServices(templateSd);
		
		SearchConstraints sc = new SearchConstraints();
		// We want to receive 10 results at most
		sc.setMaxResults(new Long(10));
  		
		addBehaviour(new SubscriptionInitiator(this, DFService.createSubscriptionMessage(this, getDefaultDF(), template, sc)) {
			protected void handleInform(ACLMessage inform) {
  			System.out.println("Agent "+getLocalName()+": Notification received from DF");
  			try {
					DFAgentDescription[] results = DFService.decodeNotification(inform.getContent());
		  		if (results.length > 0) {
		  			for (int i = 0; i < results.length; ++i) {
		  				DFAgentDescription dfd = results[i];
		  				AID provider = dfd.getName();
		  				// The same agent may provide several services; we are only interested
		  				// in the weather-forecast one
		  				Iterator it = dfd.getAllServices();
		  				while (it.hasNext()) {
		  					ServiceDescription sd = (ServiceDescription) it.next();
		  					if (sd.getType().equals("weather-forecast")) {
	  							System.out.println("Weather-forecast service for Italy found:");
		  						System.out.println("- Service \""+sd.getName()+"\" provided by agent "+provider.getName());
		  					}
		  				}
		  			}
		  		}	
	  			System.out.println();
		  	}
		  	catch (FIPAException fe) {
		  		fe.printStackTrace();
		  	}
			}
		} );
  } 
}
