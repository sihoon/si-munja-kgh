/**
 *  Class Name, ConnectionFactor
 *  Class Description, DB연결 객체를 반환 한다.
 *  Version Information, 0.1
 *  Make date, 2008.07.08
 *  Author, si hoon park
 *  Modify lists, 
 *  Copyright, si crap
 */
package com.common.db;

import java.sql.Connection;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.Context;

public class ConnectionFactory {

	static ConnectionFactory cf = null;
	
	public static ConnectionFactory getInstance(){
		
		if (cf == null) 
			cf = new ConnectionFactory();
		return cf;
	}
	/**
	 * Weblogic -매개변수 datasource 를 통해 Connection 을 반환
	 * @param datasource - 연결 구분
	 * @return Connection
	 */
	public Connection getConnectionWeblogic(String datasource) throws  Exception{

		Connection conn = null;
		DataSource ds = null;
		Context initContext = new InitialContext();
		ds = (DataSource) initContext.lookup(datasource);
		conn = ds.getConnection();
		return conn;
	}
	
	/**
	 * Tomcat -매개변수 datasource 를 통해 Connection 을 반환
	 * @param datasource - 연결 구분
	 * @return Connection
	 */
	public Connection getConnectionTomcat(String jndi , String datasource)  throws  Exception{

		Connection conn = null;
		DataSource ds = null;

		Context initContext = new InitialContext();			
		Context envContext = (Context) initContext.lookup(jndi);
		ds = (DataSource) envContext.lookup(datasource);
		conn = ds.getConnection();
		
		return conn;
	}

}