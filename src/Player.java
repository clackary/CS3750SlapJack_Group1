

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
	
	public Player(Board board){
		theBoard = board;
		hand = new Queue<>();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		btn_playTopCard = new JButton("Play Top Card");
		btn_slap = new JButton("Slap");
		
		handPanel = new JPanel();
		handPanel.setLayout(null);//so that Card objects can be positioned
		handPanel.setBackground(Color.BLUE);
		handPanel.setPreferredSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .7)));
		handPanel.setMinimumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .7)));
		
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		controlPanel.setBackground(Color.GREEN);
		controlPanel.setPreferredSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .3)));
		controlPanel.setMinimumSize(new Dimension((int)(theBoard.getWidth()*.33), (int)(theBoard.getHeight() * .3)));
		controlPanel.add(btn_playTopCard);
		controlPanel.add(btn_slap);
		
		this.add(handPanel);
		this.add(controlPanel);
	}
}
