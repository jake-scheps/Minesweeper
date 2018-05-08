package minesweeper;

public class Space implements Comparable<Space> {
	public int x, y, numAdjacentMines;
	public boolean cleared, flagged, mine, blown;
	
	public Space(int x, int y) {
		this.x=x;
		this.y=y;
		cleared = false;
		flagged = false;
		mine = false;
		numAdjacentMines = 0;
	}
	
	public void incrememntNumMines() {
		if (!mine) {
			numAdjacentMines++;
		}
	}

	public void print(int win) {
		if (flagged) {
			if ((win==-1)&&!mine) {
				System.out.print("F");
			} else {
				System.out.print("x");
			}
		}
		else if(blown) System.out.print("B");
		else if(cleared) System.out.print(String.valueOf(numAdjacentMines));
		else if(mine) {
			if (win==1) System.out.print("x");
			else if (win==-1) System.out.print("M");
			else System.out.print(".");
		}
		else System.out.print(".");
	}
	
	public Result click() {
		if (cleared) {
			return Result.CLEARED;
		}
		else if (flagged) {
			return Result.BADCLICK;
		}
		else if (mine) {
			blown=true;
			return Result.LOSE;
		}
		else {
			cleared=true;
			if (numAdjacentMines==0) {
				return Result.EMPTY;
			}
			return Result.CLICK;
		}
	}
	
	public Result flag() {
		if (!cleared) {
			flagged=!flagged;
			if (flagged) {
				return Result.FLAG;
			}
			return Result.UNFLAG;
		}
		return Result.BADFLAG;
	}
	
	@Override
	public int compareTo(Space o) {
		if (this.x==o.x&&this.y==o.y) {
			return 0;
		}
		return 1;
	}
}
