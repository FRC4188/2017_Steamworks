package org.usfirst.frc.team4188.robot.commands;

import org.usfirst.frc.team4188.robot.CHSRobotDrive;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.CHSRobotDrive.PIDType;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderTest extends Command {

	boolean blockForward, blockReverse;
	public static int pos = 0, counter;
	
	public static double speed = 1.0, position, tolerance;
	public static PIDController seatMotorPIDController;
	public static final double KP = 0.03, KI = 0.0001, KD = 0.00001;
	private static final double OUTPUT_MIN = 3.5;
	
	public EncoderTest(double tolerance){
    	//requires();
    	this.tolerance = tolerance;
	}
	
	// Called just before this Command runs the first time
	
	//currently the PID controller setpoint controls motor speed
	protected void initialize(){
	  
		//accumulator could possibly be used to determine when the encoder stops and starts
	/*	RobotMap.seatMotorHallSensor.resetAccumulator();
	 	SmartDashboard.putNumber("Oversample Bits", RobotMap.seatMotorHallSensor.getOversampleBits());
	 	SmartDashboard.putNumber("seatMotor Value", RobotMap.seatMotorHallSensor.getValue());
	 	
	 	SmartDashboard.putNumber("Average Voltage Value", RobotMap.seatMotorHallSensor.getAverageVoltage());
    	seatMotorPIDController = new PIDController(KP, KI, KD, RobotMap.seatMotorHallSensor, RobotMap.hoodRotation); 
    	
    	SmartDashboard.putData("seatMotorPIDController", seatMotorPIDController);
		SmartDashboard.putNumber("DefaultPeriod_seatMotorPIDController", PIDController.kDefaultPeriod);
    	seatMotorPIDController.setAbsoluteTolerance(tolerance);

    	//currently controls speed based on the setpoint
    	seatMotorPIDController.setSetpoint(200.0);
		seatMotorPIDController.enable();
	  
*/	  }
	

	
	/*@Override
	protected void initialize() {      
			
		//Robot.shooter.counter.reset();
			        counter = 0;

			        //gets the current position from the encoder
			         	pos = Robot.shooter.getPosition();
			        	//SmartDashboard.putNumber("Position", pos);
			        	
			        //maximum position forward
			        	if(pos >= 175)
			        		blockForward = true;
			        	else     		
			        		blockForward = false;	
			        	
			        //if the position is less than zero, it is turning in the reverse direction
			        	if(pos <= 0)
			        		blockReverse = true;
			            else 
			            	blockReverse = false;
			        	
			        	//changes the direction depending on the position
			        	if(blockForward)
			        		speed = -1;
			        	
			        	if(blockReverse)
			        		speed = 1;
			        	
			        Robot.shooter.hoodRotation.set(Robot.shooter.checkDirectionChange(speed));
			      //Robot.shooter.hoodRotation.set(.2);
	}
*/
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {

	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		seatMotorPIDController.setAbsoluteTolerance(tolerance);
    	SmartDashboard.putBoolean("IsThePIDControllerOnTarget",  seatMotorPIDController.onTarget());
		return seatMotorPIDController.onTarget();
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
    	seatMotorPIDController.disable();
        seatMotorPIDController.free();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		end();
	}
}

	
	

