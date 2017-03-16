package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.DriveStraightToDistance;
import org.usfirst.frc.team4188.robot.commands.TurnRight;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearAutonomousLeft extends CommandGroup {

    public GearAutonomousLeft() {
		//distance, speed
    	addSequential(new DriveStraightToDistance(10, 1.0));
    	addSequential(new TurnRight(-60));
//    	addSequential(new DriveStraightToDistance(2,1.0));
//    	addSequential(new TurnRight(Robot.getAngleToGoal()));

    }
}
