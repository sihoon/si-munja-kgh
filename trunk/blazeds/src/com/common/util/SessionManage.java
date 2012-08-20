/*
 *@(#)SessionMangement.java 1.0, 2009. 11. 5.
 *
 *Copyright(c) 2009 ehancast All rights reserved
 */
package com.common.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;


public class SessionManage {
	
	static SessionManage sm = null;
	
	public static SessionManage getInstance(){
		
		if (sm == null) 
			sm = new SessionManage();
		return sm;
	}
	
	public SessionManage(){}
	
	/**
	 * HashMap 데이터를 받아 key, value 쌍의 세션(HttpSession)을 생성한다.
	 * @param session
	 * @param hm
	 * @param interval
	 * @return  boolean
	 */
	public boolean create(HttpSession session, HashMap<String, String> hm, int interval) {
		
		boolean b = false;
		try {
			Set<Map.Entry<String, String>> set = hm.entrySet();
			for(Map.Entry<String, String> me : set) {
	
			  session.setAttribute(me.getKey(), me.getValue());
			}
			b = true;
			session.setMaxInactiveInterval(interval);
		}catch(Exception e) {}
		
		return b;
	}
	
	/**
	 * 사용자의 모든 session 을 HashMap<String, String>로 반환한다.
	 * @param session
	 * @return
	 */
	public HashMap<String, String> getSession(HttpSession session) {
		
		HashMap<String, String> hm = new HashMap<String, String>();
		Enumeration<String> e = session.getAttributeNames();
		
		String temp = null;
		while(e.hasMoreElements()) {
			
			temp = e.nextElement();
			hm.put(temp, (String)session.getAttribute(temp));
		}
		
		return hm;
	}
	
	/**
	 * 특정 세션값을 반환한다.
	 * @param session
	 * @param name
	 * @return
	 */
	public String getSession(HttpSession session, String name) {
		
		String rslt = (String)session.getAttribute(name);
		if (rslt != null)
			return rslt;
		else 
			return "";
	}
	
	/**
	 * 특정 세션값이 있는지 확인한다.
	 * @param session
	 * @param sessionName
	 * @return
	 */
	public boolean bCheck(HttpSession session, String sessionName) {
		
		if (session.getAttribute(sessionName) == null)
			return false;
		else
			return true;
	}
	
	/**
	 * 사용자의 세션을 삭제 한다.
	 * @param session
	 */
	public void remove(HttpSession session) {
		
		session.invalidate();
	}
}
