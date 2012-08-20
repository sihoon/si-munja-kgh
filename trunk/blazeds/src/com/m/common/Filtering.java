package com.m.common;

import com.common.VbyP;
import com.common.properties.ReadProperties;
import com.common.properties.ReadPropertiesAble;
import com.common.util.SLibrary;

/**
 * spam.properties 에서 유효성 체크
 *  1. 메시지 필터
 *  2. 접근 IP 체크
 * @author si
 *
 */
public class Filtering {
	
	public static final String FILTER_FILE="filtering.properties";
	public static final String FILTER_MESSAG_KEY="filteringMessage";
	public static final String FILTER_GLOBAL_MESSAG_KEY="globalFilteringMessage";
	public static final String NOFILTER_ID_KEY="noCheckId";
	public static final String NOIP_KEY="noIp4";
	public static final String DIV="\\|\\|";
	
	public static String messageFiltering(String user_id , String message) {
		
		if ( Filtering.isCheckID(user_id) ) {
			
			ReadPropertiesAble rp = ReadProperties.getInstance();
			
			String filterMessages = rp.getPropertiesFileValue(FILTER_FILE, FILTER_MESSAG_KEY);
			String[] arrDecode = VbyP.getDecodeFromProperties();
			
			try { filterMessages = new String(filterMessages.getBytes(arrDecode[0]), arrDecode[1]);}
			catch(Exception e) {System.out.println("messageFiltering>>>>"+e.toString());}
			
			String[] arrFilterMessage = filterMessages.split(DIV);
			String filterString = null;
						
			for (int i = 0; i < arrFilterMessage.length; i++) {
				
				if ( SLibrary.search(message, arrFilterMessage[i]) > 0 ) {
					filterString = arrFilterMessage[i];
					break;
				}
			}
			return filterString;
		}else
			return null;
	}
	
	public static String globalMessageFiltering( String message) {
		
			
		ReadPropertiesAble rp = ReadProperties.getInstance();
		
		String filterMessages = rp.getPropertiesFileValue(FILTER_FILE, FILTER_GLOBAL_MESSAG_KEY);
		
		try { filterMessages = new String(filterMessages.getBytes("ISO-8859-1"), "UTF-8");}
		catch(Exception e) {System.out.println("globalMessageFiltering>>>>"+e.toString());}
		
		String[] arrFilterMessage = filterMessages.split(DIV);
		String filterString = null;
					
		for (int i = 0; i < arrFilterMessage.length; i++) {
			
			if ( SLibrary.search(message, arrFilterMessage[i]) > 0 ) {
				filterString = arrFilterMessage[i];
				break;
			}
		}
		return filterString;
	}
	
	public static String ipFiltering(String user_id , String ip) {
		
		if ( Filtering.isCheckID(user_id) ) {
			
			ReadPropertiesAble rp = ReadProperties.getInstance();
			
			String filterIps = rp.getPropertiesFileValue(FILTER_FILE, NOIP_KEY);
			String[] arrFilterIps = (filterIps != null)?filterIps.split(DIV):null;
			String filterIp = null;
			
			for (int i = 0; i < arrFilterIps.length; i++) {
				
				if ( !SLibrary.isNull(arrFilterIps[i]) && SLibrary.search(ip, arrFilterIps[i]) > 0 ) {
					filterIp = arrFilterIps[i];
					break;
				}
			}
			
			return filterIp;
		}else
			return null;
	}
	
	public static boolean isCheckID(String user_id) {
		
		ReadPropertiesAble rp = ReadProperties.getInstance();
		
		String noFilterIDs = rp.getPropertiesFileValue(FILTER_FILE, NOFILTER_ID_KEY);
		String[] arrNoFilterIDs = noFilterIDs.split(DIV);
		boolean isCheck = true;
		
		for (int i = 0; i < arrNoFilterIDs.length; i++) {
			
			if ( user_id.equals(arrNoFilterIDs[i]) ) {
				isCheck = false;
				break;
			}
		}
		
		return isCheck;
	}
}
