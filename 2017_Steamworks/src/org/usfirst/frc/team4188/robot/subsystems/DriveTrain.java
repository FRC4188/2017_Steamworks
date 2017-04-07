package org.usfirst.frc.team4188.robot.subsystems;

import org.usfirst.frc.team4188.robot.CHSRobotDrive;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.commands.ManualDrive;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	CANTalon frontRight = RobotMap.frontRightDriveMotor;
	CANTalon frontLeft = RobotMap.frontLeftDriveMotor;
	CANTalon rearRight = RobotMap.rearRightDriveMotor;
	CANTalon rearLeft = RobotMap.rearLeftDriveMotor;
	
	CANTalon middleRight = RobotMap.middleRightDriveMotor;
	CANTalon middleLeft = RobotMap.middleLeftDriveMotor;
	
	CHSRobotDrive driveBase = RobotMap.driveBase;
	RobotDrive driveBase2 = RobotMap.DriveBase;
	//ADXRS450_Gyro gyro = RobotMap.gyro;
	AnalogGyro gyro = RobotMap.gyro;
	
	DoubleSolenoid gearShift = RobotMap.gearShift;
	
	
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ManualDrive());
    }
    public void init(){
    	RobotMap.gyro.reset();
    }
    public double xthr;
    public double ythr;
    public double twthr;
    public double throttle;
    public double direction;
    
    public void mecanumDrive(double x, double y, double twist, double direction, double throttle){
    	
    driveBase.mecanumDrive_Cartesian(x*throttle, y*throttle, twist*throttle, direction);
    		
    }
    
    public void autoDrive(double magnitude, double direction, double rotation){
    	driveBase.mecanumDrive_Polar(magnitude, direction, rotation);
    	
    }
    
    public void arcadeDrive(double x, double twist, double throttle){
    	driveBase.arcadeDrive(x*throttle, twist*throttle);
    }
    
    public void tankDrive(double leftValue, double rightValue, double throttle){
    	driveBase.tankDrive(leftValue*throttle, rightValue*throttle);
    }
   
	public void autoDrive(double moveValue, double rotateValue) {
		// TODO Auto-generated method stub
		driveBase.arcadeDrive(moveValue, rotateValue);
	}
	public void gyroReset(){
		gyro.reset();
	}
	public void shiftGearIn() {
		// TODO Auto-generated method stub
		gearShift.set(DoubleSolenoid.Value.kForward);
	}
	public void shiftGearOut() {
		// TODO Auto-generated method stub
		gearShift.set(DoubleSolenoid.Value.kReverse);
	}/*]\[]]\[]*/	
	public void shiftGearOff(){
		gearShift.set(DoubleSolenoid.Value.kOff);
	}
	public void setRampRate(double rampRate){
		
		frontLeft.setVoltageRampRate(rampRate);
		frontRight.setVoltageRampRate(rampRate);
		rearLeft.setVoltageRampRate(rampRate);
		rearRight.setVoltageRampRate(rampRate);
		middleLeft.setVoltageRampRate(rampRate);
		middleRight.setVoltageRampRate(rampRate);
	}
	public void resetEncoders(){
		rearRight.setPosition(0);
		rearLeft.setPosition(0);
	}
	public double getRightEncoderDistance(){
		double distance = rearRight.getPosition();
		SmartDashboard.putNumber("Encoder Right Distance", distance);
		return distance;
	}
	public double getLeftEncoderDistance(){
		double distance = -rearLeft.getPosition();
		SmartDashboard.putNumber("Encoder Left Distance", distance);
		return distance;
	}
	
	public void conservePower(boolean on){
		if(on){
			RobotMap.alphaValue = 0;
			RobotMap.betaValue = 0.9;
			
		}
		else{
			RobotMap.alphaValue = 1;
			RobotMap.betaValue = 1;
		}
		
	}
}

