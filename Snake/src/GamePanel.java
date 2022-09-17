import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75; //the higher the slower the game
	final int x[]= new int[GAME_UNITS]; 
	final int y[] = new int [GAME_UNITS];
	int bodyParts = 6;
	int applesEaten; 
	int appleX; //location of apple
	int appleY;
	char direction= 'R';
	boolean running = false;
	Timer timer;
	Random random; 
	
	
	GamePanel(){
		random = new Random ();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true; 
		timer = new Timer(DELAY, this);
		timer.start();
	};
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	};
	
	public void draw (Graphics g) {
		
		if(running) {
			for (int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0,i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
		
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i=0;i<bodyParts;i++) {
				if(i==0) {
					g.setColor(Color.green);
					g.fillRect(x[i],y[i],UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					g.setColor(new Color(random.nextInt(255)));
					g.fillRect(x[i],y[i],UNIT_SIZE, UNIT_SIZE);
				
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free",Font.BOLD, 40));
			//lines up in centre of screen
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
			
			}
		else {
			gameOver(g);
			}
	};
	//populate the apple for score
	//generate new coordinates for the apples
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
		
		
	};
	//move snake
	public void move() {
		//iterates through all the bodyparts
		for(int i=bodyParts;i>0;i--) {
			//shifting arrays by one spot
			x[i] = x[i-1];
			y[i] = y[i-1];
			
		}
		//switch the direction where the snake is headed
		switch(direction) {
		case 'U':
			y[0] = y[0]- UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0]- UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] +UNIT_SIZE;
			break;
		}
		
	};
	//examine coordinates of the snake and the apple
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			//snake grows after each eaten apple
			bodyParts++;
			//score
			applesEaten++;
			//generates new apple
			newApple();
		}
		
	};
	public void checkCollisions() {
		//check if head collides with body, if so game over
		//x[0] is the head of the snake
		for(int i =bodyParts;i>0;i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//check if head touches left border
		if(x[0] <0 ) {
			running = false;
		}
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH ) {
			running = false;
		}
		//check if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT ) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
		
	};
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD, 40));
		//lines up in centre of screen
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		
		//Game over text 
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD, 75));
		//lines up in centre of screen
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	};
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//check if game is running
		
		if(running) {
			//move the snake
			move();
			//check if we ran into apple
			checkApple();
			checkCollisions();
		}
		//if game is no longer running 
		repaint();
	}
	
	//inner class
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
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
