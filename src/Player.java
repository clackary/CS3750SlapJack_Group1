

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;

import sun.misc.Queue;

public class Player extends JPanel
{
	Board theBoard;
	Queue<Card> hand;
	JPanel handPanel, controlPanel;
	
	public Player(Board board){
		theBoard = board;
		hand = new Queue<>();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		handPanel = new JPanel();
		handPanel.setBackground(Color.BLUE);
		handPanel.setPreferredSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .66)));
		handPanel.setMinimumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .66)));
		
		controlPanel = new JPanel();
		controlPanel.setBackground(Color.GREEN);
		controlPanel.setPreferredSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .66)));
		controlPanel.setMinimumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .33)));
		
		this.add(handPanel);
		this.add(controlPanel);
	}
}
