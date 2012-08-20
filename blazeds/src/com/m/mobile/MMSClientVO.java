package com.m.mobile;

import java.io.Serializable;

public class MMSClientVO implements Serializable {
	
	static final long serialVersionUID = 1222179582713735138L;
	
	int MSGKEY = 0;
	String SUBJECT = "";
	String PHONE = "";
	String CALLBACK = "";
	String STATUS = "";
	String REQDATE = "";
	String MSG = "";
	int FILE_CNT = 0;
	int FILE_CNT_REAL = 0;
	String FILE_PATH1 = "";
	int FILE_PATH1_SIZ = 0;
	String FILE_PATH2 = "";
	int FILE_PATH2_SIZ = 0;
	String FILE_PATH3 = "";
	int FILE_PATH3_SIZ = 0;
	String FILE_PATH4 = "";
	int FILE_PATH4_SIZ = 0;
	String FILE_PATH5 = "";
	int FILE_PATH5_SIZ = 0;
	String EXPIRETIME = "";
	String SENTDATE = "";
	String RSLTDATE = "";
	String REPORTDATE = "";
	String TERMINATEDDATE = "";
	String RSLT = "";
	String TYPE = "";
	String TELCOINFO = "";
	String ID = "";
	String POST = "";
	String ETC1 = "";
	String ETC2 = "";
	String ETC3 = "";
	int ETC4 = 0;
	
	public int getMSGKEY() {
		return MSGKEY;
	}
	public void setMSGKEY(int mSGKEY) {
		MSGKEY = mSGKEY;
	}
	public String getSUBJECT() {
		return SUBJECT;
	}
	public void setSUBJECT(String sUBJECT) {
		SUBJECT = sUBJECT;
	}
	public String getPHONE() {
		return PHONE;
	}
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
	public String getCALLBACK() {
		return CALLBACK;
	}
	public void setCALLBACK(String cALLBACK) {
		CALLBACK = cALLBACK;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getREQDATE() {
		return REQDATE;
	}
	public void setREQDATE(String rEQDATE) {
		REQDATE = rEQDATE;
	}
	public String getMSG() {
		return MSG;
	}
	public void setMSG(String mSG) {
		MSG = mSG;
	}
	public int getFILE_CNT() {
		return FILE_CNT;
	}
	public void setFILE_CNT(int fILECNT) {
		FILE_CNT = fILECNT;
	}
	public int getFILE_CNT_REAL() {
		return FILE_CNT_REAL;
	}
	public void setFILE_CNT_REAL(int fILECNTREAL) {
		FILE_CNT_REAL = fILECNTREAL;
	}
	public String getFILE_PATH1() {
		return FILE_PATH1;
	}
	public void setFILE_PATH1(String fILEPATH1) {
		FILE_PATH1 = fILEPATH1;
	}
	public int getFILE_PATH1_SIZ() {
		return FILE_PATH1_SIZ;
	}
	public void setFILE_PATH1_SIZ(int fILEPATH1SIZ) {
		FILE_PATH1_SIZ = fILEPATH1SIZ;
	}
	public String getFILE_PATH2() {
		return FILE_PATH2;
	}
	public void setFILE_PATH2(String fILEPATH2) {
		FILE_PATH2 = fILEPATH2;
	}
	public int getFILE_PATH2_SIZ() {
		return FILE_PATH2_SIZ;
	}
	public void setFILE_PATH2_SIZ(int fILEPATH2SIZ) {
		FILE_PATH2_SIZ = fILEPATH2SIZ;
	}
	public String getFILE_PATH3() {
		return FILE_PATH3;
	}
	public void setFILE_PATH3(String fILEPATH3) {
		FILE_PATH3 = fILEPATH3;
	}
	public int getFILE_PATH3_SIZ() {
		return FILE_PATH3_SIZ;
	}
	public void setFILE_PATH3_SIZ(int fILEPATH3SIZ) {
		FILE_PATH3_SIZ = fILEPATH3SIZ;
	}
	public String getFILE_PATH4() {
		return FILE_PATH4;
	}
	public void setFILE_PATH4(String fILEPATH4) {
		FILE_PATH4 = fILEPATH4;
	}
	public int getFILE_PATH4_SIZ() {
		return FILE_PATH4_SIZ;
	}
	public void setFILE_PATH4_SIZ(int fILEPATH4SIZ) {
		FILE_PATH4_SIZ = fILEPATH4SIZ;
	}
	public String getFILE_PATH5() {
		return FILE_PATH5;
	}
	public void setFILE_PATH5(String fILEPATH5) {
		FILE_PATH5 = fILEPATH5;
	}
	public int getFILE_PATH5_SIZ() {
		return FILE_PATH5_SIZ;
	}
	public void setFILE_PATH5_SIZ(int fILEPATH5SIZ) {
		FILE_PATH5_SIZ = fILEPATH5SIZ;
	}
	public String getEXPIRETIME() {
		return EXPIRETIME;
	}
	public void setEXPIRETIME(String eXPIRETIME) {
		EXPIRETIME = eXPIRETIME;
	}
	public String getSENTDATE() {
		return SENTDATE;
	}
	public void setSENTDATE(String sENTDATE) {
		SENTDATE = sENTDATE;
	}
	public String getRSLTDATE() {
		return RSLTDATE;
	}
	public void setRSLTDATE(String rSLTDATE) {
		RSLTDATE = rSLTDATE;
	}
	public String getREPORTDATE() {
		return REPORTDATE;
	}
	public void setREPORTDATE(String rEPORTDATE) {
		REPORTDATE = rEPORTDATE;
	}
	public String getTERMINATEDDATE() {
		return TERMINATEDDATE;
	}
	public void setTERMINATEDDATE(String tERMINATEDDATE) {
		TERMINATEDDATE = tERMINATEDDATE;
	}
	public String getRSLT() {
		return RSLT;
	}
	public void setRSLT(String rSLT) {
		RSLT = rSLT;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getTELCOINFO() {
		return TELCOINFO;
	}
	public void setTELCOINFO(String tELCOINFO) {
		TELCOINFO = tELCOINFO;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getPOST() {
		return POST;
	}
	public void setPOST(String pOST) {
		POST = pOST;
	}
	public String getETC1() {
		return ETC1;
	}
	public void setETC1(String eTC1) {
		ETC1 = eTC1;
	}
	public String getETC2() {
		return ETC2;
	}
	public void setETC2(String eTC2) {
		ETC2 = eTC2;
	}
	public String getETC3() {
		return ETC3;
	}
	public void setETC3(String eTC3) {
		ETC3 = eTC3;
	}
	public int getETC4() {
		return ETC4;
	}
	public void setETC4(int eTC4) {
		ETC4 = eTC4;
	}
	
}
