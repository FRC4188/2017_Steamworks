package org.usfirst.frc.team4188.robot.commands;

import org.usfirst.frc.team4188.robot.CHSRobotDrive;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.CHSRobotDrive.PIDType;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TurnRight extends Command {
	double angle;

	public PIDController gyroPIDController;

	//PID tuned for practice bot
	private  double KP = 0.0;
	private  double KI = 0.0;
	private  double KD = 0.0;

	private long start = 0l;
	private static final double tolerance = 0.25; // to within 1.0 degree
	// private static final int ONE = 1;
	// private static final int TWO = 2;
	// private static final int THREE = 3;

	private int initCount = 0, executeCount = 0;

	public TurnRight(double setpoint) {
		// Use requires() here to declare subsystem dependencies
		if (Robot.drivetrain == null) {
			throw new NullPointerException("Robot.drivetrain is null, Vineeth.");
		}
		this.angle = setpoint; 
		requires(Robot.drivetrain);

		if(Robot.whichBot == Robot.WhichBot.SKETCHY){
			KP = 0.02;
			KI = 0.002;
			KD = 0.002;

		}else if(Robot.whichBot == Robot.WhichBot.PRACTICE){			
//			KP = 0.008;
//			KI = 0.0006;
//			KD = 0.0000;
			
			KP = 0.010;
			KI = 0.00001;
			KD = 0.0;
		}else if(Robot.whichBot == Robot.WhichBot.COMPETITION){
			KP = 0.02;
			KI = 0.0;
			KD = 0.0;
		}
	}

	// Called just before this Command runs the first time
	protected void initialize() {

		this.start = System.currentTimeMillis();

		SmartDashboard.putString("Aim Status", "Initializing");
		SmartDashboard.putString("Control Mode", "Left = " + RobotMap.frontLeftDriveMotor.getControlMode());;

		Robot.drivetrain.gyroReset();
		CHSRobotDrive.setPIDType(PIDType.turnToAngle);

		gyroPIDController = new PIDController(KP, KI, KD, RobotMap.gyro, RobotMap.driveBase);
		gyroPIDController.setAbsoluteTolerance(tolerance);

		gyroPIDController.setSetpoint(this.angle);

		//gyroPIDController.setSetpoint(10);

		gyroPIDController.enable();

		initCount++;
		SmartDashboard.putNumber("Init Count", initCount);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		boolean onTarget = gyroPIDController.onTarget();
		SmartDashboard.putString("Aim Status", "On Target =" + onTarget) ;
		return onTarget;
	}

	// Called once after isFinished returns true
	protected void end() {
		gyroPIDController.disable();
		gyroPIDController.free();

		long elapsed = System.currentTimeMillis()-start; //time that the PID has been running
		SmartDashboard.putNumber("Turn Right Elapsed", elapsed);
		//RobotMap.driveBase.stopMotor();
		// Set the motors to 0 (stop them)
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
