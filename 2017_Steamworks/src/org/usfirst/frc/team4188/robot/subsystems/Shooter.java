package org.usfirst.frc.team4188.robot.subsystems;

import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.commands.ShootFuel;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {

	public static CANTalon hoodRotation = RobotMap.hoodRotation;
	public static CANTalon shooterTalon = RobotMap.shooterTalon;
	public static AnalogInput seatMotorHallSensor = RobotMap.seatMotorHallSensor;
	
	public double speedPrevious;
	public int position;


	public void init() {
		// TODO Auto-generated method stub
		/*seatMotorHallSensor.setLimitsVoltage(3.5,3.5);*/
	}
	
	public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new ShootFuel());
    }
    
    public void shootFuel(double speed){
    	shooterTalon.setInverted(true);
    	shooterTalon.set(speed);
    }
	
    /*public double checkDirectionChange(double newSpeed){
		
		if((speedPrevious < 0 && newSpeed >= 0) || (speedPrevious >= 0 && newSpeed < 0)){
			
			position = getPosition();
			counter.reset();
			speedPrevious = newSpeed;	
		}
		return newSpeed;
	}
	
	public int getPosition(){
		
		if(speedPrevious >= 0){
			return position + counter.get();
		}	
		return position-counter.get();
		
	}*/
    
}