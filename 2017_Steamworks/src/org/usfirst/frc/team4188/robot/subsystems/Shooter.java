package org.usfirst.frc.team4188.robot.subsystems;

import org.usfirst.frc.team4188.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {

	public static CANTalon hoodRotation = RobotMap.hoodRotation;
	public static AnalogTrigger seatMotorHallSensor = RobotMap.seatMotorHallSensor;
	public static Counter counter;
	private double speedPrevious;
	private int position;
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
	
	public void init(){
    	RobotMap.gyro.reset();
    }
	
	public double checkDirectionChange(double newSpeed){
		
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
		
	}

	
	
}
