package example.PingAgent;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;

public class TimeAgent extends Agent{
	protected void setup() {
	    System.out.println("Agent "+getLocalName()+" started.");

	    // Add the TickerBehaviour (period 1 sec)
	    addBehaviour(new TickerBehaviour(this, 1000) {
	      protected void onTick() {
	        System.out.println("Agent "+myAgent.getLocalName()+": tick="+getTickCount());
	      } 
	    });

	    // Add the WakerBehaviour (wakeup-time 10 secs)
	    addBehaviour(new WakerBehaviour(this, 10000) {
	      protected void handleElapsedTimeout() {
	        System.out.println("Agent "+myAgent.getLocalName()+": It's wakeup-time. Bye...");
	        myAgent.doDelete();
	      } 
	    });
	  } 
}
