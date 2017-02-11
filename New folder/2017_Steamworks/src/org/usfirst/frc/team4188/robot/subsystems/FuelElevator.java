package org.usfirst.frc.team4188.robot.subsystems;

import org.usfirst.frc.team4188.robot.RobotMap;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class FuelElevator extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	Relay fuelElevator = RobotMap.fuelElevator;
    
	public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void elevatorUp(){
		fuelElevator.set(Value.kForward);
	}
    
    public void elevatorDown(){
		fuelElevator.set(Value.kReverse);
	}
    
	public void elevatorOff(){
		fuelElevator.set(Value.kOff);
	}
	public void init() {
		// TODO Auto-generated method stub
		
	}

}


