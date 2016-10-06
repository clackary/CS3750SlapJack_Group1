
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.RadialGradientPaint;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
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
		
		//Player 1 has A and S keys
		if (this.playerID == 1) {
		    btn_playTopCard.getInputMap().put(KeyStroke.getKeyStroke('a'), "playTopCard");
		btn_playTopCard.getActionMap().put("playTopCard", new AbstractAction () {
		    public void actionPerformed(ActionEvent arg0) {
		        theBoard.placeCardOnCenterPile(playTopCard());
		    }
		});
		}

		//Player 2 has K and L
		if (this.playerID == 2) {
            btn_playTopCard.getInputMap().put(KeyStroke.getKeyStroke('k'), "playTopCard");
        btn_playTopCard.getActionMap().put("playTopCard", new AbstractAction () {
            public void actionPerformed(ActionEvent arg0) {
                theBoard.placeCardOnCenterPile(playTopCard());
            }
        });
        }
        
				
		btn_playTopCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				theBoard.placeCardOnCenterPile(playTopCard());
			}
		});
		
		btn_slap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton)e.getSource();
				Player player = (Player)btn.getParent().getParent();
				theBoard.slap(player.playerID);
			}
		});
		
		createHandPanel();		
		createControlPanel();
			
		controlPanel.add(btn_playTopCard);
		controlPanel.add(btn_slap);

		this.add(handPanel);
		this.add(controlPanel);
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
		controlPanel.setPreferredSize(new Dimension(screenWidth/3, (int)(screenHeight * .25)));
		controlPanel.setMinimumSize(new Dimension(screenWidth/3, (int)(screenHeight * .25)));
		controlPanel.setMaximumSize(new Dimension(screenWidth/3, (int)(screenHeight * .25)));
		
	}

	private void createHandPanel() {
		handPanel = new JPanel();
		handPanel.setOpaque(false);
		handPanel.setLayout(null);//so that Card objects can be positioned
		handPanel.setPreferredSize(new Dimension(screenWidth/3, (int)(screenHeight * .75)));
		handPanel.setMinimumSize(new Dimension(screenWidth/3, (int)(screenHeight * .75)));
		handPanel.setMaximumSize(new Dimension(screenWidth/3, (int)(screenHeight * .75)));
	}
	

	
}
