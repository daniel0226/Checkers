
package cs1302.arcade;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class ArcadeApp extends Application {

	Random rng = new Random();

	@Override
		public void start(Stage stage) {
			AnchorPane root = new AnchorPane();
			root.setPrefSize(600,600);
			
			Button gameTwo = new Button("Checkers");

			gameTwo.setOnAction((event) -> {

					Checkers checkers = new Checkers();
					checkers.startGame();
					});

			gameTwo.setLayoutX(400);
			gameTwo.setLayoutY(300);

			root.getChildren().addAll(gameTwo);

			Scene scene = new Scene(root);


			stage.setScene(scene);

			stage.show();
			
		} // start

	public static void main(String[] args) {
		try {
			Application.launch(args);
		} catch (UnsupportedOperationException e) {
			System.out.println(e);
			System.err.println("If this is a DISPLAY problem, then your X server connection");
			System.err.println("has likely timed out. This can generally be fixed by logging");
			System.err.println("out and logging back in.");
			System.exit(1);
		} // try
	} // main

} // ArcadeApp

