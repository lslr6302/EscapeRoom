package escapeRoom;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import hsa2.GraphicsConsole;
import javax.imageio.ImageIO;
import sudoku.Sudoku;
import pong.Pong;
import shootingGame.ShootingGame;

public class EscapeRoom {
	
	class Button extends Rectangle {
		String text;
				
		Button(int x, int y, int w, int h) {
			super(x, y, w, h);
		}
	}

	public static void main(String[] args) {
		new EscapeRoom();
	}
	
	GraphicsConsole gc = new GraphicsConsole(967, 747);
	BufferedImage backImg, playerImg;
	Rectangle door, drawing, tv, closet, drawer, carpet;
	boolean clearScreen = false, didPong = false, didSudoku = false, didShooting = true, win = false;
	int playerX = 500, playerY = 600;
	
	EscapeRoom() {
		start();
		while(true) {
			if (gc.getMouseClick() > 0) break;
			gc.sleep(10);
		}
		
		setup();
		while(true) {
			if (gc.getMouseClick() > 0) System.out.println(gc.getMouseX() + ", " + gc.getMouseY());
			doMouseClick();
			moveCharacter();
			drawGraphics();
			if (win) break;
			gc.sleep(10);
		}
	}
	
	//setup page for the starting page
	void start() {
		gc.setTitle("Welcome!");
		gc.setLocationRelativeTo(null);
		gc.setAntiAlias(true);
		gc.enableMouse();
		gc.setFont(new Font("Arial", Font.PLAIN, 20));
		
		gc.setBackgroundColor(Color.decode("#CCDDFF"));
		gc.clear();
		
		//descriptions
		String[] instructions = {"You have entered the Spooks Escape Room.",
		"- Click in the room to find where the hidden games are to win the exit key for the escape room.",
		"- You are only able to play each game once or else you have lost :(",
		"- There will be 3 hidden games",
		"\t- Pong: after one player got 3 points, the game will end.",
		"\t- Sudoku: fill in all the blanks with 1-9, no repeated numbers are allowed in small grid, row or a column",
		"\t- Shooting game: shoot down 10 balls within 20 shots.",
		"- Good Luck!!"};
		
		for (int i = 0; i < instructions.length; i++) {
			 gc.drawString(instructions[i], 20, 100+40*i);
		}
		
		gc.drawString("--click anywhere to start--", 325, 500);
	}
	
	//setup page for the main game
	void setup() {		
		gc.setTitle("Escape Room");
		gc.setLocationRelativeTo(null);
		gc.setAntiAlias(true);
		gc.enableMouse();
		gc.clear();
		
		drawRect();
	}
	
	void drawRect() {
		door = new Rectangle(504, 119, 216, 467);
		drawing = new Rectangle(275, 48, 156, 115);
		tv = new Rectangle(215, 315, 265, 139);
		closet = new Rectangle(765, 55, 95, 609);
		drawer = new Rectangle(43, 527, 129, 97);
		carpet = new Rectangle(535, 652, 375, 80);
	}
	
	void doMouseClick() {
		int mx = gc.getMouseX();
		int my = gc.getMouseY();
		
		if (door.contains(mx, my)) if (checkWin()) win();
		if (drawing.contains(mx, my)) {
			if (!didSudoku) {
				Sudoku.main(null);	
				didSudoku = true;
				return;
			}
		}
		if (tv.contains(mx, my)) {
			if (!didPong) {
				Pong.main(null);
				didPong = true;
				return;
			}
		}
		if (carpet.contains(mx, my)) {
			if (!didShooting) {
				ShootingGame.main(null);
				didShooting = true;
				return;
			}
		}
	}
	
	//setup page when winning
	void win() {
		gc.showDialog("You win!!!", "You win!!!");
		gc.close();
		win = true;
	}
	
	boolean checkWin() {
		return didPong && didSudoku && didShooting;
	}
	
	void moveCharacter() {
		if (gc.isKeyDown(87)) {
			playerY -= 5;
			clearScreen = true;
		}		
		if (gc.isKeyDown(83)) {
			playerY += 5;
			clearScreen = true;
		}
		if (gc.isKeyDown(65)) {
			playerX -= 5;
			clearScreen = true;
		}
		if (gc.isKeyDown(68)) {
			playerX += 5;
			clearScreen = true;
		}
		
	}
	
	void drawGraphics() {
		synchronized(gc) {
			if (clearScreen) {
				gc.clear();
				clearScreen = false;
			}
			
			backImg = loadImage("escapeRoom.png");
			gc.drawImage(backImg, 0, 0, 967, 747);
			
			playerImg = loadImage("character.png");
			gc.drawImage(playerImg, playerX, playerY);
		}
	}
	
	//load image
	static BufferedImage loadImage(String filename) {
		BufferedImage img = null;			
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println(e.toString());
			JOptionPane.showMessageDialog(null, "An image failed to load: " + filename , "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		return img;
	}

}
