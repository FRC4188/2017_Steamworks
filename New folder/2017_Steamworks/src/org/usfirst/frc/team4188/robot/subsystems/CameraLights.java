package org.usfirst.frc.team4188.robot.subsystems;

import org.usfirst.frc.team4188.robot.RobotMap;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CameraLights extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	Relay cameraLights = RobotMap.cameraLights;
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void cameraLightsOn(){
		cameraLights.set(Value.kForward);
	}
	public void cameraLightsOff(){
		cameraLights.set(Value.kOff);
	}

}

