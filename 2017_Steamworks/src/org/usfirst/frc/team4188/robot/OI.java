

package org.usfirst.frc.team4188.robot;

import org.usfirst.frc.team4188.robot.commandgroups.GearAutonomousLeft;
import org.usfirst.frc.team4188.robot.commandgroups.GearAutonomousMiddle;
import org.usfirst.frc.team4188.robot.commandgroups.MiddleAutonomousDrop;
import org.usfirst.frc.team4188.robot.commands.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());

	public CHSJoystick pilotJoystick;
	public Joystick copilotController;
	public CHSJoystick pilotController;
	public XboxController pilotXboxSample;
	//0,4,12,-12.0,12.0,1,1.0,-12.0,12.0,1,1.0,-12.0,12.0,1,1.0
	
	
	public JoystickButton pilot1;
	public JoystickButton pilot2;
	public JoystickButton pilot3;
	public JoystickButton pilot4;
	public JoystickButton pilot5;
	public JoystickButton pilot6;
	public JoystickButton pilot7;
	public JoystickButton pilot8;
	public JoystickButton pilot9;
	public JoystickButton pilot10;
	public JoystickButton pilot11;
	public JoystickButton pilot12;
	
	
	public JoystickButton copilot1;
	public JoystickButton copilot2;
	public JoystickButton copilot3;
	public JoystickButton copilot4;
	public JoystickButton copilot5;
	public JoystickButton copilot6;
	public JoystickButton copilot7;
	public JoystickButton copilot8;
	public JoystickButton copilot9;
	public JoystickButton copilot10;
	public JoystickButton copilot11;
	public JoystickButton copilot12;
	
	public double XNegDeadZone = -12;
	public double XPosDeadZone = 0;
	public double YNegDeadZone ;
	public double YPosDeadZone;
	public double XScale;
	public double YScale;
	public double XMaxSpeedPercent;
	public double YMaxSpeedPercent;
    
    
	public OI(){
		
		
	//	pilotController = new CHSJoystick(0,4,12,-12.0,12.0,1,1.0,-12.0,12.0,1,1.0,-12.0,12.0,1,1.0);
		pilotXboxSample = new XboxController(0);
		//pilotJoystick = new CHSJoystick(0);
		copilotController = new Joystick(1);
		//copilotController = new Joystick(1);
		/*		
		pilot1 = new JoystickButton(pilotController, 1);
        pilot2 = new JoystickButton(pilotController, 2);
        pilot3 = new JoystickButton(pilotController, 3);
        pilot4 = new JoystickButton(pilotController, 4);
        pilot5 = new JoystickButton(pilotController, 5);
        pilot6 = new JoystickButton(pilotController, 6);
        pilot7 = new JoystickButton(pilotController, 7);
        pilot8 = new JoystickButton(pilotController, 8);
        pilot9 = new JoystickButton(pilotController, 9);
        pilot10 = new JoystickButton(pilotController, 10);
        pilot11 = new JoystickButton(pilotController, 11);
        pilot12 = new JoystickButton(pilotController, 12);
        */
        copilot1 = new JoystickButton(copilotController, 1);
        copilot2 = new JoystickButton(copilotController, 2);
        copilot3 = new JoystickButton(copilotController, 3);
        copilot4 = new JoystickButton(copilotController, 4);
        copilot5 = new JoystickButton(copilotController, 5);
        copilot6 = new JoystickButton(copilotController, 6);
        copilot7 = new JoystickButton(copilotController, 7);
        copilot8 = new JoystickButton(copilotController, 8);
        copilot9 = new JoystickButton(copilotController, 9);
        copilot10 = new JoystickButton(copilotController, 10);
        copilot11 = new JoystickButton(copilotController, 11);
        
        pilot1 = new JoystickButton(pilotXboxSample, 1);
        pilot2 = new JoystickButton(pilotXboxSample, 2);
        pilot3 = new JoystickButton(pilotXboxSample, 3);
        pilot4 = new JoystickButton(pilotXboxSample, 4);
        pilot5 = new JoystickButton(pilotXboxSample, 5);
        pilot6 = new JoystickButton(pilotXboxSample, 6);
        pilot7 = new JoystickButton(pilotXboxSample, 7);
        pilot8 = new JoystickButton(pilotXboxSample, 8);
        pilot9 = new JoystickButton(pilotXboxSample, 9);
        pilot10 = new JoystickButton(pilotXboxSample, 10);
        pilot11 = new JoystickButton(pilotXboxSample, 11);
        pilot12 = new JoystickButton(pilotXboxSample, 12);
        SmartDashboard.putData(Scheduler.getInstance());
        
        pilot6.whileHeld(new GearRelease());
        pilot6.whenReleased(new GearOff());
        pilot5.whileHeld(new GearRetract());
        pilot5.whenReleased(new GearOff());
        
        
       
        	//SmartDashboard.putNumber("OI getAngleToGoal", Robot.getAngleToGoal());
       
        	pilot4.whenPressed(new TurnRight());
        
        //pilot4.whenPressed(new AimHighGoal());
        	//pilot1.whenPressed(new GearAutonomousLeft());
        
        	
        pilot3.toggleWhenPressed(new ClimbFast());
        pilot2.toggleWhenPressed(new ClimberOff());
        pilot1.toggleWhenPressed(new ClimbSlow());
        
        pilot9.whileHeld(new GearShiftIn());
        pilot9.whenReleased(new GearShiftOff());
        pilot10.whileHeld(new GearShiftOut());
        pilot10.whenReleased(new GearShiftOff());
        
   //     pilot7.whenPressed(new DriveToTarget(0.5));
        pilot7.whenPressed(new AimHighGoal(90));
        //copilot1.whenPressed(new AimHighGoal(1.0));
       
        /**
        copilot1.whileHeld(new ShootFuel());
        copilot2.whileHeld(new RunFuelElevator());
        copilot3.toggleWhenPressed(new IntakeOn());
        copilot4.toggleWhenPressed(new IntakeOff());
        
        copilot6.whileHeld(new GearRelease());
        copilot6.whenReleased(new GearOff());
        copilot5.whileHeld(new GearRetract());
        copilot5.whenReleased(new GearOff());
        copilot11.whenPressed(new AimHighGoal(1.0));
   
        **/
        
      //  copilot7.whileHeld(new ShootFuel());
        copilot8.whileHeld(new RunFuelElevator());
        copilot8.whenReleased(new FuelElevatorOff());
       
        
        /*
        copilot2.toggleWhenPressed(new IntakeOn());
        copilot3.toggleWhenPressed(new IntakeOff());
        */
        pilot9.whenPressed(new CameraLightsOn());
        pilot10.whenPressed(new CameraLightsOff());
        copilot3.whileHeld(new IntakeOn());
        copilot3.whenReleased(new IntakeOff());
        copilot2.whileHeld(new IntakeReverse());
        copilot2.whenReleased(new IntakeOff());
        copilot6.whileHeld(new GearRelease());
        copilot6.whenReleased(new GearOff());
        copilot5.whileHeld(new GearRetract());
        copilot5.whenReleased(new GearOff());
        
        copilot9.whenPressed(new CameraLightsOn());
        copilot10.whenPressed(new CameraLightsOff());
        
           
    SmartDashboard.putData("DriveToTarget---", new DriveToTarget(0.6));    
    SmartDashboard.putData("TestAutoCenter", new MiddleAutonomousDrop());    
    SmartDashboard.putData("Test90Turn", new AimHighGoal(90)); 
        
       }
   
		
		
	public Joystick getpilotJoystick(){
		return pilotJoystick;
	}
	
	public Joystick getpilotController(){
		return pilotController;
	}
	
	public Joystick getcopilotController(){
		return copilotController;
   }

}




