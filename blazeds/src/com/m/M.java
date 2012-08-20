package com.m;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import com.common.VbyP;
import com.common.util.SLibrary;

import com.m.common.AdminSMS;
import com.m.common.BooleanAndDescriptionVO;

import com.m.member.SessionManagement;

import com.m.home.Home;

import java.util.Hashtable;

import flex.messaging.FlexContext;

public class M extends SessionManagement {
	
	
	public static int nowHour = 0;
	public static Hashtable<String, String> loginIp = null;
	/*###############################
	#	home						#
	###############################*/
	public List<HashMap<String, String>> getNotices(){
		
		VbyP.accessLog(" >> �������� ��û ");
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		
		try {
			
			conn = VbyP.getDB();
			Home hm = Home.getInstance();
			al = hm.getNotices(conn);
		}catch (Exception e) {}
		finally {
			
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("getNotices >> conn.close() Exception!"); 
			}
		}
		
		return al;
	}
	
	/*###############################
	#	send Count 					#
	###############################*/
	public static HashMap<String, String> STATE = new HashMap<String, String>();
	
	public static int getState(String user_id) { return SLibrary.parseInt( M.STATE.get(user_id) ); }
	public static void setState(String user_id , int cnt) { M.STATE.put(user_id, Integer.toString(cnt)); }
	public static void removeState(String user_id) { M.STATE.remove(user_id); }
	
	/*###############################
	#	login						#
	###############################*/
	public BooleanAndDescriptionVO login(String user_id, String password) {

		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		
		try {
			
			conn = VbyP.getDB();
			if ( SLibrary.isNull(user_id) ) {
				rvo.setbResult(false);
				rvo.setstrDescription("����� ���̵� �Է��ϼ���.");
			}else if ( SLibrary.isNull(password) ) {
				rvo.setbResult(false);
				rvo.setstrDescription("��й�ȣ�� �Է��ϼ���.");
			}else {
				
				//1�ð����� ���� �����ǿ��� 5���̻� �α��� �� �� ����
				if (loginIp == null)
					loginIp = new Hashtable<String, String>();
				if (nowHour != SLibrary.parseInt( SLibrary.getDateTimeString("HH") )) {
					nowHour = SLibrary.parseInt( SLibrary.getDateTimeString("HH") );
					loginIp = new Hashtable<String, String>();
				}
				VbyP.accessLog("ip üũ �ð�:" + nowHour);
				String ip = FlexContext.getHttpRequest().getRemoteAddr();
				if (loginIp.containsKey(ip))
					loginIp.put( ip , Integer.toString( SLibrary.parseInt(loginIp.get(ip))+1) );
				else
					loginIp.put(ip, "1");
				
				VbyP.accessLog(ip + "���� "+loginIp.get(ip)+"��° "+user_id+" �α��� ==> �ð�" + nowHour);

				if (loginIp.containsKey(ip) && SLibrary.parseInt(loginIp.get(ip)) > 5) {
					
					int configCount = SLibrary.parseInt(VbyP.getValue("loginIpCountOfHour"));
					if ( configCount > 0 && SLibrary.parseInt(loginIp.get(ip)) > configCount ) {
						
						rvo.setbResult(false);
						rvo.setstrDescription("�α��� �Ͻ� �� �����ϴ�.");
						VbyP.accessLog(">>>>> IP üũ �α��ν���!!: "+ip + "���� "+loginIp.get(ip)+"��° "+user_id+" �α��� ==> �ð�" + nowHour);
					}
					
					AdminSMS asms = AdminSMS.getInstance();
					asms.sendAdmin(conn, 
							"M[IPüũ]\r\n" + ip + "���� \r\n"+loginIp.get(ip)+"�� �α��� �õ� ("+Integer.toString(nowHour)+"��)" );
				} 
				//else
				rvo = super.login(conn, user_id, password);
			}
		}catch (Exception e) {}
		finally {
			
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("login >> conn.close() Exception!"); 
			}
		}
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO logout_session() {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		String user_id = this.getSession();		
		this.session_logout();		
		if (!this.bSession()) {
			
			VbyP.accessLog(user_id+" >>"+FlexContext.getHttpRequest().getRemoteAddr()+" �α׾ƿ� ����");
			rvo.setbResult(true);
			rvo.setstrDescription("�α� �ƿ� �Ǿ����ϴ�.");
		}
		else {
			VbyP.accessLog(user_id+" >> �α׾ƿ� ����");
			rvo.setbResult(false);
			rvo.setstrDescription("�α� �ƿ� ���� �Դϴ�..");
		}
		
		return rvo;
	}
	
		
}
