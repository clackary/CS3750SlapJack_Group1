import java.util.Collections;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import java.util.concurrent.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;

public class Board extends JPanel
{
	static Color bgColor = new Color(6, 10, 25);
	
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
	int playerCollecting;  //id of player who slaps and collects
	boolean turnGlowOn; //when a player collects center pile, their side briefly glows
	int playerUp;  //id of player who's turn it is
	private boolean soundOn = true;
	int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	private final URL shuffleURL = Deck.class.getResource("sounds/cardShuffle.wav");
	private final URL playURL = Deck.class.getResource("sounds/cardDeal.wav");
	private final URL slapURL = Deck.class.getResource("sounds/slap.wav");
	
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
		
		turnGlowOn = false; //a call to this.repaint will turn on a glow if turnGlowOn is true
		
		configureBoard();
		createPlayers();
		createCenterPanel();
		
		this.add(player1);
		this.add(centerPanel);
		this.add(player2);
		
		dealCardsToPlayers();  //initial splitting of 52 cards, 26 to each player at beginning of game.  
								//deck is empty after this.

		player1.addHandToBoard();
		player2.addHandToBoard();
		//player1.showPlayersTurn(true);//Player1's PlayTopCard button shows green
		player2.setPlayButtonEnabled(false);
		
		//centerPanel.add(new WinMessage(1));for testing
	}
	
	public void setActionKeys(int whichPlayer, String whichButton, String text){
		if (whichPlayer == 1)
			player1.changeActionKeys(whichButton, text);
		else
			player2.changeActionKeys(whichButton, text);
	}
	
	private void dealCardsToPlayers() {
		ArrayList<Card> dealtCards = new ArrayList<>();
		if(soundOn){
			this.shuffleSound();
		}
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
		int yPos = 40;

		topCard.setBounds(xPos, yPos, Card.CARD_WI + 100, Card.CARD_HI +60); //the added pixels give space for the image to be drawn on the card
		topCard.setRotation(random.nextDouble() * .25 * (centerPile.size() % 2 == 0 ? -1.0 : 1.0)); //alternates direction of rotation
		centerPanel.add(topCard);
		topCard.setFaceUp(true);
		centerPanel.setComponentZOrder(topCard, 0);
		if(soundOn){
			this.playSound();
		}
		centerPanel.repaint();
	}
	
	// Board.slappedByPlayer(playerId) is called by the player who slapped.
	public void slappedByPlayer(int playerID){
		if(soundOn){
			this.slapSound();
		}
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
					this.add(new WinMessage(theSlappingPlayer.playerID));
					theSlappingPlayer.btn_playTopCard.setEnabled(false);
				}
			} else
			{	//here theSlappingPlayer is actually the other player, 'cause the top card wasn't a jack
				theSlappingPlayer = (playerID == 1 ? player2 : player1);
			}
			Collections.shuffle(centerPile);
			if(soundOn){
				this.shuffleSound();
			}
			theSlappingPlayer.addCardsToHand(centerPile);
			playerCollecting = theSlappingPlayer.playerID;
			turnGlowOn = true;
			this.repaint();//calls the glow method
			centerPile.clear();
			centerPanel.repaint();
			theSlappingPlayer.addHandToBoard();
		}
	}

	//toggles the visual indicators of whose turn it is
	public void togglePlayersTurn(){
		if (player1.handSize()==0 && player2.handSize()==0){
			this.add(new LoseMessage());
			centerPanel.removeAll();
			centerPanel.repaint();
		}
		if (playerUp == 1){
			if (player2.handSize()!=0){
				playerUp = 2;
				//player1.showPlayersTurn(false);
				//player2.showPlayersTurn(true);
				player1.setPlayButtonEnabled(false);
				player2.setPlayButtonEnabled(true);
			}
		}else{
			if (player1.handSize()!=0){
				playerUp = 1;
				//player1.showPlayersTurn(true);
				//player2.showPlayersTurn(false);
				player1.setPlayButtonEnabled(true);
				player2.setPlayButtonEnabled(false);
			}
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
	
	private void createCenterPanel() {
		centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		centerPanel.setPreferredSize(new Dimension(screenWidth/3, screenHeight));
		centerPanel.setMaximumSize(new Dimension(screenWidth/3, screenHeight));
		centerPanel.setMinimumSize(new Dimension(screenWidth/3, screenHeight));
		centerPanel.setLayout(null);
	}

	private void configureBoard() {
		this.setBackground(bgColor); //temporary, unless we like it		
		 									//	horizontally-oriented  BoxLayout so there will be a 
		 									//* Player 1 panel on the left, a Center panel,
											//* and a Player 2 panel on the right.
		this.setOpaque(true);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	public void setSound(boolean sound){
		soundOn = sound;
	}
	
	public boolean isSoundOn(){
		return soundOn;
	}
	
	public void shuffleSound(){
		try{
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(shuffleURL);
	         // Get a sound clip resource.
	         Clip clip = AudioSystem.getClip();
	         // Open audio clip and load samples from the audio input stream.
	         clip.open(audioIn);
	         clip.start();
		}catch(NullPointerException | UnsupportedAudioFileException | IOException | LineUnavailableException e){
			e.printStackTrace();
		}
	}
	
	public void playSound(){
		try{
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(playURL);
	         // Get a sound clip resource.
	         Clip clip = AudioSystem.getClip();
	         // Open audio clip and load samples from the audio input stream.
	         clip.open(audioIn);
	         clip.start();
		}catch(NullPointerException | UnsupportedAudioFileException | IOException | LineUnavailableException e){
			e.printStackTrace();
		}
	}
	
	public void slapSound(){
		try{
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(slapURL);
	         // Get a sound clip resource.
	         Clip clip = AudioSystem.getClip();
	         // Open audio clip and load samples from the audio input stream.
	         clip.open(audioIn);
	         clip.start();
		}catch(NullPointerException | UnsupportedAudioFileException | IOException | LineUnavailableException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (turnGlowOn){
			drawGlow(g, playerCollecting);
			turnGlowOn = false;
		}
	}

	
	
	/*  playing with maybe using a less subtle version of this to indicate which player just
	 *  won the cards. 
	 * 
	 */
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
		
		Slapjack.executor.schedule(turnGlowOff, 1, TimeUnit.SECONDS);
	}
	
	class WinMessage extends JPanel{
		int playerID;
		
		WinMessage(int playerID){
			this.playerID = playerID;
			int width = (int) thisBoard.getMinimumSize().getWidth();
			int height = (int) thisBoard.getMinimumSize().getHeight();
			this.setBounds(0,0, width, height);
			this.setOpaque(false);
		}
		@Override
		public void paintComponent(Graphics g) {
			String winMessage = "Player " + playerID + " wins!";
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.WHITE);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setFont(new Font("Helvetica", Font.BOLD, 72));
			g2.drawString(winMessage, this.getWidth()/2 - (g.getFontMetrics().stringWidth(winMessage)/2), this.getHeight()/3);
		}
	}
	
	class LoseMessage extends JPanel{
		
		LoseMessage(){
			int width = (int) thisBoard.getMinimumSize().getWidth();
			int height = (int) thisBoard.getMinimumSize().getHeight();
			this.setBounds(0,0, width, height);
			this.setOpaque(false);
		}
		@Override
		public void paintComponent(Graphics g) {
			String loseMessage_1 = "You both";
			String loseMessage_2 = "FAILED";
			
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.WHITE);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setFont(new Font("Helvetica", Font.BOLD, 72));
			int wordsHeight = (int) g2.getFontMetrics().getStringBounds(loseMessage_1, g2).getHeight();
			g2.drawString(loseMessage_1, this.getWidth()/2 - (g.getFontMetrics().stringWidth(loseMessage_1)/2), this.getHeight()/3);
			g2.setColor(Color.RED);
			g2.drawString(loseMessage_2, this.getWidth()/2 - (g.getFontMetrics().stringWidth(loseMessage_2)/2), (this.getHeight()/3)+wordsHeight);
		}
	}
}
