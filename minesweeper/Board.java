package minesweeper;

import java.util.HashSet;
import java.util.Random;

public class Board {
	private Random rand=new Random();
	private HashSet<Space> spacesToClear, spacesCleared;
	int sizex, sizey, size, cleared, flagged, numMines;
	boolean minesInit;
	private Space[][] spaces;
	
	public Board(int x, int y, int mines) {
		sizex = x;
		sizey = y;
		size = x*y;
		cleared = 0;
		flagged = 0;
		spacesToClear = new HashSet<Space>();
		spacesCleared = new HashSet<Space>();
		minesInit = false;
		this.numMines = mines;
		spaces = new Space[sizex][sizey];
		initBoard();
	}
	
	private void initBoard() {
		for (int i=0;i<sizex;i++) {
			for (int j=0;j<sizey;j++) {
				spaces[i][j] = new Space(i, j);
			}
		}
	}
	
	public void initMines(int x, int y) {
		for (int i=0;i<numMines;i++) {
			boolean mined = false;
			while (!mined) {
				int minex = rand.nextInt(sizex), miney = rand.nextInt(sizey);
				Space mineSpace = spaces[minex][miney];
				if (!(minex==x && miney==y) && !mineSpace.mine) {
					mineSpace.mine=true;
					mined=true;
					incrementAdjacentSpaces(mineSpace.x, mineSpace.y);
				}
			}
		}
		minesInit=true;
	}
	
	private void incrementAdjacentSpaces(int x, int y) {
		
		HashSet<Space> adjacentSpaces = getAdjacentSpaces(spaces[x][y]);
		for (Space space : adjacentSpaces) {
			space.incrementNumMines();
		}
	}
	
	public void printBoard(int win) {
		System.out.println("mines left: " + ((win==1) ? 0 : numMines-flagged));
		for (int i=0;i<sizex;i++) {
			for (int j=0;j<sizey;j++) {
				spaces[i][j].print(win);
				System.out.print(" ");
			}
			System.out.print("\n");
		}
	}
	
	public Result clickSpace(int x, int y) {
		if (0<=x && x<sizex && 0<=y && y <sizey) {
			if (!minesInit) {
				initMines(x, y);
			}
			Result result = spaces[x][y].click();
			if (result==Result.EMPTY) {
				cleared++;
				HashSet<Space> adjacent = getAdjacentSpaces(spaces[x][y]);
				adjacent.removeAll(spacesCleared);
				spacesToClear.addAll(adjacent);
				while (!spacesToClear.isEmpty()) {
					Space space = spacesToClear.iterator().next();
					spacesToClear.remove(space);
					spacesCleared.add(space);
					clickSpace(space.x, space.y);
				}
				if (spacesToClear.isEmpty()) {
					spacesCleared.clear();
				}
				if (cleared==size-numMines) {
					result = Result.WIN;
				}
				return result;
			} else if (result==Result.CLICK) {
				cleared++;
				if (cleared==size-numMines) {
					result = Result.WIN;
				}
			}
			return result;
		} else return Result.INVALID;
	}
	
	public Result flagSpace(int x, int y) {
		if (0<=x && x<sizex && 0<=y && y <sizey) {
			Result result = spaces[x][y].flag();
			if (result==Result.FLAG) {
				flagged++;
			} else if (result==Result.UNFLAG) {
				flagged--;
			}
			return result;
		} else return Result.INVALID;
	}
	
	private HashSet<Space> getAdjacentSpaces(Space space) {
		int x = space.x, y = space.y;
		boolean xmin = (x==0), xmax = (x==(sizex-1));
		boolean ymin = (y==0), ymax = (y==(sizey-1));
		HashSet<Space> adjacent = new HashSet<Space>();
		
		//increment mines on spaces on line above
		if (!xmin) {
			if(!ymin) {
				adjacent.add(spaces[x-1][y-1]);
			}
			adjacent.add(spaces[x-1][y]);
			if(!ymax) {
				adjacent.add(spaces[x-1][y+1]);
			}
		}
		
		//increment mines on spaces on same line
		if (!ymin) {
			adjacent.add(spaces[x][y-1]);
		}
		if (!ymax) {
			adjacent.add(spaces[x][y+1]);
		}
		
		//increment mines on spaces on line below
		if (!xmax) {
			if (!ymin) {
				adjacent.add(spaces[x+1][y-1]);
			}
			adjacent.add(spaces[x+1][y]);
			if (!ymax) {
				adjacent.add(spaces[x+1][y+1]);
			}
		}
		
		return adjacent;
	}
}
