package pongGame;

import java.awt.Color;
import java.awt.Rectangle;

//This is used in the Pong.java
class Ball extends Rectangle {

	Color colour = Color.blue;
	int vx = 3, vy = 3;
	
	Ball(int x, int y) {
		this.x = x;
		this.y = y;
		width = height = 20;
	}
	
}
