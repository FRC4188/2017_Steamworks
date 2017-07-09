package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.AimHighGoal;
import org.usfirst.frc.team4188.robot.commands.AutoDrive;
import org.usfirst.frc.team4188.robot.commands.BackUpReleaseSequence;
import org.usfirst.frc.team4188.robot.commands.Delay;
import org.usfirst.frc.team4188.robot.commands.DriveStraightToDistance;
import org.usfirst.frc.team4188.robot.commands.DriveToDistance;
import org.usfirst.frc.team4188.robot.commands.DriveToTarget;
import org.usfirst.frc.team4188.robot.commands.GearRelease;
import org.usfirst.frc.team4188.robot.commands.GearRetract;
import org.usfirst.frc.team4188.robot.commands.TurnRight;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearAutonomousMiddleNoDriveAround extends CommandGroup {
public double angle = Robot.getAngleToGoal();
	
    public GearAutonomousMiddleNoDriveAround() {
    //distance, speed
    	
/*    	addSequential(new DriveStraightToDistance((36/12.0), 0.6)); //drive about 4 feet was at 0.6
    	addSequential(new TurnRight());
//    	System.out.println(angle);
    	addSequential(new DriveStraightToDistance((24.0/12.0), 0.6));
    	//addSequential(new Delay(1.0));
    	addSequential(new TurnRight());
    	addSequential(new DriveStraightToDistance((12.0/12.0), 0.6));
    	//addSequential(new TurnRight());
    	addSequential(new DriveStraightToDistance(19.0/12.0, 0.6),3); //drive about 2.75 feet was at 0.6
    	//addSequential(new BackUpReleaseSequence());
    
//    	addSequential(new DriveStraightToDistance(45.0/12.0,0.6));
//    	addSequential(new TurnRight());
//    	addSequential(new DriveStraightToDistance((29.0/12.0), 0.6));
 
*/
       //DriveToTargetChanges: 

/*    	addSequential(new DriveToTarget(0.5));*/
    	addSequential(new DriveToTarget(0.5));
    	addSequential(new BackUpReleaseSequence(60/12));

    	//addSequential(new BackUpReleaseSequence());
    	
    	//addSequential(new AimHighGoal(-90.0));
    	//addSequential(new DriveStraightToDistance(12.0/12.0, 0.6));   	
    	//addSequential(new AimHighGoal(90.0));
    	//addSequential(new DriveStraightToDistance(210/12, 0.6));
    	

    }
}
