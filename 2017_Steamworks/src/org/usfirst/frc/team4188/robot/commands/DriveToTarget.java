package org.usfirst.frc.team4188.robot.commands;

import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team4188.robot.CHSRobotDrive;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.commandgroups.TestAutonomous;
import org.usfirst.frc.team4188.robot.CHSRobotDrive.PIDType;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveToTarget extends Command {

    double speed, currentMax;
    
	public DriveToTarget(double speed, double currentMax) {

    	this.speed = speed;
    	this.currentMax = currentMax;
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMap.gyro.reset();
    	//new TestAutonomous();
    
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double angle = Robot.getAngleToGoal()*-1;
    	Robot.drivetrain.autoDrive(this.speed, (angle/60));
    	SmartDashboard.putNumber("ANGLE", angle);
    	System.out.println(angle);
    	SmartDashboard.putNumber("OutputCurrent---", RobotMap.frontRightDriveMotor.getOutputCurrent());
    	SmartDashboard.putNumber("CurrentMax---", currentMax);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return RobotMap.frontRightDriveMotor.getOutputCurrent() > currentMax;
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
