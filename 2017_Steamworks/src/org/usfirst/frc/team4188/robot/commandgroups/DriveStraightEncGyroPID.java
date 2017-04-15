package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.AimHighGoal;
import org.usfirst.frc.team4188.robot.commands.DriveStraightToDistance;
import org.usfirst.frc.team4188.robot.commands.TurnRight;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveStraightEncGyroPID extends CommandGroup {

    public DriveStraightEncGyroPID() {
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
    	
    	   //distance, speed
    	//addSequential(new DriveStraightToDistance((72.0/12.0), 0.6)); //drive about 4 feet was at 0.6
    	//addSequential(new AimHighGoal());
    	//addSequential(new DriveStraightToDistance(29/12.0, 0.6));
    	addSequential(new TurnRight());
    	addSequential(new TurnRight());
    	addSequential(new TurnRight());
    	//    	System.out.println(angle);
    	//addSequential(new DriveStraightToDistance((36.0/12.0), 0.6));
    	//addSequential(new Delay(1.0));
    	//addSequential(new TurnRight(Robot.getAngleToGoal()));
    	//addSequential(new DriveStraightToDistance(29/12.0, 0.6));
    	//addSequential(new TurnRight(Robot.getAngleToGoal()));//drive about 2.75 feet was at 0.6
    	
    	
    	
    }
}
