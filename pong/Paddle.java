package pongGame;

import java.awt.Rectangle;

//This is used in the Pong.java
class Paddle extends Rectangle {
	int speed = 5;
	
	Paddle(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
}
