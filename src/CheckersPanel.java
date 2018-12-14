import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class CheckersPanel {
	private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JCheckBox[][] checkerBoardSquares = new JCheckBox[8][8];
    private JPanel checkersBoard;
    private final JLabel message = new JLabel(
            "Turn: Black");
    private final JLabel moveHistory = new JLabel("Moves: ");
    private final JLabel winLabel = new JLabel("");
    private List<String> history = new ArrayList<String>();
    private int historySize = 3;
    private static final String COL = "ABCDEFGH";
    private BoardModel boardModel = new BoardModel();
    private int eraseX = 0;
    private int eraseY = 0;

    public CheckersPanel() {
    	history = new ArrayList<String>();
        for(int i=0; i<3; i++) {
        	history.add("");
        }
        makeGui();
        boardModel = new BoardModel();
    }
    
    private class buttonClickListener implements ActionListener{
    	private Point p;

        public buttonClickListener(Point p) {
            this.p = p;
        }

        public void actionPerformed(ActionEvent e) {
            squarePressed(p);
        }
    }

    public final void makeGui() {
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        JButton restartButton = new JButton("New");
        restartButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		restart();
        	}
        });
        tools.add(restartButton);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					save();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        	}
        });
        tools.add(saveButton);
        
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					load();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        	}
        });
        tools.add(loadButton);
        
        tools.addSeparator();
        JButton resignButton = new JButton("Resign");
        resignButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		resign();
        	}
        });
        tools.add(resignButton);
        
        tools.addSeparator();
        tools.add(message);
        tools.addSeparator();
        tools.add(moveHistory);
        tools.addSeparator();
        tools.add(winLabel);
        tools.addSeparator();
        
        JButton instructionsButton = new JButton("Instructions");
        instructionsButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		showInstructions();
        	}
        });
        
        tools.add(instructionsButton);

        checkersBoard = new JPanel(new GridLayout(0, 9));
        checkersBoard.setBorder(new LineBorder(Color.BLACK));
        gui.add(checkersBoard);

        // create the chess board squares
        Insets buttonMargin = new Insets(0,0,0,0);
        for (int i = 0; i < checkerBoardSquares.length; i++) {
            for (int j = 0; j < checkerBoardSquares[i].length; j++) {
                JCheckBox b = new JCheckBox();
                b.setMargin(buttonMargin);
                if (((j % 2 == 1) && (i % 2 == 1))
                        || ((j % 2 == 0) && (i % 2 == 0))) {
                    b.setBackground(Color.WHITE);
                    b.setOpaque(true);
                    b.setBorderPainted(false);
                } else {
                    b.setBackground(Color.BLACK);
                    b.setOpaque(true); 
                    b.setBorderPainted(false);
                }
                b.addActionListener(new buttonClickListener(new Point(i, j)));
                b.setSelected(false);
                checkerBoardSquares[i][j] = b;
            }
        }


        checkersBoard.add(new JLabel(""));

        for (int i = 0; i < 8; i++) {
            checkersBoard.add(
                    new JLabel(COL.substring(i, i + 1),
                    SwingConstants.CENTER));
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (j) {
                    case 0:
                        checkersBoard.add(new JLabel("" + (i + 1),
                                SwingConstants.CENTER));
                    default:
                        checkersBoard.add(checkerBoardSquares[j][i]);
                }
            }
        }
        draw();
    }
    
    	protected void showInstructions() {
    		JOptionPane.showMessageDialog(gui,
    			    "Instructions: \nThis is an implementation of Checkers that uses the Swing library in Java.\n"
    			    + "The objective of the game is to capture all of the opponent's pieces.\n"
    			    + "You can capture an opponent's piece by jumping over it diagonally.\n"
    			    + "The space you jump to must be an open square.\n"
    			    + "Otherwise, pieces can only move one square at a time, diagonally.\n"
    			    + "A piece can only move forward unless it is a king.\n"
    			    + "For a piece to be king, it must reach the opponent's end of the board.\n\n"
    			    + "Controls:\n"
    			    + "Use your mouse/trackpad to click on the piece you would like to move.\n"
    			    + "If the piece has any legal moves available, those moves will be highlighted in green.\n"
    			    + "Click on the green squares to move the selected piece to the new square.\n"
    			    + "Click on any other square to simply cancel the selection of a piece and/or select a new piece.\n"
    			    + "Click on the 'Save' button to save the game.\n"
    			    + "Click on the 'Load' button to load the game from the last saved point.\n"
    			    + "Click on the 'Resign' button to forfeit the game on your turn.\n"
    			    + "Click on the 'New' button to restart the game.\n\n"
    			    + "Helpful information:\n"
    			    + "Black starts the game.\n"
    			    + "When a piece becomes king, its icon will change from the colored circle to a colored crown.\n"
    			    + "Move history is shown on the top right. The history shows the last 3 moves that were made.\n"
    			    + "The current player's turn is displayed on the top middle.\n"
    			    + "If either player wins, the game will pause and the winner will be displayed on the top right.");
    	}
        
    	protected void save() throws IOException {
    		boolean turn = boardModel.getTurn();
    		String fileContent = String.valueOf(turn).substring(0, 1);
    		Piece[][] board = boardModel.getBoard();
    		for(int r=0; r<board.length; r++){
    			for(int c=0; c<board[0].length; c++) {
    				if(board[r][c] == null) {
    					fileContent += "0n";
    				}
    				else {
    					if(board[r][c].getIsPlayerOne()) {
    						fileContent += "1";
    					}
    					else {
    						fileContent += "2";
    					}
    					if(board[r][c].isKing()) {
    						fileContent += "k";
    					}
    					else {
    						fileContent += "n";
    					}
    				}
    			}
    		}
    		BufferedWriter writer = new BufferedWriter(new FileWriter("files/game.txt"));
    		writer.write(fileContent);
    		writer.close();
    	}
    	
    	protected void load() throws IOException {
    		history = new ArrayList<String>();
	        for(int i=0; i<3; i++) {
	        	history.add("");
	        }
    		BufferedReader br = new BufferedReader(new FileReader("files/game.txt"));
    		String fileText = br.readLine();
    		boardModel = new BoardModel();
    		if(fileText.substring(0, 1).equals("t")) {
    			boardModel.setTurn(true);
    		}
    		else {
    			boardModel.setTurn(false);
    		}
    		Piece[][] board = boardModel.getBoard();
    		for(int r=0; r<board.length; r++) {
    			for(int c=0; c<board[0].length; c++) {
    				int playerIndex = 1 + 2*(r*board.length + c);
    				int kingIndex = 2 + 2*(r*board.length + c);
    				if(fileText.substring(playerIndex, playerIndex + 1).equals("0")) {
    					board[r][c] = null;
    				}
    				else if(fileText.substring(playerIndex, playerIndex + 1).equals("1")) {
    					board[r][c] = new Piece(true);
    				}
    				else {
    					board[r][c] = new Piece(false);
    				}
    				if(fileText.substring(kingIndex, kingIndex + 1).equals("k")) {
    					if(board[r][c] != null) {
    						board[r][c].setIsKing(true);
    					}
    				}
    				else {
    					if(board[r][c] != null) {
    						board[r][c].setIsKing(false);
    					}
    				}
    			}
    		}
    		br.close();
	        winLabel.setText("");
	        draw();
	        clearChecks();
    	}
    	
		protected void restart() {
			history = new ArrayList<String>();
	        for(int i=0; i<3; i++) {
	        	history.add("");
	        }
			boardModel = new BoardModel();
			winLabel.setText("");
			draw();
			clearChecks();
		}
		
		protected void resign() {
			boardModel.setInGame(false);
			draw();
		}
    
        protected void squarePressed(Point p) {
        	int x = p.x;
        	int y = p.y;
        	JCheckBox checkBox = checkerBoardSquares[x][y];
        	Piece[][] board = boardModel.getBoard();
        	draw();
        	if(board[x][y] != null) {
        		if(board[x][y].getIsPlayerOne() == boardModel.getTurn()) {
        			checkerBoardSquares[eraseX][eraseY].setSelected(false);
        			eraseX = x;
        			eraseY = y;
        			checkBox.setSelected(true);
        			List<Point> list = new LinkedList<Point>();
        			list = boardModel.getLegalMoves(p);
                	for(Point legalMove : list) {
                		JCheckBox button = checkerBoardSquares[legalMove.x][legalMove.y];
                		button.setBackground(Color.GREEN);
                	}
        		}
        		else {
        			checkBox.setSelected(false);
        		}
        	}
        	else {
        		checkBox.setSelected(false);
        		for(int i=0; i<board.length; i++) {
            		for(int j=0; j<board[0].length; j++) {
            			if(checkerBoardSquares[i][j].isSelected()) {
            				List<Point> list = new LinkedList<Point>();
            				Point start = new Point(i, j);
            				list = boardModel.getLegalMoves(start);
            				if(list.contains(p)) {
            					boardModel.move(start, p);
            					String currentMove = COL.substring(x, x+1) + Integer.toString(y+1);
            					String lastMove = history.get(0);
            					String secondLastMove = history.get(1);
            					history.set(2, secondLastMove);
            					history.set(1, lastMove);
            					history.set(0, currentMove);
            					draw();
            					checkerBoardSquares[i][j].setSelected(false);
            				}
            				break;
            			}
            		}
            	}
        	}
        }
        
        public final void clearChecks() {
        	for(int i=0; i<checkerBoardSquares.length; i++) {
        		for(int j=0; j<checkerBoardSquares[0].length; j++) {
        			checkerBoardSquares[i][j].setSelected(false);
        		}
        	}
        }
        
        public final void draw() {
        	if(!boardModel.isInGame()) {
        		String winnerText = "";
        		if(!boardModel.getTurn()) {
            		winnerText += "Black";
            	}
            	else {
            		winnerText += "Red";
            	}
        		winnerText += " Wins! Game Over.";
        		winLabel.setText(winnerText);
        	}
        	String msg = "Turn: ";
        	if(boardModel.getTurn()) {
        		msg += "Black";
        	}
        	else {
        		msg += "Red";
        	}
        	message.setText(msg);
        	String historyText = "Moves: ";
        	for(int i=0; i<historySize; i++) {
        		historyText += history.get(i) + " ";
        	}
        	moveHistory.setText(historyText);
        	Piece[][] board = boardModel.getBoard();
        	for(int i=0; i<board.length; i++) {
        		for(int j=0; j<board[0].length; j++) {
        			if(((j % 2 == 1) && (i % 2 == 1)) || ((j % 2 == 0) && (i % 2 == 0))) {
            					checkerBoardSquares[i][j].setBackground(Color.WHITE);
            		}
        			if(board[i][j] != null) {
        				if(board[i][j].getIsPlayerOne()) {
        					if(board[i][j].isKing()) {
        						try {
        		                    Image img = ImageIO.read(new FileInputStream("files/blackcrown.png"));
        		                    checkerBoardSquares[i][j].setIcon(new ImageIcon(img));
        		                } 
        		    			catch (Exception ex) {
        		                    System.out.println(ex);
        		                }
        					}
        					else {
        						try {
                	                Image img = ImageIO.read(new FileInputStream("files/circleblack.png"));
                	                checkerBoardSquares[i][j].setIcon(new ImageIcon(img));
                	            } 
                				catch (Exception ex) {
                	                System.out.println(ex);
                	            }
        					}
        				}
        				else {
        					if(board[i][j].isKing()) {
        						try {
        		                    Image img = ImageIO.read(new FileInputStream("files/redcrown.png"));
        		                    checkerBoardSquares[i][j].setIcon(new ImageIcon(img));
        		                } 
        		    			catch (Exception ex) {
        		                    System.out.println(ex);
        		                }
        					}
        					else {
        						try {
            						Image img = ImageIO.read(new FileInputStream("files/circlered.png"));
                	                checkerBoardSquares[i][j].setIcon(new ImageIcon(img));
                	            } 
                				catch (Exception ex) {
                	                System.out.println(ex);
                	            }
        					}
        				}
        			}
        			else {
        				try {
    						Image img = ImageIO.read(new FileInputStream("files/transparentsquare.png"));
        	                checkerBoardSquares[i][j].setIcon(new ImageIcon(img));
        	            } 
        				catch (Exception ex) {
        	                System.out.println(ex);
        	            }
        			}
        		}
        	}
        }

		public final JComponent getCheckersBoard() {
            return checkersBoard;
        }

        public final JComponent getGui() {
            return gui;
        }
}
