package org.usfirst.frc.team4188.robot.commands;

import org.usfirst.frc.team4188.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CheckForTargetsWhileTurning extends Command {
	String direction;	

    public CheckForTargetsWhileTurning(String direction) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drivetrain);
    	this.direction = direction;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double rotate = direction == "right" ? -0.6 : 0.6;
		Robot.drivetrain.autoDrive(0.0, rotate);
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		return (Robot.robotVision.numParticles > 0);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.autoDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
	}
}
