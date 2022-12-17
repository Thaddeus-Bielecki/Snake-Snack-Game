import javax.swing.JFrame;

/**
 * To represent the window the game will sit in
 * @author Thaddeus Bielecki
 *
 */
public class GameFrame extends JFrame {
	
	//local GamePanel object
	GamePanel myPanel;
	
	/**
	 * Class's constructor
	 */
	GameFrame(){
		
		//initialize the instance of the GamePanel class
		this.add(myPanel = new GamePanel());
		
		//format the Window
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}