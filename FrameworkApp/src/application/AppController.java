package application;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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
	private ChoiceBox<String> cameraDropdown;
    @FXML
    private CheckBox exposureBox;
	@FXML
	private void initialize()  {
		cameraDropdown.setItems(FXCollections.observableArrayList("1", "2"));
	}

	 @FXML
	 private void handleButtonAction(ActionEvent event) throws IOException {
	     System.out.println("Generating File...");
	     BufferedWriter writer = new BufferedWriter(new FileWriter("file.cpp"));
	     
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
	     
	     writer.write("\nint main() {");
	     
	     if(zmqBox.isSelected()) {
	    	 writer.write("\nzmq::context_t context (1);");
	    	 writer.write("\nzmq::socket_t publisher(context, ZMQ_PUB);");
	    	 // Replace 5801 with port variable from text area
	    	 writer.write("\npublisher.bind" + "(\"tcp://*:" + "5801" + ");");
	    	 
	     }
	     if(networkTableBox.isSelected()) {
	    	 writer.write("\nNetworkTable::SetClientMode();");
	    	 writer.write("\nNetworkTable::SetIPAddress(\"10.63.25.2\";");
	    	 writer.write("\nNetworkTable::Initialize();");
	    	 writer.write("\n std::shared_ptr<NetworkTable> table = NetworkTable::GetTable(\"vision\")");

        //table->PutNumber("X", 5);");
	    	}
	     if(exposureBox.isSelected()) {
	    	 // Can for loop this till the number of cameras if the arraylist contains integers.
	    	 if(cameraDropdown.getValue() == "1"){
	    		 // Replace video0 with indices that user inputs
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video0 -c exposure_auto=1\")");
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video0 -c exposure_absolute=5\")");
	    	 }
	    	 if(cameraDropdown.getValue() == "2"){
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video0 -c exposure_auto=1\")");
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video1 -c exposure_auto=1\")");
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video0 -c exposure_absolute=5\")");
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video1 -c exposure_absolute=5\")");
	    	 } 
	     }
	    
	     
	     writer.flush();
	     writer.close();
	     
	     
	     
	 }
	 
	 

}

