package org.usfirst.frc.team4188.robot.commands;

import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team4188.robot.CHSRobotDrive;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.CHSRobotDrive.PIDType;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveToDistance extends Command {

	double PID_LOOP_TIME = 3.0;
	private static final double KP = 0.03;
	private static final double KI = 0.0;
	private static final double KD = 0.0;
	PIDController drivePID = new PIDController(KP,KI,KD,Robot.robotVision, RobotMap.driveBase);
    
    double distance;
    double speed;
	public DriveToDistance(double distance, double speed) {
    	this.distance = distance;
    	this.speed = speed;
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	CHSRobotDrive.setPIDType(PIDType.driveToDistance);
    	drivePID.setAbsoluteTolerance(1.0);
    	drivePID.setSetpoint(distance);
    	drivePID.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return drivePID.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	drivePID.disable();
    	drivePID.free();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
