package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.DriveToDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearAutonomousMiddle extends CommandGroup {

    public GearAutonomousMiddle() {
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
    	
    	
    	
    	
    	
    	//addSequential(new EncoderDriveToDistance((78.0/12.0), 0.8));
    	addSequential(new DriveToDistance(70.0/12.0,0.4));
    	//addSequential(new EncoderDriveToAngle(36, 0.8));
    	//Robot.drivetrain.resetEncoders();
    	//addSequential(new EncoderDriveToAngle(20,0.8));
    }
}