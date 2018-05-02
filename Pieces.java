package cs1302.arcade;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
* Pieces class makes use of a StackPane to create the piece
* The Position will be set in the center of the board rectangle
* if move is legal and will be set to a color/King upon 
* moves or creation.
*/
public class Pieces extends StackPane{

    private double XPos,YPos,oldXPos,oldYPos;
    private PieceType type = null;
    private PlayerType  player = null;
    private Circle cir, cir2;
    private Label King = new Label("King");

    /**
     * Constructor to create the Pieces object
     * @param i the height index
     * @param j the width index
     * @param t the type of piece
     */
    public Pieces(PieceType t,int i, int j){

        //Construct sets piece type to it's position and as normal piece
        setType(t);
        setPos(i,j);

        //Create the piece
        cir = new Circle();
        cir2 = new Circle();
        cir.setRadius(40);
        cir2.setRadius(34);

        //Place piece in it's correct position depending on index values
        if((i+j) % 2 == 0 && j>=5){
            setPlayerType(PlayerType.Black);
            cir.setFill(Color.BROWN);
            cir2.setFill(Color.BLACK);

        }
        else if((i+j) % 2 == 0 && j<=2){
            setPlayerType(PlayerType.Beige);
            cir.setFill(Color.WHITE);
            cir2.setFill(Color.BEIGE);
        }else{
            return;
        }

        //Add it to the stackpane
        getChildren().addAll(cir,cir2);
        setAlignment(Pos.CENTER);

        //Move and drag checker
        setOnMousePressed(e ->{
            XPos = e.getSceneX();
            YPos = e.getSceneY();
        });
        setOnMouseDragged(e ->{
            relocate(e.getSceneX() - XPos + oldXPos, e.getSceneY() - YPos + oldYPos);
        });
    }

    /**
     * Set position and allignment to make sure it's in the center
     * @param i index of height
     * @param j index of width
     */
    public void setPos(int i, int j){
        oldXPos = i * 100;
        oldYPos = j * 100;
        //We are doing +10, because I set the circle to radius of 40, so the difference would be
        //meaning there is a gap of 20. Since the position will set the circle in the top
        //left corner, we move it to center position.
        relocate(oldXPos+10,oldYPos+10);
    }
    public double getOldXPos(){
        return oldXPos/100;
    }
    public double getOldYPos(){
        return oldYPos/100;
    }

    /**
     * Gets the piece type.
     * This will be used when creating/analyzing checker piece movement
     * since it can depend on if it's a queen or not
     * @return piece type.
     */
    public PieceType GetTypeOfPiece(){
        //System.out.println(this.type); test
        return this.type;
    }
    public void updatePiece(){
            if(player == PlayerType.Black) {
                King.setTextFill(Color.BEIGE);
            }else{
                King.setTextFill(Color.BLACK);
            }
            if(getChildren().contains(King)){
                return;
            }else {
                getChildren().add(King);
            }

    }

    /**
     * Sets the piece type
     * @param type the type of piece
     */
    public void setType(PieceType type){
        this.type = type;
    }
    /**
    * Sets the Player type
    * @param p the player type
    */
    public void setPlayerType(PlayerType p){
        this.player = p;
    }
     /**
    * Gets the player type
    */
    public PlayerType getPlayer(){
        return player;
    }
    /**
    * Removes the pieces from the square.
    */
    public void removePiece(){
        getChildren().clear();

    }
}
