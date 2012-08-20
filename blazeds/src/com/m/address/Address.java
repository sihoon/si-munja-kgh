package com.m.address;

import java.text.MessageFormat;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.common.db.PreparedExecuteQueryManager;
import com.common.util.SLibrary;
import com.common.VbyP;
import com.m.address.AddressVO;
import com.m.address.AddressAble;

public class Address implements AddressAble {

	static Address address = new Address();
	Address(){}
	public static Address getInstance(){
		return address;
	}
	
	final String SELECT_LOG_IDX_QUERY_KEY = "";
	final String DELETE_GROUP_QUERY_KEY = "ADDRESS_DELETE_GROUP";
	final String DELETE_GROUP_MEMBER_QUERY_KEY = "ADDRESS_DELETE_GROUP_MEMBER";
	final String DELETE_MEMBER_QUERY_KEY = "ADDRESS_DELETE_MEMBER";
	final String INSERT_GROUP_QUERY_KEY = "ADDRESS_INSERT_GROUP";
	final String INSERT_MEMBER_QUERY_KEY = "ADDRESS_INSERT_MEMBER";
	final String MOVE_MEMBER_QUERY_KEY = "ADDRESS_MOVE_UPDATE_MEMBER";
	final String MOVE_MEMBER_POST_QUERY_KEY = "POSTADDRESS_MOVE_INSERT_MEMBER";
	final String SELECT_MEMBER_QUERY_KEY = "ADDRESS_SELECT_MEMBER";
	final String SELECT_ALL_LIST_QUERY_KEY = "ADDRESS_SELECT_ALL_MEMBER";
	final String SELECT_GROUP_LIST_QUERY_KEY = "ADDRESS_GROUP_INFO";
	final String SELECT_GROUP_MEMBER_QUERY_KEY = "ADDRESS_MEMBER_LIST";
	final String SELECT_GROUP_MEMBER_SEARCH_QUERY_KEY = "ADDRESS_MEMBER_SEARCH_LIST";
	final String SELECT_GROUP_MEMBER_QUERY_FORMAT_KEY = "ADDRESS_MEMBER_FORMAT_LIST";	
	final String UPDATE_GROUP_QUERY_KEY = "ADDRESS_UPDATE_GROUP";
	final String UPDATE_GROUP_MEMBER = "ADDRESS_UPDATE_GROUP_MEMBER";
	final String UPDATE_MEMBER_QUERY_KEY = "ADDRESS_UPDATE_MEMBER";
	final String SELECT_GROUP_IDX_QUERY_KEY = "ADDRESS_SELECT_GROUP_IDX";
	final String SELECT_SENDLIST_OF_GROUP = "ADDRESS_SENDLIST_OF_GROUP";
	final String SELECT_SENDLIST_OF_MEMBER = "ADDRESS_SENDLIST_OF_MEMBER";
	final String SELECT_SAVE_OF_GROUP = "ADDRESS_SAVE_OF_GROUP";
	final String SELECT_SAVE_OF_MEMBER = "ADDRESS_SAVE_OF_MEMBER";
	final String SELECT_GROUP_NAME_QUERY_KEY = "ADDRESS_SELECT_GROUP_NAME";
	final String SELECT_DUPLICATION_GROUP = "ADDRESS_SELECT_DUPLICATION";
	
	
	
	public int DeleteGroup(Connection conn , String grp, String user_id) {
		
		int rsltCount = 0;
		String SQL = VbyP.getSQL("ADDRESS_DELETE_GROUP");
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setString(2, grp);
		
		
		rsltCount = pq.executeUpdate();			
		
		return rsltCount;
	}
	
	public int DeleteGroupMember(Connection conn , String grp, String user_id) {

		
		int rsltCount = 0;
		String SQL = VbyP.getSQL(DELETE_GROUP_MEMBER_QUERY_KEY);
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setString(2, grp);
		rsltCount = pq.executeUpdate();			
		
		return rsltCount;
	}

	public int DeleteMember(Connection conn , int idx, String user_id) {

		
		int rsltCount = 0;
		String SQL = VbyP.getSQL("ADDRESS_DELETE_MEMBER");
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
		pq.setPrepared(conn, SQL);		
		pq.setString(1, user_id);
		pq.setInt(2, idx);
		rsltCount = pq.executeUpdate();		
		
		return rsltCount;
	}

	public int DeleteMember(Connection conn , int[] idx, String user_id) {

		int rsltCount = 0;
		String SQL = VbyP.getSQL(DELETE_MEMBER_QUERY_KEY);
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
		pq.setPrepared(conn, SQL);
		
		if (idx.length > 0) {
			
			int cnt = idx.length;
			for (int i = 0; i < cnt; i++) {
				pq.setInt(1, idx[i]);
				pq.setString(2, user_id);
				pq.addBatch();
			}
			
			rsltCount = pq.executeBatch();
		}	
		return rsltCount;
	}

	public int InsertGroup(Connection conn , String user_id, String groupName) {

		
		int rsltCount = 0;
		String SQL = VbyP.getSQL("ADDRESS_INSERT_GROUP");
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setInt(2, AddressVO.GROUP_FLAG);
		pq.setString(3, groupName);
		pq.setString(4, groupName);
		pq.setString(5, "");
		pq.setString(6, SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));
		
		rsltCount = pq.executeUpdate();			
		
		return rsltCount;
	}

	public int InsertMember(Connection conn, AddressVO vo) {

		
		int rsltCount = 0;
		String SQL = VbyP.getSQL("ADDRESS_INSERT_MEMBER");
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, vo.getUser_id());
		pq.setInt(2, vo.getGrp());
		pq.setString(3, vo.getGrpName());
		pq.setString(4, vo.getName());
		pq.setString(5, vo.getPhone());
		pq.setString(6, vo.getMemo());
		pq.setString(7, SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));
		
		
		rsltCount = pq.executeUpdate();			
		
		return rsltCount;
	}
	
	public int InsertMember(Connection conn , String user_id , ArrayList<AddressVO> al) {

		int rsltCount = 0;
		int count = al.size();
		int maxBatch = SLibrary.parseInt( VbyP.getValue("executeBatchCount") );
		
		if ( count > 0 ) {
			
			String SQL = VbyP.getSQL("ADDRESS_INSERT_MEMBER");
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared(conn, SQL);
			
			AddressVO vo = null;
			for (int i = 0; i < count; i++){
				
				vo = null;
				vo = (AddressVO)al.get(i);
				
				vo.phone = SLibrary.getPhone(vo.phone);
				
				if (vo.phone != null) {
					InsertMember(pq , user_id , vo);
					pq.addBatch();
					
					if (i >= maxBatch && (i%maxBatch) == 0 ) {
						
						rsltCount += pq.executeBatch();
						
						try { if ( conn != null ) conn.close(); } 
						catch(Exception e) {System.out.println( "InsertMember : conn close Error!!!!" + e.toString());}
						
						conn = VbyP.getDB();					
						if (conn != null) System.out.println("InsertMember : conn connection!!!!");
						pq.setPrepared(conn, SQL);
					}
				}
			}
			rsltCount += pq.executeBatch();
			
		}
		
		return rsltCount;
	}
	
	private void InsertMember(PreparedExecuteQueryManager linkpq , String user_id , AddressVO vo) {
			
			linkpq.setString(1, user_id);
			linkpq.setInt(2, AddressVO.ADDRESS_FLAG);
			linkpq.setString(3, vo.getGrpName());
			linkpq.setString(4, (vo.getName()==null)?"":SLibrary.replaceAll(vo.getName(), "'", "") );
			linkpq.setString(5, vo.getPhone());
			linkpq.setString(6, vo.getMemo());
			linkpq.setString(7, SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));
	}

	public int MoveGroup(Connection conn , int[] idx, String user_id, int moveGroupIdx) {

		int rsltCount = 0;
		int count = idx.length;
		
		if ( count > 0 ) {
			
			String SQL = VbyP.getSQL(MOVE_MEMBER_QUERY_KEY);
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared(conn, SQL);
			
			for (int i = 0; i < count; i++){
				
				pq.setInt(1, moveGroupIdx);
				pq.setInt(2, idx[i]);
				pq.setString(3, user_id);
				pq.addBatch();
			}
			rsltCount = pq.executeBatch();
			
		}
		
		return rsltCount;
	}
	
	public int MoveGroupPost(Connection conn , int[] idx, String dept_code, int moveGroupIdx) {

		int rsltCount = 0;
		int count = idx.length;
		
		if ( count > 0 ) {
			
			String SQL = SLibrary.messageFormat(VbyP.getSQL(MOVE_MEMBER_POST_QUERY_KEY),new Object[]{dept_code ,Integer.toString(moveGroupIdx)});
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared(conn, SQL);
			
			for (int i = 0; i < count; i++){
				
				pq.setInt(1, idx[i]);
				pq.addBatch();
			}
			rsltCount = pq.executeBatch();
			
		}
		
		return rsltCount;
	}
	
	public StringBuffer SelectTreeData(Connection conn, String user_id) {
		
		StringBuffer buf = null;
		
		ArrayList<HashMap<String, String>> al = null;
		
		String SQL = VbyP.getSQL( "ADDRESS_SELECT" );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		al = pq.ExecuteQueryArrayList();
		
		buf = makeXML(al);
		
		
		return buf;
	}

	private StringBuffer makeXML(ArrayList<HashMap<String, String>> al) {
		
		StringBuffer buf = new StringBuffer();
		int cnt = al.size();
		HashMap<String, String> hm = null;
		boolean bAddrsOpen = false;
		//buf.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
		buf.append("<root>");
		buf.append("<msg>ok</msg>");
		
		for (int i = 0; i < cnt; i++) {
			
			hm = al.get(i);
			
			if (SLibrary.IfNull(hm, "grp").equals("0") && bAddrsOpen == true) {
				buf.append("</addrs>");
				bAddrsOpen = false;
			}
			if (SLibrary.IfNull(hm, "grp").equals("0")) {
				buf.append("<addrs idx='0' type='group' label='"+SLibrary.IfNull(hm, "grpName")+"'>");
				bAddrsOpen = true;
			}
			
			if (SLibrary.IfNull(hm, "grp").equals("1")) {
				buf.append("<addr idx='"+SLibrary.IfNull(hm, "idx")+"' group='"+SLibrary.IfNull(hm, "grpName")+"' label='"+SLibrary.IfNull(hm, "name")+"' phone='"+SLibrary.IfNull(hm, "phone")+"' memo='"+SLibrary.IfNull(hm, "memo")+"' />");
			}
			
		}
		if (bAddrsOpen == true) {
			buf.append("</addrs>");
			bAddrsOpen = false;
		}
		buf.append("</root>");
		
		return buf;
		
	}
	public String[] SelectGroup(Connection conn , String user_id) {

		String[] arr = null;
		
		String SQL = VbyP.getSQL( "ADDRESS_GROUP_INFO" );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setInt(2, AddressVO.GROUP_FLAG);
		arr = pq.ExecuteQuery();
		return arr;
	}
	
	public int SelectGroupIdx(Connection conn , String user_id , String groupName) {
		int idx = 0;
		
		String SQL = VbyP.getSQL( SELECT_GROUP_IDX_QUERY_KEY );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setString(2, groupName);
		idx = pq.ExecuteQueryNum();
		return idx;
	}
	
	public String SelectGroupName(Connection conn , String user_id , int key) {
		String name = "";
		
		String SQL = VbyP.getSQL( SELECT_GROUP_NAME_QUERY_KEY );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setInt(2, key);
		name = pq.ExecuteQueryString();
		return name;
	}

	public ArrayList<HashMap<String, String>> SelectMember(Connection conn , String user_id, String grp) {

		ArrayList<HashMap<String, String>> al = null;
		
		String SQL = VbyP.getSQL( SELECT_GROUP_MEMBER_QUERY_KEY );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setString(2, grp);
		al = pq.ExecuteQueryArrayList();
		return al;
	}
	
	public ArrayList<HashMap<String, String>> SearchMember(Connection conn , String user_id, String search) {

		ArrayList<HashMap<String, String>> al = null;
		
		String SQL = VbyP.getSQL( SELECT_GROUP_MEMBER_SEARCH_QUERY_KEY );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setInt(2, AddressVO.ADDRESS_FLAG);
		pq.setString(3, "%"+search+"%");
		pq.setString(4, "%"+search+"%");
		al = pq.ExecuteQueryArrayList();
		return al;
	}

	
	//전체
	public ArrayList<HashMap<String, String>> SelectMember(Connection conn , String user_id) {

		ArrayList<HashMap<String, String>> al = null;
		
		String SQL = VbyP.getSQL( SELECT_ALL_LIST_QUERY_KEY );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setInt(2, AddressVO.ADDRESS_FLAG);
		al = pq.ExecuteQueryArrayList();
		return al;
	}
	
	
	public ArrayList<HashMap<String, String>> SelectMember(Connection conn , String user_id, int groupIdx , int startPage , int endPage) {

		ArrayList<HashMap<String, String>> al = null;
		
		String SQL = SLibrary.pgString( VbyP.getSQL( SELECT_GROUP_MEMBER_QUERY_KEY ) );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setInt(2, groupIdx);
		pq.setInt(3, startPage);
		pq.setInt(4, endPage);
		al = pq.ExecuteQueryArrayList();
		return al;
	}
	public int SelectMemberCount(Connection conn , String user_id, int groupIdx) {

		int rslt = 0;
		
		String SQL = this.countQueryString( VbyP.getSQL( SELECT_GROUP_MEMBER_QUERY_KEY ) );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setInt(2, groupIdx);
		rslt = pq.ExecuteQueryNum();
		return rslt;
	}
	
	//전체
	public ArrayList<HashMap<String, String>> SelectMember(Connection conn , String user_id , int startPage , int endPage) {

		ArrayList<HashMap<String, String>> al = null;
		
		String SQL = SLibrary.pgString( VbyP.getSQL( SELECT_ALL_LIST_QUERY_KEY ) );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setInt(2, startPage);
		pq.setInt(3, endPage);
		al = pq.ExecuteQueryArrayList();
		return al;
	}
	public int SelectMemberCount(Connection conn , String user_id) {

		int rslt = 0;
		
		String SQL = this.countQueryString( VbyP.getSQL( SELECT_ALL_LIST_QUERY_KEY ) );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		rslt = pq.ExecuteQueryNum();
		return rslt;
	}
	
	public ArrayList<HashMap<String, String>> SelectMember(Connection conn , String user_id, String whereQuery , int startPage , int endPage) {

		ArrayList<HashMap<String, String>> al = null;
		
		String SQL = SLibrary.pgString( this.messageFormat( VbyP.getSQL( SELECT_GROUP_MEMBER_QUERY_FORMAT_KEY ) , new Object[]{whereQuery} ) );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setInt(2, startPage);
		pq.setInt(3, endPage);
		al = pq.ExecuteQueryArrayList();
		return al;
	}
	public int SelectMemberCount(Connection conn , String user_id, String whereQuery) {

		int rslt = 0;
		
		String SQL = this.countQueryString( this.messageFormat( VbyP.getSQL( SELECT_GROUP_MEMBER_QUERY_FORMAT_KEY ) , new Object[]{whereQuery} ) );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		rslt = pq.ExecuteQueryNum();
		return rslt;
	}
	
	public ArrayList<HashMap<String, String>> SelectMemberData(Connection conn , String user_id, int idx) {

		ArrayList<HashMap<String, String>> al = null;
		
		String SQL = VbyP.getSQL( SELECT_MEMBER_QUERY_KEY );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setInt(2, idx);
		al = pq.ExecuteQueryArrayList();
		return al;
	}

	public int UpdateGroup(Connection conn , String grp, String user_id, String modifyGroupName) {

		int rslt = 0;
		
		String SQL = VbyP.getSQL( "ADDRESS_UPDATE_GROUP" );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, modifyGroupName);
		pq.setString(2, user_id);
		pq.setString(3, grp);
		
		rslt = pq.executeUpdate();
		return rslt;
	}
	
	public int UpdateGroupMember(Connection conn , String grp, String user_id, String modifyGroupName) {

		int rslt = 0;
		
		String SQL = VbyP.getSQL( UPDATE_GROUP_MEMBER );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, modifyGroupName);
		pq.setString(2, user_id);
		pq.setString(3, grp);
		
		rslt = pq.executeUpdate();
		return rslt;
	}

	public int UpdateMember(Connection conn , int idx, AddressVO vo) {

		int rslt = 0;
 
		String SQL = VbyP.getSQL( "ADDRESS_UPDATE_MEMBER" );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		
		pq.setString(1, vo.getGrpName());
		pq.setString(2, vo.getName());
		pq.setString(3, vo.getPhone());
		pq.setString(4, vo.getMemo());
		pq.setString(5, SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));		
		pq.setString(6, vo.getUser_id());
		pq.setInt(7, vo.getIdx());
		
		rslt = pq.executeUpdate();
		return rslt;
	}
	
	/**
	 * 해당 그룹의 column을 반환한다.
	 * @param conn - DB Connection
	 * @param strColumns - column 명
	 * @param user_id - 사용자 ID
	 * @param groupIdx - 그룹 IDX
	 * @return arrayList 
	 */
	public ArrayList<HashMap<String, String>> getSendListOfGroup( Connection conn , String strColumns , String user_id , String groupIdx) {
		
		ArrayList<HashMap<String, String>> al = null;
		
		String SQL =  this.messageFormat( VbyP.getSQL( SELECT_SENDLIST_OF_GROUP ), new Object[]{strColumns , groupIdx} );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		al = pq.ExecuteQueryArrayList();
		return al;
	}
	
	/**
	 * 해당 IDX의 column을 반환한다.
	 * @param conn - DB Connection
	 * @param strColumns - column 명
	 * @param user_id - 사용자 ID
	 * @param groupIdx - 그룹 IDX
	 * @return arrayList 
	 */
	public ArrayList<HashMap<String, String>> getSendListOfMember( Connection conn , String strColumns , String user_id , String idxInQuery , String notInGroupQuery ) {
		
		ArrayList<HashMap<String, String>> al = null;
		
		String SQL =  this.messageFormat( VbyP.getSQL( SELECT_SENDLIST_OF_MEMBER ), new Object[]{strColumns,idxInQuery,notInGroupQuery} );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		al = pq.ExecuteQueryArrayList();
		return al;
	}
	
	/**
	 * 해당 그룹의 column을 반환한다.
	 * @param conn - DB Connection
	 * @param strColumns - column 명
	 * @param user_id - 사용자 ID
	 * @param groupIdx - 그룹 IDX
	 * @return arrayList 
	 */
	public ArrayList<HashMap<String, String>> getSaveOfGroup( Connection conn , String strColumns , String user_id , String groupIdx) {
		
		ArrayList<HashMap<String, String>> al = null;
		
		String SQL =  this.messageFormat( VbyP.getSQL( SELECT_SAVE_OF_GROUP ), new Object[]{strColumns , groupIdx} );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		al = pq.ExecuteQueryArrayList();
		return al;
	}
	
	/**
	 * 해당 IDX의 column을 반환한다.
	 * @param conn - DB Connection
	 * @param strColumns - column 명
	 * @param user_id - 사용자 ID
	 * @param groupIdx - 그룹 IDX
	 * @return arrayList 
	 */
	public ArrayList<HashMap<String, String>> getSaveOfMember( Connection conn , String strColumns , String user_id , String idxInQuery , String notInGroupQuery ) {
		
		ArrayList<HashMap<String, String>> al = null;
		
		String SQL =  this.messageFormat( VbyP.getSQL( SELECT_SAVE_OF_MEMBER ), new Object[]{strColumns,idxInQuery,notInGroupQuery} );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		al = pq.ExecuteQueryArrayList();
		return al;
	}
	
	public boolean checkDuplicationGroup(Connection conn, String user_id, String groupName) {
		
		String SQL = VbyP.getSQL( "ADDRESS_SELECT_DUPLICATION" );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setString(2, (groupName == null)?"":groupName);
		int cnt = pq.ExecuteQueryNum();
		
		if (cnt > 0)
			return true;
		else
			return false;
	}
	
	
	/**
	 * 전체건수 쿼리 문자열을 반환한다.
	 */
	private String countQueryString(String sql){
		
		String preString = "SELECT count(*) as cnt FROM (  ";
		String lastString = " ) ";
		
		return preString + sql + lastString;
	}
	
	/**
	 * 문자열에 Object배열을 Format하여 반환한다.
	 * */
	public String messageFormat(String pattern , Object[] obj) {
		
		return MessageFormat.format(pattern, obj);
	}

}
