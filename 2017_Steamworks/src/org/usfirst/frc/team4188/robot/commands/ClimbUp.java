package org.usfirst.frc.team4188.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team4188.robot.Robot;

/**
 *
 */
public class ClimbUp extends Command {
	
	
	public ClimbUp() {
		// Use requires() here to declare subsystem dependencies

	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		Robot.climber.climberTalonUp(-0.6);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return true;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
