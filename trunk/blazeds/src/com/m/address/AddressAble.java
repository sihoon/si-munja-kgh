package com.m.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Connection;

public interface AddressAble {
	
	/**group process**/
	int InsertGroup(Connection conn , String user_id , String groupName);//create group
	int UpdateGroup(Connection conn , String grp , String user_id , String modifyGroupName);//modify group
	int DeleteGroup(Connection conn , String grp , String user_id);//delete group
	String[] SelectGroup(Connection conn , String user_id);//select group list	
	
	/**member process**/
	int InsertMember(Connection conn , AddressVO vo);//create member
	int InsertMember(Connection conn , String user_id, ArrayList<AddressVO> al);//create member many
	int UpdateMember(Connection conn , int idx , AddressVO vo);//modify member
	int UpdateGroupMember(Connection conn , String grp, String user_id, String modifyGroupName);
	int DeleteMember(Connection conn , int idx , String user_id);//delete member
	int DeleteMember(Connection conn , int[] idx , String user_id);//delete member many
	int DeleteGroupMember(Connection conn , String grp, String user_id);
	int MoveGroup(Connection conn , int[] idx , String user_id , int moveGroupIdx);//move member
	ArrayList<HashMap<String, String>> SelectMember(Connection conn , String user_id , String grp);//select member list
	ArrayList<HashMap<String, String>> SearchMember(Connection conn , String user_id, String grp);
	
}
