package org.usfirst.frc.team4188.robot.subsystems;

import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.commands.ManualDrive;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

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
	RobotDrive driveBase = RobotMap.driveBase;
	RobotDrive driveBase2 = RobotMap.chsDriveBase;
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ManualDrive());
    }
    public void init(){
    	RobotMap.gyro.reset();
    }
    public static double xthr;
    public static double ythr;
    public static double twthr;
    public static double throttle;
    public static double direction;
    
    public void mecanumDrive(double x, double y, double twist, double throttle, double direction){
    	if (Robot.oi.pilotContoller.getIsXbox()) {
    	driveBase.mecanumDrive_Cartesian(-y*throttle, -twist*throttle, -x*throttle, direction);
    	}
    	else {
    		driveBase.mecanumDrive_Cartesion(-x*throttle, y*throttle, -twist*throttle, direction);
    		
    	}
    	this.xthr = x * throttle;
    	this.ythr = y * throttle;
    	this.twthr = twist * throttle;
    	this.throttle = throttle;
    	this.direction = direction;
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
    public void moveRobot(){
    	frontLeft.set(0.5);
    }
    public void stop(){
    	frontLeft.set(0);
    }
	public void autoDrive(double moveValue, double rotateValue) {
		// TODO Auto-generated method stub
		driveBase.arcadeDrive(moveValue, rotateValue);
	}
}

