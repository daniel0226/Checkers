package cs1302.arcade;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Stack;
/**
* Main Checkers class that first creates the board of rectangle
* class objects. Upon certain index values, the peices will also be created
* as well as the values for each square/piece.
* Upon each move, the move will be determined legal/illegal, if legal move is 
* done, and piece/board is updated.
* After each successful move, the program then checks if the next player has any
* moves left, or if any pieces remain. Depending on result, victory message is either
* displayed or game continues.
*/
public class Checkers {

    //GLOBAL VARIABLE DECLARATIONS
    private final int width = 8;
    private final int height = 8;
    private int xPos, yPos;
    private int BeigePieces = 0;
    private int BlackPieces = 0;
    private boolean gameChecking = false;

    private String[][] board = new String[width][height];
    private BoardSquares[][] squaresArr = new BoardSquares[width][height];
    private boolean[][] legalMoves = new boolean[width][height];

    private Pane pane = new Pane();
    private Group boardTiles = new Group();
    private Group pieces = new Group();
    private messages popup = new messages();

    private Turn playerTurn;
    private Turn currentTurn;
    private Stage stage;

    /**
     * Starts the game by creating a new scene and opening
     * the window application
     */
    public void startGame(){

        //Create stage and scene
        stage = new Stage();
        Scene scene = new Scene(createBoardandPieces());

        //Initialize boolean array of possible moves
        resetMovesArray();

        //Open window
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Checkers");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }


    /**
     * Creates the Pane that will contain the tile square class objects
     * representing the board with class object pieces.
     * @return pane the finished board
     */
    public Pane createBoardandPieces(){

        pane.setPrefSize(800,800);
        pane.getChildren().addAll(boardTiles, pieces);
        //Set Player 1 turn
        setTurn(Turn.Black);
        //REFERENCE, THE BOARD INDEX 0,0 IS AT THE TOP LEFT CORNER
        //AND THE BOARD INDEX 7,7 IS AT THE BOTTOM RIGHT CORNER
        for(int i = 0; i<height; i++){
            for(int j = 0; j<width; j++){
                BoardSquares square;
                Pieces p;
                //create the board contents
                square = createSquare(i,j);

                //Add to the board
                boardTiles.getChildren().add(square);

                //Create the board pieces
                if(((i+j) % 2 == 0 && j<=2)){
                    BeigePieces++;
                    p = createPiece(i, j);
                    square.setPiece(p);
                    pieces.getChildren().add(p);
                }
                if((i+j) % 2 == 0 && j>=5) {
                    BlackPieces++;
                    p = createPiece(i, j);
                    square.setPiece(p);
                    pieces.getChildren().add(p);
                }
            }
        }
        return pane;
    }

    /**
     * Helper function to create the square object
     * @param i index of pane
     * @param j index of pane
     * @return the new square tile
     */
    public BoardSquares createSquare(int i, int j){
        BoardSquares square = new BoardSquares(i,j,board);
        squaresArr[i][j] = square;
        return square;
    }

    /**
     * Checks if the current turn is correct for the player.
     * If it's the player's turn, they can make a move.
     * If it's not, they will not be able to move.
     * @param currentTurn who's turn it is
     * @param p the piece they are moving
     * @param oldXIndex original piece's index position
     * @param oldYIndex original piece's index position
     * @return whether the player can move or not
     */
    public boolean checkTurn(Turn currentTurn, Pieces p, int oldXIndex, int oldYIndex) {
        if (currentTurn == Turn.Black) {
            if (p.getPlayer() == PlayerType.Beige) {
                String message = "It is Player 1's Turn(Black).";
                popup.NotYourTurn(message);
                p.setPos(oldXIndex, oldYIndex);
                return false;
            }
        }
        if (currentTurn == Turn.Beige) {
            if (p.getPlayer() == PlayerType.Black) {
                String message = "It is Player 2's Turn(Beige).";
                popup.NotYourTurn(message);
                p.setPos(oldXIndex, oldYIndex);
                return false;
            }
        }
        return true;
    }

    /**
     * Creates the piece object.
     * The player can click and drag the piece, however
     * under certain circumstance's, the player can release
     * and officially move the piece.
     * @param i index piece will be created at
     * @param j index piece will be created at
     * @return the created piece
     */
    public Pieces createPiece(int i, int j){

        Pieces p = new Pieces(PieceType.Normal,i,j);
        currentTurn = getTurn();

        Turn player1 = Turn.Black;
        Turn player2 = Turn.Beige;

        //After clicking on the piece and moving, upon release
        p.setOnMouseReleased(e->{
            int oldXIndex = (int)p.getOldXPos();
            int oldYIndex = (int)p.getOldYPos();

            //Get the board index position
            xPos = (int)(p.getLayoutX() + 100/2)/100;
            yPos = (int)(p.getLayoutY() + 100/2)/100;

            //Check if valid turn
                if((checkTurn(currentTurn, p, oldXIndex, oldYIndex)) == false){
                    p.setPos(oldXIndex, oldYIndex);
                    return;
                }else {

                    //If correct turn,
                    //check if the move is valid
                    if (tryMove(gameChecking,p, p.getPlayer(), p.GetTypeOfPiece(), xPos, yPos)) {
                        squaresArr[oldXIndex][oldYIndex].setPiece(null);
                        squaresArr[xPos][yPos].setPiece(p);
                        p.setPos(xPos,yPos);
                        //End player's turn
                        if (currentTurn == Turn.Black) {
                            //If the black piece reaches the top, it's a queen now
                            if(yPos == 0) {
                                p.setType(PieceType.Queen);
                                p.updatePiece();
                                System.out.println(p.getPlayer() + " is now a King.");
                            }
                            System.out.println("It is now Beige's turn.");
                            setTurn(player2);
                        } else {
                            //If the beige piece reaches the top, it's a queen now
                            if(yPos == 7) {
                                p.setType(PieceType.Queen);
                                p.updatePiece();
                                System.out.println(p.getPlayer() + " is now a King.");
                            }
                            System.out.println("It is now Black's turn.");
                            setTurn(player1);
                        }
                        //update turn
                        currentTurn = getTurn();
                        resetMovesArray();
                        AnyMovesLeft();
                        //popup.Victory("popup.Victory(message, stage);", stage);

                    }
                }
        });

        return p;
    }
    /**
    * Resets the boolean array that contains which index values are legal moves
    */
    void resetMovesArray(){
        for(int i = 0; i<height; i++){
            for(int j = 0; j<width; j++){
                legalMoves[i][j] = false;
            }
        }
    }

    /**
     * Check's if there are any possible moves left to do for respective player
     * If there are none, then the game is over.
     * Also checks if there are any pieces left of the respective player
     * If none are available, then the other player wins
     */
    public void AnyMovesLeft(){
        Turn currentTurn = getTurn();
        Pieces p;
        Stack<Boolean> StackBlack = new Stack<>();
        Stack<Boolean> StackBeige = new Stack<>();
        gameChecking = true;

        //We will store possible moves in a Stack.
        StackBlack.push(true);
        StackBeige.push(true);
        //Player One(Black) just completed their turn. Now check if any more
        //possible moves are avilable. If not, Player Two(Beige) wins.
        if(currentTurn == Turn.Black){
            for(int i = 0; i<height; i++){
                for(int j = 0; j<width; j++){
                    //If the board has a black player piece
                    if(squaresArr[i][j].hasPiece() && squaresArr[i][j].currentPiece().getPlayer() == PlayerType.Black){
                        p = squaresArr[i][j].currentPiece();
                        //Check if any normal moves are possible
                        if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i + 1, j - 1))
                            StackBlack.push(true);
                        if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i + 1, j + 1))
                            StackBlack.push(true);
                        if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i - 1, j - 1))
                            StackBlack.push(true);
                        if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i - 1, j + 1))
                            StackBlack.push(true);
                    }
                }
            }//end for
        }else{//Same principal if it's Player Two's turn(Beige)
            for(int i = 0; i<height; i++){
                for(int j = 0; j<width; j++){
                    //If the board has a black player piece
                    if(squaresArr[i][j].hasPiece() && squaresArr[i][j].currentPiece().getPlayer() == PlayerType.Beige){
                        p = squaresArr[i][j].currentPiece();
                        //Check if any normal moves are possible
                        if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i + 1, j - 1))
                            StackBlack.push(true);
                        if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i + 1, j + 1))
                            StackBlack.push(true);
                        if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i - 1, j - 1))
                            StackBlack.push(true);
                        if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i - 1, j + 1))
                            StackBlack.push(true);
                    }
                }
            }//end for
        }//end else
        //We couldn't find any normal moves. Now to check if any possible jumps are avilable
        if(StackBlack.isEmpty() || StackBeige.isEmpty()){
            if(currentTurn == Turn.Black){
                for(int i = 0; i<height; i++){
                    for(int j = 0; j<width; j++){
                        //If the board has a black player piece
                        if(squaresArr[i][j].hasPiece() && squaresArr[i][j].currentPiece().getPlayer() == PlayerType.Black){
                            p = squaresArr[i][j].currentPiece();
                            //Check if any normal moves are possible
                            if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i + 2, j - 2))
                                StackBeige.push(true);
                            if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i + 2, j + 2))
                                StackBeige.push(true);
                            if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i - 2, j - 2))
                                StackBeige.push(true);
                            if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i - 2, j + 2))
                                StackBeige.push(true);
                        }
                    }
                }//end for
            }else{//Same principal if it's Player Two's turn(Beige)
                for(int i = 0; i<height; i++){
                    for(int j = 0; j<width; j++){
                        //If the board has a black player piece
                        if(squaresArr[i][j].hasPiece() && squaresArr[i][j].currentPiece().getPlayer() == PlayerType.Beige){
                            p = squaresArr[i][j].currentPiece();
                            //Check if any normal moves are possible
                            if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i + 2, j - 2))
                                StackBeige.push(true);
                            if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i + 2, j + 2))
                                StackBeige.push(true);
                            if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i - 2, j - 2))
                                StackBeige.push(true);
                            if (tryMove(gameChecking, p, p.getPlayer(), p.GetTypeOfPiece(), i - 2, j + 2))
                                StackBeige.push(true);
                        }
                    }
                }//end for
            }//end else
        }

        //We're done checking
        gameChecking = false;

        //If we couldn't find any legal moves, than it's the game is done
        if(StackBlack.isEmpty()){
            String Beigewins = "Player Two(Beige) Wins!";
            popup.Victory(Beigewins, stage);
            return;
        }
        if(StackBeige.isEmpty()){
            String BlackWins = "Player One(Black) Wins!";
            popup.Victory(BlackWins, stage);
            return;
        }
        if(BlackPieces == 0){
            String Beigewins = "Player Two(Beige) Wins!";
            popup.Victory(Beigewins, stage);
            return;
        }
        if(BeigePieces == 0){
            String BlackWins = "Player One(Black) Wins!";
            popup.Victory(BlackWins, stage);
            return;
        }
        StackBlack.clear();
        StackBeige.clear();

    }
    public void setTurn(Turn turn){
        playerTurn = turn;
    }
    public Turn getTurn(){
        return playerTurn;
    }

    /**
     * tests the players movement
     * General Rules of Checkers: Players can only move diagonally, not straight onto pieces opposite
     * of color. Players can also only move forward unless it's a queen piece. If an enemy piece
     * is diagonally to your piece, you can do jump(s).
     * @param player Whether player one or two
     * @param type Whether Queen or Normal
     * @param x index we are trying to move to
     * @param y index we are trying to move to
     * @return
     */
    public boolean tryMove(Boolean gameCheck,Pieces p,PlayerType player,PieceType type,int x, int y) {
        if(x < 0 || x > 7 || y < 0 || y > 7)return false; //Out of bounds

        int oldX = (int) p.getOldXPos(); //Get old X index value
        int oldY = (int) p.getOldYPos(); //Get old Y index value
        
        if(squaresArr[x][y].hasPiece()){ //Can't move if a piece already exists there
            p.setPos(oldX,oldY);
            return false;
        }
        if(squaresArr[x][y].getColor() == SquareColor.Beige){
            p.setPos(oldX,oldY);
            return false;
        }

        if(gameCheck == false){
            System.out.println("Attempting move from: " + "(" + oldX + "," + oldY + ")" + " to: " +
                    "(" + x + "," + y + ")");
        }

        if(type == PieceType.Normal){
            if(Math.abs(oldY-y) > 1){
                if(checkJump(gameCheck,type,player, oldX, oldY, x, y)){
                    return true;
                }else{
                    p.setPos(oldX,oldY);
                    return false;
                }
            }else{
                if (checkNormalMove(type,player, oldX, oldY, x, y)){
                    return true;
                }else{
                    p.setPos(oldX,oldY);
                    return false;
                }
            }
        }else{
            if(checkQueenMove(gameCheck,type,player,oldX,oldY,x,y)){
                return true;
            }else{
                p.setPos(oldX,oldY);
                return false;
            }
        }
    }//end tryMove
    /**
    * Check's if the normal move is valid. 
    * A normal move is when a piece moves diagonally once
    */
    public boolean checkNormalMove(PieceType type,PlayerType player, int oldX, int oldY, int newX, int newY){
        
        if(Math.abs(oldX - newX) != 1)return false;//Cannot move more than 1
        if(Math.abs(oldY - newY) != 1)return false;//Cannot move more than 1
        if(squaresArr[newX][newY].hasPiece()){//Cannot have a piece
            return false;
        }
        if(type != PieceType.Queen) {//We can neglect Queen's since they can move any direction
            if (player == PlayerType.Black) {
                if (newY > oldY) {
                    return false;
                }
            } else {
                if (newY < oldY) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
    * Checks if the jump is a valid move. 
    * This does not take into count of multi-jumps.
    */
    public boolean checkJump(Boolean gameCheck,PieceType type,PlayerType player,int oldX, int oldY, int x, int y){
        if(oldX < 0 || x < 0 || oldX > 7 || x > 7 || y < 0 || oldY < 0 || oldY > 7 || y > 7)return false;
        //base, can't move odd index values diagonally
        //cannot move 2 index value straight
        if(type != PieceType.Queen) {
            if ((oldX - x) % 2 != 0 || (Math.abs(oldY - y) == 2 && oldX - x == 0)) {
                return false;
            }
        }
        legalMoves[oldX][oldY] = true;

        int permaCol = oldY;

        //Determine if move is legal for normal pieces
        if(type == PieceType.Normal) {
            //Helper to find if jump is legal
            helper(gameCheck,type, player, permaCol,oldX, oldY, x, y);
        }else{//for queen piece
            helperQueen(gameCheck,player, oldX, oldY, x ,y);
        }
        //printBoolTest();

        return legalMoves[x][y];
    }
    /**
    * Determines if the queen jump is valid.
    * We check the boundaries as well as if the piece we are jumping over
    * is valid. Also checks depending on which direction the queen moved.
    */
    public void helperQueen(boolean gameCheck,PlayerType player, int oldX, int oldY, int newX, int newY){
        if(oldX < 0 || newX < 0 || oldX > 7 || newX > 7 || newY < 0 || oldY < 0 || oldY > 7 || newY > 7)return;//Out of bounds
        if(squaresArr[newX][newY].hasPiece()){
            legalMoves[newX][newY] = false;//If a piece already exists
        }
        if(gameCheck){
            if(newX+ 1 > 7 || newY + 1 > 7 || newX - 1 < 0 || newY + 1 > 7 || newY - 1 < 0 )return;
        }
        BoardSquares MiddleSq;
        //Queen moved NorthEast
        //3, 5 -> 4,4
        if(newX>oldX && newY<oldY){
            MiddleSq = squaresArr[oldX+1][oldY-1];
            if(MiddleSq.hasPiece()){//Middle square has a piece
                //We are player black and we found a beige in the middle
                if(player == PlayerType.Black && MiddleSq.currentPiece().getPlayer() == PlayerType.Beige){
                    if(gameCheck){
                        return;
                    }
                    deleteMiddlePiece(newX, newY, oldX +1, oldY -1);
                    BeigePieces--;
                    return;
                }//We are player Beige and we found a black in the middle
                if(player == PlayerType.Beige && MiddleSq.currentPiece().getPlayer() == PlayerType.Black){
                    if(gameCheck){
                        return;
                    }
                    deleteMiddlePiece(newX, newY, oldX +1, oldY -1);
                    BlackPieces--;
                    return;
                }
            }
        }//Queen moved NorthWest
        //2,2 -> 0,0
        if(newX < oldX && newY < oldY){
            MiddleSq = squaresArr[oldX-1][oldY-1];
            if(MiddleSq.hasPiece()){//Middle square has a piece
                //We are player black and we found a beige in the middle
                if(player == PlayerType.Black && MiddleSq.currentPiece().getPlayer() == PlayerType.Beige){
                    if(gameCheck){
                        return;
                    }
                    deleteMiddlePiece(newX, newY, oldX -1, oldY -1);
                    BeigePieces--;
                    return;
                }//We are player Beige and we found a black in the middle
                if(player == PlayerType.Beige && MiddleSq.currentPiece().getPlayer() == PlayerType.Black){
                    if(gameCheck){
                        return;
                    }
                    deleteMiddlePiece(newX, newY, oldX -1, oldY -1);
                    BlackPieces--;
                    return;
                }
            }
        }
        //Queen moved SouthEast
        //2,2 -> 4,4
        if(newX > oldX && newY > oldY){
            MiddleSq = squaresArr[oldX+1][oldY+1];
            if(MiddleSq.hasPiece()){//Middle square has a piece
                //We are player black and we found a beige in the middle
                if(player == PlayerType.Black && MiddleSq.currentPiece().getPlayer() == PlayerType.Beige){
                    if(gameCheck){
                        return;
                    }
                    deleteMiddlePiece(newX, newY, oldX + 1, oldY + 1);
                    BeigePieces--;
                    return;
                }//We are player Beige and we found a black in the middle
                if(player == PlayerType.Beige && MiddleSq.currentPiece().getPlayer() == PlayerType.Black){
                    if(gameCheck){
                        return;
                    }
                    deleteMiddlePiece(newX, newY, oldX + 1, oldY + 1);
                    BlackPieces--;
                    return;
                }
            }
        }
        //Queen moved SouthWest
        //2,2 -> 0,4
        if(newX < oldX && newY > oldY){
            MiddleSq = squaresArr[oldX-1][oldY+1];
            if(MiddleSq.hasPiece()){//Middle square has a piece
                //We are player black and we found a beige in the middle
                if(player == PlayerType.Black && MiddleSq.currentPiece().getPlayer() == PlayerType.Beige){
                    if(gameCheck){
                        return;
                    }
                    deleteMiddlePiece(newX, newY, oldX - 1, oldY + 1);
                    BeigePieces--;
                    return;
                }//We are player Beige and we found a black in the middle
                if(player == PlayerType.Beige && MiddleSq.currentPiece().getPlayer() == PlayerType.Black){
                    if(gameCheck){
                        return;
                    }
                    deleteMiddlePiece(newX, newY, oldX - 1, oldY + 1);
                    BlackPieces--;
                    return;
                }
            }
        }
    }
    /**
    * Helper function to delete the middle peice that was jumped over
    * upon completion of move.
    */
    public void deleteMiddlePiece(int row, int col, int middleRow, int middleCol){
        legalMoves[row][col] = true;
        squaresArr[middleRow][middleCol].currentPiece().removePiece();
        squaresArr[middleRow][middleCol].setPiece(null);
    }

    /**
     * This function determines if the jump by a normal piece is legal or not
     * @param player whether player is beige or black piece
     * @param permaCol cannot exceed this value
     * @param oldrow old x value
     * @param oldcol old y value
     * @param row new x value
     * @param col new y value
     */
    public void helper(Boolean gameCheck,PieceType type,PlayerType player,int permaCol,int oldrow, int oldcol, int row, int col){
        if(oldrow < 0 || row < 0 || row > 7 || oldrow > 7 || col < 0 || oldcol < 0 || oldcol > 7 || col > 7)return;//Out of bounds
        if(squaresArr[row][col].hasPiece()){
            legalMoves[row][col] = false;//If a piece already exists
        }
        if(gameCheck){
            if(oldrow + 1 > 7 || oldcol + 1 > 7 || oldrow - 1 < 0 || oldcol + 1 > 7 || oldcol - 1 < 0 )return;
        }
        if(type != PieceType.Queen) {
            if (row % 2 == 0 && oldrow % 2 != 0) return;
            if (row % 2 != 0 && oldrow % 2 == 0) return;
            if (col % 2 == 0 && oldcol % 2 != 0) return;
            if (col % 2 != 0 && oldcol % 2 == 0) return;
        }
        if(player == PlayerType.Black){
            if(col >= permaCol && type != PieceType.Queen)return;

            if(row<oldrow){//we jumped left
                if (squaresArr[oldrow - 1][oldcol - 1].hasPiece()) {
                    if ((squaresArr[oldrow - 1][oldcol - 1].currentPiece().getPlayer() == PlayerType.Beige)
                            && legalMoves[oldrow][oldcol]) {
                        if(gameCheck){
                            return;
                        }
                        deleteMiddlePiece(row,col,oldrow-1,oldcol-1);
                        BeigePieces--;
                        return;
                    }
                }
            }else{//we jumped right
                if (squaresArr[oldrow + 1][oldcol - 1].hasPiece()) {
                    if ((squaresArr[oldrow + 1][oldcol - 1].currentPiece().getPlayer() == PlayerType.Beige)
                            && legalMoves[oldrow][oldcol]) {
                        if(gameCheck){
                            return;
                        }
                        deleteMiddlePiece(row,col,oldrow+1,oldcol-1);
                        BeigePieces--;
                        return;
                    }
                }
            }
        }else{//Beige player
            if(col <= permaCol && type != PieceType.Queen)return;

            if(row<oldrow){//we jumped left
                if (squaresArr[oldrow - 1][oldcol + 1].hasPiece()) {
                    if ((squaresArr[oldrow - 1][oldcol + 1].currentPiece().getPlayer() == PlayerType.Black)
                            && legalMoves[oldrow][oldcol]) {
                        if(gameCheck){
                            return;
                        }
                        deleteMiddlePiece(row,col,oldrow - 1,oldcol + 1);
                        BlackPieces--;
                        return;
                    }
                }

            }else{//we jumped right
                if(type != PieceType.Queen) {
                    if (squaresArr[oldrow + 1][oldcol + 1].hasPiece()) {
                        if ((squaresArr[oldrow + 1][oldcol + 1].currentPiece().getPlayer() == PlayerType.Black)
                                && legalMoves[oldrow][oldcol]) {
                            if(gameCheck){
                                return;
                            }
                            deleteMiddlePiece(row,col,oldrow + 1,oldcol + 1);
                            BlackPieces--;
                            return;
                        }
                    }
                }
            }
        }
    }
    /**
    * Checks if the queen move was valid
    * Calls upon the normal functions for normal piece, but carries over
    * that the player is a queen rather than a normal piece.
    */
    public boolean checkQueenMove(Boolean gamecheck,PieceType type,PlayerType player, int oldX, int oldY, int newX, int newY){
        resetMovesArray();

        legalMoves[oldX][oldY] = true;
        int permaCol = oldY;
        if(type != PieceType.Queen)return false;

        //we jumped
        if(Math.abs(oldY-newY) > 1){
            return checkJump(gamecheck,type,player, oldX, oldY, newX,newY);
        }else{//we didn't jump
            return checkNormalMove(type,player, oldX, oldY, newX, newY);
        }
    }

}//end Checkers
