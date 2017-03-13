package org.usfirst.frc.team4188.robot.commands;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4188.robot.GripPipeline;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.VisionProcessing;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

/**
 * 
 *
 */


public class GetVisionValues extends Command {

Mat mat = Robot.getMat();
GripPipeline vision = Robot.vision;
private double distance, angle, lengthBetweenContours;	
VisionProcessing visionProcessing = new VisionProcessing();    
VisionThread visionThread;
UsbCamera camera;

public GetVisionValues() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    	Robot.visionThread.start();
    }
    	
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @SuppressWarnings("deprecation")
	protected void end() {
    	Robot.visionThread.stop();    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
