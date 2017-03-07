package org.usfirst.frc.team4188.robot.commands;

import org.usfirst.frc.team4188.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearAutonomousLeft extends CommandGroup {

    public GearAutonomousLeft() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.

    	Robot.drivetrain.resetEncoders();
    	addSequential(new EncoderDriveToDistance((6.5-(33/12)), 1.0),3);
    	Robot.drivetrain.resetEncoders();
    	addSequential(new AutoDrive(0,-0.8,0.28),3);
    	Robot.drivetrain.resetEncoders();
    	//addSequential(new EncoderDriveToDistance(71.5/12.0,1.0),3);
    	addSequential(new EncoderDriveToDistance(3.3,1.0),3);
    }
}
