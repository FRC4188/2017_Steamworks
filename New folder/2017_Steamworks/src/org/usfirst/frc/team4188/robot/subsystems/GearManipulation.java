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


		CANTalon motorLeftRight = RobotMap.gearLeftRight;
		CANTalon motorUpDown = RobotMap.gearUpDown;
		DoubleSolenoid gearRelease = RobotMap.gearRelease;

	    // Put methods for controlling this subsystem
	    // here. Call these from Commands.



    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
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
    	motorUpDown.set(speed);
    	
    }
    public void runGearLeftRight(double speed){
    	motorLeftRight.set(speed);
    }
    public void stopGearUpDown(){
    	motorUpDown.set(0);
    }
    public void stopGearRightLeft(){
    	motorLeftRight.set(0);
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

