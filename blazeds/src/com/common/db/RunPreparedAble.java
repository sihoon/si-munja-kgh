package com.common.db;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.io.StringReader;


public interface RunPreparedAble {
	
	public void setPrepared(String sql);
	
	public void setPrepared(String strConn , String sql);
	
    public void setPrepared(Connection conn , String sql);
    
    public void setNull(int index, int sqlType) throws SQLException;
    
    public void setBoolean(int index, boolean x) throws SQLException;
    
    public void setByte(int index, byte x) throws SQLException ;
    
    public void setShort(int index, short x) throws SQLException;
    
    public void setInt(int index, int x) throws SQLException;
    
    public void setLong(int index, long x) throws SQLException;
    
    public void setFloat(int index, float x) throws SQLException ;
    
    public void setDouble(int index, double x) throws SQLException ;
    
    public void setBigDecimal(int index, BigDecimal x) throws SQLException ;
    
    public void setString(int index, String x) throws SQLException ;
    
    public void setBytes(int index, byte x[]) throws SQLException ;
    
    public void setDate(int index, Date x) throws SQLException ;
    
    public void setTime(int index, Time x) throws SQLException ;
    
    public void setTimestamp(int index, Timestamp x) throws SQLException;
    
    public void setAsciiStream(int index, InputStream x, int length) throws SQLException;
    
    public void setBinaryStream(int index, InputStream x, int length) throws SQLException;
    
    public void clearParameters() throws SQLException;
    
    public void setObject(int index, Object x, int target, int scale) throws SQLException;
    
    public void setObject(int index, Object x, int target) throws SQLException;
    
    public void setObject(int index, Object x) throws SQLException;
    
    public void setCharacterStream(int index, StringReader sr, int length) throws SQLException; 
    
    public void close() throws Exception;
}
