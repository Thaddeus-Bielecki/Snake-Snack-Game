import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Font.*;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.*;

import javax.swing.JPanel;

/**
 * Class to represent the panel to house the game and perform required functionalities
 * @author Thaddeus Bielecki
 *
 */
public class GamePanel extends JPanel implements ActionListener{

	//constants
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75; //higher number results in slower game
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	int bodyParts = 6; //initial length of snake
	int applesEaten = 0; //set apples eaten aka score
	//to represent the coordinates of the apple
	int appleX;
	int appleY;
	
	//direction of the snake's current movement
	char direction = 'R';
	
	//to be true while program is running
	boolean running = false;
	
	//required instances of outside classes
	Timer timer;
	Random random;
	Graphics g;
	
	/**
	 * Constructor - set up the panel and begin the game
	 */
	GamePanel(){
		//initialize random of the Random class
		random = new Random();
		
		//set up the layout of the game area
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		
		//add the key listener and start the game
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	/**
	 * To begin the game
	 */
	public void startGame() {
		//spawn in the apple
		newApple();
		
		//set running to true and start the timer
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();

	}
	
	/**
	 * To paint the game and redraw game elements
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//call the draw method
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) {
			//draw the grid on the screen
			g.setColor(Color.white);
			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			//draw the apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			//draw the snake
			for(int i=0; i<bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					//Optional "disco" mode: (Will allow each block of the snake to be a randomly colored block
					//g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			
			//display the score in the top middle of the screen
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score " + applesEaten))/2, g.getFont().getSize());
	
		} else {
			//if running is false game over
			gameOver(g);
		}
	}
	
	/**
	 * create a new apple
	 */
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	/**
	 * Function to move the snake
	 */
	public void move() {
		//Increment the snake
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		//to change the direction the snake is moving
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
			
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
			
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
			
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	/**
	 * check if the apple has been eaten
	 */
	public void checkApple() {
		//check if the head of the snake is at the same location as the apple
		if((x[0] == appleX) && (y[0] == appleY)) {
			//add 1 to the snake's length
			bodyParts++;
			//increment apples eaten and spawn in a new apple
			applesEaten++;
			newApple();
		}
	}
	
	/**
	 * Check if the snake has run into one of the walls or itself
	 */
	public void checkCollisions() {
		
		//check for head colliding w body
		for(int i = bodyParts; i>0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
			
		}
		
		//check for head touching right border
		if(x[0] >= SCREEN_WIDTH) {
			running = false;
		}
		
		//check for head touching top border
		if(y[0] < 0) {
			running = false;
		}
		
		//check if head touches bottom border
		if(y[0] >= SCREEN_HEIGHT) {
			running = false;
		}
		
		//if no longer running -> stop the game
		if(running == false) {
			timer.stop();
		}
	}
	
	/**
	 * To inform the user the game is over
	 * @param g
	 */
	public void gameOver(Graphics g) {
		//Display the final score at the top middle of the screen
		g.setColor(Color.red);
		g.setFont( new Font("Serif", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score " + applesEaten))/2, g.getFont().getSize());
		
		//Display game over text
		g.setFont( new Font("Serif", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT / 2);
		
	}

	
	@Override
	/**
	 * Override the actionPerformed method
	 */
	public void actionPerformed(ActionEvent e) {
		
		//as long as running is true
		if(running) {
			//move the snake and check the apple and for a collision
			move();
			checkApple();
			checkCollisions();
			
			//repaint the game
			repaint();
		}
	}
	
	/**
	 * Inner class to listen to keys pressed by the user
	 * @author Thaddeius Bielecki
	 *
	 */
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		/**
		 * To Override the parent function
		 */
		public void keyPressed(KeyEvent e) {
			//determine key pressed and adjust the direction of the snake as a result
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
				direction = 'L';
				}
				break;
				
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
				direction = 'R';
				}
				break;
				
			case KeyEvent.VK_UP:
				if(direction != 'D') {
				direction = 'U';
				}
				break;
				
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
				
			}
		}
	}
}
