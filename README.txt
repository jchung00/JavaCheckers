=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays
  I used a 2D array in the BoardModel class in order to represent the state of the pieces on the board.
  A 2D array was the best choice to represent the state, because the size of the board does not change.
  The checkers game is always played on an 8 by 8 board, and the initial setup is also constant.
  Each point on the 2D array was represented by the Piece class, which I created.
  The Piece class contains local variables that determine the owner of the piece (player 1 or 2) and whether it is a king or not.
  An empty square on the board would be represented by null.

  2. Collections
  Collections were incorporated in a couple places in the overall game design.
  In the proposal, I noted that I would use Collections in order to keep move history in a LinkedList.
  I ended up using an ArrayList for this feature, because it was important to modify elements in the list by their index.
  I also used a list for the getLegalMoves method in the BoardModel class.
  The getLegalMoves method implements the algorithm to find the valid moves for a certain piece on the board.
  It returns all points that a certain piece could move to in the form of a list.
  I decided to use a LinkedList of points for this, because the size of the list of valid moves would typically vary for each piece.
  Adding an element to a LinkedList is also much faster than potential alternatives.
  Finally, there was no need for the points in the list to be sorted, so using a LinkedList made a lot of sense.
  

  3. File I/O
  I used File I/O in order to implement the Save/Load feature for the game.
  When the user clicks on the "Save" button, the save method from the CheckersPanel class is called, and the data in the BoardModel class is saved into a text file.
  The first letter in the file -"t" or "f"- represents the turn when the game was saved.
  The remaining 128 letters represents the pieces on the board, each point getting two letters.
  If the point is an unoccupied square, "0n" would be saved into the file.
  If the point is an occupied square, the number would represent the player that owns the piece ("1" for player 1, "2" for player 2).
  The letter would then represent if the piece was a king. "k" represents king, "n" represent not king.
  The "Load" button calls the load method from the CheckersPanel class, and the data from the text file is read to initialize a new instance of the BoardModel class.
  Then, the draw method is called to draw the board according to the data in the BoardModel class.
  I used a BufferReader and BufferWriter that took in a FileReader and FileWriter, respectively, in order to read from and write into files.
  The data I wrote into these files were formatted in a more simple manner than what I initially planned in the proposal.
  There was no need to write in special characters when I could use the indices in the string to determine the point on the board that the substring represented.

  4. Testable Component
  The BoardModel class is fully testable. I wrote several test cases for the getLegalMoves and move methods.
  I tested edge cases such as trying to move a non-existent piece (represented by null on the board).
  I tested invalid moves, valid moves to open spaces, capture moves, moving a piece to make it a king, 
  and moving to win and verifying that the BoardModel recognized the changes in game state.
  The current code passed all of the JUnit test cases that I wrote in the BoardModelTest class.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  Game class:
  The Game class implements the Runnable interface. 
  It constructs an instance of the CheckersPanel class and adds the GUI component to the main frame.
  The Game class has a main method and can be run to create the Java application.
  
  CheckersPanel class:
  The CheckersPanel class initializes the GUI component, and implements all of the GUI-related logic of the game.
  It constantly uses the data from the instance of the BoardModel class and its methods.
  For example, in order to highlight the legal moves green for a certain point on the board, the CheckersPanel class calls the getLegalMoves method from the BoardModel class.
  Every click calls the squarePressed method, which also calls the draw method.
  The draw method essentially repaints the board depending on the square pressed and any changes to the BoardModel.
  For example, if a piece was moved to the opponent's end of the board through a move, 
  the draw method would draw the piece in its new position, and draw the king icon instead of the original circle icon.
  The draw method also updates the history log on the GUI, and updates the text showing which player's turn it is by calling the getTurn method in the BoardModel class.
  The save and load methods write into and read data from text files, and are part of the action listeners associated with the respective buttons.
  
  BoardModel class:
  The BoardModel class keeps the complete state of the board.
  It holds a 2D array of pieces that represent the location of each piece on the board.
  It also holds boolean values for the current player's turn and whether the players are still in game.
  The BoardModel constructor creates the initial board layout and sets the turn as player one.
  The getLegalMoves method returns a list of points that a piece at a certain point can move to.
  The move method method moves a piece from its original point to another position.
  The move method returns true if the move was valid and successful. If not, it returns false.
  If the piece reaches the opponent's end on the board, the move method checks for this and converts the piece to a king.
  The move method also checks whether a player has no more pieces left, and sets the inGame value to false if one of the players have won.
  
  
  Piece class:
  The piece class holds two local variables: isPlayerOne and isKing.
  These values determine the player that owns the piece, as well as whether the piece is a king or not.
  The board in the BoardModel is a 2D array of the Piece class.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  It took me quite a while to get the GUI look like how it is right now. 
  I had to play around a lot with the GridLayout to get the classic board design that I wanted.
  I also ended up changing the design from using JButtons for the squares on the board to JCheckButtons.
  This is because I have to keep the state of which square was pressed before certain clicks in order to have both the start and end points when calling the move method.
  I also made the mistake of using an ItemListener that called the itemStateChanged method for the JCheckButtons.
  This caused the program to go into an infinite loop when the checkbox state changed, 
  and it took a lot of debugging to realize that the itemStateChanged method was causing it.
  The solution was to use an ActionListener for the JCheckButtons instead.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  
  I think that my design for this project was quite solid.
  The GUI logic and data model were implemented in separate classes.
  All data relating to the board state and making modifications to the state is done through the public methods in the BoardModel class.
  The CheckersPanel class simply calls these methods to find the most current values in order to keep the GUI constant with the data in the BoardModel instance.
  I especially benefited from this design when implementing the save and load functionalities.
  When writing data to the text file, the load method in the CheckersPanel class writes data solely based on the BoardModel instance.
  Then, the save method reads the text file and creates a new instance of the BoardModel class that reflects the exact state of the board that was saved.
  Though I had predicted the save and load functionalities to be difficult to implement, 
  it was actually just a matter of writing accurate data into a text file, and then parsing the String correctly to create a new BoardModel instance.
  There were no changes that had to be made in the BoardModel class at all, 
  and there was no change needed in any of the GUI logic for the CheckersPanel class outside of the save and load methods themselves.

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  I used iconsdb.com for the icons used to represent the various pieces on the board. 
  All libraries used were standard Java libraries.
