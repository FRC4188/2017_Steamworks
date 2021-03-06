package org.usfirst.frc.team4188.robot;

import org.usfirst.frc.team4188.robot.subsystems.Climber;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.vision.AxisCamera;

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
	

	public static CANTalon frontRightDriveMotor;
	public static CANTalon frontLeftDriveMotor;
	public static CANTalon rearRightDriveMotor;
	public static CANTalon rearLeftDriveMotor;
	public static CANTalon middleRightDriveMotor;
	public static CANTalon middleLeftDriveMotor;
	
	public static CANTalon climberTalon2;
	public static CANTalon climberTalon;
	public static CANTalon gearLeftRight;
	public static CANTalon gearUpDown;
	public static CANTalon hoodRotation;
	public static CANTalon shooterTalon;
	
	public static CHSRobotDrive driveBase;
	public static RobotDrive DriveBase;
	
	public static AnalogGyro gyro;
	
	public static Relay cameraLights;
	public static Relay intakeRelay;
	public static Relay fuelElevator;
	
	public static AxisCamera camera;

	public static PIDController gyroPIDController;
	public static PIDController driveTrainPIDController;
	public static PIDController driveAimPIDController;
	
	public static DoubleSolenoid gearShift;
	
	public static DoubleSolenoid gearRelease;
	
	public static AnalogTrigger seatMotorHallSensor;
	
	public static PowerDistributionPanel pdp;
	
	public static double betaValue;
	public static double alphaValue;

	
		public static void init() {
		// TODO Auto-generated method stub
		
		alphaValue = 1;	
		betaValue = 1;
		gyro = new AnalogGyro(1);
//		gyro.calibrate(); believe that gyro already does this
		
		cameraLights= new Relay(0);
		intakeRelay = new Relay(1);
		fuelElevator = new Relay(2);
		
		climberTalon = new CANTalon(17);
		climberTalon2 = new CANTalon(19);
		
		frontLeftDriveMotor = new CANTalon(14);
		rearLeftDriveMotor = new CANTalon(15);
		middleLeftDriveMotor = new CANTalon(16);
		
		frontRightDriveMotor = new CANTalon(11);
		rearRightDriveMotor = new CANTalon(12);
		middleRightDriveMotor = new CANTalon(13);
		
		rearLeftDriveMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		rearRightDriveMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		
		//drive for two motor transmission
		//driveBase = new CHSRobotDrive( rearLeftDriveMotor,frontLeftDriveMotor, rearRightDriveMotor, frontRightDriveMotor);
		
		//drive for three motor transmission	
		driveBase = new CHSRobotDrive( rearLeftDriveMotor,frontLeftDriveMotor, middleLeftDriveMotor, rearRightDriveMotor, frontRightDriveMotor, middleRightDriveMotor);
		
		driveBase.setSafetyEnabled(false);
		driveBase.setExpiration(0.1);
		driveBase.setSensitivity(0.5);
		driveBase.setMaxOutput(1.0);
		
		hoodRotation = new CANTalon(21);
		shooterTalon = new CANTalon(18);
		shooterTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		shooterTalon.enableLimitSwitch(true, true);
		shooterTalon.ConfigFwdLimitSwitchNormallyOpen(true);
		shooterTalon.ConfigRevLimitSwitchNormallyOpen(true);
		gearShift = new DoubleSolenoid(0,1);
		gearRelease = new DoubleSolenoid(2,3);
		
		pdp = new PowerDistributionPanel();
		
		
		//seatMotorHallSensor = new AnalogTrigger(0);
        
		}
		


}
