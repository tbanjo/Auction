package example.broker.protocol;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;

public class BrokerAgent extends Agent{
	private AID responder;
	
	  protected void setup() {
	  	// Read the name of agent to forward requests to
	  	Object[] args = getArguments();
	  	if (args != null && args.length > 0) {
	  		responder = new AID((String) args[0], AID.ISLOCALNAME);
	  	
		  	System.out.println("Agent "+getLocalName()+" waiting for requests...");
		  	MessageTemplate template = MessageTemplate.and(
		  		MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
		  		MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );
		  		
				AchieveREResponder arer = new AchieveREResponder(this, template) {
					protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
						System.out.println("Agent "+getLocalName()+": REQUEST received from "+request.getSender().getName()+". Action is "+request.getContent());
						ACLMessage agree = request.createReply();
						agree.setPerformative(ACLMessage.AGREE);
						return agree;
					}
				};
				// Register an AchieveREInitiator in the PREPARE_RESULT_NOTIFICATION state
				arer.registerPrepareResultNotification(new AchieveREInitiator(this, null) {
					// Since we don't know what message to send to the responder
					// when we construct this AchieveREInitiator, we redefine this 
					// method to build the request on the fly
					protected Vector prepareRequests(ACLMessage request) {
						// Retrieve the incoming request from the DataStore
						String incomingRequestKey = (String) ((AchieveREResponder) parent).REQUEST_KEY;
						ACLMessage incomingRequest = (ACLMessage) getDataStore().get(incomingRequestKey);
						// Prepare the request to forward to the responder
						System.out.println("Agent "+getLocalName()+": Forward the request to "+responder.getName());
						ACLMessage outgoingRequest = new ACLMessage(ACLMessage.REQUEST);
						outgoingRequest.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
						outgoingRequest.addReceiver(responder);
						outgoingRequest.setContent(incomingRequest.getContent());
						outgoingRequest.setReplyByDate(incomingRequest.getReplyByDate());
						Vector v = new Vector(1);
						v.addElement(outgoingRequest);
						return v;
					}
					
					protected void handleInform(ACLMessage inform) {
						storeNotification(ACLMessage.INFORM);
					}
					
					protected void handleRefuse(ACLMessage refuse) {
						storeNotification(ACLMessage.FAILURE);
					}
					
					protected void handleNotUnderstood(ACLMessage notUnderstood) {
						storeNotification(ACLMessage.FAILURE);
					}
					
					protected void handleFailure(ACLMessage failure) {
						storeNotification(ACLMessage.FAILURE);
					}
					
					protected void handleAllResultNotifications(Vector notifications) {
						if (notifications.size() == 0) {
							// Timeout
							storeNotification(ACLMessage.FAILURE);
						}
					}
					
					private void storeNotification(int performative) {
						if (performative == ACLMessage.INFORM) {
							System.out.println("Agent "+getLocalName()+": brokerage successful");
						}
						else {
							System.out.println("Agent "+getLocalName()+": brokerage failed");
						}
							
						// Retrieve the incoming request from the DataStore
						String incomingRequestkey = (String) ((AchieveREResponder) parent).REQUEST_KEY;
						ACLMessage incomingRequest = (ACLMessage) getDataStore().get(incomingRequestkey);
						// Prepare the notification to the request originator and store it in the DataStore
						ACLMessage notification = incomingRequest.createReply();
						notification.setPerformative(performative);
						String notificationkey = (String) ((AchieveREResponder) parent).RESULT_NOTIFICATION_KEY;
						getDataStore().put(notificationkey, notification);
					}
				} );
				
				addBehaviour(arer);
	  	}
	  	else {
	  		System.out.println("No agent to forward requests to specified.");
	  	}
	  }

}
