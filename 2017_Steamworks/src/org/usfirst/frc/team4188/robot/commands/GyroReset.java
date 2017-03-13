package org.usfirst.frc.team4188.robot.commands;

import org.usfirst.frc.team4188.robot.CHSRobotDrive;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.CHSRobotDrive.PIDType;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroReset extends Command {

	@Override
	protected void initialize() {
		RobotMap.gyro.reset();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

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
		end();
	}
	

}
