package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.AimHighGoal;
import org.usfirst.frc.team4188.robot.commands.BackUpReleaseSequence;
import org.usfirst.frc.team4188.robot.commands.DriveStraightToDistance;
import org.usfirst.frc.team4188.robot.commands.DriveToTarget;
import org.usfirst.frc.team4188.robot.commands.TurnRight;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MiddleAutonomousDrop extends CommandGroup {

    public MiddleAutonomousDrop() {
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
  	
    	addSequential(new DriveToTarget(0.6));
    	addSequential(new BackUpReleaseSequence(60/12));
    	addSequential(new AimHighGoal(-90));
    	addSequential(new DriveStraightToDistance(96/12.0,0.7));
    	addSequential(new AimHighGoal(90));
    	addSequential(new DriveStraightToDistance(400/12,0.9));
    //	addSequential(new BackUpReleaseSequence());

    	
    }
}
