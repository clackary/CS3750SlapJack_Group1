import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Card extends JPanel{ //Variables
    private final Suit suit; 
    private final Value num;
    private final Toolkit tools  = Toolkit.getDefaultToolkit();
    private Image img;
    //private String urlString;
    private URL url;
    private double rotationRadians;
    
    public static final int CARD_WI = 230;
	public static final int CARD_HI = (int) (CARD_WI * 1.452); // Keep aspect ratio of card
    
    //Constructor 
    public Card(Suit suit, Value num){
    	
        this.suit = suit; 
        this.num = num;
		String urlString = num.getValueName() + suit.getSuitName() + ".png";
        url = Card.class.getResource(urlString);
        System.out.println("constructor");
        setOpaque(false);  //the Card itself is larger than the image.  It needs to be set opaque(false) so it's see-through around the image.
        
    }
    
    // Default for adding blanks.
    public Card(){
        this.suit = null;
        this.num = null;
        setOpaque(false);
        String urlString = "back2.jpg";
        url = Card.class.getResource(urlString);
    }
    
    @Override
    public void paintComponent(Graphics g){
    	
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
    	System.out.println("paintComponent");
    	/*  NOTE: to save anyone's sanity:  the image has to be instantiated HERE, not in the constructor.
    	 *  Trust.
    	 *  Also, if the image comes from getScaledInstance(), it will NOT SHOW UP.
    	 *  I have saved you, dear Reader, hours of your life.
    	 */
    	img = new ImageIcon(url).getImage();//.getScaledInstance(CARD_WI, -1, Image.SCALE_SMOOTH);
    	g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  //Not sure...this makes edges a little smoother?
    	
    	/* rotation could be some sort of method
    	 * that gets random radians values passed in,
    	 * so that the card pile can appear messy.
    	 * Something to think about.
    	 */
    	int originX = this.getWidth()/2;
    	int originY = this.getHeight()/2;
    	g2.rotate(rotationRadians, originX, originY);
        g2.drawImage(img, 50, 30, CARD_WI, CARD_HI, null);  
    }
    
    public void setRotation(double radians){
    	rotationRadians = radians;
    }
    
    public Image getImage(){
        return img;
    }
    
    //getters 
    //return Suit name 
    public String getSuit(){
    	return suit.getSuitName();
    }
    //return suit number 0,1,2 or 3 
    public int getSuitNumber(){ return suit.getSuitNum(); }
    //return Value name 
    public String getValueName(){ return num.getValueName(); }
    //return value of the card 
    public int getCardValue(){ return num.getValue(); } //end getters
    //Display cards 
    @Override 
    public String toString(){ 
        String display = "";
        display += num.getValue() + " of " + suit.getSuitName(); 
        return display;
    }
    
 enum Suit {
    CLUBS("Clubs", 0), DIAMONDS("Diamonds", 1), HEARTS("Hearts", 2), SPADES("Spades", 3);
    //Variables 
    private final String suitName; 
    private final int suitNum;
    //Constructor 
    private Suit(String suitName, int suitNum){
        this.suitName = suitName; 
        this.suitNum = suitNum;
    }
    //getters 
    public int getSuitNum(){ return suitNum; }
    public String getSuitName(){ return suitName; }
    }
    
 
 enum Value { TWO("Two", 2), THREE("Three", 3), FOUR("Four", 4), 
        FIVE("Five", 5), SIX("Six", 6), SEVEN("Seven", 7), EIGHT("Eight", 8), 
        NINE("Nine", 9), TEN("Ten", 10), JACK("Jack", 11), QUEEN("Queen", 12), 
        KING("King", 13), ACE("Ace", 1);
    //Variables
    private final String valueName; 
    private final int value;
    private Value(String valueName, int value){ 
        this.valueName = valueName;
        this.value = value;
    }
    //getters 
    public String getValueName(){ return valueName; }
    public int getValue(){ return value; }
    }
}

     
