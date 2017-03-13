
package org.usfirst.frc.team4188.robot;


import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.CameraServer;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4188.robot.commands.AutoDrive;
import org.usfirst.frc.team4188.robot.commands.GearAutonomous;
import org.usfirst.frc.team4188.robot.commands.GearAutonomousLeft;
import org.usfirst.frc.team4188.robot.commands.GearAutonomousMiddle;
import org.usfirst.frc.team4188.robot.commands.GearAutonomousRight;
import org.usfirst.frc.team4188.robot.subsystems.CameraLights;
import org.usfirst.frc.team4188.robot.subsystems.Climber;
import org.usfirst.frc.team4188.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4188.robot.subsystems.FuelElevator;
import org.usfirst.frc.team4188.robot.subsystems.GearManipulation;
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
	// COMPETITION is the 3.0 robot we expect to use in competition.
	// PRACTICE is the 2.0 robot that's similar to COMPETITION
	// SKETCHY is the 1.0 robot
	public enum WhichBot { COMPETITION, PRACTICE, SKETCHY }
	public static WhichBot whichBot = WhichBot.PRACTICE;

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
	private static double angleToGoal, lengthBetweenContours, distanceFromTarget;
	private static VisionThread visionThread;
	private static final double DISTANCE_CONSTANT= 5280*(3/Math.PI);
	private static final double CAMERA_OFFSET = 21.904-2.5;
	private static final double WIDTH_BETWEEN_TARGET = 8.5;
	private static final double CAMERA_WIDTH = 640;
//	private static final int IMG_WIDTH = 640;
//	private static final int IMG_HEIGHT = 480;
	
	Command autonomousCommand;
	SendableChooser autoChooser;
	NetworkTable table;

	public Robot() {
		table = NetworkTable.getTable("GRIP/targets");
	}
	
	//private VisionThread visionThread;
	

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
		RobotMap.init();
		
		shooter = new Shooter();
		climber = new Climber();
		gearManipulation = new GearManipulation();
		drivetrain = new DriveTrain();
		cameraLights = new CameraLights();
		cameraLights.cameraLightsOn();
		intake = new BallIntake();
		fuelElevator = new FuelElevator();

		//gearAutonomous = new GearAutonomous();
		climber.init();
		shooter.init();
		fuelElevator.init();
		//spinTurret = new Shooter();
		drivetrain.init();
		oi = new OI();

		autoChooser = new SendableChooser();
		autoChooser.addObject("Vision based Gear Center Auto :|", new GearAutonomous("RIGHT"));
		autoChooser.addObject("Vision based Gear Right Auto :|", new GearAutonomous("RIGHT"));
		autoChooser.addObject("Vision based Gear Left Auto :|", new GearAutonomous("LEFT"));
		autoChooser.addObject("Gear Right Auto", new GearAutonomousRight());
	    autoChooser.addObject("Gear Left Auto", new GearAutonomousLeft());
	    autoChooser.addDefault("Gear Center Auto", new GearAutonomousMiddle());

		SmartDashboard.putData("CHOOSE AN AUTONOMOUS MODE", autoChooser);
		SmartDashboard.putString("Which Bot", whichBot.toString());
		RobotMap.gyro.calibrate();

		//camera settings
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		camera.getProperty("contrast").set(10);
		camera.getProperty("sharpness").set(100);
		camera.getProperty("saturation").set(100);
		camera.getProperty("brightness").set(0);

		visionThread = new VisionThread(camera , new GripPipeline(), VisionPipeline -> {
			double error = 0;
			
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

				Imgproc.drawContours(mat, VisionPipeline.filterContoursOutput(), 0, new Scalar(0,0,255), 5);
				//get distance from target || or length between contours         				

				if(!VisionPipeline.filterContoursOutput.isEmpty() && VisionPipeline.filterContoursOutput.size() >= 2) {
					Rect r = Imgproc.boundingRect(VisionPipeline.filterContoursOutput.get(1));
					Rect r1 = Imgproc.boundingRect(VisionPipeline.filterContoursOutput.get(0));
					double [] centerX = new double[]{r1.x, r.x};
					SmartDashboard.putBoolean("Found two rectangles", true);

					Imgcodecs.imwrite("output.png", mat);
					
						if(centerX.length == 2){
							// subtracts one another to get length in pixels
							lengthBetweenContours = Math.abs(centerX[0] - centerX[1]);         						
						}
					
					SmartDashboard.putNumber("Length Between Contours", lengthBetweenContours);
//get distance from target
					
					distanceFromTarget = DISTANCE_CONSTANT / lengthBetweenContours;
					SmartDashboard.putNumber("DistanceFromTarget",distanceFromTarget);
					
					double constant = WIDTH_BETWEEN_TARGET / lengthBetweenContours;
//get Angle     						
					
					if(centerX.length == 2){
						double distanceFromCenterPixels= ((centerX[0] + centerX[1]) / 2) - (CAMERA_WIDTH / 2);
						// Converts pixels to inches using the constant from above.
					
						//Imgproc.drawMarker(mat,distanceFromCenterPixels, new Scalar(255,255,255));
						double distanceFromCenterInch = distanceFromCenterPixels * constant;
						// math brought to you buy Chris and Jones
						angleToGoal = Math.atan(distanceFromCenterInch / distanceFromTarget);
						angleToGoal = Math.toDegrees(angleToGoal);
						 
						//AIM_ERROR on chassis 1.0 is 21.904
						
						//AIM_ERROR on chassis 1.0 is 21.904

						//if angle to goal is negative, add the aim error ; else subtract the aim Error
						if(error < 0){
							error += CAMERA_OFFSET;
						}else{
							error -= CAMERA_OFFSET;
						}
					 setAngleToGoal(error);
				
					SmartDashboard.putString("Aim_Error", String.format("%6.1f", error));
				}
		
				//Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
			}
		});

		visionThread.start();
	}

	public static double getAngleToGoal() {
		return angleToGoal;
	}

	public static void setAngleToGoal(double angleToGoal) {
		Robot.angleToGoal = angleToGoal;
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
        Robot.drivetrain.resetEncoders();
        
		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Gear Left :|":
			autonomousCommand = new GearAutonomous("LEFT");
			break;
		case "Gear Middle :|":
			autonomousCommand = new GearAutonomous("MIDDLE");
		default:
			autonomousCommand = new GearAutonomous("MIDDLE");
		}
*/		if (autonomousCommand != null) autonomousCommand.start();

    /**
     * This function is called periodically during autonomous
     */
    }
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        Robot.drivetrain.getRightEncoderDistance();
        Robot.drivetrain.getLeftEncoderDistance();
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
        SmartDashboard.putBoolean("running", true);
		SmartDashboard.putString("Gyro", String.format("%5.1f", RobotMap.gyro.getAngle()));
        
        //robotVision.periodic();
        
     /*   while(isEnabled() && isOperatorControl()){
        	RobotMap.seatMotorHallSensor.setLimitsVoltage(3.5,5.0);
        	SmartDashboard.putBoolean("Is it in the Window", RobotMap.seatMotorHallSensor.getInWindow());
        	
        	if(RobotMap.seatMotorHallSensor.getInWindow()){
        		
        	   RobotMap.hoodRotation.set(0);
        		
        	}
        	
        	else{
        		RobotMap.hoodRotation.set(-1);
        	}	
        	
        	RobotMap.seatMotorHallSensor.free();
        	
        }
     */   
        
        /* boolean blockForward, blockReverse;
        int pos = 0;
        double speed = 1.0;
        //Robot.shooter.counter.reset();
        
        while(isEnabled() && isOperatorControl()){
        	
        	pos = shooter.getPosition();
        	SmartDashboard.putNumber("Position", pos);
        	
        	if(pos >= 175)
        		blockForward = true;
        	else{       		
        		blockForward = false;	
        	}
        	
        	if(pos <= 0)
        		blockReverse = true;
            else {
            	blockReverse = false;
            }
        	
        	if(blockForward)
        		speed = -1;
        	if(blockReverse)
        		speed = 1;
    */    	
       // shooter.hoodRotation.set(shooter.checkDirectionChange(speed));
     SmartDashboard.putBoolean("running", true);   
     
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
