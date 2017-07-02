package org.usfirst.frc.team4188.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BackUpReleaseSequence extends CommandGroup {

    public BackUpReleaseSequence(double backUpDistance) {
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
    	addSequential(new Delay(0.5));
    	addSequential(new GearRelease());
    	addSequential(new Delay(0.3));
    	//addSequential(new AutoDrive(-0.5,0,1.5));
    	addSequential(new DriveStraightToDistance(backUpDistance,-0.6));
    	addSequential(new GearRetract());
    }
}
