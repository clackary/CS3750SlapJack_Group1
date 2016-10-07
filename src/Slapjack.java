import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;

public class Slapjack extends JFrame{
	
	Board board;

	public Slapjack(){
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
				if(board.isSoundOn()){
					board.setSound(false);
					toggleSound.setText("Turn On");
					//I haven't added any sound yet, but I am working on that now. This just makes it easy to implement.
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
