package com.m.excel;

import java.util.List;
import java.util.HashMap;

public interface ExcelLoaderAble {
	
	String uploadExcelFile(byte[] bytes, String path, String fileName)  throws Exception;
	List<HashMap<String, String>> getExcelData(String path, String fileName)  throws Exception;
	
}
