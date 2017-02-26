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
public class AimHighGoal extends Command {
	private int a = 0;
	double finalAngle;

	public PIDController gyroPIDController;
	
//	PID tuned for competition bot
//	private static final double KP = 0.03;
//	private static final double KI = 0.00001;
//	private static final double KD = 0.0;
	
	
	//PID tuned for practice bot
	private static final double KP = 0.5;//0.015
	private static final double KI = 0.0000;//0.0
	private static final double KD = 0.00000;//0.0
	 
	private double angle;
	private double tolerance;
	
    public AimHighGoal(double tolerance) {
        // Use requires() here to declare subsystem dependencies 
    	//requires(Robot.drivetrain);
    	this.tolerance = tolerance;
    	int a = 0;
    }

    // Called just before this Command runs the first time
    protected void initialize() {  
    	SmartDashboard.putString("Aim Status", "Initializing");
    	SmartDashboard.putString("Control Mode", "Left = " + RobotMap.frontLeftDriveMotor.getControlMode());;
    	
    	CHSRobotDrive.setPIDType(PIDType.turnToAngle);
    	gyroPIDController = new PIDController(KP, KI, KD, RobotMap.gyro, RobotMap.driveBase);
    	
    	angle = Robot.getAngleToGoal();
    	
    	Robot.drivetrain.gyroReset();
		gyroPIDController.setAbsoluteTolerance(tolerance);
		SmartDashboard.putNumber("SETPOINT", angle);
		gyroPIDController.setSetpoint(90);
		gyroPIDController.enable();	
		
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	SmartDashboard.putString("Aim Status", "Running");	
    	SmartDashboard.putString("Output Voltage", String.format("Left = %7.3f, Right = %7.3f",RobotMap.frontLeftDriveMotor.getOutputVoltage(),RobotMap.frontRightDriveMotor.getOutputVoltage()));
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}

