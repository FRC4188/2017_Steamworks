package org.usfirst.frc.team4188.robot.commands;

import org.usfirst.frc.team4188.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunGearManipulationUpDown extends Command {

    public RunGearManipulationUpDown() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.gearManipulation);
    }

 // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Robot.gearManipulation.runGearUpDown(-Robot.oi.copilotJoystick.getY()*Robot.oi.copilotJoystick.getThrottle());
    	Robot.gearManipulation.runGearUpDown(-Robot.oi.copilotController.getRawAxis(1)*0.5);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

