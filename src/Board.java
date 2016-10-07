import java.util.Collections;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.util.concurrent.*;

public class Board extends JPanel
{
	static Color bgColor = new Color(6, 10, 25);
	ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	Runnable turnGlowOff;
	Board thisBoard;
	
	Deck deck;
	JPanel board;
	JPanel centerPanel; //where the center pile will be displayed
						//probably will contain another panel with null layout, for card pile positioning
	Player player1, player2;
	ArrayList<Card> centerPile;
	Card testCard1, testCard2, testCard3;
	Random random;
	int playerUp;  //id of player whose turn it is
	int playerCollecting;  //id of player who slaps and collects
	
	boolean turnGlowOn; //when a player slaps and gathers cards, their side briefly glows
	
	int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public Board(){
		thisBoard = this;
		turnGlowOff = new Runnable() {
			public void run() {
				thisBoard.repaint();
			}
		};
		//takes up whole screen but leaves room at bottom
		this.setPreferredSize(new Dimension(screenWidth, screenHeight - (screenHeight / 20))); 
		this.setVisible(true);
		deck = new Deck();
		centerPile = new ArrayList<>();
		random = new Random();//for randomizing card rotation
		playerUp = 1;
		turnGlowOn = false;
		
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
		player1.showPlayersTurn(true);
		
	}
	
	public void togglePlayersTurn(){
		if (playerUp == 1){
			playerUp = 2;
			player1.showPlayersTurn(false);
			player2.showPlayersTurn(true);
			player1.setPlayButtonEnabled(false);
			player2.setPlayButtonEnabled(true);
		}else{
			playerUp = 1;
			player1.showPlayersTurn(true);
			player2.showPlayersTurn(false);
			player1.setPlayButtonEnabled(true);
			player2.setPlayButtonEnabled(false);
		}
	}
	
	private void dealCardsToPlayers() {
		ArrayList<Card> dealtCards = new ArrayList<>();
		deck.shuffle();
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
	
	// Board.slap(playerId) is called by the player who slapped.
	public void slap(int playerID){
		Player theSlappingPlayer;
		Player theOtherPlayer;  
				
		if (!centerPile.isEmpty())//nothing happens if the center pile is empty
		{
			if (isTopCardJack())
			{
				if (playerID == 1){
					theSlappingPlayer = player1;
					theOtherPlayer = player2;
				}else{
					theSlappingPlayer = player2;
					theOtherPlayer = player1;
				}
				if (theOtherPlayer.handSize() == 0){
					System.out.println("Player " + theSlappingPlayer.playerID + " wins!");
				}
			} else
			{	//here theSlappingPlayer is actually the other player, 'cause the top card wasn't a jack
				theSlappingPlayer = (playerID == 1 ? player2 : player1);
			}
			Collections.shuffle(centerPile);
			theSlappingPlayer.addCardsToHand(centerPile);
			playerCollecting = theSlappingPlayer.playerID;
			turnGlowOn = true;
			this.repaint();			
			centerPile.clear();
			centerPanel.repaint();
			theSlappingPlayer.addHandToBoard();
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
		this.setBackground(bgColor); //temporary, unless we like it		
		 									//	horizontally-oriented  BoxLayout so there will be a 
		 									//* Player 1 panel on the left, a Center panel,
											//* and a Player 2 panel on the right.
		this.setOpaque(true);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	

	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (turnGlowOn){
			drawGlow(g, playerCollecting);
			turnGlowOn = false;
		}
	}
	
	
	private void drawGlow(Graphics g, int playerID) {
		int centerX, centerY;
		if (playerID == 1){
			centerX = (int)(player1.getPreferredSize().getWidth() / 2);
			centerY = (int)(player1.getPreferredSize().getHeight() / 3);
		}else{
			centerX = (int)((player2.getPreferredSize().getWidth() / 2) + (2 * player2.getPreferredSize().getWidth()));
			centerY = (int)((player2.getPreferredSize().getHeight() / 3));
		}
			
		Point2D gradient_CenterPoint = new Point2D.Float(centerX, centerY);
		float radius = 400f;
		float[] dist = { 0.2f, .8f };  //first float is where first color begins, and then gradually reaches second color at second float
		Color[] colors = { new Color(255, 255, 255, 80), new Color(255, 255, 255, 0) };

		RadialGradientPaint radialGradientPaint = new RadialGradientPaint(gradient_CenterPoint, radius, dist,
				colors);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(radialGradientPaint);
		g2d.fillArc(centerX-400, centerY-400, 800, 800, 0, 360);//upper left-hand corner, width, height, startArc, endArc
		
		executor.schedule(turnGlowOff, 1, TimeUnit.SECONDS);
	}
	
	
}