package gridGame;

import java.awt.Color;
import java.awt.Font;
import hsa2.GraphicsConsole;
import java.util.*;

public class Sudoku {

	public static void main(String[] args) {
		new Sudoku();
	}
	
	int winw = 460, winh = 460;
	GraphicsConsole gc = new GraphicsConsole(winw, winh);
	
	int[][] board = GenerateBoard.getBoard();
	int[][] originalBoard = new int[9][9];
	
	int numToTakeAway = 10;
	
	Sudoku() {
		setup();
		
		for (int i = 0; i < 9; i++) {
			System.arraycopy(board[i], 0, originalBoard[i], 0, 9);
		}
		
		String s = gc.showInputDialog("Enter the difficulty you want to try (easy/medium/hard)", "Select Your Difficulty");
		if (s.equals("easy")) numToTakeAway = 10;
		else if(s.equals("medium")) numToTakeAway = 20;
		else if(s.equals("hard")) numToTakeAway = 40;
		
		setBoard();
		
		while(true) {
			if (gc.getMouseClick() > 0) handleMouseClick();
			drawGraphics();
			if (numToTakeAway == 0) {
				gc.showDialog("You Win!!!", "Congrats");
				gc.close();
				break;
			}
			gc.sleep(10);
		}
	}
	
	void setup() {
		gc.setTitle("Sudoku");
		gc.setLocationRelativeTo(null);
		gc.setAntiAlias(true);
		gc.enableMouse();  //enables mouse clicking
		
		gc.setStroke(2);
		gc.setBackgroundColor(Color.decode("#f5f2eb"));
		gc.clear();		
	}
	
	//randomly set number to 0 according to the difficulty
	void setBoard() {
		int cnt = numToTakeAway;
		while (cnt > 0) {
			int x = (int)(Math.random()*9);
			int y = (int)(Math.random()*9);
			if (board[x][y] != 0) {
				board[x][y] = 0;
				cnt--;
			}
		}
	}
	
	void handleMouseClick() {
		int col = gc.getMouseX() * 9/winw;
		int row = gc.getMouseY() * 9/winh;
		
		if (board[row][col] == 0) {
			int result = Integer.valueOf(gc.showInputDialog("Enter a number from 1-9 that you think will fit in this spot.", "Enter Your Answer"));
			if (result == originalBoard[row][col]) {
				board[row][col] = result;
				numToTakeAway--;
			}
		}
	}
	
	void drawGraphics() {
		synchronized(gc) {
			gc.clear();
			
			//draw grid
			gc.setColor(Color.blue.darker().darker().darker());
			int cnt = 0;
			for (int i = 5; i < 460; i+=50) {
				if (cnt % 3 == 0) gc.setStroke(4);
				else gc.setStroke(2);
				
				gc.drawLine(5, i, 455, i);
				gc.drawLine(i, 5, i, 455);
				cnt++;
			}
			
			//print the numbers
			gc.setFont(new Font("Arial", Font.BOLD, 30));
			for(int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (board[i][j] == 0) continue;
					gc.drawString(Integer.toString(board[i][j]), 20+j*50, 40+i*50);
				}
			}
		}
	}

}
