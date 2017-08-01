package bookTrading.buyer;

public interface BookBuyerGui {
	void setAgent(BookBuyerAgent a);
	void show();
	void hide();
	void notifyUser(String message);
	void dispose();
}
