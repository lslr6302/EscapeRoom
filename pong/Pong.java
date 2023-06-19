package pongGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import hsa2.GraphicsConsole;

public class Pong {

	public static void main(String[] args) {
		new Pong();
	}
	
	static final int WINH = 600, WINW = 1000;
	GraphicsConsole gc = new GraphicsConsole(WINW, WINH);
	
	Ball b = new Ball(100, 220);  //the ball
	Paddle p1 = new Paddle(100, 220, 10, 100);  //2 paddles for 2 players
	Paddle p2 = new Paddle(890, 220, 10, 100);
	int sleepTime = 5;
	
	boolean clearScreen = false;
	
	int playerOneScore = 0, playerTwoScore = 0;
	
	Pong() {
		setup();
		run();
	}
	
	//setup
	void setup() {
		gc.setTitle("Game of Pong");
		gc.setAntiAlias(true);
		gc.setLocationRelativeTo(null); //center the window
		gc.setColor(Color.white);
		gc.setBackgroundColor(Color.black);
		gc.clear();
		
		b = new Ball(300, 500);
		gc.setFont(new Font("Arial", Font.BOLD, 30));
	}
	
	void run() {
		while (true) {
			moveBall(b);
			moveLeftPaddle(p1);
			moveRightPaddle(p2);
			checkCollision();
			drawGraphics();
			if (checkWin()) {
				gc.showDialog("A Winner has appeared!!!", "Congragulations!");
				gc.close();
				break;
			}
			gc.sleep(sleepTime);
		}
	}
	
	void moveBall(Ball b) {
		b.x += b.vx;
		b.y += b.vy;
		
		//bounce off of walls
		if (b.x < 0) {
			b.vx *=-1;
			playerTwoScore++;
		}
		if (b.y < 0) b.vy *=-1;
		if (b.x+b.width > WINW) {
			b.vx *=-1;
			playerOneScore++;
		}
		if (b.y+b.width > WINH) b.vy *=-1;
	}
	
	void moveLeftPaddle(Paddle p) {	
		if (gc.isKeyDown(87) && p.y > 0) {  //w
			p.y -= p.speed;
			clearScreen = true;
		}		
		if (gc.isKeyDown(83) && p.y+p.height < WINH) {  //s
			p.y += p.speed;
			clearScreen = true;
		}
		if (gc.isKeyDown(65) && p.x > 0) {  //a
			p.x -= p.speed;
			clearScreen = true;
		}
		if (gc.isKeyDown(68) && p.x+p.width < 497) {  //d
			p.x += p.speed;
			clearScreen = true;
		}
	}
	
	void moveRightPaddle(Paddle p) {
		if (gc.isKeyDown(38) && p.y > 0) {  //up arrow
			p.y -= p.speed;
			clearScreen = true;
		}		
		if (gc.isKeyDown(40) && p.y+p.height < WINH) {  //down arrow
			p.y += p.speed;
			clearScreen = true;
		}
		if (gc.isKeyDown(37) && p.x > 503) {  //left arrow
			p.x -= p.speed;
			clearScreen = true;
		}
		if (gc.isKeyDown(39) && p.x+p.width < WINW) {  //right arrow
			p.x += p.speed;
			clearScreen = true;
		}
	}
	
	void drawGraphics() {
		synchronized(gc) {
			if (clearScreen == true) {
				gc.clear();
				clearScreen = false;
			}
			gc.clear();
			
			gc.setColor(Color.white);
			gc.fillRect(p1.x, p1.y, p1.width, p1.height);
			gc.fillRect(p2.x, p2.y, p2.width, p2.height);
			
			gc.fillOval(b.x, b.y, b.width, b.height);
			
			gc.drawString("" + playerOneScore, 30, 30);
			gc.drawString("" + playerTwoScore, 970, 30);
		}
	
		//draw the field
		gc.setColor(Color.cyan);
		for (int i = 0; i < 10; i++) {
			gc.fillRect(497, i*60, 6, 50);
		}
	}
	
	//check if winning
	boolean checkWin() {
		return playerOneScore == 3 || playerTwoScore == 3;
	}
	
	void checkCollision() {
		if (b.intersects(p1)) {
			if (b.y + b.height > p1.y && b.y < p1.y && b.vy > 0) {
				//is the whole ball between the left and right sides of the box?
				if (b.x > p1.x && b.x+b.width < p1.x+p1.width) {
					b.x -= b.vx;	//undo last move
					b.y -= b.vy;
					b.vy *= -1;					
					//return;
				}
			}

			//hit from left (left side of block)
			if (b.x + b.width > p1.x && b.vx > 0) {	//moving right 
				if (b.y > p1.y && b.y + b.height < p1.y + p1.height) {
					b.x -= b.vx;	//undo last move
					b.y -= b.vy;					
					b.vx *= -1;						
					//return;							
				}
			}
			
			//hit bottom (moving upwards)
			if (b.y < p1.y+p1.height && b.vy < 0){ 				
				if (b.x > p1.x && b.x+b.width < p1.x+p1.width) {
					b.x -= b.vx;	//undo last move
					b.y -= b.vy;					
					b.vy *= -1;							
					//return;
				}
			}
			
			//hitting right side, moving from left
			if (b.x < p1.x+p1.width && b.vx < 0){
				if (b.y > p1.y && b.y + b.height < p1.y + p1.height) {
					b.x -= b.vx;	//undo last move
					b.y -= b.vy;					
					b.vx *= -1;						
					//return;
				}
			}
			
			//undo last move
			b.x -= b.vx;	
			b.y -= b.vy;

			//Find centre of rect:
			int crX = p1.x + p1.width/2;
			int crY = p1.y + p1.height/2;
			
			// Find which corner ball is bouncing off of, then bounce the ball away from the corner 
			//top left corner
			if (b.x+b.width < crX && b.y + b.height < crY) {	
				b.vx = - Math.abs(b.vx);
				b.vy = - Math.abs(b.vy);
				//return;
			}
			
			//top right corner
			if (b.x > crX && b.y + b.height < crY) {	
				b.vx = + Math.abs(b.vx);
				b.vy = - Math.abs(b.vy);
				//return;
			}
			
			//bottom left corner
			if (b.x + b.width < crX && b.y > crY) {	
				b.vx = - Math.abs(b.vx);
				b.vy = + Math.abs(b.vy);
				//return;
			}
			
			//bottom right corner
			if (b.x > crX && b.y > crY) {	
				b.vx = + Math.abs(b.vx);
				b.vy = + Math.abs(b.vy);
				//return;
			}
		}
	}
	
	void checkCollision2() {
		if (b.intersects(p2)) {
			b.x -= b.vx;
			b.y -= b.vy;
			b.vy *= -1;
			b.vx *= -1;
			
			b.x -= b.vx;	
			b.y -= b.vy;
			
			if (b.y + b.height > p2.y && b.y < p2.y && b.vy > 0) {
				//is the whole ball between the left and right sides of the box?
				if (b.x > p2.x && b.x+b.width < p2.x+p2.width) {
					b.x -= b.vx;	//undo last move
					b.y -= b.vy;
					b.vy *= -1;					
					return;
				}
			}

			//hit from left (left side of block)
			if (b.x + b.width > p2.x && b.vx > 0) {	//moving right 
				if (b.y > p2.y && b.y + b.height < p2.y + p2.height) {
					b.x -= b.vx;	//undo last move
					b.y -= b.vy;					
					b.vx *= -1;						
					return;							
				}
			}
			
			//hit bottom (moving upwards)
			if (b.y < p2.y+p2.height && b.vy < 0){ 				
				if (b.x > p2.x && b.x+b.width < p2.x+p2.width) {
					b.x -= b.vx;	//undo last move
					b.y -= b.vy;					
					b.vy *= -1;							
					return;
				}
			}
			
			//hitting right side, moving from left
			if (b.x < p2.x+p2.width && b.vx < 0){
				if (b.y > p2.y && b.y + b.height < p2.y + p2.height) {
					b.x -= b.vx;	//undo last move
					b.y -= b.vy;					
					b.vx *= -1;						
					return;
				}
			}
			
			//undo last move
			b.x -= b.vx;	
			b.y -= b.vy;

			//Find centre of rect:
			int crX = p2.x + p2.width/2;
			int crY = p2.y + p2.height/2;
			
			// Find which corner ball is bouncing off of, then bounce the ball away from the corner 
			//top left corner
			if (b.x+b.width < crX && b.y + b.height < crY) {	
				b.vx = - Math.abs(b.vx);
				b.vy = - Math.abs(b.vy);
				return;
			}
			
			//top right corner
			if (b.x > crX && b.y + b.height < crY) {	
				b.vx = + Math.abs(b.vx);
				b.vy = - Math.abs(b.vy);
				return;
			}
			
			//bottom left corner
			if (b.x + b.width < crX && b.y > crY) {	
				b.vx = - Math.abs(b.vx);
				b.vy = + Math.abs(b.vy);
				return;
			}
			
			//bottom right corner
			if (b.x > crX && b.y > crY) {	
				b.vx = + Math.abs(b.vx);
				b.vy = + Math.abs(b.vy);
				return;
			}
		}
	}
}
