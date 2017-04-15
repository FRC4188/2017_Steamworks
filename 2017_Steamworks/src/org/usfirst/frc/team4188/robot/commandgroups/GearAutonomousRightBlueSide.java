package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.AimHighGoal;
import org.usfirst.frc.team4188.robot.commands.DriveStraightToDistance;
import org.usfirst.frc.team4188.robot.commands.TurnRight;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearAutonomousRightBlueSide extends CommandGroup {

    public GearAutonomousRightBlueSide() {
    	//distance, speed
//    	addSequential(new DriveStraightToDistance((53.0/13.9), 0.6));
    	addSequential(new DriveStraightToDistance((85.0/12.0), 0.6));

    	addSequential(new AimHighGoal(-56));
    	addSequential(new DriveStraightToDistance(35.0/12.0,0.6));
    	addSequential(new TurnRight());
    	addSequential(new DriveStraightToDistance((39.0/12.0), 0.6));
    	//addSequential(new DriveStraightToDistance(54.0/12.0,0.6));

    }
}
