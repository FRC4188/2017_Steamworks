package org.usfirst.frc.team4188.robot.commands;

import org.usfirst.frc.team4188.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearAutonomous extends CommandGroup {

	public static String position = "MIDDLE"; 

	public GearAutonomous(String position) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		if(position.equalsIgnoreCase("RIGHT")){
			Robot.drivetrain.resetEncoders();
			//addSequential(new EncoderDriveToDistance((6.42-(33/12)), 1.0),3);
			/////addSequential(new EncoderDriveToDistance(8.8,1.0),3);	/
			addSequential(new EncoderDriveToDistance(9.8,1.0));	//move forward  about 7.5 feet
			
			Robot.drivetrain.resetEncoders();
			/////addSequential(new AutoDrive(0,0.8,0.28),3);		 
			addSequential(new AutoDrive(0,0.85,0.40));		//turn 
			addSequential(new AimGearPeg(Robot.getAngleToGoal()));	//align with gear Peg
			Robot.drivetrain.resetEncoders();
			
			addSequential(new EncoderDriveToDistance(8.8,1.0));	//move forward  about 7.5 feet
			
			Robot.drivetrain.resetEncoders();
			//addSequential(new EncoderDriveToDistance(8.8,1.0),3);
			addSequential(new EncoderDriveToDistance((6.7), 1.0));	//move forward about 12.5 feet
			addSequential(new AimGearPeg(Robot.getAngleToGoal()));	//align with gear Peg
			
			Robot.drivetrain.resetEncoders();
			addSequential(new EncoderDriveToDistance((.7), 1.0));	//move forward about 12.5 feet
			
			
	SmartDashboard.putString("Auto Version","Right");																
		}
		else if(position.equalsIgnoreCase("MIDDLE")){
			addSequential(new EncoderDriveToDistance(35.0/12.0,0.4));
			addSequential(new EncoderDriveToDistance(35.0/12.0,0.4));
	SmartDashboard.putString("Auto Version","Middle" );																
			
		}
		else if(position.equalsIgnoreCase("LEFT")){
			Robot.drivetrain.resetEncoders();
			addSequential(new EncoderDriveToDistance(8.8,1.0),4);	//move forward  about 7.5 feet
			
			Robot.drivetrain.resetEncoders();
			addSequential(new AutoDrive(0,-0.8,0.50),6);		//turn 
			addSequential(new EncoderDriveToDistance(8.8,1.0),2);	//move forward  about 7.5 feet
			addSequential(new AimGearPeg(Robot.getAngleToGoal()));	//align with gear Peg
			Robot.drivetrain.resetEncoders();
			addSequential(new EncoderDriveToDistance((9.22), 1.0),3);	//move forward about 12.5 feet
			
	SmartDashboard.putString("Auto Version", position );																
			
		}
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
