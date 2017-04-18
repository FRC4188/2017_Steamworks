package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.AimHighGoal;
import org.usfirst.frc.team4188.robot.commands.AutoDrive;
import org.usfirst.frc.team4188.robot.commands.Delay;
import org.usfirst.frc.team4188.robot.commands.DriveStraightToDistance;
import org.usfirst.frc.team4188.robot.commands.DriveToDistance;
import org.usfirst.frc.team4188.robot.commands.GearRelease;
import org.usfirst.frc.team4188.robot.commands.GearRetract;
import org.usfirst.frc.team4188.robot.commands.TurnRight;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearAutonomousRightRedSide extends CommandGroup {

	public GearAutonomousRightRedSide() {
		//distance, speed
//    	addSequential(new DriveStraightToDistance((53.0/13.9), 0.6));
    	addSequential(new DriveStraightToDistance((88.0/12.0), 0.6));

    	addSequential(new AimHighGoal(-56));
    	addSequential(new DriveStraightToDistance(45.0/12.0,0.6));
    	addSequential(new TurnRight());
    	addSequential(new DriveStraightToDistance((31.0/12.0), 0.6),5);
    	addSequential(new Delay(1));
    	addSequential(new GearRetract());
    	addSequential(new AutoDrive(-0.5,0,1));
    	addSequential(new GearRelease());
    	//addSequential(new DriveStraightToDistance(54.0/12.0,0.6));
	}
}
