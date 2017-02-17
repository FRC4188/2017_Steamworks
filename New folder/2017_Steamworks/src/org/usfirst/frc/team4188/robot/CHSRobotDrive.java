package org.usfirst.frc.team4188.robot;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 */
public class CHSRobotDrive extends RobotDrive implements PIDOutput {
    //static final int kFrontLeft_val = 0;
    //static final int kFrontRight_val = 1;
    //static final int kRearLeft_val = 2;
    //static final int kRearRight_val = 3;
    //static final double minValue = 0.17;
	protected RobotDrive robotDrive;
	
    public CHSRobotDrive(SpeedController rearLeft, SpeedController frontLeft, SpeedController rearRight, SpeedController frontRight){
    	super(rearLeft, frontLeft, rearRight, frontRight);
    }
    
    public CHSRobotDrive(SpeedController rearLeft, SpeedController frontLeft, SpeedController middleLeft,SpeedController rearRight,
    		SpeedController middleRight, SpeedController frontRight){
    	
    	super(rearLeft, frontLeft, rearRight, frontRight);
    	middleLeft.setInverted(true);
    	middleRight.setInverted(true);
    	robotDrive = new RobotDrive(middleLeft, middleRight);
    	
    
    }
    
    public void setSafetyEnabled(boolean enabled){
    	 super.setSafetyEnabled(enabled);
    	 robotDrive.setSafetyEnabled(enabled);
    }

    public void arcadeDrive(double moveValue, double rotateValue){
    	super.arcadeDrive(moveValue, rotateValue);
    	robotDrive.arcadeDrive(moveValue, rotateValue);
    }
    
    private static final double OUTPUT_MIN = 0.35;
    // at 0.05, even the motors with no gears could barely run.
    // same at 0.1

    public enum PIDType {
    	turnToAngle,
    	driveToDistance
    }
    
    private static PIDType driveType = PIDType.turnToAngle;
    public static void setPIDType(PIDType type) {    	
    	driveType = type;
    	switch (driveType) {
    	case turnToAngle:
        	SmartDashboard.putString("Setting PIDType =", "turnToAngle");
        	break;
    	case driveToDistance:
        	SmartDashboard.putString("Setting PIDType =", "driveToDistance");
        	break;
    	}
    }
       
    public void pidWrite(double output){
    	if (Math.abs(output)< OUTPUT_MIN) {
    		output = OUTPUT_MIN * Math.signum(output);
    	}
    	SmartDashboard.putNumber("PID", output);
    	switch(driveType){
    	case turnToAngle:
    		super.setLeftRightMotorOutputs(output,-output);
    		robotDrive.setLeftRightMotorOutputs(-output,output);
    	case driveToDistance:
        	super.setLeftRightMotorOutputs(output,output);
        	robotDrive.setLeftRightMotorOutputs(output,output);
        break;
    	}
    }

    private int getInverted(SpeedController motor){
    	if(motor.getInverted())
    		return -1;
    	else
    		return 1;
    
    }
}



