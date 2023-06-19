package gridGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GenerateBoard {

	public static int[][] getBoard() {
		int[][] board = new int[9][9];
		
		board = new int[9][9];
		for (int i = 0; i < 3; i++) {
			board = putDiagonalGrid(board, i*3, i*3);
		}
		board = putRest(board);
		
		return board;
	}
	
	//put diagonal 3x3 grid
	public static int[][] putDiagonalGrid(int[][] board, int I, int J) {
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		for (int i = 1; i < 10; i++) tmp.add(i);
		
		for (int i = I; i < I+3; i++) {
			for (int j = J; j < J+3; j++) {
				int rdNum = (int)(Math.random()*tmp.size()-1);
				board[i][j] = tmp.get(rdNum);
				tmp.remove(rdNum);
			}
		}
		
		return board;
	}
	
	static int[][] putRest(int[][] board) {
		while (true) {
			int[] pos = findStart(board);
			if (findPossible(pos[0], pos[1], board).size() > 1) {
				break;
			}
			board = fillSingle(board);
		}
		
		HashMap<Integer, Set<Integer>> allPossible = new HashMap<Integer, Set<Integer>>();
		allPossible = hashMapGen(board);
		Set<Integer> keys = new HashSet<Integer>();
		keys = allPossible.keySet();
		int[] keyTrack = {};
		for (int i : keys) {
			keyTrack = appendInt(keyTrack, i);
		}
		
		backTrack(board, keyTrack, 0);
		
		return board;
	}
	
	public static int[][] fillSingle(int[][] matrix) {
		HashMap<Integer, Set<Integer>> allPossible = new HashMap<Integer, Set<Integer>>();
		allPossible = hashMapGen(matrix);
		
		Set<Integer> keys = new HashSet<Integer>();
		keys = allPossible.keySet();
		for (int i : keys) {
			if (allPossible.get(i).size() == 1) {
				int tmp = allPossible.get(i).iterator().next();
				matrix[i/10%10-1][i%10-1] = tmp;
			}
		}
		
		return matrix;
	}
	
	public static boolean solveSudoku(int[][] matrix, int x) {
		boolean notDone = true;
		
		int row = -1;
		int col = -1;
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < x; j++) {
				if (matrix[i][j] == 0) {
					row = i;
					col = j;
					notDone = false;
					break;
				}
			}
			if (notDone == false) {
				break;
			}
		}
		
		if (notDone) {
			return true;
		}
		
		for (int n = 1; n <= x; n++) {
			Set<Integer> solutions = findPossible(row+1, col+1, matrix);
			if (solutions.contains(n)) {
				matrix[row][col] = n;
				if (solveSudoku(matrix, x)) {
					return true;
				}
				else {
					matrix[row][col] = 0;
				}
			}
		}
		return false;
	}
	
	public static boolean backTrack(int[][] matrix, int[] keyTrack, int cnt) {		
		if (cnt == keyTrack.length) {
			return true;
		}
		
		int row = keyTrack[cnt]/10%10-1;
		int col = keyTrack[cnt]%10-1;
		
		for (int n = 1; n <= 9; n++) {
			Set<Integer> solutions = findPossible(row+1, col+1, matrix);
			if (solutions.contains(n)) {
				matrix[row][col] = n;
				cnt++;
				if (backTrack(matrix, keyTrack, cnt)) {
					return true;
				}
				else {
					matrix[row][col] = 0;
					cnt--;
				}
			}
		}
		return false;
	}
	
	public static HashMap<Integer, Set<Integer>> hashMapGen(int[][] matrix) {
		HashMap<Integer, Set<Integer>> allPossible = new HashMap<Integer, Set<Integer>>();
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (matrix[i][j] == 0) {
					int pos = (i+1)*10 + (j+1);
					allPossible.put(pos, findPossible(i+1, j+1, matrix));
				}
			}
		}
		return allPossible;
	}
	
	public static int[] findStart(int[][] matrix) {
		int options = 9;
		int[] pos = {0,0};
		Set<Integer> set = new HashSet<Integer>();
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (matrix[i][j] == 0) {
					set = findPossible(i+1, j+1, matrix);
					if (set.size() < options) {
						options = set.size();
						pos[0] = i+1;
						pos[1] = j+1;
					}
				}
			}
		}
		
		return pos;
	}
	
	public static Set<Integer> findPossible(int row, int col, int[][] matrix) {
		Set<Integer> set = new HashSet<Integer>();
		
		//row&column
		for (int i = 0; i < 9; i++) {
			set.add(matrix[row-1][i]);
			set.add(matrix[i][col-1]);
		}
		
		//sub-matrix
		for (int i = (int) (Math.ceil(row/3.0)*3-3); i < Math.ceil(row/3.0)*3; i++) {
			for (int j = (int) (Math.ceil(col/3.0)*3-3); j < Math.ceil(col/3.0)*3; j++) {
				set.add(matrix[i][j]);
			}
		}
		
		//find the options
		Set<Integer> options = new HashSet<Integer>();
		for (int i = 0; i < 10; i++) {
			options.add(i);
		}
		options.removeAll(set);
		
		return options;
	}
	
	public static boolean valid(int[][] x, int[][] y, int[][] z) {
		if (validRow(x) && validCol(y) && validMat(z)) {
			return true;
		}
		return false;
	}
	
	public static boolean validRow(int[][] matrix) {
		int[] test = {1,2,3,4,5,6,7,8,9};
		
		for (int i = 0; i < 9; i++) {
			Arrays.sort(matrix[i]);
			if (Arrays.equals(matrix[i], test) == false) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean validCol(int[][] matrix) {
		int[] test = {1,2,3,4,5,6,7,8,9};
		
		for (int i = 0; i < 9; i++) {
			int[] tmp = new int[9];
			
			for (int j = 0; j < 9; j++) {
				tmp[j] = matrix[j][i];
			}
			
			Arrays.sort(tmp);
			if (Arrays.equals(tmp, test) == false) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean validMat(int[][] matrix) {
		int[] test = {1,2,3,4,5,6,7,8,9};
		int cJ = 0;
		int I = 0;
		
		int[] tmp = new int[9];
		for (int k = 0; k < 9; k++) {
			if (k%3 == 0) {
				I = k;
			}
			
			for (int i = I; i < I+3; i++) {
				for (int j = (k%3)*3; j < (k%3)*3+3; j++) {
					tmp[cJ] = matrix[i][j];
					cJ++;
				}
			}
			Arrays.sort(tmp);
			if (Arrays.equals(tmp, test) == false) {
				return false;
			}
			cJ = 0;
		}
		return true;
	}

	public static int[] appendInt(int[] arr, int x) {
		int[] newArr = new int[arr.length + 1];
		
		for (int i = 0; i < arr.length; i++) {
			newArr[i] = arr[i];
		}
		newArr[newArr.length - 1] = x; //or newArr[arr.length] = x;
		
		return newArr;
	}
	
}
