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
	protected RobotDrive robotDrive2;
	protected RobotDrive robotDrive3;
	private static double outputMin = 0.35;
	
//    public CHSRobotDrive(SpeedController rearLeft, SpeedController frontLeft, SpeedController rearRight, SpeedController frontRight){
//    	super( frontLeft,rearLeft,frontRight, rearRight);
//    }
    
    public CHSRobotDrive(SpeedController rearLeft, SpeedController frontLeft, SpeedController middleLeft,SpeedController rearRight,
    		SpeedController frontRight, SpeedController middleRight){
    	super(frontLeft, frontRight);
    	robotDrive2 = new RobotDrive(rearLeft, rearRight);
    	
    	middleLeft.setInverted(true);
    	middleRight.setInverted(true);
    	robotDrive3 = new RobotDrive(middleLeft, middleRight);
    	
    	//the outputMin for SKETCHY is different from the other bots
    	switch(Robot.whichBot){
    		case SKETCHY:
    			outputMin = 0.15;
    			break;
    		case PRACTICE:
    			outputMin = 0.15;
    			break;
    		case COMPETITION:
    			outputMin = 0.35;
    			break;
    	}
    }
    
    public void setSafetyEnabled(boolean enabled){
    	 super.setSafetyEnabled(enabled);
    	
    	robotDrive2.setSafetyEnabled(enabled);
    	robotDrive3.setSafetyEnabled(enabled);
    	
    }

    public void arcadeDrive(double moveValue, double rotateValue){
    	super.arcadeDrive(moveValue, rotateValue);
    	
    	robotDrive2.arcadeDrive(moveValue, rotateValue);
    	robotDrive3.arcadeDrive(moveValue, rotateValue);
    	
    }
    
    // at 0.05, even the motors with no gears could barely run.
    // same at 0.1

    public enum PIDType {
    	turnToAngle,
    	driveToDistance,
    	driveToDistanceTwoEncoder
    	
    }
    
    private static PIDType driveType;
    public static void setPIDType(PIDType type) {    	
    	driveType = type;
    	switch (driveType) {
    	case turnToAngle:
        	SmartDashboard.putString("Setting PIDType =", "turnToAngle");
        	outputMin = .15;
        	break;
    
    	case driveToDistance:
        	SmartDashboard.putString("Setting PIDType =", "driveToDistance");
        	outputMin = .35;
        	break;
    	case driveToDistanceTwoEncoder:
    		SmartDashboard.putString("Setting PIDType =", "driveToDistance");
    		break;
    	}
    }
       
    public void pidWrite(double output){
    	if (Math.abs(output)< outputMin) {
    		output = outputMin * Math.signum(output);
    	}
    	SmartDashboard.putNumber("PID", output);
    	switch(driveType){
    	  case turnToAngle:
    	
    		super.setLeftRightMotorOutputs(output,-output);
        	robotDrive2.setLeftRightMotorOutputs(output, -output);
        	robotDrive3.setLeftRightMotorOutputs(output,-output);
        	break;
        	
    	  case driveToDistance:
    		
        	super.setLeftRightMotorOutputs(output,output);
        	robotDrive2.setLeftRightMotorOutputs(output,output);
        	robotDrive3.setLeftRightMotorOutputs(output, output);
            break;
        
    	  case driveToDistanceTwoEncoder:
    		double outputConstant;
    		if(Robot.drivetrain.getLeftEncoderDistance()<Robot.drivetrain.getRightEncoderDistance()){
    			outputConstant = 1.05;
    		}
    		if(Robot.drivetrain.getLeftEncoderDistance()>Robot.drivetrain.getRightEncoderDistance()){
    			outputConstant = 0.95;
    		}
    		else{
    			outputConstant = 1.00;
    		}
    		super.setLeftRightMotorOutputs(outputConstant*output, output);
    		
    		robotDrive2.setLeftRightMotorOutputs(outputConstant*output, output);
    		robotDrive3.setLeftRightMotorOutputs(outputConstant*output, output);
    	}
    }

    private int getInverted(SpeedController motor){
    	if(motor.getInverted())
    		return -1;
    	else
    		return 1;
    
    }
}



