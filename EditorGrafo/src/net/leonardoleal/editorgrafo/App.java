package net.leonardoleal.editorgrafo;


import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		URL mainSceneUrl = getClass().getResource("Views/mainScene.fxml");
		FXMLLoader loader = new FXMLLoader(mainSceneUrl);
		Parent mainSceneParent = loader.load();
		Scene scene = new Scene(mainSceneParent);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("graFORGE");
		primaryStage.show();
		
	}

}
