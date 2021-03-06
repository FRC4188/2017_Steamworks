package org.usfirst.frc.team4188.robot.commands;

import org.usfirst.frc.team4188.robot.CHSJoystick;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.Robot.PowerState;
import org.usfirst.frc.team4188.robot.RobotMap;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualDrive extends Command {
	CHSJoystick pilotController = Robot.oi.pilotController;
	CHSJoystick pilotJoystick = Robot.oi.pilotJoystick;
	XboxController pilotXboxSample = Robot.oi.pilotXboxSample;
	
    public ManualDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Robot.drivetrain.mecanumDrive(pilotController.getRawAxis(0), pilotController.getRawAxis(1), pilotController.getRawAxis(2), 1.0, 0.0);
    
    	double moveValue;
    	double rotateValue;
    	double rotateConstant = 0.85;
    	double brownOutConstant = 1.0;
    
    	if(pilotXboxSample.getTriggerAxis(Hand.kRight)-0.5>0 && !((pilotXboxSample.getTriggerAxis(Hand.kLeft)-0.5)>0)){
    		rotateConstant = 0.85+(0.15*(pilotXboxSample.getTriggerAxis(Hand.kRight)-0.5)) ;
    	}
    	if((pilotXboxSample.getTriggerAxis(Hand.kLeft)-0.5>0) && !((pilotXboxSample.getTriggerAxis(Hand.kRight)-0.5)>0)){
    		rotateConstant = 0.85-(0.35*(pilotXboxSample.getTriggerAxis(Hand.kLeft)-0.5));
    	}
    	
    	
    	
    	
    	Robot.drivetrain.arcadeDrive(pilotXboxSample.getY(Hand.kLeft), -pilotXboxSample.getX(Hand.kRight)*rotateConstant, 1.0);
    	
    	
    	/**	
    	if(pilotController.getIsXbox()){
    	Robot.drivetrain.arcadeDrive(-pilotController.getRawAxis(1), -pilotController.getRawAxis(4)*0.85 , 1.0);
    	}
    	else{
    	
    	double rotateValue;
    	if(Math.abs(pilotController.getTwist()) > Math.abs(pilotController.getX())){
    		rotateValue = pilotController.getTwist();
    	}
    	
    	else {
    		rotateValue = 0.7*pilotController.getX();
    	}
    		Robot.drivetrain.arcadeDrive(-pilotController.getYOutput(), rotateValue, pilotController.getThrottle());
    	
    	}
    	**/
    	
    	
    	
    	//Robot.drivetrain.mecanumDrive(pilotJoystick.getX(), pilotJoystick.getY(), pilotJoystick.getTwist(), pilotJoystick.getThrottle(), 0 );
    	
    									// **************Field Oriented*************//
    	/**
    	if(pilotController.getIsXbox()==true){
    		Robot.drivetrain.mecanumDrive(-pilotController.getRawAxis(0)/2, 1.0*pilotController.getRawAxis(1)/2, pilotController.getRawAxis(4)/2, 1.0, 0.0);
    	}
    	else{
    		Robot.drivetrain.mecanumDrive(pilotController.getRawAxis(2)/2, -1.0*pilotController.getRawAxis(1)/2, pilotController.getRawAxis(0)/2, pilotController.getThrottle(),0.0 );
    	}
    	**/
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    /**
    public double getX(GenericHID.Hand hand)
    {
        double X = Robot.oi.pilotJoystick.getX();
        
        // Configure Dead Zone from negDeadZone to posDeadZone on twist axis.
        if (((X*128)>XNegDeadZone)&&((X*128)<XPosDeadZone))
            X = 0.0;
        else
        {
            
			// Multiply inputs.
            for(int i = 0; i<XScale; i++) {
                X *= Math.abs(X);
            }
            // Multiply by max output.
            X*=XMaxSpeedPercent;
        }
        
        return X;
    }

    /**
     * Get the Y value of the current joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * Method implements dead zone, multiplier, and max speed percent initialized in constructor.
     * @override getY() method in parent Joystick class.
     * @param hand Passed to super method - unused.
     * @return The Y value of the joystick.
     */
    /**
    public double getY(GenericHID.Hand hand)
    {
        double Y = Robot.oi.pilotJoystick.getY(hand);
        
        // Configure Dead Zone from negDeadZone to posDeadZone on twist axis.
        if (((Y*128)>YNegDeadZone)&&((Y*128)<YPosDeadZone))
            Y = 0.0;
        else
        {
            // Multiply inputs.
            for(int i = 0; i< YScale; i++) {
                Y *= Math.abs(Y);
            }
            // Multiply by max output, and invert because Y axis is inverted.
            Y*=YMaxSpeedPercent*-1;
        }
        
        	return Y;
        
    }
    **/
}
