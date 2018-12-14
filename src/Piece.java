
public class Piece {
	private boolean isPlayerOne;
	private boolean isKing;
	
	public Piece(boolean isPlayerOne) {
		this.isPlayerOne = isPlayerOne;
		isKing = false;
	}
	
	public boolean getIsPlayerOne() {
		return isPlayerOne;
	}
	public void setIsPlayerOne(boolean isPlayerOne) {
		this.isPlayerOne = isPlayerOne;
	}
	public boolean isKing() {
		return isKing;
	}
	public void setIsKing(boolean isKing) {
		this.isKing = isKing;
	}
}
