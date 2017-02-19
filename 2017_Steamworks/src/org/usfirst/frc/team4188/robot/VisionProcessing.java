package org.usfirst.frc.team4188.robot;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionPipeline;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class VisionProcessing {
	//static GripPipeline vision; 
	//static VisionThread visionThread;
	
	static double lengthBetweenContours, distanceFromTarget, lengthError, angleToGoal;
	static double[] centerX;
	static Mat sourceMat;
	
	public static final double OFFSET_TO_FRONT = 0;
	public static final double CAMERA_WIDTH = 640;
	public static final double DISTANCE_CONSTANT= 5738;
	public static final double WIDTH_BETWEEN_TARGET = 8.5;
	
	public void periodic(GripPipeline vision, UsbCamera camera){
		
	//	visionThread = new VisionThread(camera , vision = new GripPipeline(), VisionPipeline -> {
	  
/*		CvSink cvSink = CameraServer.getInstance().getVideo();	// Get a CvSink. This will capture Mats from the camera
		CvSource outputStream = CameraServer.getInstance().putVideo("Processed Image", 640, 480);			// Setup a CvSource. This will send images back to the Dashboard

		sourceMat = new Mat();	// Mats are very memory expensive. Lets reuse this Mat.

			vision.process(sourceMat);
			Imgproc.drawContours(sourceMat, vision.filterContoursOutput(), 0, new Scalar(0,0,255), 10);
			//Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);
			// Give the output stream a new image to display
			outputStream.putFrame(sourceMat);
			//returnCenterX();
			SmartDashboard.putNumber("Distance From Target",distanceFromTarget(vision));
			SmartDashboard.putNumber("Change angle", getAngle(vision));*/
		
   //});
}
	
///////////////// END OF GRIP GENERATED CODE //////////////////////
public static double returnCenterX(GripPipeline vision){
	double[] defaultValue = new double[0];
	// This is the center value returned by GRIP thank WPI
	
	if(!vision.filterContoursOutput.isEmpty() && vision.filterContoursOutput.size() >= 2){
		Rect r = Imgproc.boundingRect(vision.filterContoursOutput.get(1));
		Rect r1 = Imgproc.boundingRect(vision.filterContoursOutput.get(0)); 
		centerX = new double[]{r1.x + (r1.width / 2), r.x + (r.width / 2)};
		Imgcodecs.imwrite("output.png", sourceMat);
		//System.out.println(centerX.length); //testing
		// this again checks for the 2 shapes on the target
	
	if(centerX.length == 2){
		// subtracts one another to get length in pixels
		lengthBetweenContours = Math.abs(centerX[0] - centerX[1]);
	}
}
SmartDashboard.putNumber("LengthBetweenContours", lengthBetweenContours);
return lengthBetweenContours;
}

public static double distanceFromTarget(GripPipeline vision){
	// distance costant divided by length between centers of contours
	distanceFromTarget = DISTANCE_CONSTANT / returnCenterX(vision);
	SmartDashboard.putNumber("DistanceFromTarget", distanceFromTarget);
return distanceFromTarget - OFFSET_TO_FRONT; 
}

public static double getAngle(GripPipeline vision){
SmartDashboard.putBoolean("Running getAngle", false);
	// 8.5in is for the distance from center to center from goal, then divide by lengthBetweenCenters in pixels to get proportion
	double constant = WIDTH_BETWEEN_TARGET / returnCenterX(vision);
SmartDashboard.putNumber("Constant", constant);
	/*double angleToGoal = 0;*/
	//Looking for the 2 blocks to actually start trig
	if(!vision.filterContoursOutput.isEmpty() && vision.filterContoursOutput.size() >= 2){

		if(centerX.length == 2){
			// this calculates the distance from the center of goal to center of webcam 
			double distanceFromCenterPixels= ((centerX[0] + centerX[1]) / 2) - (CAMERA_WIDTH / 2);
			// Converts pixels to inches using the constant from above.
			double distanceFromCenterInch = distanceFromCenterPixels * constant;
			// math brought to you buy Chris and Jones
			angleToGoal = Math.atan(distanceFromCenterInch / distanceFromTarget(vision));
			angleToGoal = Math.toDegrees(angleToGoal);
		}
	}
return angleToGoal;
}



}