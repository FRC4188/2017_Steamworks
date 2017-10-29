package org.usfirst.frc.team4188.robot.subsystems;

import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	CANTalon climberTalon = RobotMap.climberTalon; 
	CANTalon climberTalon2 = RobotMap.climberTalon2;
	
	
	
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		init();
	}

	public void init(){
		
	}
	
	public void climberTalonUp(double throttle)
	{
		climberTalon.set(throttle);
		climberTalon2.set(-throttle);
	}
	
	public void climberTalonDown(double throttle)
	{
		climberTalon.set(-throttle);
		climberTalon2.set(throttle);
	}
	
	public void climberTalonOff(){
		climberTalon.set(0);
		climberTalon2.set(0);
	}

}
