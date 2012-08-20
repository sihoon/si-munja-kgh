/*
 *@(#)ReadPropertiesAble.java 1.0, 2009. 11. 5.
 *
 *Copyright(c) 2009 ehancast All rights reserved
 */
package com.common.properties;

import java.io.IOException;
import java.util.Hashtable;

public interface ReadPropertiesAble {
	
	public static final String COMMON_PROPERTIES = "common.properties";
	public static final String SQL_PROPERTIES = "sql.properties";
	public static final String FILTERING_PROPERTIES = "filtering.properties";
	public static final String HeadDir = "";
	
	public String getPath() throws NullPointerException;
	public String getValue( String key ) throws IOException , NullPointerException;
	public String getSQL( String key );
	public void setProperties(String key , String value);
	public String getPropertiesFileValue(String fileName, String key) ;
	public Hashtable<String, String> getKeys(String fileName);
	public boolean isKey(String fileName, String key);
	
}
