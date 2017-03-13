package org.usfirst.frc.team4188.robot.subsystems;

import org.usfirst.frc.team4188.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
//15 17
/**
 *
 */
public class GearManipulation extends Subsystem {


		CANTalon gearMotorUpDown = RobotMap.shooterTalon;
		DoubleSolenoid gearRelease = RobotMap.gearRelease;

	    // Put methods for controlling this subsystem
	    // here. Call these from Commands.



    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
   
    public void runGearUpDown(double speed){
    	gearMotorUpDown.set(speed);
    	
    }
  
    public void stopGearUpDown(){
    	gearMotorUpDown.set(0);
    }
   
    public void deployV(){
    	gearRelease.set(DoubleSolenoid.Value.kForward);
   
    }
    
    public void retractV(){
    	gearRelease.set(DoubleSolenoid.Value.kReverse);
    	
    	
    }
    public void doNothingV(){
    	gearRelease.set(DoubleSolenoid.Value.kOff);
    	
    	
    }
}

