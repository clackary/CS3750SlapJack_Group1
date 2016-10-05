import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Card extends JPanel{ //Variables
    private final Suit suit; 
    private final Value num;
    private final Toolkit tools  = Toolkit.getDefaultToolkit();
    private Image img;
    
    public static final int CARD_WI = 150;
	public static final int CARD_HI = (int) (CARD_WI * 1.452); // Keep aspect ratio of card
    
    //Constructor 
    public Card(Suit suit, Value num){
        this.suit = suit; 
        this.num = num;
        int w = (tools.getScreenSize().width/6)-80;//card width
        int h = (int) (w*1.45);
        String urlString = "/images/" + num.getValueName() + suit.getSuitName() + ".png";
        /*if(num.getValue() > 10 || num.getValue() == 1){
            urlString = "images/"+num.getValueName()+suit.getSuitName()+".png";
        } else{
            urlString = "images/"+num.getValueName()+suit.getSuitName()+".png";
            System.out.println(urlString);
        }*/
        URL url = getClass().getResource(urlString);
        System.out.println(url.toString());
        img = new ImageIcon(url).getImage().getScaledInstance(50, -1, Image.SCALE_SMOOTH);
        Dimension prefSize = new Dimension(CARD_WI, CARD_HI);
        this.setPreferredSize(prefSize);
    }
    // Default for adding blanks.
    public Card(){
        this.suit = null;
        this.num = null;
        this.setBackground(Color.RED);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
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

     
