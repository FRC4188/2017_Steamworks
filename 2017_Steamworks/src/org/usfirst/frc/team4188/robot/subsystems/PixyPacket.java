package org.usfirst.frc.team4188.robot.subsystems;



/**
 *
 */
/**
*
*/
public class PixyPacket {
	public int Signature;
	public int X;
	public int Y;
	public int Width;
	public int Height;
	public int checksumError;
	
	public String toString(){
		return "" +
	" Signature:" + Signature +
	" X:" + X +
	" Y;" + Y + 
	" Width:" + Width +
	" Height:" + Height;
	}
}