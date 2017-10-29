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
public class GearAutonomousRightSide extends CommandGroup {

    public GearAutonomousRightSide() {
    	addSequential(new DriveStraightToDistance((58.0/12.0), 0.75));
    	addSequential(new Delay(0.5));
    	
    	addSequential(new AimHighGoal(-50));
       	addSequential(new CheckForTargetsWhileTurning());
    	addSequential(new Delay(.5));
    	
    	System.out.println("Drive To Target");
    	addSequential(new DriveToTarget(0.55));
    	
    	addSequential(new BackUpReleaseSequence(32/12));
    	addSequential(new AimHighGoal(60));
    	addSequential(new DriveStraightToDistance((370/12.0), 0.95));
    }
}