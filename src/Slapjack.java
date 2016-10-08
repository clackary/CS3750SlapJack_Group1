import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class Slapjack extends JFrame{
	
	private boolean soundOn = true;
	
	//using this to turn off the glow after Player collects center pile
	public static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private JPanel keyBindingsPanel;
	
	public Slapjack(){
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		//takes up whole screen but leaves room at bottom
		this.setSize(new Dimension(screenWidth, screenHeight - (screenHeight / 20))); 
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		createKeyBindingsPanel();//put this before createMenu() so panel exists already
		createMenu();
		this.repaint();
		Board board = new Board();
		this.add(board);
		
		this.pack();//this way the board JPanel shows up
		
	}
	
	private void createKeyBindingsPanel(){
		keyBindingsPanel = new JPanel();
		keyBindingsPanel.setLayout(new BoxLayout(keyBindingsPanel, BoxLayout.X_AXIS));
		JPanel player1KeysPanel = new JPanel();
		JPanel player2KeysPanel = new JPanel();
		player1KeysPanel.setLayout(new GridLayout(3,2));
		player2KeysPanel.setLayout(new GridLayout(3,2));
		
		JLabel plyr_1 = new JLabel("Player 1");
		plyr_1.setHorizontalAlignment(SwingConstants.CENTER);
		player1KeysPanel.add(plyr_1);
		player1KeysPanel.add(Box.createRigidArea(new Dimension(1,1)));//placeholder
		JLabel ptc_1 = new JLabel("Play Top Card:");
		ptc_1.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel slap_1 = new JLabel("Slap:");
		slap_1.setHorizontalAlignment(SwingConstants.RIGHT);
		player1KeysPanel.add(ptc_1);
		player1KeysPanel.add(Box.createRigidArea(new Dimension(1,1)));//placeholder for where dropdown will go
		player1KeysPanel.add(slap_1);
		
		JLabel plyr_2 = new JLabel("Player 2");
		plyr_2.setHorizontalAlignment(SwingConstants.CENTER);
		player2KeysPanel.add(plyr_2);
		player2KeysPanel.add(Box.createRigidArea(new Dimension(1,1)));//placeholder
		JLabel ptc_2 = new JLabel("Play Top Card:");
		ptc_2.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel slap_2 = new JLabel("Slap:");
		slap_2.setHorizontalAlignment(SwingConstants.RIGHT);
		player2KeysPanel.add(ptc_2);
		player2KeysPanel.add(Box.createRigidArea(new Dimension(1,1)));//placeholder for where dropdown will go
		player2KeysPanel.add(slap_2);
		
		
		keyBindingsPanel.add(player1KeysPanel);
		keyBindingsPanel.add(new JSeparator(SwingConstants.VERTICAL));
		keyBindingsPanel.add(player2KeysPanel);
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
		JMenuItem toggleSound = new JMenuItem("Turn Off");
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
		
		JMenu optionsMenu = new JMenu("Options");
		JMenu setKeysMenu = new JMenu("Set Action Keys");
		JMenuItem setKeysMenuItem = new JMenuItem();
		setKeysMenuItem.setPreferredSize(new Dimension(500, 200));
		setKeysMenuItem.add(keyBindingsPanel);
		setKeysMenu.add(setKeysMenuItem);
		optionsMenu.add(setKeysMenu);
		
		
		menuBar.add(gameMenu);
		menuBar.add(soundMenu);
		menuBar.add(optionsMenu);
		this.setJMenuBar(menuBar);
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
