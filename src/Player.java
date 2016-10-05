

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import sun.misc.Queue;

public class Player extends JPanel
{
	Board theBoard;
	Queue<Card> hand;
	JPanel handPanel, controlPanel;
	JButton btn_playTopCard, btn_slap;
	Card[] testBacks;
	int playerID;
	
	public Player(Board board, int id){
		theBoard = board;
		playerID = id;
		hand = new Queue<>();
		
		this.setOpaque(false);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		btn_playTopCard = new JButton("Play Top Card");
		btn_slap = new JButton("Slap");
				
		createHandPanel();		
		createControlPanel();
		
		int xPos = (int)((theBoard.getWidth() * .25) - Card.CARD_WI) / 2;
		int yPos = (int)((theBoard.getHeight() * .7) - Card.CARD_HI) / 2;
		
		double radians = playerID==1 ? .07 : -.12;
		testBacks = new Card[12];
		for (int i=0; i < 12; i++){
			testBacks[i] = new Card();
			testBacks[i].setBounds(xPos, yPos, Card.CARD_WI + 100, Card.CARD_HI +60);
			testBacks[i].setRotation(radians);
			handPanel.add(testBacks[i]);
			radians+=(playerID==1 ? -.012 : .015);
		}
		
		controlPanel.add(btn_playTopCard);
		controlPanel.add(btn_slap);

		
		this.add(handPanel);
		this.add(controlPanel);
	}

	
	public void addCardsToHand(ArrayList<Card> cardsToAdd){
		for (Card c : cardsToAdd){
			hand.enqueue(c);
		}
	}
	
	
	private void createControlPanel() {
		controlPanel = new JPanel();	
		controlPanel.setOpaque(false);
		controlPanel.setPreferredSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .3)));
		controlPanel.setMinimumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .3)));
		controlPanel.setMaximumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .3)));
		
	}

	private void createHandPanel() {
		handPanel = new JPanel();
		handPanel.setLayout(null);//so that Card objects can be positioned
		//handPanel.setBackground(Color.DARK_GRAY);////a dark background might help the card outline not look so rough
		handPanel.setOpaque(false);
		handPanel.setPreferredSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .7)));
		handPanel.setMinimumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .7)));
		handPanel.setMaximumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .7)));
	}
}
