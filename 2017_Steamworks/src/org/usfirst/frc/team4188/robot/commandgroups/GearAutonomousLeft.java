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
//    	addSequential(new DriveStraightToDistance((53.0/13.9), 0.6));
    	addSequential(new DriveStraightToDistance((96.0/12.0), 0.6));

    	addSequential(new TurnRight(-60));
    	addSequential(new DriveStraightToDistance(35.0/12.0,0.6));
    	addSequential(new TurnRight(Robot.getAngleToGoal()));
    	addSequential(new DriveStraightToDistance((48.0/12.0), 0.6));
    	//addSequential(new DriveStraightToDistance(54.0/12.0,0.6));

    }
}
