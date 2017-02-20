package org.usfirst.frc.team4188.robot.commands;

import org.usfirst.frc.team4188.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearAutonomous extends CommandGroup {

    public GearAutonomous() {
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
    	//addSequential(new GearShiftOut(),1);
		
    	
    	//addSequential(new EncoderDriveToDistance((45)/12.0, 0.8),2);
    	//Robot.drivetrain.resetEncoders();
    	addSequential(new EncoderTurnToAngleRight((100)/12.0, 0.8));
    	//addSequential(new AutoDrive(0.0,0.8, 3));
		//addSequential(new GearRelease(),2);
		//addSequential(new GearRetract(),1);
    	
     	
    }
}
