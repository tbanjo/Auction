package example.PingAgent;

import jade.core.Agent;
import jade.core.behaviours.*;


public class ComplexBehaviorAgent extends Agent{
		class SingleStepBehaviour extends OneShotBehaviour {
	    private String myStep;

	    public SingleStepBehaviour(Agent a, String step) {
	      super(a);
	      myStep = step;
	    }

	    public void action() {
	      System.out.println("Agent "+getName()+": Step "+myStep);
	    } 

	  }

	  protected void setup() {
	    SequentialBehaviour myBehaviour1 = new SequentialBehaviour(this) {
	      public int onEnd() {
	        reset();
	        return super.onEnd();
	      } 

	    };
	    SequentialBehaviour myBehaviour2 = new SequentialBehaviour(this);
	    SequentialBehaviour myBehaviour2_1 = new SequentialBehaviour(this);
	    SequentialBehaviour myBehaviour2_2 = new SequentialBehaviour(this);

	    myBehaviour2_1.addSubBehaviour(new SingleStepBehaviour(this, "2.1.1"));
	    myBehaviour2_1.addSubBehaviour(new SingleStepBehaviour(this, "2.1.2"));
	    myBehaviour2_1.addSubBehaviour(new SingleStepBehaviour(this, "2.1.3"));
	    myBehaviour2_2.addSubBehaviour(new SingleStepBehaviour(this, "2.2.1"));
	    myBehaviour2_2.addSubBehaviour(new SingleStepBehaviour(this, "2.2.2"));

	    Behaviour b = new SingleStepBehaviour(this, "2.2.3");

	    myBehaviour2_2.addSubBehaviour(b);
	    myBehaviour1.addSubBehaviour(new SingleStepBehaviour(this, "1.1"));
	    myBehaviour1.addSubBehaviour(new SingleStepBehaviour(this, "1.2"));
	    myBehaviour1.addSubBehaviour(new SingleStepBehaviour(this, "1.3"));
	    myBehaviour2.addSubBehaviour(myBehaviour2_1);
	    myBehaviour2.addSubBehaviour(myBehaviour2_2);
	    myBehaviour2.addSubBehaviour(new SingleStepBehaviour(this, "2.3"));
	    myBehaviour2.addSubBehaviour(new SingleStepBehaviour(this, "2.4"));
	    myBehaviour2.addSubBehaviour(new SingleStepBehaviour(this, "2.5"));
	    addBehaviour(myBehaviour1);
	    addBehaviour(myBehaviour2);
	  } 
	
}
