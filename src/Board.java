import java.util.Collections;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.util.Random;

public class Board extends JPanel
{
	Deck deck;
	JPanel board;
	JPanel centerPanel; //where the center pile will be displayed
						//probably will contain another panel with null layout, for card pile positioning
	Player player1, player2;
	ArrayList<Card> centerPile;
	Card testCard1, testCard2, testCard3;
	Random random;
	
	int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public Board(){
		
		//takes up whole screen but leaves room at bottom
		this.setPreferredSize(new Dimension(screenWidth, screenHeight - (screenHeight / 20))); 
		this.setVisible(true);
		deck = new Deck();
		centerPile = new ArrayList<>();
		random = new Random();//for randomizing card rotation
		
		configureBoard();
		createPlayers();
		
		centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		centerPanel.setPreferredSize(new Dimension(screenWidth/3, screenHeight));
		centerPanel.setMaximumSize(new Dimension(screenWidth/3, screenHeight));
		centerPanel.setMinimumSize(new Dimension(screenWidth/3, screenHeight));
		centerPanel.setLayout(null);
		
		this.add(player1);
		this.add(centerPanel);
		this.add(player2);
		
		
		
		dealCardsToPlayers();  //initial splitting of 52 cards, 26 to each player at beginning of game.  
								//deck is empty after this.

		player1.addHandToBoard();
		player2.addHandToBoard();
		
				
		/*testCard1 = new Card(Card.Suit.DIAMONDS, Card.Value.FOUR);
		testCard2 = new Card(Card.Suit.CLUBS, Card.Value.JACK);
		testCard3 = new Card(Card.Suit.SPADES, Card.Value.ACE);

		int xPos = (int) (centerPanel.getPreferredSize().getWidth() /2 - (Card.CARD_WI + 100) / 2);
		int yPos = 40 ;

		testCard1.setBounds(xPos, yPos, Card.CARD_WI + 100, Card.CARD_HI +60); //the added pixels give space for the image to be drawn on the card
		testCard1.setRotation(.2);
		testCard2.setBounds(xPos, yPos, Card.CARD_WI + 100, Card.CARD_HI +60);
		testCard2.setRotation(0);
		testCard3.setBounds(xPos, yPos, Card.CARD_WI + 100, Card.CARD_HI +60);
		testCard3.setRotation(-.15);

		centerPanel.add(testCard2);
		centerPanel.add(testCard1);
		centerPanel.add(testCard3);

		testCard2.repaint();
		this.repaint();
		*/
	}
	
	private void dealCardsToPlayers() {
		ArrayList<Card> dealtCards = new ArrayList<>();
		dealtCards.addAll(deck.getCards(26));
		player1.addCardsToHand(dealtCards);
		dealtCards.clear();
		dealtCards.addAll(deck.getCards(26));
		player2.addCardsToHand(dealtCards);
	}
	
	public void placeCardOnCenterPile(Card card){
		centerPile.add(0, card);
		Card topCard = centerPile.get(0);
		
		int xPos = (int) (centerPanel.getPreferredSize().getWidth() /2 - (Card.CARD_WI + 100) / 2);
		int yPos = 40 ;

		topCard.setBounds(xPos, yPos, Card.CARD_WI + 100, Card.CARD_HI +60); //the added pixels give space for the image to be drawn on the card
		topCard.setRotation(random.nextDouble() * .25 * (centerPile.size() % 2 == 0 ? -1.0 : 1.0)); //alternates direction of rotation
		centerPanel.add(topCard);
		topCard.setFaceUp(true);
		centerPanel.setComponentZOrder(topCard, 0);
		centerPanel.repaint();
	}
	
	//can be called by player.  Whichever listener (button, key) can
	//send player id to this method
	public void slap(int playerID){
		Player thePlayer;
				
		if (!centerPile.isEmpty())//nothing happens if the center pile is empty
		{
			if (isTopCardJack())
			{
				thePlayer = (playerID == 1 ? player1 : player2);
			} else
			{
				thePlayer = (playerID == 1 ? player2 : player1);
			}
			Collections.shuffle(centerPile);
			thePlayer.addCardsToHand(centerPile);
			centerPile.clear();
			centerPanel.repaint();
			thePlayer.addHandToBoard();
		}
	}


	
	public boolean isTopCardJack(){
        if (centerPile.get(0).getValueName().equals("Jack")){//(Card.Value.JACK)) {
            //Top card is jack
            return true;
        }
		return false;
	}
	
	private void createPlayers() {
		player1 = new Player(this, 1);
		player2 = new Player(this, 2);
	}
	

	private void configureBoard() {
		this.setBackground(new Color(6, 10, 25)); //temporary, unless we like it		
		 									//	horizontally-oriented  BoxLayout so there will be a 
		 									//* Player 1 panel on the left, a Center panel,
											//* and a Player 2 panel on the right.
		this.setOpaque(true);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
}

