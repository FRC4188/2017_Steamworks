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
	public PixyPacket[] packet = new PixyPacket[7];
	
	

	
	
	public PixyProcessing(){
		pixy1 = new PixyI2C(new I2C(port,PORT_ID), packet, new PixyException(print), new PixyPacket());
	}
	
	public void periodic(){
		for(int i = 0; i< packet.length; i++){
			try {
				packet[i]= pixy1.readPacket(1);
			} catch (PixyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SmartDashboard.putString("Pixy Error: " + i, "exception");
			}
			if(packet[i]==null) {
				SmartDashboard.putString("Pixy Error: " + i, "true");
				continue;
			}
			
			SmartDashboard.putNumber("Gear Pixy X Value " +i, packet[i-1].X);
			SmartDashboard.putNumber("Gear Pixy Y Value " +i, packet[i-1].Y);
			SmartDashboard.putNumber("Gear Pixy Width Value " +i, packet[i-1].Width);
			SmartDashboard.putNumber("Gear Pixy Height Value " +i, packet[i-1].Height);
			SmartDashboard.putNumber("Gear Pixy Area Value " +i, (packet[i-1].Width)*(packet[i-1].Height));
			SmartDashboard.putString("Pixy Error: " +i, "false");
			
			
			
		}
	}
	

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}