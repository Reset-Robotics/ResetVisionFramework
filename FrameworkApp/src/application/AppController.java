package application;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class AppController {

	
	@FXML
	private CheckBox gpuAccelBox;
	
	@FXML
	private CheckBox hsvBox;
	@FXML
	private CheckBox contoursBox;
	@FXML
	private CheckBox networkTableBox;
	@FXML
	private CheckBox zmqBox;
	@FXML
	private Button submitButton;
	

	@FXML
	private void initialize()  {
		gpuAccelBox.setSelected(true);
	}

	 @FXML
	 private void handleButtonAction(ActionEvent event) throws IOException {
	     // Button was clicked, do something...
	     System.out.println("Button CLicked!");
	     BufferedWriter writer = new BufferedWriter(new FileWriter("test.cpp"));
	     writer.write("#include <opencv2/highgui/highgui.hpp> \n"
					+ "#include <opencv2/imgproc/imgproc.hpp> \n"
					+ "#include <stdio.h> \n"
					+ "#include <stdlib.h> \n"
					+ "#include <stdio.h> \n"
					+ "#include <stdlib.h> \n"
					+ "#include <string.h> \n"
					+ "#include <time.h> \n"
					+ "#include <sys/time.h> \n"
					+ "#include <cstdio> \n"
					+ "#include <cmath> \n"
					+ "#include <cstdlib> \n"
					+ "#include <sstream> \n");
	     if (gpuAccelBox.isSelected()) {
	    	 writer.write("#include " + "\"opencv2/gpu/gpu.hpp\" \n" );
	     }
	     
	     writer.close();
	     
	 }

}

