package org.usfirst.frc.team4188.robot.commands;

import org.usfirst.frc.team4188.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoDriveEncoder extends Command {

	double moveValue;
	double rotateValue;
	double distance;
	boolean doneYet;
	
	
    public AutoDriveEncoder(double moveValue, double rotateValue, double distance) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drivetrain);
    	this.moveValue = moveValue;
    	this.rotateValue = rotateValue;
    	this.distance = distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drivetrain.resetEncoders();
    	doneYet = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.drivetrain.getLeftEncoderDistance() < distance) {
    		Robot.drivetrain.autoDrive(moveValue, rotateValue); //negative means it goes right
    		
    		}
    		else{
    			Robot.drivetrain.autoDrive(0, 0);
    			doneYet = true;
    		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return doneYet;
    }

    // Called once after isFinished returns true
    protected void end() {
    	doneYet = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
