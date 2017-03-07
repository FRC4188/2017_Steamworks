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
	private static final double KP = 0.01;//0.015
	private static final double KI = 0.0000;//0.0
	private static final double KD = 0.000;//0.0

	private double angle;
	private static final double tolerance = 1.0; // to within 1.0 degree

    public TurnRight(double angle) {
      // Use requires() here to declare subsystem dependencies
			if (Robot.drivetrain == null) {
				throw new NullPointerException("Robot.drivetrain is null, Vineeth.");
			}
    	requires(Robot.drivetrain);
			this.angle = angle;
			CHSRobotDrive.setPIDType(PIDType.turnToAngle);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	SmartDashboard.putString("Aim Status", "Initializing");
    	SmartDashboard.putString("Control Mode", "Left = " + RobotMap.frontLeftDriveMotor.getControlMode());;

    	angle = Robot.getAngleToGoal();
    	Robot.drivetrain.gyroReset();

     	SmartDashboard.putNumber("SETPOINT", angle);

     	gyroPIDController = new PIDController(KP, KI, KD, RobotMap.gyro, RobotMap.driveBase);
     	gyroPIDController.setAbsoluteTolerance(tolerance);

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
			// Set the motors to 0 (stop them)
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
