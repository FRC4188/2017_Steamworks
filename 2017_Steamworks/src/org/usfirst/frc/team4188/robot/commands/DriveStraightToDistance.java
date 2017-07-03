package org.usfirst.frc.team4188.robot.commands;

import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team4188.robot.CHSRobotDrive;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.CHSRobotDrive.PIDType;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveStraightToDistance extends Command {

    double distance;
    double speed;
    /*FUDGE_FACTOR is based upon real world results driving 5 feet at a speed of 0.6
    	The fudge factor is tuned for 5 feet other distances will require manual adjustment
    	2 feet goes approx. 31 inches
    	3 feet goes approx. 42 inches
    */
    
    static final double FUDGE_FACTOR = 0.68;
	public DriveStraightToDistance(double distance, double speed) {
    	this.distance = distance*FUDGE_FACTOR;
    	this.speed = speed;
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drivetrain.resetEncoders();
    	RobotMap.gyro.reset();
    	System.out.println("Drive Straight To Distance");
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double angle = RobotMap.gyro.getAngle();
    	Robot.drivetrain.autoDrive(this.speed, angle/12);
    	SmartDashboard.putNumber("ANGLE", angle);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	double distance = Math.max(Robot.drivetrain.getLeftEncoderDistance(), Robot.drivetrain.getRightEncoderDistance());
    	return distance >= this.distance;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.autoDrive(0, 0);
    	new AimHighGoal(0);
    

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
