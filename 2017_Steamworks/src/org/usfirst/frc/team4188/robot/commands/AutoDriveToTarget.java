package org.usfirst.frc.team4188.robot.commands;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4188.robot.Robot;
import org.usfirst.frc.team4188.robot.RobotMap;
import org.usfirst.frc.team4188.robot.Vision;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.vision.VisionThread;

/**
 *
 */
public class AutoDriveToTarget extends Command {
	private static final int IMG_WIDTH = 640;
	private static final int IMG_HEIGHT = 480;
	
	private VisionThread visionThread;
	private double centerX = 0.0;
	
	
	private final Object imgLock = new Object();
    public AutoDriveToTarget() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
   
    	
    	
    	RobotMap.camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
         
    
         visionThread = new VisionThread(RobotMap.camera, new Vision(), VisionPipeline -> {
             if (!VisionPipeline.filterContoursOutput().isEmpty()) {
                 Rect r = Imgproc.boundingRect(VisionPipeline.filterContoursOutput().get(0));
                 synchronized (imgLock) {
                     centerX = r.x + (r.width / 2);
                 }
             }
         });
         visionThread.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double centerX;
     	synchronized (imgLock) {
     		centerX = this.centerX;
     	}
     	double turn = centerX - (IMG_WIDTH / 2);
     	Robot.drivetrain.autoDrive(-0.4, turn*0.005, 0);
     	
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
}
