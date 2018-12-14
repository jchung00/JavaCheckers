import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Collection;
import java.util.List;

public class BoardModelTest {
	private BoardModel model;
	
	@Before
	public void setUp() {
		model = new BoardModel();
		Piece[][] board = model.getBoard();
		for(int r=0; r<board.length; r++) {
			for(int c=0; c<board[0].length; c++) {
				board[r][c] = null;
			}
		}
		board[0][0] = new Piece(false);
		board[1][1] = new Piece(true);
		board[1][1].setIsKing(true);
		board[5][1] = new Piece(true);
	}
	
	@Test
	public void testLegalMoves() {
		List<Point> movesList = model.getLegalMoves(new Point(5, 1));
		assertTrue("Legal move one is contained", movesList.contains(new Point(4, 0)));
		assertTrue("Legal move two is contained", movesList.contains(new Point(6, 0)));
		assertTrue("List size is two", movesList.size() == 2);
	}
	
	@Test
	public void testLegalMovesNotTurn() {
		List<Point> movesList = model.getLegalMoves(new Point(0, 0));
		assertTrue("List is empty", movesList.isEmpty());
	}
	
	@Test
	public void testLegalMovesNullPiece() {
		List<Point> movesList = model.getLegalMoves(new Point(2, 0));
		assertTrue("List is empty", movesList.isEmpty());
	}
	
	@Test
	public void testKingLegalMoves() {
		List<Point> movesList = model.getLegalMoves(new Point(1, 1));
		assertTrue("Legal move one is contained", movesList.contains(new Point(0, 2)));
		assertTrue("Legal move two is contained", movesList.contains(new Point(2, 0)));
		assertTrue("Legal move three is contained", movesList.contains(new Point(2, 2)));
		assertTrue("List size is three", movesList.size() == 3);
	}
	
	@Test
	public void testPotentialCapture() {
		model.setTurn(false);
		List<Point> movesList = model.getLegalMoves(new Point(0, 0));
		assertTrue("Legal capture move is contained", movesList.contains(new Point(2, 2)));
		assertTrue("List size is one", movesList.size() == 1);
	}
	
	@Test
	public void testInvalidMove() {
		assertFalse("Invalid move", model.move(new Point(5, 1), new Point(4, 2)));
		assertTrue("Piece still at original location", model.getBoard()[5][1] != null);
	}
	
	@Test
	public void testMoveNull() {
		assertFalse("Move empty location", model.move(new Point(3, 1), new Point(4, 0)));
	}
	
	@Test
	public void testMoveNonCapture() {
		assertTrue("Valid non-capture move", model.move(new Point(1, 1), new Point(2, 2)));
		assertTrue("Piece not at original location", model.getBoard()[1][1] == null);
		assertTrue("Piece at new location", model.getBoard()[2][2] != null);
	}
	
	@Test
	public void testMoveCapture() {
		assertTrue(model.move(new Point(5, 1), new Point(6, 0)));
		assertTrue("Valid capture move", model.move(new Point(0, 0), new Point(2, 2)));
		assertTrue("Piece captured", model.getBoard()[1][1] == null);
		assertTrue("Piece not at original location", model.getBoard()[0][0] == null);
		assertTrue("Piece at new location", model.getBoard()[2][2] != null);
	}
	
	@Test
	public void testMoveToWin(){
		assertTrue(model.move(new Point(1, 1), new Point(2, 0)));
		assertTrue(model.move(new Point(0, 0), new Point(1, 1)));
		assertTrue("Valid move to win", model.move(new Point(2, 0), new Point(0, 2)));
		assertTrue("Game ended", !model.isInGame());
	}
	
	@Test
	public void testMoveToKing() {
		assertTrue("Valid move to king", model.move(new Point(5, 1), new Point(6, 0)));
		assertTrue("Piece is king", model.getBoard()[6][0].isKing());
	}
}
