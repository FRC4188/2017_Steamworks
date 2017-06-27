package org.usfirst.frc.team4188.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class PixyProcessing extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	public PixyI2C pixy1;
	final int PORT_ID = 0x54;
	Port port = Port.kOnboard;
	String print;
	//public PixyPacket[] packet = pixy1.packets;
	
	

	
	
	public PixyProcessing(){
		pixy1 = new PixyI2C();
	}
	
	public void periodic(){
		for(int i = 0; i< pixy1.packets.length; i++){
			try {
				pixy1.packets[i]= pixy1.readPacket(1);
			} catch (PixyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SmartDashboard.putString("Pixy Error: " + i, "exception");
			}
			if(pixy1.packets[i]==null) {
				SmartDashboard.putString("Pixy Error: " + i, "true");
				continue;
			}
			
			SmartDashboard.putNumber("Gear Pixy X Value " +i, pixy1.packets[i-1].X);
			SmartDashboard.putNumber("Gear Pixy Y Value " +i, pixy1.packets[i-1].Y);
			SmartDashboard.putNumber("Gear Pixy Width Value " +i, pixy1.packets[i-1].Width);
			SmartDashboard.putNumber("Gear Pixy Height Value " +i, pixy1.packets[i-1].Height);
			SmartDashboard.putNumber("Gear Pixy Area Value " +i, (pixy1.packets[i-1].Width)*(pixy1.packets[i-1].Height));
			SmartDashboard.putString("Pixy Error: " +i, "false");
			
			
			
		}
	}
	

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}