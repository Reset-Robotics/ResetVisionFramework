package application;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FrameworkBuilder extends Application {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(FrameworkBuilder.class, args);
		
		

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
        Parent root = FXMLLoader.load(getClass().getResource("/application/AppScene.fxml"));
        primaryStage.setTitle("ResetVisionFramework Builder");	
	    primaryStage.setScene(new Scene(root));
	    primaryStage.show();
	    
	    
		
	}

}
