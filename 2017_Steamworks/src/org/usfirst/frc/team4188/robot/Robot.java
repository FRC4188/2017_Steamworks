
package org.usfirst.frc.team4188.robot;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.CameraServer;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4188.robot.Robot.WhichBot;
import org.usfirst.frc.team4188.robot.commandgroups.GearAutonomousRightBlueSide;
import org.usfirst.frc.team4188.robot.commandgroups.TestAutonomous;
import org.usfirst.frc.team4188.robot.commandgroups.GearAutonomousLeft;
import org.usfirst.frc.team4188.robot.commandgroups.GearAutonomousMiddle;
import org.usfirst.frc.team4188.robot.commandgroups.GearAutonomousRightRedSide;
import org.usfirst.frc.team4188.robot.commands.AimHighGoal;
import org.usfirst.frc.team4188.robot.commands.AutoDrive;
import org.usfirst.frc.team4188.robot.commands.TurnRight;
import org.usfirst.frc.team4188.robot.subsystems.CameraLights;
import org.usfirst.frc.team4188.robot.subsystems.Climber;
import org.usfirst.frc.team4188.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4188.robot.subsystems.FuelElevator;
import org.usfirst.frc.team4188.robot.subsystems.GearManipulation;
import org.usfirst.frc.team4188.robot.subsystems.PixyProcessing;
import org.usfirst.frc.team4188.robot.subsystems.Shooter;
import org.usfirst.frc.team4188.robot.subsystems.BallIntake;
import org.usfirst.frc.team4188.robot.subsystems.Vision2;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionPipeline;
import edu.wpi.first.wpilibj.vision.VisionThread;


/*
 is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public enum WhichBot { COMPETITION, PRACTICE, SKETCHY }
	public static WhichBot whichBot = WhichBot.PRACTICE;

	public enum PowerState{NORMAL, POWERCONSERVING}
	public static PowerState powerState = PowerState.NORMAL; 
	
	public static DriveTrain drivetrain;
	public static CameraLights cameraLights;
	public static OI oi;
	public static GearManipulation gearManipulation;
	
	public static Vision2 robotVision;
	public static Climber climber;
	public static BallIntake intake;
	public static  AnalogInput seatMotorHallSensor;
	public static Shooter shooter;
	public static FuelElevator fuelElevator;
	public static Shooter spinTurret;
	public static GripPipeline vision;


	private static Mat mat;
	private static double distance, angleToGoal, lengthBetweenContours, distanceFromTarget;
	public static double error;
	
	
	public static VisionThread visionThread;
	
	public static PixyProcessing pixyProcessing;
	/**
	public static final double EXPERIMENTAL_CORRECTION = (80.5/73.02);
	public static final double DISTANCE_CONSTANT= 5280*(3/Math.PI)*EXPERIMENTAL_CORRECTION;
	public static final double AIM_ERROR = -11.0;
	public static double testVariable;
	private static final double CAMERA_OFFSET = 28.0;
//	public static final double AIM_ERROR = 21.904;
**/
	public static final double EXPERIMENTAL_CORRECTION = (80.5/73.02);
	public static final double DISTANCE_CONSTANT= 5280*(3/Math.PI)*EXPERIMENTAL_CORRECTION;
	public static final double AIM_ERROR = -11.0;
	public static double testVariable;
	private static final double CAMERA_OFFSET = -6.0;
	
	public static final double WIDTH_BETWEEN_TARGET = 8.5;
	public static final double CAMERA_WIDTH = 640;
	
    Command autonomousCommand;
    //Command gearAutonomous;
    SendableChooser autoChooser;
    
    private static final int IMG_WIDTH = 640;
	private static final int IMG_HEIGHT = 480;
	
	NetworkTable table;
	
	
	
	public Robot() {
		table = NetworkTable.getTable("GRIP/myContoursReport");
	}
	
	//private VisionThread visionThread;
	

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		RobotMap.init();
		
		shooter = new Shooter();
		climber = new Climber();
		gearManipulation = new GearManipulation();
		drivetrain = new DriveTrain();
		cameraLights = new CameraLights();
		cameraLights.cameraLightsOn();
        autoChooser = new SendableChooser();
        //gearAutonomous = new GearAutonomous();
        climber.init();
        shooter.init();
        intake = new BallIntake();
        
        shooter = new Shooter();
        fuelElevator = new FuelElevator();
        fuelElevator.init();
        spinTurret = new Shooter();
        drivetrain.init();
        
        oi = new OI();
        RobotMap.gyro.calibrate();
        pixyProcessing = new PixyProcessing();

      //  robotVision = new Vision2("10.41.88.12");
      // SmartDashboard.putNumber("Distance", robotVision.distance);
        
        autoChooser.addObject("Gear Right Red Auto", new GearAutonomousRightRedSide());
        autoChooser.addObject("Gear Right Blue Auto", new GearAutonomousRightBlueSide());
        autoChooser.addObject("Gear Center Auto", new GearAutonomousMiddle());
        autoChooser.addDefault("Gear Left Auto", new GearAutonomousLeft());
        autoChooser.addObject("Gear Autonomous Testing (Testing Left)", new TestAutonomous());
        
        SmartDashboard.putData("AUTONOMOUS", autoChooser);
        SmartDashboard.putNumber("GYRO VALUE", RobotMap.gyro.getAngle());
      //SmartDashboard.putData("Vision2", robotVision);
        
         SmartDashboard.putData("Turn to Vision Angle", new TurnRight());
        // UsbCamera camera;
         // Set the resolution
      	  
   		//camera settings
       UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.getProperty("contrast").set(10);
		camera.getProperty("sharpness").set(100);
		camera.getProperty("saturation").set(100);
		camera.getProperty("brightness").set(0);

		visionThread = new VisionThread(camera , new GripPipeline(), VisionPipeline -> {
			
			
			AxisCamera axisCamera = CameraServer.getInstance().addAxisCamera("axis-camera.local");
			axisCamera.setFPS(10);
			camera.setResolution(640, 480);
			CvSink cvSink1 = CameraServer.getInstance().getVideo();
			CvSource outputStream1 = CameraServer.getInstance().putVideo("Gear Pickup", 320, 240);

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				VisionPipeline.process(mat);

				Imgproc.drawContours(mat, VisionPipeline.filterContoursOutput(), -1, new Scalar(0,0,255), 2);
				//get distance from target || or length between contours         				

				if(!VisionPipeline.filterContoursOutput.isEmpty() && VisionPipeline.filterContoursOutput.size() >= 2) {
					List<Rect> rects = getRectangles(VisionPipeline.filterContoursOutput());
					rects = getTwoBiggest(rects);
					if(rects != null){
						//at this point, there are two rectangles with a reasonable aspect ratio
						
						Rect r = rects.get(0);
						Rect r1 = rects.get(1);
						
					
						double [] centerX = new double[]{r1.x, r.x};
	
						//System.out.println("Rectangle Center X" + r1.x + "," + r.x);
						//System.out.println("Rectangle Y" + r1.y + "," + r.y);
						System.out.println("Rectangle 1 Height, Width" + r.height + "," + r.width);
						System.out.println("Rectangle 2 Height, Width" + r1.height + "," + r1.width);
						
						System.out.printf("Aspect Ratio 1 = %4.2f\n", (double)r.width/r.height );
						System.out.printf("Aspect Ratio 2 = %4.2f\n", (double)r1.width/r1.height );
						//System.out.println("Mat width = " + mat.width() + ", height = " + mat.height());
						
						SmartDashboard.putBoolean("Found two rectangles", true);
	
						Imgcodecs.imwrite("output.png", mat);
	
						// subtracts one another to get length in pixels
						lengthBetweenContours = Math.abs(centerX[0] - centerX[1]);
						
	
						SmartDashboard.putNumber("Length Between Contours", lengthBetweenContours);
						//get distance from target
	
						distanceFromTarget = DISTANCE_CONSTANT / lengthBetweenContours;
						SmartDashboard.putNumber("DistanceFromTarget",distanceFromTarget);
	
						double constant = WIDTH_BETWEEN_TARGET / lengthBetweenContours;
						//get Angle
	
						
						// target the center of the rightmost rectangle
						// figure out which rect is on the right (has greatest X value)
						double targetX = lengthBetweenContours/2; // x value of the rightmost rectangle
						/*
						if (centerX[0] > centerX[1]) { 
							targetX = centerX[0];
						} 
						else{
							targetX = centerX[1];
						}
						*/
						double distanceFromCenterPixels = ((centerX[0] + centerX[1]) / 2) - (CAMERA_WIDTH / 2);
						
						
		
							// Converts pixels to inches using the constant from above.
						
							//Imgproc.drawMarker(mat,distanceFromCenterPixels, new Scalar(255,255,255));
							double distanceFromCenterInch = distanceFromCenterPixels * constant;
							error = Math.atan(distanceFromCenterInch / distanceFromTarget);
							error = Math.toDegrees(error);
							
							//AIM_ERROR on chassis 1.0 is 21.904
	
							//if angle to goal is negative, add the aim error ; else subtract the aim Error
							if(error < 0){
								error += CAMERA_OFFSET;
							}else{
						
								error -= CAMERA_OFFSET;
							}
						 setAngleToGoal(error);
					
						SmartDashboard.putString("Aim_Error", String.format("%6.1f", error));
						testVariable = 7;
					}
				}
		
				//Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});

		visionThread.start();
	}

public static double getAngleToGoal() {
return Robot.angleToGoal;
}

//returns a list of rectangles with a reasonable aspect ratio
public static List<Rect> getRectanglesByAspectRatio (ArrayList<MatOfPoint> contours){
	List<Rect> rectangleList = new ArrayList<Rect>();
	if(!contours.isEmpty() && contours.size() >= 2) {
		
		for(MatOfPoint contour: contours){
			Rect rect = Imgproc.boundingRect(contour);
			double aspectRatio = (double)rect.width/rect.height;
			
			if(aspectRatio > 0.3 && aspectRatio < 0.6){
				rectangleList.add(rect);
			}
		}
	
	}

	return rectangleList;
}
public static List<Rect> getRectangles (ArrayList<MatOfPoint> contours){
	List<Rect> rectangleList = new ArrayList<Rect>();
	if(!contours.isEmpty() && contours.size() >= 2) {		
		for(MatOfPoint contour: contours){
			Rect rect = Imgproc.boundingRect(contour);
			rectangleList.add(rect);
		}
	}
	return rectangleList;
}
public static List<Rect> getTwoBiggest(List<Rect> before){
	List<Rect> after = null; 
	
	if(before.size() == 2){		
		after = before;	
	}
	else if(before.size() > 2){
		after = new ArrayList<Rect>();
		List<SortableRect> sortableList = new ArrayList<SortableRect>();
		for(Rect r: before){
			sortableList.add(new SortableRect(r));
		}
		java.util.Collections.sort(sortableList);
		after.add(sortableList.get(0).getRect());
		after.add(sortableList.get(1).getRect());
	}
		
	return after;
}

public static void setAngleToGoal(double angleToGoal) {
SmartDashboard.putNumber("setAngleToGoal", angleToGoal);
Robot.angleToGoal = angleToGoal;
SmartDashboard.putNumber("Class Variable setAngleToGoal", Robot.angleToGoal);
}

	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
        autonomousCommand = (Command) autoChooser.getSelected();
//        autonomousCommand = new GearAutonomousMiddle();
        Robot.drivetrain.resetEncoders();
       
		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		} */
    	
    	// schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    	
    //}
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        Robot.drivetrain.getRightEncoderDistance();
        Robot.drivetrain.getLeftEncoderDistance();
        SmartDashboard.putNumber("GYRO VALUE", RobotMap.gyro.getAngle());
      //gearAutonomous.start();
     //robotVision.periodic();
        
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        //if (autonomousCommand != null) autonomousCommand.cancel();
    //}
    	Robot.drivetrain.setRampRate(12/0.1);//MaxVoltage/rampTime was 12/0.2
    	Robot.drivetrain.resetEncoders();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        Robot.drivetrain.getRightEncoderDistance();
        Robot.drivetrain.getLeftEncoderDistance();
        SmartDashboard.putNumber("setAngleToAngle to goal in Teleop Periodic", Robot.getAngleToGoal());
     
        SmartDashboard.putNumber("GYRO VALUE", RobotMap.gyro.getAngle());
        
       // pixyProcessing.periodic();
        
        //SmartDashboard.putNumber("Current Voltage Output", RobotMap.pdp.getVoltage());
        //SmartDashboard.putNumber("Total Current Output", RobotMap.pdp.getTotalCurrent());
        
        //System.out.println("Current Voltage Output: " + RobotMap.pdp.getVoltage());
       // System.out.println("Total Current Output: " + RobotMap.pdp.getTotalCurrent());
        
       
        Robot.gearManipulation.getEncoderValue();
        
        SmartDashboard.putBoolean("running", true); 
        
        if(powerState == PowerState.NORMAL){
        	if(RobotMap.pdp.getVoltage() < 7.0){
        		Robot.drivetrain.conservePower(true);
        		powerState = PowerState.POWERCONSERVING;
        	}
        }
        if(powerState == PowerState.POWERCONSERVING){
        	//Robot.drivetrain.conservePower(false);
        	//powerState = PowerState.NORMAL;
        	
        	
        	if(Math.abs(Robot.oi.pilotXboxSample.getY(Hand.kLeft))<0.3){
        			Robot.drivetrain.conservePower(false);
        			powerState = PowerState.NORMAL;
        		
        	}
        	
        }
     
    }
    
    /**
     * This function is called periodically during test mode
     */
    
    public static Mat getMat(){
    	
    	return mat;
    	
    }
    
    public void testPeriodic() {
        LiveWindow.run();
    }
    
}
