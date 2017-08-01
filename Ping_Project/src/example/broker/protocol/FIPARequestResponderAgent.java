package example.broker.protocol;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class FIPARequestResponderAgent extends Agent {
	protected void setup() {
	  	System.out.println("Agent "+getLocalName()+" waiting for requests...");
	  	MessageTemplate template = MessageTemplate.and(
	  		MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
	  		MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );
	  		
			addBehaviour(new AchieveREResponder(this, template) {
				protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
					System.out.println("Agent "+getLocalName()+": REQUEST received from "+request.getSender().getName()+". Action is "+request.getContent());
					if (checkAction()) {
						// We agree to perform the action. Note that in the FIPA-Request
						// protocol the AGREE message is optional. Return null if you
						// don't want to send it.
						System.out.println("Agent "+getLocalName()+": Agree");
						ACLMessage agree = request.createReply();
						agree.setPerformative(ACLMessage.AGREE);
						return agree;
					}
					else {
						// We refuse to perform the action
						System.out.println("Agent "+getLocalName()+": Refuse");
						throw new RefuseException("check-failed");
					}
				}
				
				protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
					if (performAction()) {
						System.out.println("Agent "+getLocalName()+": Action successfully performed");
						ACLMessage inform = request.createReply();
						inform.setPerformative(ACLMessage.INFORM);
						return inform;
					}
					else {
						System.out.println("Agent "+getLocalName()+": Action failed");
						throw new FailureException("unexpected-error");
					}	
				}
			} );
	  }
	  
	  private boolean checkAction() {
	  	// Simulate a check by generating a random number
	  	return (Math.random() > 0.2);
	  }
	  
	  private boolean performAction() {
	  	// Simulate action execution by generating a random number
	  	return (Math.random() > 0.2);
	  }

}
