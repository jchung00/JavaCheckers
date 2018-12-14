import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public class BoardModel {
	private static final int row = 8;
	private static final int column = 8;
	private Piece[][] b;
	private boolean turn;
	private boolean inGame;
	
	public BoardModel() {
		setInGame(true);
		turn = true;
		b = new Piece[row][column];
		for(int r=0; r<row; r++) {
			for(int c=0; c<column; c++) {
				if(((r % 2 == 0) && (c % 2 == 0)) || ((r % 2 != 0) && (c % 2 != 0))) {
					if(c <= 2) {
						b[r][c] = new Piece(false);
					}
					else if(c >= 5) {
						b[r][c] = new Piece(true);
					}
				}
			}
		}
	}
	
	public static boolean pointOutOfBounds(Point p) {
		int x = p.x;
		int y = p.y;
		if(x < 0 || x >= row || y < 0 || y >= column) {
			return true;
		}
		return false;
	}
	
	public boolean isTurn(Point p) {
		int x = p.x;
		int y = p.y;
		if(b[x][y] == null) {
			return false;
		}
		if(b[x][y].getIsPlayerOne() == turn) {
			return true;
		}
		return false;
	}
	
	public List<Point> getLegalMoves(Point start){
		List<Point> points = new LinkedList<Point>();
		if(!inGame) {
			return points;
		}
		int x = start.x;
		int y = start.y;
		if(!isTurn(start)) {
			return points;
		}
		if(pointOutOfBounds(start)) {
			return points;
		}
		else{
			int c;
			if(b[x][y].getIsPlayerOne()) {
				c = 1;
			}
			else {
				c = -1;
			}
			if(b[x][y].isKing()) {
				int xf1 = x-1;
				int yf1 = y+c;
				Point f1 = new Point(xf1, yf1);
				if(!pointOutOfBounds(f1)) {
					if(b[xf1][yf1] == null) {
						points.add(f1);
					}
					else if(b[x][y].getIsPlayerOne() != b[xf1][yf1].getIsPlayerOne()) {
						int xf11 = xf1-1;
						int yf11 = yf1+c;
						Point f11 = new Point(xf11, yf11);
						if(!pointOutOfBounds(f11) && b[xf11][yf11] == null) {
							points.add(f11);
						}
					}
				}
				int xf2 = x+1;
				int yf2 = y+c;
				Point f2 = new Point(xf2, yf2);
				if(!pointOutOfBounds(f2)) {
					if(b[xf2][yf2] == null) {
						points.add(f2);
					}
					else if(b[x][y].getIsPlayerOne() != b[xf2][yf2].getIsPlayerOne()) {
						int xf22 = xf2+1;
						int yf22 = yf2+c;
						Point f22 = new Point(xf22, yf22);
						if(!pointOutOfBounds(f22) && b[xf22][yf22] == null) {
							points.add(f22);
						}
					}
				}
			}
			int xf1 = x+1;
			int yf1 = y-c;
			Point f1 = new Point(xf1, yf1);
			if(!pointOutOfBounds(f1)) {
				if(b[xf1][yf1] == null) {
					points.add(f1);
				}
				else if(b[x][y].getIsPlayerOne() != b[xf1][yf1].getIsPlayerOne()) {
					int xf12 = xf1+1;
					int yf12 = yf1-c;
					Point f12 = new Point(xf12, yf12);
					if(!pointOutOfBounds(f12) && b[xf12][yf12] == null) {
						points.add(f12);
					}
				}
			}
			int xf2 = x-1;
			int yf2 = y-c;
			Point f2 = new Point(xf2, yf2);
			if(!pointOutOfBounds(f2)) {
				if(b[xf2][yf2] == null) {
					points.add(f2);
				}
				else if(b[x][y].getIsPlayerOne() != b[xf2][yf2].getIsPlayerOne()) {
					int xf21 = xf2-1;
					int yf21 = yf2-c;
					Point f21 = new Point(xf21, yf21);
					if(!pointOutOfBounds(f21) && b[xf21][yf21] == null) {
						points.add(f21);
					}
				}
			}
		}
		return points;
	}
	
	public boolean move(Point start, Point end) {
		if(!inGame) {
			return false;
		}
		List<Point> potentialPoints = getLegalMoves(start);
		if(potentialPoints.contains(end)) {
			int xi = start.x;
			int yi = start.y;
			setTurn(!b[xi][yi].getIsPlayerOne());
			int xf = end.x;
			int yf = end.y;
			b[xf][yf] = b[xi][yi];
			if(yf == 0 || yf == column - 1) {
				b[xf][yf].setIsKing(true);
			}
			b[xi][yi] = null;
			if(Math.abs(yf-yi) >= 2) {
				int capturedX = (xf+xi)/2;
				int capturedY = (yf+yi)/2;
				b[capturedX][capturedY] = null;
			}
			int countBlack = 0;
			int countRed = 0;
			for(int r=0; r<row; r++) {
				for(int c=0; c<column; c++) {
					if(b[r][c] != null) {
						if(b[r][c].getIsPlayerOne()) {
							countBlack++;
						}
						else {
							countRed++;
						}
					}
				}
			}
			if(countBlack == 0 || countRed ==0) {
				inGame = false;
			}
			return true;
		}
		return false;
	}

	public boolean getTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}
	
	public Piece[][] getBoard() {
		return b;
	}

	public boolean isInGame() {
		return inGame;
	}
	
	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
}
