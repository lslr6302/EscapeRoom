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
	
	GraphicsConsole gc = new GraphicsConsole(967, 747, "FISH!");
	BufferedImage backImg, playerImg;
	Rectangle door, drawing, tv, closet, drawer, carpet;
	int difficulty;
	boolean clearScreen = false, didPong = false, didSudoku = false, didShooting = false, win = false;
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
		gc.setTitle("Testing Starting Page");
		gc.setLocationRelativeTo(null);
		gc.setAntiAlias(true);
		gc.enableMouse();  //enables mouse clicking
		
		gc.setBackgroundColor(Color.decode("#CCDDFF"));
		gc.clear();
	}
	
	//setup page for the main game
	void setup() {		
		gc.setTitle("Escape Room");
		gc.setLocationRelativeTo(null);
		gc.setAntiAlias(true);
		gc.enableMouse();
		
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
		
		String s = "dont close";
		
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
				new Pong(s);
				didPong = true;
				return;
			}
		}
		if (closet.contains(mx, my)) gc.showDialog("There is nothing hiding here.", "Nice Try");
		if (drawer.contains(mx, my)) gc.showDialog("There is nothing hiding here.", "Nice Try");
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

/*
Door
	top-left:  504, 119
	top-right： 720, 119
	btm-left:  503, 586
	btm-right: 720, 586

Picture on the shelf
	top-left:  275, 48
	top-right： 431, 48
	btm-left:  275, 163
	btm-right: 431, 163
	
TV
	top-left:  215, 315
	top-right： 480, 315
	btm-left:  215, 454
	btm-right: 480, 454
	
Closet
	top-left:  765, 55
	top-right： 860, 100
	btm-left:  765, 604
	btm-right: 860, 664
	
Drawer
	top-left:  43, 584
	top-right： 172, 527
	btm-left:  43, 684
	btm-right: 172, 624
	
Carpet
	top-left:  535, 652
	top-right： 792, 652
	btm-left:  635, 732
	btm-right: 910, 732
*/
