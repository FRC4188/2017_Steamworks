package org.usfirst.frc.team4188.robot.commands;

import org.usfirst.frc.team4188.robot.CHSRobotDrive;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.CHSRobotDrive.PIDType;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TurnRight extends Command {
	double finalAngle;

	public PIDController gyroPIDController;

	//PID tuned for practice bot
	private  double KP = 0.0;
	private  double KI = 0.0;
	private  double KD = 0.0;

	private long start = 0l;
	private static double angle;
	private double angleFromVision;
	private static final double tolerance = 0.25; // to within 1.0 degree
	// private static final int ONE = 1;
	// private static final int TWO = 2;
	// private static final int THREE = 3;

	private int initCount = 0, executeCount = 0;

	// private int bot = ONE; //set bot to ONE, TWO, or THREE to set the PID values for chassis 1.0, 2.0, or 3.0

	private boolean getAngleFromVision = false;

    public TurnRight(double targetAngle) {
      // Use requires() here to declare subsystem dependencies
	  if (Robot.drivetrain == null) {
		  throw new NullPointerException("Robot.drivetrain is null, Vineeth.");
	  }
    requires(Robot.drivetrain);
	this.angle = targetAngle;
	  
	if(Robot.whichBot == Robot.WhichBot.SKETCHY){
		  KP = 0.02;
		  KI = 0.002;
		  KD = 0.002;

	  }else if(Robot.whichBot == Robot.WhichBot.PRACTICE){
		  KP = 0.01;
		  KI = 0.0;
		  KD = 0.0;
	  }else if(Robot.whichBot == Robot.WhichBot.COMPETITION){
		  KP = 0.01;
		  KI = 0.0;
		  KD = 0.0;
	  }
	this.getAngleFromVision  = false;
   }

    public TurnRight(){

    	this.angle = Robot.getAngleToGoal();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	this.start = System.currentTimeMillis();
    	
    	SmartDashboard.putString("Aim Status", "Initializing");
    	SmartDashboard.putString("Control Mode", "Left = " + RobotMap.frontLeftDriveMotor.getControlMode());;

    	//angle = Robot.getAngleToGoal();

    	Robot.drivetrain.gyroReset();
   	    CHSRobotDrive.setPIDType(PIDType.turnToAngle);

     	SmartDashboard.putNumber("SETPOINT", this.angle);

     	gyroPIDController = new PIDController(KP, KI, KD, RobotMap.gyro, RobotMap.driveBase);
     	gyroPIDController.setAbsoluteTolerance(tolerance);

     	gyroPIDController.setSetpoint(Robot.getAngleToGoal());

     	gyroPIDController.enable();

     	initCount++;
     	SmartDashboard.putNumber("Init Count", initCount);
     	SmartDashboard.putNumber("ANGLE FROM VISION", Robot.getAngleToGoal());
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	boolean onTarget = gyroPIDController.onTarget();
    	SmartDashboard.putString("Aim Status", "On Target =" + onTarget) ;
    	return onTarget;
    }

    // Called once after isFinished returns true
    protected void end() {
      gyroPIDController.disable();
      gyroPIDController.free();
      long elapsed = System.currentTimeMillis()-start;
      SmartDashboard.putNumber("Turn Right Elapsed", elapsed);
      //RobotMap.driveBase.stopMotor();
      // Set the motors to 0 (stop them)
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
