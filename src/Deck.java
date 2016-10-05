import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class Deck {
	
	//ArrayList to hold cards
	private final ArrayList<Card> deck = new ArrayList<Card>(52);
	
	//Constructor
	public Deck(){
		
	}
	
	//add cards to deck
	public void addCard(Card card){
		deck.add(card);
	}
	
	//see the number of cards in the deck
	public int deckSize(){
		return this.deck.size();
	}
	
	//Clears the deck if needed
	public void clear(){
		deck.clear();
	}
	
	//Create the deck
	public void initialize(){            
            for(Card.Suit suitName : Card.Suit.values()){
                for(Card.Value num : Card.Value.values()){
                    Card card = new Card(suitName, num);
                    this.addCard(card);
                }
            }
	}
	
	//Shuffle the deck
	public void shuffle(){
		Collections.shuffle(deck);
	}
	
	//return contents of the deck
	@Override
	public String toString(){
		int num = 1;
		String display = "";
		for(Card C: deck){
			display += num + ": " + C.toString() + "\n";
			num++;
		}
		System.out.println(display);
		System.out.println();
		return display;
	}
	
	public Card getTopCard() 	
	{ 		
		return deck.get(0);
	}
	
	public Card getBottomCard()
	{
		return deck.get(deck.size() - 1);
	}
	
	public Card getCard(int index)
	{
		return deck.get(index);
	}
	
	/*  this will be called by Board, 
	 *  to be passed to a player.	 * 
	 */
	public ArrayList<Card> getCards(int numCards){
		ArrayList<Card> cardsToGet = new ArrayList<>();
		for (int i=0; i<numCards; i++){
			cardsToGet.add(deck.remove(i));
		}
		return cardsToGet;
	}
	
	public void removeCard()
	{
		deck.remove(0);
	}
	
}
