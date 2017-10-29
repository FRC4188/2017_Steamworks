package org.usfirst.frc.team4188.robot.commands;

import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team4188.robot.CHSRobotDrive;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.commandgroups.MiddleAutonomousDrop;
import org.usfirst.frc.team4188.robot.CHSRobotDrive.PIDType;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveToTarget extends Command {

    double speed, currentMax, startDistance;
    int count;
	public DriveToTarget(double speed) {
		
    	this.speed = speed;
    	this.currentMax = currentMax;
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	count = 0;
    	RobotMap.gyro.reset();
    	Robot.drivetrain.resetEncoders();
    	System.out.println("DriveToTarget");
    	startDistance = -10;
    	
    	//new TestAutonomous();
    
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    
//    	double angle = Robot.getAngleToGoal()*-1;
    	double angle = Robot.getTurnValue()*-1;
//    	System.out.println("Angle = " + angle);
//    	Robot.drivetrain.autoDrive(this.speed, 10);
    	System.out.println("Angle = " + Robot.getAngleToGoal());
    	System.out.println("Turn Value = " + (Robot.getTurnValue()*0.005));
    	Robot.drivetrain.autoDrive(this.speed, (angle*.005));
    	SmartDashboard.putNumber("ANGLE", angle);
    	SmartDashboard.putNumber("AngleDivided", angle*.005);
    	SmartDashboard.putNumber("OutputCurrent---", RobotMap.frontRightDriveMotor.getOutputCurrent());
    	SmartDashboard.putNumber("CurrentMax---", currentMax);
    

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
//    	return RobotMap.frontRightDriveMotor.getOutputCurrent() > currentMax;
    	if (Robot.drivetrain.getRightEncoderDistance()-startDistance <= 0) {    		
    		count++;
    	} else {
    		count = 0;
    	}
		startDistance=Robot.drivetrain.getRightEncoderDistance();
    	
    	if (count > 20) {
    		return true;
    	}
    	return false;
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
