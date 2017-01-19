package org.usfirst.frc.team4188.robot;

import org.usfirst.frc.team4188.robot.subsystems.Climber;

import com.ctre.CANTalon;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	
	public static CANTalon climberTalon;
	public static CANTalon frontRightDriveMotor;
	public static CANTalon frontLeftDriveMotor;
	public static CANTalon rearRightDriveMotor;
	public static CANTalon rearLeftDriveMotor;
	public static CANTalon gearLeftRight;
	public static CANTalon gearUpDown;
	public static CHSRobotDrive driveBase;
	public static RobotDrive DriveBase;
	public static ADXRS450_Gyro gyro;
	public static Relay cameraLights;
	public static AxisCamera camera;
	public static Relay intakeRelay;
	public static PIDController gyroPIDController;
	public static PIDController driveTrainPIDController;
	public static PIDController driveAimPIDController;
	
	
	
	
	
	
	
	
	
		public static void init() {
		// TODO Auto-generated method stub
		climberTalon = new CANTalon(1);
		gyro = new ADXRS450_Gyro();
		cameraLights= new Relay(0);
		frontLeftDriveMotor = new CANTalon(2);
		rearLeftDriveMotor = new CANTalon(1);
		frontRightDriveMotor = new CANTalon(3);
		rearRightDriveMotor = new CANTalon(4);
		
		driveBase = new CHSRobotDrive( rearLeftDriveMotor,frontLeftDriveMotor, rearRightDriveMotor, frontRightDriveMotor);
		
		driveBase.setSafetyEnabled(false);
		driveBase.setExpiration(0.1);
		driveBase.setSensitivity(0.5);
		driveBase.setMaxOutput(1.0);
		
		gearLeftRight = new CANTalon(17);
		gearUpDown = new CANTalon(15);
		camera = CameraServer.getInstance().addAxisCamera("10.41.88.11");
		intakeRelay = new Relay(0);
	

	
		
		}
		


}
