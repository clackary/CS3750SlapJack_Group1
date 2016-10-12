import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;


public class Slapjack extends JFrame{
	
	private boolean soundOn = true;
	
	private Board board;
	private MenuActionListener mal = new MenuActionListener();
	
	private final URL iconURL = Slapjack.class.getResource("slapjack.png");
	private final Image icon = new ImageIcon(iconURL).getImage();
	
	//using this to turn off the glow after Player collects center pile
	public static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	private static Font rulesFont = new Font("Verdana", Font.PLAIN, 20);
	private static Font actionKeysFont = new Font("Verdana", Font.PLAIN, 16);
	
	public Slapjack(){
		this.setIconImage(icon);
		this.setTitle("Slapjack - Group 1");
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		//takes up whole screen but leaves room at bottom
		this.setSize(new Dimension(screenWidth, screenHeight - (screenHeight / 20))); 
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
//		this.repaint(); //not necessary?
		board = new Board();
		this.add(board);
		createMenu();
		this.pack();//this way the board JPanel shows up
	}
	
	
	private void createMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenuItem newGame = new JMenuItem("New Game", 'N');
		newGame.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		newGame.setToolTipText("Starts A New Game");
		newGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				newGame();
			}
		});
		JMenuItem exit = new JMenuItem("Exit", 'E');
		exit.setToolTipText("Exits The Game");
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		gameMenu.add(newGame);
		gameMenu.add(exit);
		
		JMenu soundMenu = new JMenu("Sound");
		JMenuItem toggleSound = new JMenuItem("Turn Off", 'O');
		toggleSound.setAccelerator(KeyStroke.getKeyStroke("alt S"));
		toggleSound.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(board.isSoundOn()){
					board.setSound(false);
					toggleSound.setText("Turn On");
				}
				else {
					board.setSound(true);
					toggleSound.setText("Turn Off");
				}
			}
		});
		soundMenu.add(toggleSound);
		
		JMenu optionsMenu = createOptionsMenu();		
		JMenu helpMenu = createHelpMenu();
		
		gameMenu.setMnemonic('g');
		soundMenu.setMnemonic('s');
		optionsMenu.setMnemonic('o');
		helpMenu.setMnemonic('h');
		
		menuBar.add(gameMenu);
		menuBar.add(soundMenu);
		menuBar.add(optionsMenu);
		menuBar.add(helpMenu);
		this.setJMenuBar(menuBar);
	}


	private JMenu createHelpMenu() {
		JMenu helpMenu = new JMenu("Help");
		JMenu gameRules = new JMenu("How To Play");
		gameRules.setMnemonic('h');
		JMenuItem gameRulesItem = new JMenuItem();
		gameRulesItem.setPreferredSize(new Dimension(760,680));
		JPanel rulesPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		JEditorPane textPane = new JEditorPane();
		textPane.setFont(rulesFont);
		String howToPlay = 
				"<html><strong>Slapjack Rules:</strong>"
						+ "<hr><br>"
				+ "Each player begins with a hand of 26 cards facing down.<p>"
				+ "The players alternate placing their top card onto the board, forming a center pile.<p>"
				+ "When a Jack appears, the first player to <strong>SLAP THE JACK</strong> collects the entire center pile.<p>"
				+ "<strong><font color=\"red\">Be careful!</font></strong><br>  If a player slaps a card that is NOT a jack, the other player collects the entire center pile.<p>"
				+ "When a player runs out of cards, he or she gets <br><strong>ONE MORE CHANCE</strong> to slap the next appearing jack. "
				+ "<br>If successful, that player gets more cards and keeps playing.  Otherwise, that player loses.<p>"
				+ "Play continues until a player loses.  The other player wins!  <p>"
				+ "<i>(Unless both players run out of cards and fail to slap jacks...then they both lose.)</i><p><p>"
				+ "<strong>Controls:</strong>"
				+ "<hr><br>"
				+ "Players may click the appropriate buttons, but it is easier to use the action keys "
				+ "indicated on the buttons.<p>For example, Slap [S] means press S for slap.<p>  A player may not place his or her top card when the<br>"
				+ "\"Play Top Card\" button is disabled (gray), but both players may activate \"Slap\" at any time.<p>"
				+ "Action keys may be changed in the Options menu."
				+ "</html>";
		scrollPane.setPreferredSize(new Dimension(730, 680));
		textPane.setMargin(new Insets(20,20,20,20));
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		textPane.setText(howToPlay);
		scrollPane.getViewport().add(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		rulesPanel.add(scrollPane);
		gameRulesItem.add(rulesPanel);
		gameRules.add(gameRulesItem);
		helpMenu.add(gameRules);
		return helpMenu;
	}


	private JMenu createOptionsMenu() {
		JMenu optionsMenu = new JMenu("Options");
		JMenu setKeysMenu = new JMenu("Set Action Keys");
		setKeysMenu.setMnemonic('k');
		
		JMenu setKeysMenu_Player1 = new JMenu("Player 1");
		JMenu setKeysMenu_Player2 = new JMenu("Player 2");
		setKeysMenu_Player1.setMnemonic('1');
		setKeysMenu_Player2.setMnemonic('2');
		
		String [] keyChoices_p1 = {"1", "2","3","4","Q","W","E","R","A","S","D","F","Z","X"};
		String [] keyChoices_p2 = {"7", "8","9","0","U","I","O","P","H","J","K","L","N","M"};
		
		JMenu playTop_p1 = new JMenu("Play Top Card");
		playTop_p1.setMnemonic('p');
		ArrayList<JMenuItem> playTopMenuItems_p1 = new ArrayList<>();
		for (String choice : keyChoices_p1){
			playTopMenuItems_p1.add(new JMenuItem(choice, choice.charAt(0)));
			playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1).setName("ptp1");
			playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1).setFont(actionKeysFont);
			playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1).addActionListener(mal);
			playTop_p1.add(playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1));
		}
		
		JMenu slap_p1 = new JMenu("Slap");
		slap_p1.setMnemonic('s');
		ArrayList<JMenuItem> slapMenuItems_p1 = new ArrayList<>();
		for (String choice : keyChoices_p1){
			slapMenuItems_p1.add(new JMenuItem(choice, choice.charAt(0)));
			slapMenuItems_p1.get(slapMenuItems_p1.size()-1).setName("slp1");
			slapMenuItems_p1.get(slapMenuItems_p1.size()-1).setFont(actionKeysFont);
			slapMenuItems_p1.get(slapMenuItems_p1.size()-1).addActionListener(mal);
			slap_p1.add(slapMenuItems_p1.get(slapMenuItems_p1.size()-1));
		}
		
		JMenu playTop_p2 = new JMenu("Play Top Card");
		playTop_p2.setMnemonic('p');
		ArrayList<JMenuItem> playTopMenuItems_p2 = new ArrayList<>();
		for (String choice : keyChoices_p2){
			playTopMenuItems_p2.add(new JMenuItem(choice, choice.charAt(0)));
			playTopMenuItems_p2.get(playTopMenuItems_p2.size()-1).setName("ptp2");
			playTopMenuItems_p2.get(playTopMenuItems_p2.size()-1).setFont(actionKeysFont);
			playTopMenuItems_p2.get(playTopMenuItems_p2.size()-1).addActionListener(mal);
			playTop_p2.add(playTopMenuItems_p2.get(playTopMenuItems_p2.size()-1));
		}
		
		JMenu slap_p2 = new JMenu("Slap");
		slap_p2.setMnemonic('s');
		ArrayList<JMenuItem> slapMenuItems_p2 = new ArrayList<>();
		for (String choice : keyChoices_p2){
			slapMenuItems_p2.add(new JMenuItem(choice, choice.charAt(0)));
			slapMenuItems_p2.get(slapMenuItems_p2.size()-1).setName("slp2");
			slapMenuItems_p2.get(slapMenuItems_p2.size()-1).setFont(actionKeysFont);
			slapMenuItems_p2.get(slapMenuItems_p2.size()-1).addActionListener(mal);
			slap_p2.add(slapMenuItems_p2.get(slapMenuItems_p2.size()-1));
		}
		
		setKeysMenu_Player1.add(playTop_p1);
		setKeysMenu_Player1.add(slap_p1);
		setKeysMenu_Player2.add(playTop_p2);
		setKeysMenu_Player2.add(slap_p2);
		setKeysMenu.add(setKeysMenu_Player1);
		setKeysMenu.add(setKeysMenu_Player2);
		optionsMenu.add(setKeysMenu);
		return optionsMenu;
	}
	
	class MenuActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem menuItem = (JMenuItem)e.getSource();
			String text;
			switch (menuItem.getName()){
			case "ptp1":
				text = menuItem.getActionCommand();
				board.setActionKeys(1, "playTop", text);
				break;
			case "slp1":
				text = menuItem.getActionCommand();
				board.setActionKeys(1, "slap", text);
				break;
			case "ptp2":
				text = menuItem.getActionCommand();
				board.setActionKeys(2, "playTop", text);
				break;
			case "slp2":
				text = menuItem.getActionCommand();
				board.setActionKeys(2, "slap", text);
				break;
			}
		}
	}
	
	public boolean isSoundOn(){
		return soundOn;
	}
	
	private void newGame(){
		this.dispose();
		new Slapjack();
	}
	
	public static void main(String[] args){
		try{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) { //Nimbus UI
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		}catch(Exception e){
			e.printStackTrace(); //this should really never happen
		}
		new Slapjack();
	}
}

