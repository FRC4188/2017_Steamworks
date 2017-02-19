
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
	public static DriveTrain drivetrain;
	public static OI oi;
	
	public static GearManipulation gearManipulation;
	
	public static Climber climber;
	
	public static BallIntake intake;
	public static  AnalogInput seatMotorHallSensor;
	public static Shooter shooter;
	public static Shooter spinTurret;
	public static FuelElevator fuelElevator;

	
	public static Vision2 robotVision;
	public static GripPipeline gripVision;		//Vision
	public static VisionProcessing visionProcessing;
	//public static double distance, angle, lengthBetweenContours, aimError, optimalDistance, x;
	public static double aimError, optimalDistance;
	public static VisionThread visionThread;
	public static UsbCamera camera;
	public static CameraLights cameraLights;

	//public static Rect r, r1;
	//static double[] centerX;
	//private static Mat mat;
		
    //Command autonomousCommand;
    //Command gearAutonomous;
    SendableChooser chooser;
    
    private static final int IMG_WIDTH = 640;
	private static final int IMG_HEIGHT = 480;
	NetworkTable table;
	

	public Robot() {
		table = NetworkTable.getTable("GRIP/targets");
	}
	
	 /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	RobotMap.init();
    	oi = new OI();
		
		drivetrain = new DriveTrain();
		drivetrain.init();
        RobotMap.gyro.calibrate();
		
        shooter = new Shooter();
		spinTurret = new Shooter();
		shooter.init();
		fuelElevator = new FuelElevator();
        fuelElevator.init();
        intake = new BallIntake();
		
		climber = new Climber();
		climber.init();
		
		gearManipulation = new GearManipulation();
		
		cameraLights = new CameraLights();
		cameraLights.cameraLightsOn();
        camera = CameraServer.getInstance().startAutomaticCapture();
 	    camera.setResolution(640, 480);
        Object imgLock = new Object();
    	
        chooser = new SendableChooser();
        //gearAutonomous = new GearAutonomous();
        //SmartDashboard.putData("Auto mode", chooser);
       
		//VisionThread Start
        
        visionThread = new VisionThread(camera, gripVision = new GripPipeline(), VisionPipeline ->{
        	
        	//synchronized (imgLock) {
        		try {
					//visionProcessing.periodic(gripVision, camera );
					
					CvSink cvSink = CameraServer.getInstance().getVideo();	// Get a CvSink. This will capture Mats from the camera
					CvSource outputStream = CameraServer.getInstance().putVideo("Processed Image", 640, 480);			// Setup a CvSource. This will send images back to the Dashboard

					Mat sourceMat = new Mat();	// Mats are very memory expensive. Lets reuse this Mat.

					gripVision.process(sourceMat);
					Imgproc.drawContours(sourceMat, gripVision.filterContoursOutput(), 0, new Scalar(0,0,255), 10);
					outputStream.putFrame(sourceMat);
					
        		} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Robot.setAimError(visionProcessing.getAngle(gripVision));
				
				SmartDashboard.putNumber("PrintAimError", getAimError());
				//Timer.delay(1.0);
				SmartDashboard.putString("Vision Status", "Thread_Running");
        	//}		
        });
      
       visionThread.start();
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
        //autonomousCommand = (Command) chooser.getSelected();
        
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
        //if (autonomousCommand != null) autonomousCommand.start();
    //}
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
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
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    public static void setAimError(double v){
    	aimError = v;
    	 }
    public static double getAimError(){
    	return aimError;
    }
    public static void setDistance(double d){
    	optimalDistance = d;
    }
    public static double getDistance(){
		return optimalDistance;
    }
}
