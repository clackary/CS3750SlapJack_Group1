import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Slapjack extends JFrame{
	
	private boolean soundOn = true;
	
	private Board board;
	private MenuActionListener mal = new MenuActionListener();
	
	//using this to turn off the glow after Player collects center pile
	public static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	public Slapjack(){
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		//takes up whole screen but leaves room at bottom
		this.setSize(new Dimension(screenWidth, screenHeight - (screenHeight / 20))); 
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//createKeyBindingsPanel();//put this before createMenu() so panel exists already
		
		this.repaint();
		board = new Board();
		this.add(board);
		createMenu();
		this.pack();//this way the board JPanel shows up
		
	}
	
	
	private void createMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				newGame();
			}
		});
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				executor.shutdown();
				System.exit(0);
			}
		});
		gameMenu.add(newGame);
		gameMenu.add(exit);
		JMenu soundMenu = new JMenu("Sound");
		final JMenuItem toggleSound = new JMenuItem("Turn Off");
		toggleSound.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(soundOn){
					soundOn = false;
					toggleSound.setText("Turn On");
					//I haven't added any sound yet, but I am working on that now. This just makes it easy to implement.
				}
				else {
					soundOn = true;
					toggleSound.setText("Turn Off");
				}
			}
		});
		soundMenu.add(toggleSound);
		
		JMenu optionsMenu = createOptionsMenu();
		
		
		menuBar.add(gameMenu);
		menuBar.add(soundMenu);
		menuBar.add(optionsMenu);
		this.setJMenuBar(menuBar);
	}


	private JMenu createOptionsMenu() {
		JMenu optionsMenu = new JMenu("Options");
		JMenu setKeysMenu = new JMenu("Set Action Keys");
		
		JMenu setKeysMenu_Player1 = new JMenu("Player 1");
		JMenu setKeysMenu_Player2 = new JMenu("Player 2");
		
		
		
		String [] keyChoices_p1 = {"1", "2","3","4","Q","W","E","R","A","S","D","F","Z","X"};
		String [] keyChoices_p2 = {"7", "8","9","0","U","I","O","P","H","J","K","L","N","M"};
		
		JMenu playTop_p1 = new JMenu("Play Top Card");
		ArrayList<JMenuItem> playTopMenuItems_p1 = new ArrayList<>();
		for (String choice : keyChoices_p1){
			playTopMenuItems_p1.add(new JMenuItem(choice));
			playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1).setName("ptp1");
			playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1).addActionListener(mal);
			playTop_p1.add(playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1));
		}
		
		JMenu slap_p1 = new JMenu("Slap");
		ArrayList<JMenuItem> slapMenuItems_p1 = new ArrayList<>();
		for (String choice : keyChoices_p1){
			slapMenuItems_p1.add(new JMenuItem(choice));
			slapMenuItems_p1.get(slapMenuItems_p1.size()-1).setName("slp1");
			slapMenuItems_p1.get(slapMenuItems_p1.size()-1).addActionListener(mal);
			slap_p1.add(slapMenuItems_p1.get(slapMenuItems_p1.size()-1));
		}
		
		JMenu playTop_p2 = new JMenu("Play Top Card");
		ArrayList<JMenuItem> playTopMenuItems_p2 = new ArrayList<>();
		for (String choice : keyChoices_p2){
			playTopMenuItems_p2.add(new JMenuItem(choice));
			playTopMenuItems_p2.get(playTopMenuItems_p2.size()-1).setName("ptp2");
			playTopMenuItems_p2.get(playTopMenuItems_p2.size()-1).addActionListener(mal);
			playTop_p2.add(playTopMenuItems_p2.get(playTopMenuItems_p2.size()-1));
		}
		
		JMenu slap_p2 = new JMenu("Slap");
		ArrayList<JMenuItem> slapMenuItems_p2 = new ArrayList<>();
		for (String choice : keyChoices_p2){
			slapMenuItems_p2.add(new JMenuItem(choice));
			slapMenuItems_p2.get(slapMenuItems_p2.size()-1).setName("slp2");
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
		new Slapjack();
	}
}

