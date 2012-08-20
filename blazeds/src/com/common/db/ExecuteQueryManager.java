/**
 *  Class Name, 클래스 이름
 *  Class Description, 클래스 설명
 *  Version Information, 버전 정보
 *  Make date, 작성일
 *  Author, 작성자
 *  Modify lists, 수정내역
 *  Copyright, 저작권 정보
 */
package com.common.db;

import java.util.Vector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import com.common.VbyP;

/**
 * mathod to change the owner name
 * 
 * @param filename
 *            변경하고자 하는 새로운 Owner명을 parameter로 한다.
 * @return return되는 값은 없습니다.
 * @see #ConnectionPool
 * @see #ExecuteQueryManager
 */
public class ExecuteQueryManager {


	/**
	 * update insert Query 처리 객체
	 * 
	 * @param sql -
	 *            Query
	 * @return true : 성공
	 */
	public boolean ExecuteUpdate(String sql) {
		boolean bCheck = false;

		Connection conn = VbyP.getDB();

		try {

			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			bCheck = true;
			stmt.close();
		} catch (Exception e) {
			
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>2" + sql);
			bCheck = false;
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ es.toString() + "=>2" + sql);
				bCheck = false;
			}			
		}

		return bCheck;
	}

	/**
	 * 메게 변수 connection을 통해 update insert Query 처리
	 * 
	 * @param conn -
	 *            Connection
	 * @param sql -
	 *            query
	 * @return true : 성공
	 */
	public boolean ExecuteUpdateConn(Connection conn, String sql) {
		boolean bCheck = false;
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			try {
				bCheck = true;
				stmt.close();
			} catch (Exception e) {
				VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
			}
		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		return bCheck;
	}

	/**
	 * 매게변수 sql 을 통해 nColumnCount 수 만큼의 문자열 배열을 Vector로 반환
	 * 
	 * @param sql -
	 *            Query
	 * @param nColumncount -
	 *            검색 칼럼 수
	 * @return Vector -> String[nColumncount]
	 */
	public Vector<String[]> ExecuteQuery(String sql, int nColumnCount) {
		Vector<String[]> vtList = new Vector<String[]>();
		Connection conn = VbyP.getDB();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			String[] strTemp = null; // 검색 내용 임시 저장소
			int i;
			int j;

			while (rs.next()) {
				strTemp = new String[nColumnCount];

				for (i = 0; i < nColumnCount; i++) {
					j = i + 1;
					strTemp[i] = rs.getString(j);
					if (strTemp[i] == null || strTemp[i].equals("null")) strTemp[i] = "";
				}

				vtList.add(strTemp);
			}
			
			rs.close();
			stmt.close();
		} catch (Exception e) {
			
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ es.toString() + "=>2" + sql);
			}
		}

		return vtList;
	}
	
	

	/**
	 * 매게변수 sql 을 통해 nColumnCount 수 만큼의 문자열 배열을 Vector로 반환
	 * 
	 * @param sql -
	 *            Query
	 * @param nColumncount -
	 *            검색 칼럼 수
	 * @param conn -
	 *            DB Connection
	 * @return Vector -> String[nColumncount]
	 */
	public Vector<String[]> ExecuteQuery(String sql, int nColumnCount, Connection conn) {
		
		Vector<String[]> vtList = new Vector<String[]>();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			String[] strTemp = null; // 검색 내용 임시 저장소
			int i;
			int j;

			while (rs.next()) {
				strTemp = new String[nColumnCount];

				for (i = 0; i < nColumnCount; i++) {
					j = i + 1;
					strTemp[i] = rs.getString(j);
					if (strTemp[i] == null || strTemp[i].equals("null")) strTemp[i] = "";
				}

				vtList.add(strTemp);
			}
			
			rs.close();
			stmt.close();
			
		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		return vtList;
	}

	/**
	 * 매게변수 sql 을 통해 nColumnCount 수 만큼의 문자열 배열을 반환
	 * 
	 * @param sql -
	 *            Query
	 * @param nColumncount -
	 *            검색 칼럼 수
	 * @return String[nColumncount]
	 */
	public String[] ExecuteQueryCols(String sql, int nColumnCount) {
		
		String[] strTemp = new String[nColumnCount]; // 검색 내용 저장소
		Connection conn = VbyP.getDB();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			
			rs = stmt.executeQuery(sql);

			int i;
			int j;

			while (rs.next()) {
				for (i = 0; i < nColumnCount; i++) {
					j = i + 1;
					strTemp[i] = rs.getString(j);
					if (strTemp[i] == null || strTemp[i].equals("null")) strTemp[i] = "";
				}
			}
			
			rs.close();
			stmt.close();

		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ es.toString() + "=>2" + sql);
			}
		}

		return strTemp;
	}

	/**
	 * sql 결과를 문자열 배열로 반환
	 * 
	 * @param sql -
	 *            Query
	 * @return String[]
	 */
	public String[] ExecuteQuery(String sql) {
		
		String[] strTemp = null; // 검색 내용 임시 저장소
		Connection conn = VbyP.getDB();
		try {

			Statement stmt = conn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			// 첫번째 매새변수는 쿼리문, 두번째는 커서이동여부, 세번째는 커서가 가리키는 행의 수정가능여부

			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			int i = 0;
			rs.last();
			strTemp = new String[rs.getRow()];
			rs.beforeFirst();

			while (rs.next()) {
				strTemp[i] = rs.getString(1);
				if (strTemp[i] == null || strTemp[i].equals("null")) strTemp[i] = "";
				i++;
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "22222=>" + sql);
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ es.toString() + "=>2" + sql);
			}
		}

		return strTemp;
	}

	/**
	 * 쿼리 결과 숫자 반환
	 * 
	 * @param sql -
	 *            Query
	 * @return int
	 */
	public int ExecuteQueryNum(String sql) {
		
		int num = 0;
		Connection conn = VbyP.getDB();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				num = rs.getInt(1);
			}

			rs.close();
			stmt.close();

		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ es.toString() + "=>2" + sql);
			}
		}
		return num;
	}

	/**
	 * 쿼리 결과 숫자 반환
	 * 
	 * @param sql -
	 *            Query
	 * @return int
	 */
	public int ExecuteQueryNum(String sql, Connection conn) {
		
		int num = 0;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				num = rs.getInt(1);
			}

			rs.close();
			stmt.close();
			
		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		
		return num;
	}

	/**
	 * 쿼리 결과 문자열 반환
	 * 
	 * @param sql -
	 *            Query
	 * @return String
	 */
	public String ExecuteQueryString(String sql) {
		
		String str = null;
		Connection conn = VbyP.getDB();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				
				str = rs.getString(1);
			}

			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ "conn.close() Error !! "
						+ es.toString());
			}

		}
		if (str == null || str.equals("null")) str = "";
		return str;
	}

	/**
	 * 쿼리 결과 문자열 반환
	 * 
	 * @param sql -
	 *            Query
	 * @return String
	 */
	public String ExecuteQueryString(String sql, Connection conn) {
		
		String str = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				str = rs.getString(1);
			}
			
			rs.close();
			stmt.close();

		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		if (str == null || str.equals("") || str.equals("null")) str = "";
		return str;
	}

	/**
	 * 쿼리 결과 문자열 반환
	 * 
	 * @param sql -
	 *            Query
	 * @return String
	 */
	public String ExecuteQueryStringConn(String sql, Connection conn) {
		
		String str = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				str = rs.getString(1);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		if (str == null || str.equals("null")) str = "";
		return str;
	}
};
