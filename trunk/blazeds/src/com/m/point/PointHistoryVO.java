package com.m.point;

import java.io.Serializable;

public class PointHistoryVO implements Serializable {
	
	private static final long serialVersionUID = 1324179582713735628L;
	
	int no;					//순번
	String message	= "";	//내용
	int point;				//건수
	String time		= "";	//시간
	
	public void setAll(int no, String message, int point, String time){
		
		this.no			= no;
		this.message	= message;
		this.point		= point;
		this.time		= time;
		
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
