package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.AimHighGoal;
import org.usfirst.frc.team4188.robot.commands.BackUpReleaseSequence;
import org.usfirst.frc.team4188.robot.commands.CheckForTargetsWhileTurning;
import org.usfirst.frc.team4188.robot.commands.Delay;
import org.usfirst.frc.team4188.robot.commands.DriveStraightToDistance;
import org.usfirst.frc.team4188.robot.commands.DriveToTarget;
import org.usfirst.frc.team4188.robot.commands.TurnRight;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearAutonomousRightSideNoDriveAround extends CommandGroup {

    public GearAutonomousRightSideNoDriveAround() {
    	//distance, speed
//    	addSequential(new DriveStraightToDistance((53.0/13.9), 0.6));
    	addSequential(new DriveStraightToDistance((82.0/12.0), 0.6));
    	addSequential(new AimHighGoal(-59));
    	addSequential(new CheckForTargetsWhileTurning());
    	addSequential(new Delay(1));
    	
    	System.out.println("Drive To Target");
    	addSequential(new DriveToTarget(0.5));
    	
    	addSequential(new BackUpReleaseSequence(30/12));
 /*   	addSequential(new AimHighGoal(60));
    	addSequential(new DriveStraightToDistance((370/12.0), 0.9));
    	//addSequential(new BackUpReleaseSequence());
*/
    }
}
