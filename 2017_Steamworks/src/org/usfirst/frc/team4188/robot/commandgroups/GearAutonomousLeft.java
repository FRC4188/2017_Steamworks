package org.usfirst.frc.team4188.robot.commandgroups;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.commands.AimHighGoal;
import org.usfirst.frc.team4188.robot.commands.AutoDrive;
import org.usfirst.frc.team4188.robot.commands.Delay;
import org.usfirst.frc.team4188.robot.commands.DriveStraightToDistance;
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
    	addSequential(new DriveStraightToDistance((88.0/12.0), 0.6));

    	addSequential(new AimHighGoal(65));
    	addSequential(new DriveStraightToDistance(35.0/12.0,0.6));
    	addSequential(new TurnRight());
    	addSequential(new DriveStraightToDistance((48.0/12.0), 0.6));
    	addSequential(new Delay(1));
    	addSequential(new GearRetract());
    	addSequential(new AutoDrive(-0.5,0,1));
    	addSequential(new GearRelease());
    }
}
