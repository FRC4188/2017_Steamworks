package org.usfirst.frc.team4188.robot.subsystems;

//import java.io.IOException;

//import com.ni.vision.NIVision;
import java.util.Comparator;
import java.util.Vector;


import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.MeasurementType;
import com.ni.vision.NIVision.ParticleFilterCriteria2;
import com.ni.vision.NIVision.ShapeMode;
import com.ni.vision.VisionException;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
//import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.HSLImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.vision.USBCamera;
import edu.wpi.first.wpilibj.vision.VisionPipeline;


/**
 *
 */
public class Vision2 extends Subsystem implements PIDSource {
	int cameraType;
	public double aimError;
	double pixelErrorAdjustment = SmartDashboard.getNumber("EnterPixelError", 0.0);
	//
	public class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport>{
		

		double PercentAreaToImageArea;
		double Area;
		double BoundingRectLeft;
		double BoundingRectTop;
		double BoundingRectRight;
		double BoundingRectBottom;
		
		public int compareTo(ParticleReport r)
		{
			return (int)(r.Area - this.Area);
		}
		
		public int compare(ParticleReport r1, ParticleReport r2)
		{
			return (int)(r1.Area - r2.Area);
		}
	};
	
	public class Scores{
		double area;
		double aspect;
	}
	
	Image frame;
	Image binaryFrame;
	int imaqError;
	AxisCamera camera;
	USBCamera usbCamera;
	private static final int HUE_GREEN = 102;//Changed to 128 from 115 changed to 102 Championship from 120
	private static final int HUE_TOLERANCE = 18; //Changed to 18 Championship from 10
	/*
	NIVision.Range GOAL_HUE_RANGE = new NIVision.Range(50, 140);	//Default hue range for goal tote
	NIVision.Range GOAL_SAT_RANGE = new NIVision.Range(40, 255);// Changed From (53,255) to (40, 255) for Championship //Default saturation range for High Goal
	NIVision.Range GOAL_VAL_RANGE = new NIVision.Range(55, 255);//Changed From (92,255) to (140, 255) for Championship	//Default value range for High Goal
	*/
	
	NIVision.Range GOAL_HUE_RANGE = new NIVision.Range(110, 130);	//Default hue range for goal tote
	NIVision.Range GOAL_SAT_RANGE = new NIVision.Range(53, 255);// Changed From (53,255) to (40, 255) for Championship //Default saturation range for High Goal
	NIVision.Range GOAL_VAL_RANGE = new NIVision.Range(92, 255);//Changed From (92,255) to (140, 255) for Championship	//Default value range for High Goal
	
	double AREA_MINIMUM = 0.3311; //Default Area minimum for particle as a percentage of total image area
	double LONG_RATIO = 2.22; //Tote long side = 26.9 / Tote height = 12.1 = 2.22
	double SHORT_RATIO = 1.4; //Tote short side = 16.9 / Tote height = 12.1 = 1.4
	double SCORE_MIN = 75.0;  //Minimum score to be considered a tote
	double VIEW_ANGLE = 64; //View angle fo camera, set to Axis m1011 by default, 64 for m1013, 51.7 for 206, 52 for HD3000 square, 60 for HD3000 640x480
	NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
	NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0,0,1,1);
	Scores scores = new Scores();
	private static final double TARGET_WIDTH = 10.25;//2017
	private static final double TARGET_HEIGHT = 5;//2017
	private static final double TARGET_ASPECT_RATIO = TARGET_WIDTH/TARGET_HEIGHT; //2016 Target is 20 inches Wide and 12 inches tall.
	private static final double TARGET_TAPE_AREA = (10.0*2.0)+(10.0*2.0)+(TARGET_WIDTH*1.75); //2016 Area of the reflective tape in square inches.
	private static final double TARGET_BOUNDING_RECTANGLE_AREA = TARGET_WIDTH*TARGET_HEIGHT; // 2016 Bounding rectangle area in square inches.
	private int imageWidthPix = 0;
	
	public Integer numParticles;
	public Vision2(String ip){
		this.camera = new AxisCamera(ip); 
    	this.camera.writeResolution(AxisCamera.Resolution.k320x240);
    	this.imageWidthPix = 320;
    	this.camera.writeMaxFPS(10);
    	this.camera.writeCompression(30);
		frame = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		criteria[0] = new NIVision.ParticleFilterCriteria2(NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, AREA_MINIMUM, 100.0, 0, 0);

		//Put default values to SmartDashboard so fields will appear
		SmartDashboard.putNumber("Goal hue min", GOAL_HUE_RANGE.minValue);
		SmartDashboard.putNumber("Goal hue max", GOAL_HUE_RANGE.maxValue);
		SmartDashboard.putNumber("Goal sat min", GOAL_SAT_RANGE.minValue);
		SmartDashboard.putNumber("Goal sat max", GOAL_SAT_RANGE.maxValue);
		SmartDashboard.putNumber("Goal val min", GOAL_VAL_RANGE.minValue);
		SmartDashboard.putNumber("Goal val max", GOAL_VAL_RANGE.maxValue);
		SmartDashboard.putNumber("Area min %", AREA_MINIMUM);
		
		cameraType=1;
	}
	
	public Vision2(){
		
		this.usbCamera = new USBCamera("cam1");
		//this.usbCamera.setSize(width, height);
		this.usbCamera.setBrightness(30);
		this.usbCamera.setExposureManual(0);
		this.usbCamera.setFPS(10);
		frame = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		criteria[0] = new NIVision.ParticleFilterCriteria2(NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, AREA_MINIMUM, 100.0, 0, 0);

		//Put default values to SmartDashboard so fields will appear
		SmartDashboard.putNumber("Goal hue min", GOAL_HUE_RANGE.minValue);
		SmartDashboard.putNumber("Goal hue max", GOAL_HUE_RANGE.maxValue);
		SmartDashboard.putNumber("Goal sat min", GOAL_SAT_RANGE.minValue);
		SmartDashboard.putNumber("Goal sat max", GOAL_SAT_RANGE.maxValue);
		SmartDashboard.putNumber("Goal val min", GOAL_VAL_RANGE.minValue);
		SmartDashboard.putNumber("Goal val max", GOAL_VAL_RANGE.maxValue);
		SmartDashboard.putNumber("Area min %", AREA_MINIMUM);
		
		cameraType = 0;
	}
	
	public double distance;
	double ratio;
	public double constantVariation;
	public void periodic() throws VisionException {
		try {
			switch(cameraType){
			case 0: usbCamera.getImage(frame);
			case 1: camera.getImage(frame);
			break;
			}
		}
		catch(VisionException ex) {
			ex.printStackTrace();
		}
//		CameraServer.getInstance().setImage(frame);
		
		GOAL_HUE_RANGE.minValue = (int)SmartDashboard.getNumber("Goal hue min", GOAL_HUE_RANGE.minValue);
		GOAL_HUE_RANGE.maxValue = (int)SmartDashboard.getNumber("Goal hue max", GOAL_HUE_RANGE.maxValue);
		GOAL_SAT_RANGE.minValue = (int)SmartDashboard.getNumber("Goal sat min", GOAL_SAT_RANGE.minValue);
		GOAL_SAT_RANGE.maxValue = (int)SmartDashboard.getNumber("Goal sat max", GOAL_SAT_RANGE.maxValue);
		GOAL_VAL_RANGE.minValue = (int)SmartDashboard.getNumber("Goal val min", GOAL_VAL_RANGE.minValue);
		GOAL_VAL_RANGE.maxValue = (int)SmartDashboard.getNumber("Goal val max", GOAL_VAL_RANGE.maxValue);
		
		
	
	
		NIVision.imaqColorThreshold(binaryFrame, frame, 255, NIVision.ColorMode.HSV, GOAL_HUE_RANGE, GOAL_SAT_RANGE, GOAL_VAL_RANGE);
		numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
		SmartDashboard.putNumber("Masked particles", numParticles);
	
		float areaMin = (float)SmartDashboard.getNumber("Area min %", AREA_MINIMUM);
	 	criteria[0].lower = areaMin;
	 
	 	imaqError = NIVision.imaqParticleFilter4(binaryFrame, binaryFrame, criteria, filterOptions, null);
	 
	 	numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
	
	 	if(numParticles > 0) {
			 Vector<ParticleReport> particles = new Vector<ParticleReport>();
			 for(int particleIndex = 0; particleIndex < numParticles; particleIndex++) {
				ParticleReport par = new ParticleReport();
				par.PercentAreaToImageArea = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA);
				par.Area = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_AREA);
				par.BoundingRectTop = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
				par.BoundingRectLeft = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
				par.BoundingRectBottom = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_BOTTOM);
				par.BoundingRectRight = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_RIGHT);
				particles.add(par);
			 }
			scores.aspect = aspectScore(particles.elementAt(0));
			SmartDashboard.putNumber("Aspect", scores.aspect);
			scores.area = areaScore(particles.elementAt(0));
			SmartDashboard.putNumber("Area", scores.area);
			boolean isGoalHot = scores.aspect > SCORE_MIN && scores.area > SCORE_MIN;
	
			//Send distance and tote status to dashboard. The bounding rect, particularly the horizontal center (left - right) may be useful for rotating/driving towards a tote
			SmartDashboard.putBoolean("IsGoalHot", isGoalHot);
			SmartDashboard.putNumber("Distance", computeDistance(binaryFrame, particles.elementAt(0)));
			distance = computeDistance(binaryFrame, particles.elementAt(0));
			Robot.setDistance(distance);
			aimError = computePanAngle(distance,particles.elementAt(0));
			SmartDashboard.putNumber("Pixel Error", pixel_Error);
			SmartDashboard.putNumber("Change Angle", aimError); 
			Robot.setAimError(aimError);
			
			double height = particles.firstElement().BoundingRectBottom - particles.firstElement().BoundingRectTop; 
			double width = particles.firstElement().BoundingRectRight - particles.firstElement().BoundingRectLeft;
			ratio = height/width;
			this.constantVariation = 1;
			SmartDashboard.putNumber("Target Ratio", ratio);
			SmartDashboard.putNumber("Constant Variation", this.constantVariation );
			SmartDashboard.putNumber("Distance/Speed Calculation", SmartDashboard.getNumber("Constant Variation", this.constantVariation)*-0.00862*Robot.robotVision.distance+0.560);
			for(ParticleReport particle: particles){
				outlineParticle(frame, particle);
			}
			
	 	} else {
			SmartDashboard.putBoolean("IsGoalHot", false);
			Robot.setAimError(Double.NaN);
		}
//	 	CameraServer.getInstance().setImage(frame);
	 }
	
	
	private void outlineParticle(Image image,ParticleReport particle){
		int top = (int)particle.BoundingRectTop;
		int height = (int)(particle.BoundingRectBottom - particle.BoundingRectTop);
		int left = (int)particle.BoundingRectLeft;
		int width = (int)(particle.BoundingRectRight -  particle.BoundingRectLeft);
		NIVision.Rect rect = new NIVision.Rect(top,left,height, width);
		NIVision.imaqDrawShapeOnImage(image, image, rect,
                DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 2000.0f);
//		NIVision.imaqDrawLineOnImage(image, image, DrawMode.DRAW_VALUE, NIVision.Point(left+(width/2), end, newPixelValue);
	}
	 
	double ratioToScore(double ratio)
	{
		return (Math.max(0, Math.min(100*(1-Math.abs(TARGET_ASPECT_RATIO-ratio)), 100)));
	}

	/**
	 * Calculate how closely the ratio of the ideal reflective area (tape) to its bounding box is 
	 * to the ratio of our particle's reflective area to its bounding box.
	 * 
	 * @param report
	 * @return a score between 0(bad) and 100(good).
	 */
	
	
	double areaScore(ParticleReport report)
	{
		//Calculate the area of the bounding rectangle
		double boundingArea = (report.BoundingRectBottom - report.BoundingRectTop) * (report.BoundingRectRight - report.BoundingRectLeft);
		//Tape is 7" edge so 49" bounding rect. With 2" wide tape it covers 24" of the rect.
		return ratioToScore((TARGET_BOUNDING_RECTANGLE_AREA/TARGET_TAPE_AREA)*report.Area/boundingArea);
	}

	/**
	 * Method to score if the aspect ratio of the particle appears to match the retro-reflective target. Target is 20" x 12" so aspect should be 1
	 */
	double aspectScore(ParticleReport report)
	{
		return ratioToScore(((report.BoundingRectRight-report.BoundingRectLeft)/(report.BoundingRectBottom-report.BoundingRectTop)));
	}

	/**
	 * Computes the estimated distance to a target using the width of the particle in the image. For more information and graphics
	 * showing the math behind this approach see the Vision Processing section of the ScreenStepsLive documentation.
	 *
	 * @param image The image to use for measuring the particle estimated rectangle
	 * @param report The Particle Analysis Report for the particle
	 * @param isLong Boolean indicating if the target is believed to be the long side of a tote
	 * @return The estimated distance to the target in feet.
	 */
	
	double computeDistance (Image image, ParticleReport report) {
		double normalizedWidth, normalizedHeight;
		double particleWidth = report.BoundingRectRight - report.BoundingRectLeft;
		double particleHeight = report.BoundingRectBottom - report.BoundingRectTop;
		NIVision.GetImageSizeResult size = NIVision.imaqGetImageSize(image);
		normalizedWidth = 2*(particleWidth)/size.width;
		normalizedHeight = 2*(particleHeight)/size.height;
		
		double heightDistance = TARGET_HEIGHT/(normalizedHeight*12*Math.tan(VIEW_ANGLE*Math.PI/(120*2)));
		double widthDistance = TARGET_WIDTH/(normalizedWidth*12*Math.tan(VIEW_ANGLE*Math.PI/(180*2)));
		SmartDashboard.putNumber("heightDistance", heightDistance); 
		SmartDashboard.putNumber("widthDistance", widthDistance); 
		
		return Math.sqrt(Math.pow(heightDistance, 2) + Math.pow(widthDistance, 2));
//		return 1.2706 * widthDistance - 1.1203;
	}
	
	double pixel_Error;
	double computePanAngle(double distance, ParticleReport particle){
		// angle = (desired change /320) / Field of View (60 degrees for current camera)
		double x = particle.BoundingRectLeft - pixelErrorAdjustment;
		double pixelError = x - (this.imageWidthPix/2);

		// angle = (pixels/320) * 60   320=image width, 60 = camera FieldOfView in degrees
		double changeAngle = pixelError * 0.2;
		return changeAngle;
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }


	PIDSourceType type;
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		// TODO Auto-generated method stub
		return distance;
	}


	
}
