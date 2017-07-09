package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.AimHighGoal;
import org.usfirst.frc.team4188.robot.commands.AutoDrive;
import org.usfirst.frc.team4188.robot.commands.BackUpReleaseSequence;
import org.usfirst.frc.team4188.robot.commands.CheckForTargetsWhileTurning;
import org.usfirst.frc.team4188.robot.commands.Delay;
import org.usfirst.frc.team4188.robot.commands.DriveStraightToDistance;
import org.usfirst.frc.team4188.robot.commands.DriveToTarget;
import org.usfirst.frc.team4188.robot.commands.GearRelease;
import org.usfirst.frc.team4188.robot.commands.GearRetract;
import org.usfirst.frc.team4188.robot.commands.TurnRight;

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
    	addSequential(new DriveStraightToDistance((82.0/12.0), 0.6));
    	addSequential(new AimHighGoal(59));
    	//addSequential(new CheckForTargetsWhileTurning());
    	addSequential(new Delay(0.5));
    	System.out.println("Drive To Target");
    	addSequential(new DriveToTarget(0.5));
    	
    	addSequential(new BackUpReleaseSequence(30/12));
    	addSequential(new AimHighGoal(-60));
    	addSequential(new DriveStraightToDistance((370/12.0), 0.9));
    	//new GearAutonomousMiddle();

    	
    	
/*    	addSequential(new AimHighGoal(60),1);
    	//addSequential(new TurnRight());
    	addSequential(new DriveStraightToDistance(35.0/12.0,0.6));
    	addSequential(new TurnRight());
    	addSequential(new DriveStraightToDistance((48.0/12.0), 0.6),3.0);
    	//addSequential(new BackUpReleaseSequence());
    	
*/    }
}
