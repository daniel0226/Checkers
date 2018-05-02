package cs1302.arcade;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
* Opens a message upon either user tries to move when it's not their turn
* Or either user wins the game.
*/
public class messages{
    
    Stage brStage;
    Scene brScene;
    BorderPane newPane;

    /**
     * Opens a message stating it's not your turn
     * @param message the message whether it's player 1 or 2's turn.
     */
    public void NotYourTurn(String message){
        brStage = new Stage();
        newPane = new BorderPane();
        newPane.setPrefSize(250,100);

        Text error = new Text(message);
        newPane.setCenter(error);

        brScene = new Scene(newPane);
        brStage.setScene(brScene);
        brStage.sizeToScene();
        brStage.setTitle("Not your Turn!!");
        brStage.initModality(Modality.APPLICATION_MODAL);
        brStage.show();
    }
    /**
     * messages message, if player deicides to play again, the board
     * will reset, otherwise player can quit.
     * @param message the message whether player1 or 2 won.
     * @param currentStage the stage we are playing on
     */
    public void Victory(String message, Stage currentStage){

        //Create the new victory popup stage
        brStage = new Stage();
        newPane = new BorderPane();
        newPane.setPrefSize(250,100);

        //Message
        Text victoryMessage = new Text(message);

        //Will hold the buttons
        HBox buttons = new HBox();

        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(33);

        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(15);

        Button restart = new Button("Play again?");
        Button quit = new Button("Quit");
        buttons.getChildren().addAll(restart,quit);

        box.getChildren().addAll(victoryMessage, buttons);

        //Player wants to restart
        restart.setOnAction(e->{
            Checkers ch = new Checkers();
            ch.startGame();
            currentStage.close();
            brStage.close();
        });
        //Player wants to quit
        quit.setOnAction(e->{
            currentStage.close();
            brStage.close();
        });

        newPane.setCenter(box);

        brScene = new Scene(newPane);

        //Create scene
        brStage.setScene(brScene);
        brStage.sizeToScene();
        brStage.setTitle("You Win!");
        brStage.initModality(Modality.APPLICATION_MODAL);
        brStage.show();
    }

}
