
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.RadialGradientPaint;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

import sun.misc.Queue;

public class Player extends JPanel
{
	static Color btnColor_playersTurn = new Color(0,102,29);
	static Color btnColor_playersTurn_hover = new Color(0,131,29);
	static Color btnColor_regular = new Color(178,18,18);
	static Color btnColor_regular_hover = new Color(208,18,18);
	Board theBoard;
	ArrayList<Card> hand;
	JPanel handPanel, controlPanel;
	JButton btn_playTopCard, btn_slap;
	Card[] testBacks;
	int playerID;
	
	String playTopActionKey;
	String slapActionKey;
	
	int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public Player(Board board, int id){
		theBoard = board;
		playerID = id;
		playTopActionKey = (playerID == 1 ? "A" : "K");//initializing the action keys
		slapActionKey = (playerID == 1 ? "S" : "L");
		hand = new ArrayList<Card>();
		
		this.setPreferredSize(new Dimension(screenWidth/3, screenHeight));
		this.setMaximumSize(new Dimension(screenWidth/3, screenHeight));
		this.setMinimumSize(new Dimension(screenWidth/3, screenHeight));
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setOpaque(false);
		
		createHandPanel();		
		createControlPanel();
		createButtons();
		
		setActionKeys();
        
				
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
				theBoard.slappedByPlayer(player.playerID);
			}
		});
		
		
			
		controlPanel.add(btn_playTopCard);
		controlPanel.add(btn_slap);

		this.add(handPanel);
		this.add(controlPanel);
	}

	private void setActionKeys() {
		//Player 1 has A and S keys
		//Player 2 has K and L keys
		btn_playTopCard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put(KeyStroke.getKeyStroke(playTopActionKey), "playTopCard");
		btn_playTopCard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put(KeyStroke.getKeyStroke(playTopActionKey.toLowerCase()), "playTopCard");
		
	/*	btn_playTopCard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke(playerID==1 ? 'a' : 'k'), "playTopCard");
		btn_playTopCard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put(KeyStroke.getKeyStroke(playerID==1 ? 'A' : 'K'), "playTopCard");//also capital letters
*/		btn_playTopCard.getActionMap().put("playTopCard", new AbstractAction () {
		    public void actionPerformed(ActionEvent arg0) {
		    	if (!hand.isEmpty()){
		    		theBoard.placeCardOnCenterPile(playTopCard());
		    		theBoard.togglePlayersTurn();
		    	}
		    }
		});

		btn_slap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(slapActionKey),
				"slap");
		btn_slap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(slapActionKey.toLowerCase()),
				"slap");// also capital letters
		
		/*btn_slap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(playerID == 1 ? 's' : 'l'), "slap");
		btn_slap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put(KeyStroke.getKeyStroke(playerID == 1 ? 'S' : 'L'), "slap");//also capital letters
*/		btn_slap.getActionMap().put("slap", new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				theBoard.slappedByPlayer(playerID);
			}
		});
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

	/*  a method to do 1000 things to buttons but not
	 *  have to repeat the same code twice.
	 */
	private void configureButtons(JButton button) {
		button.setFocusPainted(false);
		button.setSize(300, 45);
		button.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 24));
		button.setBackground(btnColor_regular);
		button.setForeground(Color.WHITE);
		
		//button.setBorder(new LineBorder(Color.WHITE, 2));//looks crappy with rounded buttons
		button.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e) {
			      JButton btn = (JButton)e.getSource();
			      if (btn.getBackground()== btnColor_playersTurn)
			    	  btn.setBackground(btnColor_playersTurn_hover);
			      else
			    	  btn.setBackground(btnColor_regular_hover);
			  }
			public void mouseExited(MouseEvent e) {
			      JButton btn = (JButton)e.getSource();
			      if (btn.getBackground()==btnColor_playersTurn_hover)
			    	  btn.setBackground(btnColor_playersTurn);
			      else
			    	  btn.setBackground(btnColor_regular);
			  }
		});
		button.putClientProperty("Button.disabled", btnColor_regular_hover);		
	}
	
	public void changeActionKeys(String whichButton, String text){
		switch (whichButton){
		case "playTop":
			String previousKey_PT = btn_playTopCard.getText().substring(btn_playTopCard.getText().length()-2, btn_playTopCard.getText().length()-1);
			playTopActionKey = text;
			if (btn_slap.getText().contains("[" + playTopActionKey)){
				JOptionPane.showMessageDialog(theBoard, "Don't set both Play Top and Slap to the same key.",
						"Trouble", JOptionPane.WARNING_MESSAGE);
			}else{
				btn_playTopCard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.remove(KeyStroke.getKeyStroke(previousKey_PT));//clear out old action key
				btn_playTopCard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.remove(KeyStroke.getKeyStroke(previousKey_PT.toLowerCase()));//clear out old action key lowercase
				btn_playTopCard.setText("Play Top Card" + "      [" + playTopActionKey + "]");
				setActionKeys();
			}
			break;
		case "slap":
			String previousKey_S = btn_slap.getText().substring(btn_slap.getText().length()-2, btn_slap.getText().length()-1);
			slapActionKey = text;
			if (btn_playTopCard.getText().contains("[" + slapActionKey)){
				JOptionPane.showMessageDialog(theBoard, "Don't set both Play Top and Slap to the same key.",
						"Trouble", JOptionPane.WARNING_MESSAGE);
			}else{
				btn_slap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.remove(KeyStroke.getKeyStroke(previousKey_S));//clear out old action key
				btn_slap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.remove(KeyStroke.getKeyStroke(previousKey_S.toLowerCase()));//clear out old action key lowercase
				btn_slap.setText("Slap      [" + slapActionKey + "]");
				setActionKeys();
				break;
			}
		}
	}
	
	
	/*  Changes playTopCard button color for the player
	 *  whose turn it is
	 *  
	 *  COMMENTED IT OUT because it's not working well with Nimbus Look and Feel
	 */
	/*public void showPlayersTurn(boolean playerIsUp){
		if (playerIsUp){
			btn_playTopCard.setBackground(btnColor_playersTurn);
		}else{
			btn_playTopCard.setBackground(btnColor_regular);
		}
	}*/
	
	//called by Board to toggle who can use the button
	public void setPlayButtonEnabled(boolean enabled){
		btn_playTopCard.setEnabled(enabled);
		if (!btn_playTopCard.isEnabled())
			btn_playTopCard.setForeground(Color.GRAY);
		else
			btn_playTopCard.setForeground(Color.WHITE);
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
