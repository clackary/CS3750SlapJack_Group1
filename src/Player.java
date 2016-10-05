

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import sun.misc.Queue;

public class Player extends JPanel
{
	Board theBoard;
	Queue<Card> hand;
	JPanel handPanel, controlPanel;
	JButton btn_playTopCard, btn_slap;
	Card testCard;
	
	public Player(Board board){
		theBoard = board;
		hand = new Queue<>();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		btn_playTopCard = new JButton("Play Top Card");
		btn_slap = new JButton("Slap");
		
		testCard = new Card(Card.Suit.DIAMONDS, Card.Value.FOUR);
		//testCard = new Card();
		testCard.setBounds(10, 10, Card.CARD_WI, Card.CARD_HI);
		
		createHandPanel();		
		createControlPanel();
		
		handPanel.add(testCard);
		
		controlPanel.add(btn_playTopCard);
		controlPanel.add(btn_slap);
		
		/*JPanel testPanel = new JPanel();
		testPanel.setBackground(Color.RED);
		testPanel.setBounds(10, 10, 20, 40);
		handPanel.add(testPanel);*/

	
		
		this.add(handPanel);
		this.add(controlPanel);
	}

	
	
	private void createControlPanel() {
		controlPanel = new JPanel();
		//controlPanel.setLayout(new FlowLayout());
		controlPanel.setOpaque(true);
		controlPanel.setBackground(Color.GREEN);//temp
		controlPanel.setPreferredSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .3)));
		controlPanel.setMinimumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .3)));
		controlPanel.setMaximumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .3)));
		
	}

	private void createHandPanel() {
		handPanel = new JPanel();
		handPanel.setLayout(null);//so that Card objects can be positioned
		handPanel.setBackground(Color.BLUE);//temp
		handPanel.setOpaque(true);
		handPanel.setPreferredSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .7)));
		handPanel.setMinimumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .7)));
		handPanel.setMaximumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .7)));
	}
}
