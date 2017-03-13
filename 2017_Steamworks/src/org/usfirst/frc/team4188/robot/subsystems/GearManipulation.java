package org.usfirst.frc.team4188.robot.subsystems;

import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.commands.RunGearManipulationUpDown;

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
        setDefaultCommand(new RunGearManipulationUpDown());
    }
    
    public void runGearUp(double speed){
    	motorUpDown.setInverted(false);
    	motorUpDown.set(Math.abs(speed));
    }
    
    public void runGearDown(double speed){
    	motorUpDown.setInverted(true);
    	motorUpDown.set(-Math.abs(speed));
    	
    }
    public void runGearLeft(double speed){
    	motorLeftRight.setInverted(true);
    	motorLeftRight.set(Math.abs(speed));
    }
    public void runGearRight(double speed){
    	motorLeftRight.setInverted(false);
    	motorLeftRight.set(Math.abs(speed));
    }
    public void gearMotorsOff(){
    	motorLeftRight.set(0);
    	motorUpDown.set(0);
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

