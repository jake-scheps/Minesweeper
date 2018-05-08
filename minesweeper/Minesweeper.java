package minesweeper;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Minesweeper {

	public static void main(String args[]) {
		boolean playing = true;
		Board board = new Board(9, 9, 10);
		Scanner scanner = new Scanner(System.in);
		Pattern clickPattern = Pattern.compile(".\\s*(\\d+)\\s+(\\d+)");
		Pattern restartPattern = Pattern.compile(".\\s*(\\d+)\\s+(\\d+)\\s+(\\d+)");
		printRules();
		while (playing) {
			Result result= Result.INVALID;
			board.printBoard(0);
			if (scanner.hasNextLine()) {
				String input = scanner.nextLine();
				if (input!=null && input.length()!=0) {
					char command = input.charAt(0);
					if (command=='q') {
						result=Result.QUIT;
					} else if (command=='r') {
						int x, y, mines;
						Matcher m = restartPattern.matcher(input);
						if (m.matches()) {
							result = Result.RESTART;
							x = new Integer(m.group(1));
							y = new Integer(m.group(2));
							mines = new Integer(m.group(3));
							if (x<=1||y<=1) {
								System.out.println("Invalid board: too small.");
								result = Result.INVALID;
							} else if (mines >= x*y) {
								System.out.println("Invalid board: too many mines.");
								result = Result.INVALID;
							} else if (mines < 1) {
								System.out.println("Invalid board: too few mines.");
								result = Result.INVALID;
							} else {
								board = new Board(x, y, mines);
							}
						} else {
							result = Result.INVALID;
						}
					} else {
						int x, y;
						Matcher m = clickPattern.matcher(input);
						if (m.matches()) {
							x = new Integer(m.group(1));
							y = new Integer(m.group(2));
							x--;
							y--;
							if (command=='c') {
								result = board.clickSpace(x, y);
							} else if (command=='f') {
								result = board.flagSpace(x, y);
							} else {
								result=Result.INVALID;
							}
						} else {
							result=Result.INVALID;
						}
					}
				} else {
					result=Result.INVALID;
				}
			}
			switch (result) {
			case CLICK :
			case CLEARED :
			case FLAG :
			case UNFLAG :
			case EMPTY :
				break;
			case BADCLICK :
				System.out.println("You must unflag the space before clicking it.");
				break;
			case LOSE :
				System.out.println("Oh no!");
				playing=false;
				board.printBoard(-1);
				break;
			case BADFLAG : 
				System.out.println("Cannot flag a cleared space!");
				break;
			case WIN : 
				System.out.println("Congrats!");
				playing=false;
				board.printBoard(1);
				break;
			case INVALID : 
				printRules();
				break;
			case QUIT : 
				System.out.println("Bye!");
				playing=false;
				break;
			case RESTART :
				System.out.println("Starting new board.");
			}
		}
		scanner.close();
	}
	
	public static void printRules() {
		System.out.println("Commands start with c for click, f for flag, r for restart, or q for quit."
				+ " For c or f, use two numbers separated by spaces, the first is the row the second is the column."
				+ " When restarting you may enter three numbers, first is number of rows, second is number of columns, third is number of mines."
				+ " To read the board: '.' is unchecked, 'x' is flagged, any number tells you the number of adjacent mines."
				+ " When you lose, 'B' is the mine that went off, and 'F' indicates any incorrectly placed flags.");
	}
}
