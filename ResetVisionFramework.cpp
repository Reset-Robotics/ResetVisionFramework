/*
 Reset Robotics, FRC Team #6325 - Reset Vison Framework
 
 * Name          : Reset Vision Framework
 * Version       : 1.1
 * Build Date    : 19 July 2017
 * Last Update   : 19 July 2017
 * Developed By  : Reset Robotics, FRC Team #6325
 * Author(s)     : Reset Robotics - Prajwal Vedula
 
 Copyright (C) 2017 Reset Robotics
 
 /*
 This is the Reset Vision Framework.
 
 There are 7 steps to the Reset Framework code.
 1. Includes
 2. Initializing variables and communication
 3. Performing GPU Accelerated operations
 4. Performing regular operations
 5. Performing any extra filtering
 6. Calculations
 7. Sending the Data
 
 */
// Step One

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <sys/time.h>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <atomic>
#include <unistd.h>
// Communication Includes. Below is the ZMQ includes. Add your NetworkTable/UDP includes here.
#include <zmq.hpp>

// GPU Acceleration Includes. Comment out if not using GPU Acceleration
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "opencv2/gpu/gpu.hpp"


// Step Two
int main() {
    
    // Setting up ports for ZMQ. If using your own communication method delete the 3 lines following
    zmq::context_t context (1);
    zmq::socket_t publisher(context, ZMQ_PUB);
    publisher.bind("tcp://*:5801");
    
    // If using Network Tables or other protocol set it up below
    
    
    std::cout << "starting server\n";
    
    // Prints current exposure. Remeber to change video0 to whatever index your camera is. You can do this to multiple cameras (like video0 and video1)
    printf("Current exposure settings:\n");
    system("v4l2-ctl -d /dev/video0 -C exposure_auto");
    system("v4l2-ctl -d /dev/video0 -C exposure_absolute");
    
    
    //Set exposure. Change the exposure_absolute to whatever exposure you want. Keep exposure_auto at 1
    system("v4l2-ctl -d /dev/video0 -c exposure_auto=1");
    system("v4l2-ctl -d /dev/video0 -c exposure_absolute=5");
    
    
    //print the new exposure of the cameras
    printf("New exposure settings:\n");
    system("v4l2-ctl -d /dev/video0 -C exposure_absolute");
    
    
    int img_scale_factor = 1; // Change scale factor to whatever you want to divide a 640 x 480 image by
    
    
    //HSV Thresholding Vallues ( Currently at purple LED ring and Exposure of 5 on camera) Change to your needs
    int h_lowerb = 97;
    int h_upperb = 179;
    int s_lowerb = 185;
    int s_upperb = 255;
    int v_lowerb = 52;
    int v_upperb = 255;
    
    //Images. If you need any more images for blurs and other operations add them here.
    cv::Mat imgRaw; //input image
    cv::Mat imgResize; //image resized based on scale factor
    cv::Mat imgHSV; //switch to HSV colorspace
    cv::Mat imgThreshold; //HSV thresholding image
    cv::Mat imgContour; // thresholded image copied to find contours so that thresholded image can be seen with imshow
    cv::Mat imgOutput; // output image
    
    // Below you can add images for a second or third camera if needed, Remember to do it for both GPUMats and Regulat Mats
    
    // GpuMats for GPU Acceleration. If you need any more GPUMats add them here
    cv::gpu::GpuMat src, resize, hsv, threshold;
    
    
    //HSV Threshold Sliders (can comment out for use on field)
    
    // This creates a window called HSV Thresholding
    cv::namedWindow("HSV Thresholding");
    
    // This creates the track bar.
    // The first parameter is the name of the bar
    // The second parameter is the name of the window to be put on (Created above)
    // The third parameter is variable to change with the &.
    // The last parameter is the last number to be shown on the trackbar.
    cv::createTrackbar("Hue Lower Bound", "HSV Thresholding", &h_lowerb, 179);
    cv::createTrackbar("Hue Upper Bound", "HSV Thresholding", &h_upperb, 179);
    cv::createTrackbar("Saturation Lower Bound", "HSV Thresholding", &s_lowerb, 255);
    cv::createTrackbar("Saturation Upper Bound", "HSV Thresholding", &s_upperb, 255);
    cv::createTrackbar("Value Lower Bound", "HSV Thresholding", &v_lowerb, 255);
    cv::createTrackbar("Value Upper Bound", "HSV Thresholding", &v_upperb, 255);
    
    // Add another contours variable for another camera if necessary
    // Contours is the variable in which the original contours that are found will be stored
    // filteredContours will be where the filtered contours are stored.
    std::vector<std::vector<cv::Point> > contours, filteredContours; //array of contours (which are each an array of points)
    
    
    //start the video. Change to whatever indices you are using.. Add another videoCapture for another camera.
    cv::VideoCapture leftCamera(0);
    cv::VideoWriter output;
    
    // Variables used for the calculations. Center is the CenterX in this example. Can add another variable for more cameras/more things to calculate.
    
    double center = 0;
    
    // Step Three
    //loop for each frame
    for (;;) {
        //reads the image from each camera. Can add another if statement with videocapture and image name for more cameras.
        if (!leftCamera.read(imgRaw)) {
            break;
        }
        
        
        /* The GPU Acceleration Parts for a Jetson TK1/TX1/TX2. If you are using a Pi or something else delete the GPU Accel parts and perform all operaions in the next section which is the non accelerated functions. Only certain functions can be accelerated.
         You can do any regular functions compatible functions for GPU Accel but you must put the input and output as GpuMats and do cv::gpu::function. First you must convert the image from camera to a GpuMat. Do gpuMatName.upload(regularMatName); Now you can perform any operations. For resize do cv::gpu::resize(gpuInputMat, gpuOutputMat, width, height, interpolation). Then you can use the gpuOutputMat into a different operation such as cvtColor. To get the gpuMat back to a regularMat do gpuMatName.download(regularMatName); The code below resizes the image from camera and converts it into the HSV Colorspace. If using multiple cameras remember to perform your operations to both cameras.
         */
        
        //upload regular Mat to GPU as a GpuMat
        src.upload(imgRaw);
        
        
        //gpu accelerated image transformations
        cv::gpu::resize(src, resize, cv::Size(leftCamera.get(CV_CAP_PROP_FRAME_WIDTH) / img_scale_factor,
                                              leftCamera.get(CV_CAP_PROP_FRAME_HEIGHT) / img_scale_factor), CV_INTER_CUBIC);
        
        
        cv::gpu::cvtColor(resize, hsv, CV_BGR2HSV);
        
        //download GpuMat from the GPU
        hsv.download(imgHSV);
        resize.download(imgResize);
        
        
        // End GPU Acceleration section.
        
        // Step Four
        
        /* Here perform operations that cannot be GPU Accelerated such as finding contours. Do all your functions here if you do not use a TX1. The code below applies an HSV threshold and finds contours. Don't forgot to do this for all your cameras. */
        
        
        cv::inRange(imgHSV, cv::Scalar(h_lowerb, s_lowerb, v_lowerb), cv::Scalar(h_upperb, s_upperb, v_upperb), imgThreshold);
        
        imgThreshold.copyTo(imgContour);
        
        cv::findContours(imgContour, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);
        
        // Step Five
        // Here you can do various methods and functions to filter out contours and what not.
        
        // This iterates through each contours and will filter out any contours that do not pass the tests below.
        // The contours that pass the tests will be pushed into the variable filteredContours.
        // You can add/remove any filteration tests within this for loop.
        filteredContours.clear();
        for (std::vector<cv::Point> currentContour: contours) // enhanced for loop
        {
            Rect boundRect = boundingRect(contours[i]);
            float contourArea = contourArea(contours[i]);
            float aspectRatio = (float)boundRect.width/boundRect.height // Contour width/ contour height
            float solidityRatio = contourArea(contours[i])/(boundRect.width*boundRect.height); // the closer ratio to one, the more rectangle-y it is.
            if (contourArea > maxArea || contourArea < minArea) continue;
            if (aspectRatio > maxAspectRatio || aspectRatio < minAspectRatio) continue;
            if (solidityRatio > maxSolidityRatio || solidityRatio < minASolidityRatio) continue;
            
            filteredContours.push_back(currentContour);
            
        }
        
        
        // End various filtering
        
        // Step Six
        
        /* This is where the calculation happen. filteredContours is the output from all the filtering.
         
         */
        
        //If the filtered contours are not empty (is there any contours that passed the filtering), then calculate centerX.
        // You can change this if statement to only calculate under conditions you want/
        // For example, in Steamworks there were 2 vision tapes at the peg so you could do filteredContours==2 or filteredContours>=2
        if (!filteredContours.empty()) {
            cv::Rect r = boundingRect(filteredContours[1]);
            cv::Rect  r1 = boundingRect(filteredContours[0]);
            double centerX= r.x + (r.width/2);
            double centerX1= r1.x + (r1.width/2);
            center = ((centerX + centerX1) / 2);
            std::cout << "Offset" << center << std::endl;
            
        }
        // This is optional. We use it so that the robot knows no target is in view. Use a number that your CenterX will not be (centerX can be zero depending on your case so be careful).
        else{
            center = 0; //if no target is detected, send 0
        }
        // Step 7
        // Sends message using your communication protocl. Delete following 3 lines if not using ZMQ.
            zmq::message_t message(20);
            snprintf ((char *) message.data(), 20, "displacement %f %f", center;
            publisher.send(message);
                  
        // Send values via NetworkTables or other communication protocol here
                  
                  // shows thresholded image
                  cv::imshow("stream", imgThreshold);
                  
                  //if spacebar is pressed, quit the program
                  char c = cv::waitKey(1);
                  if (c == ' ') {
                      printf("Stream has ended.");
                      break;
                  }
                  
                  
                  
                  }
