package cs1302.arcade;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardSquares extends Rectangle{

    private Pieces piece = null;
    private SquareColor color;

    public BoardSquares(int i, int j, String board[][]){

        setHeight(100);
        setWidth(100);

        if((i+j) % 2 == 0){
            setFill(Color.BLACK);
            board[i][j] = "black";
            color = SquareColor.Black;
        }
        if((i+j) % 2 != 0){
            setFill(Color.BEIGE);
            board[i][j] = "beige";
            color = SquareColor.Beige;
        }
        relocate((double) i * 100, (double) j * 100);
    }
    public void setPiece(Pieces piece){
        this.piece = piece;
    }
    public Pieces currentPiece(){
        return this.piece;
    }
    public SquareColor getColor(){
        return this.color;
    }

    public boolean hasPiece(){
        if(this.piece == null){
            return false;
        }else{
            return true;
        }
    }
}
