package com.common.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public interface ExecuteQueryManagerAble {

	public ResultSet executeQuery() throws SQLException ;
    
    public int executeUpdate() throws SQLException,Exception;	
	
	public Vector<String[]> ExecuteQuery( int nColumnCount) throws SQLException,Exception;
	
	public String[] ExecuteQueryCols(int nColumnCount)throws SQLException , Exception;	
	
	public int ExecuteQueryNum() throws SQLException , Exception;

	public String ExecuteQueryString() throws SQLException,Exception ;
}
