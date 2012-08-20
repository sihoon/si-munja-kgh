package com.common.util;

public class StopWatch {

	String timeBuffer;
	int startTime;
	
	public StopWatch(){
		
	}
	
	public void play() {
		
		startTime = (int) System.currentTimeMillis() / 1000;
	}
	
	public String getTime() {
		
		int hour, min, sec;
		
		int secs = ((int) System.currentTimeMillis() / 1000) - startTime;		
		
		sec  = secs % 60;
		min  = secs / 60 % 60;
		hour = secs / 3600;
		
		return String.format("%02d:%02d:%02d", hour, min, sec); 
	}

}
