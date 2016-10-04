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
		centerPanel.setBackground(Color.WHITE);//temporary
		
		board.add(player1);
		board.add(centerPanel);
		board.add(player2);
	}

	public boolean isTopCardJack(){
		return false;
	}
	
	private void createPlayers() {
		player1 = new Player(this);
		player2 = new Player(this);
		
		player1.setBackground(Color.PINK);//temporary
		player2.setBackground(Color.CYAN);//temporary
	}

	private void createBoard() {
		board = new JPanel();
		board.setBackground(Color.GREEN); //temporary, just for setting up layout
		
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
