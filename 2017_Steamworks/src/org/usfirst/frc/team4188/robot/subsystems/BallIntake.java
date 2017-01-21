package org.usfirst.frc.team4188.robot.subsystems;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BallIntake extends Subsystem {
	Relay intakeRelay = RobotMap.intakeRelay;
	

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
   
    }
    
    public void init(){
    	
    }
    
    public void IntakeOn()
	{
		intakeRelay.set(Relay.Value.kReverse);
    }
    public void IntakeOff(){
    	intakeRelay.set(Relay.Value.kOff);
    }
}

