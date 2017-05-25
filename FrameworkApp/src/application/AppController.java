package application;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class AppController {

	
	@FXML
	private CheckBox gpuAccelBox;
	@FXML
	private CheckBox hsvBox;
	@FXML
	private CheckBox contourBox;
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
    private TextField portText;
    @FXML
    private CheckBox trackbarBox;
    @FXML
    private TextField hField;
    @FXML
    private CheckBox cmakeBox;
	@FXML
	private void initialize()  {
		cameraDropdown.setItems(FXCollections.observableArrayList("1", "2"));
		hField.setVisible(false);
		  
	}

	@FXML
	 private void handleButtonAction(ActionEvent event) throws IOException {
	     System.out.println("Generating File...");
	     BufferedWriter writer = new BufferedWriter(new FileWriter("file.cpp"));
	     
	     writer.write("/*");
	     writer.newLine();
	     writer.newLine();
	     writer.write("Reset Robotics, FRC Team #6325 - Vison Framework");
	     writer.newLine();
	     writer.newLine();
	     writer.write("* Name          : Vision Framework Generator");
	     writer.write("\n* Version       : 0.5.0");
	     writer.write("\n* Build Date    : 22 May 2017");
	     writer.write("\n* Last Updated    : 22 May 2017");
	     writer.write("\n* Developed By  : Reset Robotics, FRC Team #6325");
	     writer.write("\n* Author(s)     : Reset Robotics - Prajwal Vedula");
	     writer.newLine();
	     writer.write("\nCopyright (C) 2017 Reset Robotics");
	     writer.newLine();
	     writer.write("\n*/");
	 
	     
	     writer.write("\n#include <opencv2/highgui/highgui.hpp> \n"
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
	    	 String port;
	    	 if(portText.getText() != null) {
	    		 port = portText.getText(); 
	    	 } else {
	    	  port = "5801";
	    	 }
	         writer.write("\nzmq::context_t context (1);");
	    	 writer.write("\nzmq::socket_t publisher(context, ZMQ_PUB);");
	    	 writer.write("\npublisher.bind" + "(\"tcp://*:" + port + "\");");
	    	 
	     }
	     if(networkTableBox.isSelected()) {
	    	 writer.write("\nNetworkTable::SetClientMode();");
	    	 writer.write("\nNetworkTable::SetIPAddress(\"10.63.25.2\");");
	    	 writer.write("\nNetworkTable::Initialize();");
	    	 writer.write("\nstd::shared_ptr<NetworkTable> table = NetworkTable::GetTable(\"vision\")");

        //table->PutNumber("X", 5);");
	    	}
	     if(exposureBox.isSelected()) {
	    	 // Can for loop this till the number of cameras if the arraylist contains integers.
	    	 if(cameraDropdown.getValue() == "1"){
	    		 // Replace video0 with indices that user inputs
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video0 -c exposure_auto=1\");");
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video0 -c exposure_absolute=5\");");
	    	 }
	    	 if(cameraDropdown.getValue() == "2"){
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video0 -c exposure_auto=1\");");
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video1 -c exposure_auto=1\");");
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video0 -c exposure_absolute=5\");");
	    		 writer.write("\nsystem" + "(\"v4l2-ctl -d /dev/video1 -c exposure_absolute=5\");");
	    	 } 
	     }
	    
	     writer.write("\nint img_scale_factor = 1; //halves the size of the picture");
	     if(hsvBox.isSelected()) {
	    	 // Get HSV from user and store in variable
	    	 writer.write("\nint h_lowerb = ;");
	    	 writer.write("\nint h_upperb = ;");
	    	 writer.write("\nint s_lowerb = ;");
	    	 writer.write("\nint s_upperb = ;");
	    	 writer.write("\nint v_lowerb = ;");
	    	 writer.write("\nint v_upperb = ;");
	     }
	     
	     writer.write("\ncv::Mat imgRaw; //input image");
	     writer.write("\ncv::Mat imgResize; //resized image");
	     if(cameraDropdown.getValue() == "2"){
	    	 writer.write("\ncv::Mat secondimgRaw; //input image");
		     writer.write("\ncv::Mat secondimgResize; //resized image");
	     }
	     
	     if(hsvBox.isSelected()) {
	    	 writer.write("\ncv::Mat imgHSV; //switch to HSV colorspace");
	    	 writer.write("\ncv::Mat imgThreshold; //apply threshold");
	    	 if(cameraDropdown.getValue() == "2"){
	    		 writer.write("\ncv::Mat secondimgHSV; //switch to HSV colorspace");
		    	 writer.write("\ncv::Mat secondimgThreshold; //apply threshold");
		     }
	     }
	     
	     if(contourBox.isSelected()) {
	    	 writer.write("\ncv::Mat imgContour;");
	     }
	     
	     if(gpuAccelBox.isSelected()) {
	    	 writer.write("\ncv::gpu::GpuMat src,resize;");
	    	 if(cameraDropdown.getValue() == "2") {
	    		 writer.write("\ncv::gpu::GpuMat secondSrc,secondResize;");
	    	 }
	    	 
	    	 if(hsvBox.isSelected()) {
	    		 writer.append("\ncv::gpu::GpuMat hsv,threshold;");
	    		 if(cameraDropdown.getValue() == "2") {
		    		 writer.write("\ncv::gpu::GpuMat secondHsv,secondThreshold;");
		    	 }
	    	 }
	     }
	     if(trackbarBox.isSelected()) {
	    	 writer.write("\ncv::namedWindow(\"HSV Thresholding\"); \n" 
	    			 + "cv::createTrackbar(\"Hue Lower Bound\", \"HSV Thresholding\", &h_lowerb, 179); \n"
	    			 + "cv::createTrackbar(\"Hue Upper Bound\", \"HSV Thresholding\", &h_upperb, 179); \n"
	    			 + "cv::createTrackbar(\"Hue Upper Bound\", \"HSV Thresholding\", &s_lowerb, 179); \n"
	    			 + "cv::createTrackbar(\"Hue Upper Bound\", \"HSV Thresholding\", &s_upperb, 179); \n"
	    			 + "cv::createTrackbar(\"Hue Upper Bound\", \"HSV Thresholding\", &l_lowerb, 179); \n"
	    			 + "cv::createTrackbar(\"Hue Upper Bound\", \"HSV Thresholding\", &l_upperb, 179); \n");
	     }
	     // Use camera names from user
	     writer.write("\ncv::VideoCapture camera1(0);");
	     if(cameraDropdown.getValue() == "2"){
	    	 writer.write("\ncv::VideoCapture camera2(1);");
	     }
	     writer.write("\nfor (;;) {");
	     writer.write("\nif (!camera1.read(imgRaw)) {");
	     writer.newLine();
	     writer.write("break;");
	     writer.write("\n}");
	     
	     if(cameraDropdown.getValue() == "2"){
	    	 writer.write("\nif (!camera2.read(secondImgRaw)) {");
		     writer.newLine();
		     writer.write("break;");
		     writer.write("\n}");
	     }
	     if(gpuAccelBox.isSelected()) {
	    	 writer.write("\nsrc.upload(imgRaw);");
	    	 writer.write("\ncv::gpu::resize(src, resize, cv::Size(camera1.get(CV_CAP_PROP_FRAME_WIDTH) / img_scale_factor, camera1.get(CV_CAP_PROP_FRAME_HEIGHT) / img_scale_factor), CV_INTER_CUBIC);");
	    	 writer.write("\nresize.download(imgResize);");
	    	 // Add more operations here
	    	 if(hsvBox.isSelected()) {
	    		 writer.write("cv::gpu::cvtColor(resize, hsv, CV_BGR2HSV);");
	    		 writer.write("hsv.download(imgHSV);");
		    	 if(cameraDropdown.getValue() == "2") {
		    		 writer.write("cv::gpu::cvtColor(secondResize, secondHsv, CV_BGR2HSV);");
		    		 writer.write("secondHsv.download(secondImgHSV);");
		    	 }
		    	 
	    	 }
	    	 
	     } else {
	    	 //Operations above without GPU Acceleration plus extra operations here.
	    	 writer.write("cv::resize(imgRaw, imgResize, cv::Size(camera1.get(CV_CAP_PROP_FRAME_WIDTH) / img_scale_factor, camera1.get(CV_CAP_PROP_FRAME_HEIGHT) / img_scale_factor), CV_INTER_CUBIC);");
	    	 // Extra operations before Threshold
	    	 if(hsvBox.isSelected()) {
	    		 writer.write("cv::cvtColor(imgResize, imgHSV, CV_BGR2HSV");
	    	 }
	     }
	    
	     
	     // Filtering
	  
	     // Calculations
	     
	     
	     
	     // Sending Data
	     
	     
	     
	     // CMAKELists
	     if(cmakeBox.isSelected()) {
	    	 BufferedWriter cmakeWriter = new BufferedWriter(new FileWriter("CMakeLists.txt"));
	    	 cmakeWriter.write("cmake_minimum_required (VERSION 2.8)");
	    	 cmakeWriter.write("project (ResetVision)");
	    	 cmakeWriter.write("find_package(OpenCV REQUIRED)");
	    	 
	    	 cmakeWriter.write("find_package(ZeroMQ REQUIRED)");
	    	 
	    	 cmakeWriter.write("set(CMAKE_CXX_FLAGS \"${CMAKE_CXX_FLAGS} -std=c++11\")");
	    	 cmakeWriter.write("include_directories(${OpenCV_INCLUDE_DIRS})");
	    	 cmakeWriter.write("include_directories(${allwpilib/wpilibc/athena/include})");
	    	 cmakeWriter.write("include_directories(${WPILib.h})");
	    	 cmakeWriter.write("include_directories(/usr/local/include)");
	    	 cmakeWriter.write("include_directories(/usr/local/lib)");
	    	 cmakeWriter.write("unset(CUDA_USE_STATIC_CUDA_RUNTIME CACHE)");
	    	 cmakeWriter.write("option(CUDA_USE_STATIC_CUDA_RUNTIME OFF)");
	    	 cmakeWriter.write("add_executable(resetFramework resetFramework.cpp)");
	    	 cmakeWriter.write("target_link_libraries(resetFramework ${OpenCV_LIBS})");
	    	 
	    	 cmakeWriter.write("target_link_libraries(resetFramework zmq)");
	    	 cmakeWriter.write("target_link_libraries(resetFramework ${zmq.h}))");
	    	 
	    	 cmakeWriter.write("target_link_libraries(target_link_libraries(resetFramework ${allwpilib/wpilibc/athena/include})");
	    	 cmakeWriter.write("target_link_libraries(resetFramework ${WPILib.h})");
	    	 
	    	 
	     }
	     
	     writer.flush();
	     writer.close();
	     
	     
	     
	 }
	 
	 

}

