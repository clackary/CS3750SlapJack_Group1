

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
	private ArrayList<Card> hand;
//    private Queue<Card> hand;
	JPanel handPanel, controlPanel;
	JButton btn_playTopCard, btn_slap;
	Card[] testBacks;
	int playerID;
	
	public Player(Board board, int id){
		theBoard = board;
		playerID = id;
		hand = new ArrayList<Card>();
		
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

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
    
    /** 
    * When a Jack occurs slap will be called by player and the pile
    * from the center will be given to the player who slapped first.
    * 
    * Returns true and adds cards to player's hand if player is the first
    * to slap. Returns false if the pile is empty, or if the card that 
    * was slapped was not a jack.
    * 
    * Note: Board is responsible for resetting the pile after slap returns.
    **/
    public boolean slap(ArrayList<Card> pile)
    {
//        if (pile.isEmpty()) {
//            System.out.println("Too late! Pile is empty");
//            return false;
//        }
        
        //Jack is top card of pile, jack was slapped
        if (!pile.isEmpty() && pile.get(0).getValueName().equals(Card.Value.JACK)) {
            //Player gets pile, add pile to player's hand
            for (Card c : pile) {
                hand.add(c);
            }
            return true;
        } else {
            //Jack was not slapped, add pile to opponent's hand.
            return false;
        }        
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
            this.hand.add(c);
        }
    }
    
	public void addCardsToHand(ArrayList<Card> cardsToAdd){
		for (Card c : cardsToAdd){
			this.hand.add(c);
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

    /**
     * Adds a single card to bottom of player's hand.
     */
    public void addCardToHand (Card card)
    {
        this.hand.add(card);
    }
}
