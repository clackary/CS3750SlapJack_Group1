
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.RadialGradientPaint;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

import sun.misc.Queue;

public class Player extends JPanel
{
	static Color btnColor_playersTurn = new Color(0,102,29);
	static Color btnColor_regular = new Color(178,18,18);
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
		
		createHandPanel();		
		createControlPanel();
		createButtons();
		
		//Player 1 has A and S keys
		//Player 2 has K and L keys
		btn_playTopCard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke(playerID==1 ? 'a' : 'k'), "playTopCard");
		btn_playTopCard.getActionMap().put("playTopCard", new AbstractAction () {
		    public void actionPerformed(ActionEvent arg0) {
		    	if (!hand.isEmpty()){
		    		theBoard.placeCardOnCenterPile(playTopCard());
		    		theBoard.togglePlayersTurn();
		    	}
		    }
		});
		
		btn_slap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(playerID == 1 ? 's' : 'l'), "slap");
		btn_slap.getActionMap().put("slap", new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				theBoard.slap(playerID);
			}
		});
        
				
		btn_playTopCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				theBoard.placeCardOnCenterPile(playTopCard());
				theBoard.togglePlayersTurn();
			}
		});
		
		btn_slap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton)e.getSource();
				Player player = (Player)btn.getParent().getParent();
				theBoard.slap(player.playerID);
			}
		});
		
		
			
		controlPanel.add(btn_playTopCard);
		controlPanel.add(btn_slap);

		this.add(handPanel);
		this.add(controlPanel);
	}

	private void createButtons() {
		btn_playTopCard = new JButton("Play Top Card" + "      ["
				+ (playerID == 1 ? "A" : "K") + "]");
		btn_slap = new JButton("Slap      ["
				+ (playerID == 1 ? "S" : "L") + "]");
		btn_playTopCard.setLocation((int) (controlPanel.getPreferredSize().getWidth()/2)-150, 0);
		btn_slap.setLocation((int) (controlPanel.getPreferredSize().getWidth()/2)-150, 60);
		configureButtons(btn_playTopCard);
		configureButtons(btn_slap);
	}

	private void configureButtons(JButton button) {
		button.setFocusPainted(false);
		button.setSize(300, 45);
		button.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 24));
		button.setBackground(btnColor_regular);
		button.setForeground(Color.WHITE);
		button.setBorder(new LineBorder(Color.WHITE, 2));
	}
	
	public void showPlayersTurn(boolean playerIsUp){
		if (playerIsUp){
			btn_playTopCard.setBackground(btnColor_playersTurn);
		}else{
			btn_playTopCard.setBackground(btnColor_regular);
		}
	}
	
	public void addHandToBoard(){
		//first clear the cards that are already on the board
		handPanel.removeAll();
		handPanel.repaint();//necessary?  don't know
		
		// xPos and yPos are for centering the card
		int xPos = (int) (handPanel.getPreferredSize().getWidth() /2 - (Card.CARD_WI + 100) / 2) ;
		int yPos = (int) (handPanel.getPreferredSize().getHeight() /2 - (Card.CARD_HI + 60) / 2) ;
		
		double radians = playerID==1 ? .07 : -.12; //so the hands aren't identically messy
		for (Card c : hand){
			c.setFaceUp(false);
			c.setBounds(xPos, yPos, Card.CARD_WI + 100, Card.CARD_HI + 60);
			c.setRotation(radians*.45);
			handPanel.add(c);
			radians+=(playerID==2 ? -.012 : .015);
		}
	}
	
	public void addCardsToHand(ArrayList<Card> cardsToAdd){
		for (Card c : cardsToAdd){
			hand.add(c);
		}
	}
	
	public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
	
    
    //When a Jack occurs slap will be called by player and the pile
    // from the center will be given to the player who slapped first.
    public void slap(ArrayList<Card> pile)
    {
        if (pile.isEmpty()) {
            System.out.println("Too late! Pile is empty");
        }
        
        if (pile.get(0).getValueName().equals(Card.Value.JACK)) {
            //Jack is top card of pile, jack was slapped
            //Player gets pile, add pile to player's hand
            
        } else {
            //Jack was not slapped, add pile to opponent's hand.
        }
        
        return;
    }
    
    //Removes the top card from the hand of player.
    //and returns it to be placed in the center pile
    public Card playTopCard()
    {
        Card topCard;
        topCard = hand.get(0);
        hand.remove(0);
        return topCard;
    }
    
    //Returns size of the hand
    public int handSize()
    {
        return hand.size();
    }
    
    //When player places a Jack then center pile is added in hand using this function
    public void addCenterPileToHand(ArrayList<Card> centerPile){
        for (Card c : centerPile) {
            hand.add(c);
        }
    }
    
	
	private void createControlPanel() {
		controlPanel = new JPanel();	
		controlPanel.setOpaque(false);
		controlPanel.setPreferredSize(new Dimension(screenWidth/3, (int)(screenHeight * .3)));
		controlPanel.setMinimumSize(new Dimension(screenWidth/3, (int)(screenHeight * .3)));
		controlPanel.setMaximumSize(new Dimension(screenWidth/3, (int)(screenHeight * .3)));
		controlPanel.setLayout(null);
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
