package com.common;
import java.sql.Connection;
import java.sql.SQLException;

import com.common.VbyP;
import com.common.util.SLibrary;
import com.m.member.SessionManagement;
import com.m.member.UserInformationVO;


public class UseFunction extends SessionManagement {
	
	public UserInformationVO getUserInformation() {
		
		Connection conn = null;
		UserInformationVO vo = null;
		try {
			
			conn = VbyP.getDB();
			if ( !SLibrary.IfNull( super.getSession() ).equals("") )
				vo = super.getUserInformation(conn);
		}catch (Exception e) {}
		finally {
			
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("getUserInformation >> conn.close() Exception!"); 
			}
		}
		
		return vo;
	}
}
