
package org.usfirst.frc.team4188.robot;


import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.CameraServer;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4188.robot.commands.AutoDrive;
import org.usfirst.frc.team4188.robot.commands.GearAutonomous;
import org.usfirst.frc.team4188.robot.subsystems.CameraLights;
import org.usfirst.frc.team4188.robot.subsystems.Climber;
import org.usfirst.frc.team4188.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4188.robot.subsystems.GearManipulation;
import org.usfirst.frc.team4188.robot.subsystems.Shooter;
import org.usfirst.frc.team4188.robot.subsystems.BallIntake;
import org.usfirst.frc.team4188.robot.subsystems.Vision2;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static DriveTrain drivetrain;
	public static CameraLights cameraLights;
	public static OI oi;
	public static GearManipulation gearManipulation;
	
	public static Vision2 robotVision;
	public static Climber climber;
	public static BallIntake intake;
    public static Shooter shooter;
    public static Vision newRobotVision;
	
	public static double aimError;
	public static double optimalDistance;
	
	public AnalogTrigger seatMotorHallSensor;
	//private AnalogTrigger seatMotorHallSensor;
	
    Command autonomousCommand;
    Command gearAutonomous;
    SendableChooser chooser;
    VisionThread visionThread;
    
    private static final int IMG_WIDTH = 640;
	private static final int IMG_HEIGHT = 480;
	
	//private VisionThread visionThread;
	

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
		RobotMap.init();
		climber = new Climber();
		gearManipulation = new GearManipulation();
		drivetrain = new DriveTrain();
		cameraLights = new CameraLights();
        chooser = new SendableChooser();
        gearAutonomous = new GearAutonomous();
        climber.init();
        intake = new BallIntake();
        shooter = new Shooter();
        shooter.init();
       // robotVision = new Vision2("10.41.88.12");
      // robotVision = new Vision2();
      
        SmartDashboard.putNumber("Distance", robotVision.distance);
  
        //chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
        SmartDashboard.putData("Vision2", robotVision);
        drivetrain.init();
        RobotMap.gyro.calibrate();
        //seatMotorHallSensor.setLimitsVoltage(3.5, 3.5);
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setBrightness(30);
        camera.setExposureManual(0);
        camera.setFPS(10);
        
    	camera.setResolution(640, 480);
        visionThread = new VisionThread(camera, new Vision(), VisionPipeline -> {
        CvSink cvSink = CameraServer.getInstance().getVideo();
        CvSource outputStream = CameraServer.getInstance().putVideo("Processed Image", 640 , 480);
        	
        	Mat source = new Mat();
        	
        	
        	 if (!VisionPipeline.filterContoursOutput().isEmpty()){
        		
        		 cvSink.grabFrame(source);
        		 newRobotVision.process(source);
        		 Imgproc.drawContours(source,VisionPipeline.filterContoursOutput(), 0, new Scalar(0,0,255));
        		outputStream.putFrame(source);
        		
        
        		
        	}
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
        autonomousCommand = (Command) chooser.getSelected();
        
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
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
      //gearAutonomous.start();
     robotVision.periodic();
        
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
       // robotVision.periodic();
   /**
        boolean blockForward, blockReverse;
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
        	 shooter.hoodRotation.set(shooter.checkDirectionChange(speed));
        }
       
     **/   
     
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
