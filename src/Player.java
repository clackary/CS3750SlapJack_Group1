

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import sun.misc.Queue;

public class Player extends JPanel
{
	Board theBoard;
	ArrayList<Card> hand;
	JPanel handPanel, controlPanel;
	JButton btn_playTopCard, btn_slap;
	Card[] testBacks;
	int playerID;
	
	int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public Player(Board board, int id){
		theBoard = board;
		playerID = id;
		hand = new ArrayList<Card>();
		
		
		this.setPreferredSize(new Dimension(screenWidth/3, screenHeight));
		this.setMaximumSize(new Dimension(screenWidth/3, screenHeight));
		this.setMinimumSize(new Dimension(screenWidth/3, screenHeight));
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setOpaque(false);
		btn_playTopCard = new JButton("Play Top Card");
		btn_slap = new JButton("Slap");
				
		createHandPanel();		
		createControlPanel();
		
		
		
		
		controlPanel.add(btn_playTopCard);
		controlPanel.add(btn_slap);

		
		this.add(handPanel);
		this.add(controlPanel);
		
	}

	
	public void addHandToBoard(){
		
		int xPos = (int) (handPanel.getPreferredSize().getWidth() /2 - (Card.CARD_WI + 100) / 2) ;
		int yPos = (int) (handPanel.getPreferredSize().getHeight() /2 - (Card.CARD_HI + 80) / 2) ;
		
		
		double radians = playerID==1 ? .07 : -.12; //so the hands aren't identically messy
		for (Card c : hand){
			c.setFaceUp(false);
			c.setBounds(xPos, yPos, Card.CARD_WI + 100, Card.CARD_HI + 60);
			c.setRotation(radians);
			handPanel.add(c);
			radians+=(playerID==1 ? -.012 : .015);
		}
	}
	
	public void addCardsToHand(ArrayList<Card> cardsToAdd){
		for (Card c : cardsToAdd){
			hand.add(c);
		}
	}
	
	
	private void createControlPanel() {
		controlPanel = new JPanel();	
		controlPanel.setOpaque(false);
		controlPanel.setPreferredSize(new Dimension(screenWidth/3, (int)(screenHeight * .3)));
		controlPanel.setMinimumSize(new Dimension(screenWidth/3, (int)(screenHeight * .3)));
		controlPanel.setMaximumSize(new Dimension(screenWidth/3, (int)(screenHeight * .3)));
		
	}

	private void createHandPanel() {
		handPanel = new JPanel();
		handPanel.setOpaque(false);
		handPanel.setLayout(null);//so that Card objects can be positioned
		handPanel.setPreferredSize(new Dimension(screenWidth/3, (int)(screenHeight * .7)));
		handPanel.setMinimumSize(new Dimension(screenWidth/3, (int)(screenHeight * .7)));
		handPanel.setMaximumSize(new Dimension(screenWidth/3, (int)(screenHeight * .7)));
	}
}
