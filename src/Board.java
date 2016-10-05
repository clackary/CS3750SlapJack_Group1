import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Board extends JFrame
{
	Deck deck;
	JPanel board;
	JPanel centerPanel; //where the center pile will be displayed
						//probably will contain another panel with null layout, for card pile positioning
	Player player1, player2;
	ArrayList<Card> centerPile;
	Card testCard1, testCard2, testCard3;
	
	public Board(){
		deck = new Deck();
		centerPile = new ArrayList<>();
		
		createBoard();

		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		//takes up whole screen but leaves room at bottom
		this.setPreferredSize(new Dimension(screenWidth, screenHeight - (screenHeight / 20))); 
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.add(board);
		this.pack();//this way the board JPanel shows up
		

		
		createPlayers();
		
		centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		centerPanel.setLayout(null);
		
		board.add(player1);
		board.add(centerPanel);
		board.add(player2);
		
		testCard1 = new Card(Card.Suit.DIAMONDS, Card.Value.FOUR);
		testCard2 = new Card(Card.Suit.CLUBS, Card.Value.JACK);
		testCard3 = new Card(Card.Suit.SPADES, Card.Value.ACE);
		
		int xPos = (int)((this.getWidth() * .25) - Card.CARD_WI) / 2;
		int yPos = (int)((this.getHeight() * .7) - Card.CARD_HI) / 2;
		
		testCard1.setBounds(xPos, yPos, Card.CARD_WI + 100, Card.CARD_HI +60); //the added pixels give space for the image to be drawn on the card
		testCard1.setRotation(.2);
		testCard2.setBounds(xPos, yPos, Card.CARD_WI + 100, Card.CARD_HI +60);
		testCard2.setRotation(0);
		testCard3.setBounds(xPos, yPos, Card.CARD_WI + 100, Card.CARD_HI +60);
		testCard3.setRotation(-.15);
		
		centerPanel.add(testCard2);
		centerPanel.add(testCard1);
		centerPanel.add(testCard3);
	
	}
	
	

	public boolean isTopCardJack(){
		return false;
	}
	
	private void createPlayers() {
		player1 = new Player(this, 1);
		player2 = new Player(this, 2);
		
	}

	private void createBoard() {
		board = new JPanel();
		board.setBackground(Color.DARK_GRAY); //temporary, unless we like it		
		/* horizontally-oriented  BoxLayout so there will be a 
		 * Player 1 panel on the left, a Center panel,
		 * and a Player 2 panel on the right.*/
		board.setLayout(new BoxLayout(board, BoxLayout.X_AXIS));
	}
	
    public static void main (String[] args)
    {
    	new Board();
    }
}
