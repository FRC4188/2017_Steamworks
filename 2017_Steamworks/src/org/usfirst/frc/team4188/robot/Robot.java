
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
import org.opencv.core.Scalar;
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
	public static double distance, angle, lengthBetweenContours;
	
	public static VisionThread visionThread;
    
	public static double aimError;
	public static double optimalDistance;

    //Command autonomousCommand;
    //Command gearAutonomous;
    SendableChooser chooser;
    
    private static final int IMG_WIDTH = 640;
	private static final int IMG_HEIGHT = 480;
//	NetworkTable table;
	
	
	
	public Robot() {
		//table = NetworkTable.getTable("GRIP/myContoursReport");
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
        chooser = new SendableChooser();
        //gearAutonomous = new GearAutonomous();
        climber.init();
        shooter.init();
        intake = new BallIntake();
        shooter = new Shooter();
        fuelElevator = new FuelElevator();
        fuelElevator.init();
        spinTurret = new Shooter();
        drivetrain.init();
        
        
        
        
    
        	
        	
        
      //  robotVision = new Vision2("10.41.88.12");
      // SmartDashboard.putNumber("Distance", robotVision.distance);
  
      //chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
      //SmartDashboard.putData("Vision2", robotVision);
         RobotMap.gyro.calibrate();
         
        // UsbCamera camera;
         // Set the resolution
      	  
 	    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
 	    camera.setResolution(640, 480);
         
		visionThread = new VisionThread(camera, new GripPipeline(), VisionPipeline ->{
 
			
			
			// Get a CvSink. This will capture Mats from the camera
		CvSink cvSink = CameraServer.getInstance().getVideo();
		// Setup a CvSource. This will send images back to the Dashboard
		CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

		// Mats are very memory expensive. Lets reuse this Mat.
		mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			
				// Put a rectangle on the image
				/*
				Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
						new Scalar(0, 0, 255), 5);*/
				VisionPipeline.process(mat);
				
						
				//if(!VisionPipeline.filterContoursOutput.isEmpty()&& Imgproc.boundingRect(VisionPipeline.filterContoursOutput.get(0)).area()>5000 && Imgproc.boundingRect(VisionPipeline.filterContoursOutput.get(1)).area()>5000){
				Imgproc.drawContours(mat, VisionPipeline.filterContoursOutput(), 0, new Scalar(0,0,255), 10);
				//Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);
				// Give the output stream a new image to display
				
				outputStream.putFrame(mat);
				//Imgproc.drawContours(sourceMat, VisionPipeline.filterContoursOutput(), 0, new Scalar(0,0,255), 10);
				//Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);
				// Give the output stream a new image to display
				//outputStream.putFrame(sourceMat);
				//returnCenterX();
				distance = VisionProcessing.distanceFromTarget(VisionPipeline); 
				angle = VisionProcessing.getAngle(VisionPipeline);
				Robot.setAimError(angle);
				lengthBetweenContours = VisionProcessing.returnCenterX(VisionPipeline);
				SmartDashboard.putNumber("Distance From Target", distance);
				//SmartDashboard.putNumber("Return Center X of Target",VisionProcessing.distanceFromTarget(VisionPipeline));
				SmartDashboard.putNumber("Change angle", angle);
				SmartDashboard.putNumber("Length Between Contours", VisionProcessing.returnCenterX(VisionPipeline));
				//SmartDashboard.putNumber("Area for Contour 1", Imgproc.boundingRect(vision.filterContoursOutput.get(0)).area());
				//SmartDashboard.putNumber("Area for Contour 2", Imgproc.boundingRect(vision.filterContoursOutput.get(1)).area());
				SmartDashboard.putString("Vision Status", "Running");
				
				
				SmartDashboard.putBoolean("THREADRunning", true);
				while (!Thread.interrupted()) {
					// Tell the CvSink to grab a frame from the camera and put it
					// in the source mat.  If there is an error notify the output.
					if (cvSink.grabFrame(mat) == 0) {
						// Send the output the error.
						outputStream.notifyError(cvSink.getError());
						// skip the rest of the current iteration
					//	continue;
					}
		
		}
			//}
       });
      
       visionThread.start();
    }
       /**
            AxisCamera camera = CameraServer.getInstance().addAxisCamera("10.41.88.11");
            camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
            
            visionThread = new VisionThread( new Vision2("10.41.88.11") -> {
                while (!Thread.interrupted()) {
                   
                    }
             });
    
            visionThread.start();
          **/
       
    
    
 
	
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
