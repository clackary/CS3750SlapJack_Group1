import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class Slapjack extends JFrame{
	
	private boolean soundOn = true;
	
	private String playTopKey_p1 = "";
	private String slapKey_p1 = "";
	
	private Board board;
	private MenuActionListener mal = new MenuActionListener();
	
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
		
		JMenu setKeysMenu_Player1 = new JMenu("Player 1");
		
		ArrayList<JMenuItem> playTopMenuItems_p2 = new ArrayList<>();
		
		ArrayList<JMenuItem> slapMenuItems_p2 = new ArrayList<>();
		String [] keyChoices_p1 = {"1", "2","3","4","Q","W","E","R","A","S","D","F","Z","X","C","V"};
		String [] keyChoices_p2 = {"7", "8","9","0","U","I","O","P","J","K","L",";","N","M",",","."};
		
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
		
		
	/*	for (String choice : keyChoices_p1){
			playTopMenuItems_p1.add(new JMenuItem(choice));
			playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1).setName("ptp1");
			playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1).addActionListener(mal);
			playTop_p1.add(playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1));
		}
		for (String choice : keyChoices_p1){
			playTopMenuItems_p1.add(new JMenuItem(choice));
			playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1).setName("ptp1");
			playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1).addActionListener(mal);
			playTop_p1.add(playTopMenuItems_p1.get(playTopMenuItems_p1.size()-1));
		}*/
		
		
		/*JMenuItem slap_p1_1 = new JMenuItem("1");
		JMenuItem slap_p1_2 = new JMenuItem("2");
		JMenuItem slap_p1_3 = new JMenuItem("W");
		JMenuItem slap_p1_4 = new JMenuItem("S");
		JMenuItem slap_p1_5 = new JMenuItem("X");
		
		
		
		slap_p1.add(slap_p1_1);
		slap_p1.add(slap_p1_2);
		slap_p1.add(slap_p1_3);
		slap_p1.add(slap_p1_4);
		slap_p1.add(slap_p1_5);*/
		
		setKeysMenu_Player1.add(playTop_p1);
		setKeysMenu_Player1.add(slap_p1);
		setKeysMenu.add(setKeysMenu_Player1);
		optionsMenu.add(setKeysMenu);
		
		
		menuBar.add(gameMenu);
		menuBar.add(soundMenu);
		menuBar.add(optionsMenu);
		this.setJMenuBar(menuBar);
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








/*	private void createKeyBindingsPanel(){
		String[] playTop_choices_1 = {"A","S","D"};
		String[] playTop_choices_2 = {"J","K","L"};
		String[] slap_choices_1 = {"A","S","D"};
		String[] slap_choices_2 = {"J","K","L"};
		
		JComboBox playTop_comboBox_1 = new JComboBox(playTop_choices_1);
		JComboBox playTop_comboBox_2 = new JComboBox(playTop_choices_2);
		JComboBox slap_comboBox_1 = new JComboBox(slap_choices_1);
		JComboBox slap_comboBox_2 = new JComboBox(slap_choices_2);
		
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
		player1KeysPanel.add(playTop_comboBox_1);
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
	}*/