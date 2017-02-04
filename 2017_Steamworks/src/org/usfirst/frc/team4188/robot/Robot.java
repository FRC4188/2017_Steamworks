
package org.usfirst.frc.team4188.robot;


import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.CameraServer;


import org.usfirst.frc.team4188.robot.commands.AutoDrive;
import org.usfirst.frc.team4188.robot.commands.EncoderTest;
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
	
	public static double aimError;
	public static double optimalDistance;
	public AnalogInput seatMotorHallSensor;
	
    Command autonomousCommand;
    Command gearAutonomous;
    SendableChooser chooser;
    
    private static final int IMG_WIDTH = 640;
	private static final int IMG_HEIGHT = 480;

    public void robotInit() {
		oi = new OI();
		RobotMap.init();
		//climber = new Climber();
		//gearManipulation = new GearManipulation();
		//drivetrain = new DriveTrain();
		//cameraLights = new CameraLights();
        //chooser = new SendableChooser();
        //gearAutonomous = new GearAutonomous();
        //climber.init();
        //intake = new BallIntake();
        shooter = new Shooter();
        shooter.init();
        //robotVision = new Vision2("10.41.88.12");
        //SmartDashboard.putNumber("Distance", robotVision.distance);
  
        //chooser.addObject("My Auto", new MyAutoCommand());
        //SmartDashboard.putData("Auto mode", chooser);
        //SmartDashboard.putData("Vision2", robotVision);
        //drivetrain.init();
       // RobotMap.gyro.calibrate();
        //seatMotorHallSensor.setLimitsVoltage(3.5, 3.5);
        
        
       /**
            AxisCamera camera = CameraServer.getInstance().addAxisCamera("10.41.88.11");
            camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
            
            visionThread = new VisionThread( new Vision2("10.41.88.11") -> {
                while (!Thread.interrupted()) {
                   
                    }
             });
    
            visionThread.start();
          **/
        
    }
 
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

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
     //robotVision.periodic();
        
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
      //  SmartDashboard.putData(new EncoderTest(1.0));
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
       /* SmartDashboard.putNumber("Counter", EncoderTest.counter);
        SmartDashboard.putNumber("Position", EncoderTest.pos);
        SmartDashboard.putString("Encoder Status", "Initializing");
	 	SmartDashboard.putNumber("Accumulator Output", RobotMap.seatMotorHallSensor.getAccumulatorValue());
    	SmartDashboard.putNumber("TicksPerSecond", RobotMap.seatMotorHallSensor.kSystemClockTicksPerMicrosecond);
    	SmartDashboard.putData("EncoderPIDController", EncoderTest.seatMotorPIDController);
    	*/
        // robotVision.periodic();
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
