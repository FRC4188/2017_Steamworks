package org.usfirst.frc.team4188.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class PixyI2C {
	PixyPacket values;
	I2C pixy;
	Port port = Port.kOnboard;
	PixyPacket[] packets;
	PixyException pExc;
	String print;
	public PixyI2C() {
//		pixy = new I2C(port, 0x54);
//		packets = new PixyPacket[7];
//		pExc = new PixyException(print);
//		values = new PixyPacket();
	}
	public PixyI2C(I2C argPixy, PixyPacket[] argPixyPacket, PixyException argPixyException, PixyPacket argValues){
		pixy = argPixy;
		packets = argPixyPacket;
		pExc = argPixyException;
		values = argValues;
	}
	//This method parses raw data from the pixy into readable integers
	public int cvt(byte upper, byte lower) {
		return (((int)upper & 0xff) << 8) | ((int)lower & 0xff);
	}
	/*
	public void pixyReset(){
		pixy.reset();
	}
	*/
	//This method gathers data, then parses that data, and assigns the ints to global variables
	public PixyPacket readPacket(int Signature) throws PixyException { //The signature should be which number object in 
		int Checksum;												   //pixymon you are trying to get data for
		int Sig;
		byte[] rawData = new byte[32];
		SmartDashboard.putString("rawData", rawData[0] + " " + rawData[1] + " " + rawData[15] + " " + rawData[31]);		
		try{
			pixy.readOnly(rawData, 32);	
		} catch (RuntimeException e){
			SmartDashboard.putString("Pixy RuntimeException", "Error");
		}
		if(rawData.length < 32){
			System.out.println("byte array length is broken");
			return null;
		}
		for (int i = 0; i <= 16; i++) {
			int syncWord = cvt(rawData[i+1], rawData[i+0]); //Parse first 2 bytes
			if (syncWord == 0xaa55) { //Check is first 2 bytes equal a "sync word", which indicates the start of a packet of valid data
				syncWord = cvt(rawData[i+3], rawData[i+2]); //Parse the next 2 bytes
				if (syncWord != 0xaa55){ //Shifts everything in the case that one syncword is sent
					i -= 2;
				}
				//This next block parses the rest of the data
				Checksum = cvt(rawData[i+5], rawData[i+4]);
				Sig = cvt(rawData[i+7], rawData[i+6]);
				if(Sig <= 0 || Sig > packets.length){
					break;
				}
				
				packets[Sig - 1] = new PixyPacket();
				packets[Sig - 1].X = cvt(rawData[i+9], rawData[i+8]);
				packets[Sig - 1].Y = cvt(rawData[i+11], rawData[i+10]);
				packets[Sig - 1].Width = cvt(rawData[i+13], rawData[i+12]);
				packets[Sig - 1].Height = cvt(rawData[i+15], rawData[i+14]);
				//Checks whether the data is valid using the checksum *This if block should never be entered*
				if (Checksum != Sig + packets[Sig - 1].X + packets[Sig - 1].Y + packets[Sig - 1].Width + packets[Sig - 1].Height) {
					packets[Sig - 1] = null;
					throw pExc;
				}
				break;
			}
			else
				SmartDashboard.putNumber("syncword: ", syncWord);
		}
		//Assigns our packet to a temp packet, then deletes data so that we dont return old data
		PixyPacket pkt = packets[Signature - 1];
		packets[Signature - 1] = null;
		return pkt;
	}
	
	private byte [] readData(int len){
		byte [] rawData = new byte[len];
		try{
			pixy.readOnly(rawData, len);
			
		}
		catch(RuntimeException e){
			SmartDashboard.putString("Status", e.toString());
			System.out.println("Spaces" + e);
		}
		
		if(rawData.length < len){
			SmartDashboard.putString("Status", "raw data length" + rawData.length);
			return null;
		}
		return rawData;
		
	}
	
	private int readWord(){
		byte [] data = readData(2);
		if(data == null){
			return 0;
		}
		return cvt(data[1], data[0]);
	}
	
	private PixyPacket readBlock(int checksum){
		byte [] data = readData(12);
		
		if(data== null){
			return null;
		}
		PixyPacket block = new PixyPacket();
		block.Signature = cvt(data[1], data[0]);
		if(block.Signature <= 0 || block.Signature > 7){
			return null;
		}
		block.X = cvt(data[3], data[2]);
		block.Y = cvt(data[5], data[4]);
		block.Width = cvt(data[7], data[6]);
		block.Height = cvt(data[9], data[8]);
		
		int sum = block.Signature + block.X + block.Y + block.Width + block.Height;
		if(sum != checksum){
			return null;
		}
		return block;
	}
	
	private final int MAX_SIGNATURE = 7;
	private final int OBJECT_SIZE = 14;
	private final int START_WORD = 0xaa55;
	private final int START_WORD_CC = 0xaa5;
	private final int START_WORD_X = 0x55aa;
	
	public boolean getStart(){
		int numBytesRead = 0;
		int lastWord = 0xffff;
		
		while(numBytesRead < (OBJECT_SIZE*MAX_SIGNATURE)){
			int word = readWord();
			numBytesRead +=2;
			if(word == 0 && lastWord == 0){
				return false;
			}
			else if(word == START_WORD && lastWord == START_WORD ){
				return true;
			}
			else if(word == START_WORD_CC && lastWord == START_WORD){
				return true;
			}
			else if(word == START_WORD_X){
				byte [] data = readData(1);
				numBytesRead +=1;
			}
			lastWord = word;
		
		}
		return false;
	}
	
	private boolean skipStart = false;
	
	public PixyPacket [] readBlocks(){
		int maxBlocks = 2;
		PixyPacket [] blocks = new PixyPacket [maxBlocks];
		if (!skipStart){
			if(getStart()==false){
				return null;
			}
			else{
				skipStart = false;
			}
		}
		
		for(int i = 0; i < maxBlocks; i++){
			blocks[i] = null;
			int checkSum = readWord();
			if(checkSum == START_WORD){
				skipStart = true;
				return blocks;
				
			}
			else if(checkSum == START_WORD_CC){
				skipStart = true;
				return blocks;
			}
			else if(checkSum == 0){
				return blocks;
			}
			blocks[i] = readBlock(checkSum);
		}
		return blocks;
	}
	
	
	
	
	public int getX(){
		return values.X;
	}
	
	public int getY(){
		return values.Y;
	}
	
	public int getWidth(){
		return values.Width;
	}
	
	public int getHeight(){
		return values.Height;
	}
	
	public int getArea(){
		return getWidth() * getHeight();
	}
	
}

