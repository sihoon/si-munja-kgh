package com.m.excel;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import com.common.util.ExcelManagerByPOI36;
import com.m.common.FileUtils;


public class ExcelLoader extends ExcelManagerByPOI36  implements ExcelLoaderAble {

	@Override
	public String uploadExcelFile(byte[] bytes, String path, String fileName) throws Exception  {
		

		FileUtils fu = new FileUtils();
		return fu.doUploadRename(bytes, path, fileName);
	}

	@Override
	public List<HashMap<String, String>> getExcelData(String path, String fileName) throws Exception {
		

		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		String[][] rslt = null;
		int countMaxColumn = 0;
		int rowCount = 0;
		HashMap<String, String> hm = null;
		
		rslt = super.Read(path + fileName);
		countMaxColumn = super.getmaxCountColumn();
		
		rowCount = rslt.length;
		for (int i = 0; i < rowCount; i++) {
			
			hm = new HashMap<String, String>();
			hm.put("/", Integer.toString(i+1) );
			for (int j = 0; j < countMaxColumn; j++) {
				
				hm.put( this.getExcelColumnTitle(j+1), ( j >= rslt[i].length  )?"":rslt[i][j] );
			}
			
			al.add(hm);
		}
		
		
		return al;
	}
	
	private String getExcelColumnTitle(int index) {
		
		int base = (int)(char)'A';
		int div = (int)(char)'Z' - base +1;		
		StringBuffer buf = new StringBuffer();
		
		if ( (index-1) >= 0 ){
			
			//twoLength String
			if ( index-1 >= div ) {
				
				buf.append( new Character( (char)(base + (int)( (index-1)/div ) -1) ).toString() );
				buf.append( new Character( (char)(base + (int)( (index-1)%div ) ) ).toString() );
			}else {
				buf.append( new Character( (char)(base+index-1) ).toString() );
			}
		}
				
		return buf.toString();
	}

}
