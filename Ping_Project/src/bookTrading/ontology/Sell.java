package bookTrading.ontology;
import jade.content.AgentAction;
import jade.core.AID;
public class Sell implements AgentAction {
	private Book item;

	  public Book getItem() {
	    return item;
	  }

	  public void setItem(Book item) {
	    this.item = item;
	  }
}
