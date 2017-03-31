package org.usfirst.frc.team4188.robot;

import org.opencv.core.Rect;

public class SortableRect implements Comparable{
	Rect rect = null;
	double area = Double.NaN;
	
	public SortableRect(Rect rect){
		
		this.rect = rect;
		this.area = rect.area();
	}
	
	@Override
	public int compareTo(Object thatObj) {
		// TODO Auto-generated method stub
		SortableRect that = (SortableRect)thatObj; 
		int result = 0;
		
		if(this.area < that.area){
			result = 1;
		}
		else if(this.area > that.area){
			result = -1;
		}
	return result;
}
	
	public Rect getRect(){
		
		return this.rect;
	}
	
}