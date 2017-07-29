package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.AimHighGoal;
import org.usfirst.frc.team4188.robot.commands.BackUpReleaseSequence;
import org.usfirst.frc.team4188.robot.commands.Delay;
import org.usfirst.frc.team4188.robot.commands.DriveStraightToDistance;
import org.usfirst.frc.team4188.robot.commands.DriveToTarget;
import org.usfirst.frc.team4188.robot.commands.TurnRight;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MiddleAutonomousDropRight extends CommandGroup {

    public MiddleAutonomousDropRight() {
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
    	addSequential(new Delay(.25));

    	
    	addSequential(new DriveToTarget(0.55));
    	addSequential(new BackUpReleaseSequence(59/12));
    	addSequential(new AimHighGoal(89));
    	addSequential(new DriveStraightToDistance(120/12.0,0.7));
    	addSequential(new AimHighGoal(-91.5));
    	addSequential(new DriveStraightToDistance(450/12,0.95));
    //	addSequential(new BackUpReleaseSequence());

    	
    }
}
