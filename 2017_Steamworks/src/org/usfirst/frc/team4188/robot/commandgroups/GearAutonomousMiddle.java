package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.AutoDrive;
import org.usfirst.frc.team4188.robot.commands.Delay;
import org.usfirst.frc.team4188.robot.commands.DriveStraightToDistance;
import org.usfirst.frc.team4188.robot.commands.DriveToDistance;
import org.usfirst.frc.team4188.robot.commands.TurnRight;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearAutonomousMiddle extends CommandGroup {
public double angle = Robot.getAngleToGoal();
	
    public GearAutonomousMiddle() {
    //distance, speed
    	
    	addSequential(new DriveStraightToDistance((36/12.0), 0.6)); //drive about 4 feet was at 0.6
    	addSequential(new TurnRight());
//    	System.out.println(angle);
    	addSequential(new DriveStraightToDistance((24.0/12.0), 0.6));
    	//addSequential(new Delay(1.0));
    	addSequential(new TurnRight());
    	addSequential(new DriveStraightToDistance((12.0/12.0), 0.6));
    	addSequential(new TurnRight());
    	addSequential(new DriveStraightToDistance(20.0/12.0, 0.6)); //drive about 2.75 feet was at 0.6

    	/**
    	addSequential(new DriveStraightToDistance(45.0/12.0,0.6));
    	addSequential(new TurnRight());
    	addSequential(new DriveStraightToDistance((29.0/12.0), 0.6));
    **/
    }
}
