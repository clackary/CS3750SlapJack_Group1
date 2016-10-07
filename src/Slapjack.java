import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Slapjack extends JFrame{
	
	Board board;
	private final URL iconURL = Slapjack.class.getResource("images/slapjack.png");
	private final Image icon = new ImageIcon(iconURL).getImage();
	//using this to turn off the glow after Player collects center pile
	public static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public Slapjack(){
		this.setIconImage(icon);
		this.setTitle("Slapjack - Group 1");
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		//takes up whole screen but leaves room at bottom
		this.setSize(new Dimension(screenWidth, screenHeight - (screenHeight / 20))); 
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		createMenu();
		this.repaint();
		board = new Board();
		this.add(board);
		this.pack();//this way the board JPanel shows up

	}
	
	private void createMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenuItem changeKeys = new JMenuItem("Change Keys", 'C');
		changeKeys.setToolTipText("Change The Keys Used To Place Cards And Slap");
		changeKeys.setAccelerator(KeyStroke.getKeyStroke("alt C"));
		changeKeys.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Do something here - probably call board
			}
		});
		gameMenu.add(changeKeys);
		gameMenu.addSeparator();
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
		menuBar.add(gameMenu);
		menuBar.add(soundMenu);
		this.setJMenuBar(menuBar);
	}
	
	private void newGame(){
		this.dispose();
		new Slapjack();
	}
	
	public static void main(String[] args){
		try{
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //The System UI
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
