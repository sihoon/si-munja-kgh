package com.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelManagerByPOI36 {
	
	String sheetName = null;
	int maxCountColumn = 0;
	int limitColumn = 0;
	int limitRow = 0;
	boolean bLimitRow = false;
	int lastRow = 0;
	boolean bLimitColumn = false;
	String [] arrTitle = null;
	
	public ExcelManagerByPOI36() {
		
		sheetName = null;
		maxCountColumn = 0;
		limitColumn = 50;
		limitRow = 500000;
	}
	
	public void setSheetName(String str){ this.sheetName = str; }
	public void setlimitColumn(int i){ this.limitColumn = i; }
	public void setlimitRow(int i){ this.limitRow = i; }
	public String getSheetName(){ return this.sheetName; }
	public int getlimitColumn(){ return this.limitColumn; }
	public int getlimitRow(){ return this.limitRow; }
	public boolean getbLimitColumn() { return this.bLimitColumn; }
	public boolean getbLimitRow() { return this.bLimitRow; }
	public void setTitle(String [] arr) { this.arrTitle = arr;}
	
	public int getmaxCountColumn(){ return this.maxCountColumn;}
	
	public String[][] Read(String filePath) {
		
		InputStream fin = null;
		Workbook workbook =  null;
		try {
			fin = new FileInputStream(filePath);
			workbook = WorkbookFactory.create(fin);
		}catch(IOException ie) {
			System.out.println(ie);
		}catch(InvalidFormatException ins) {
			System.out.println(ins);
		}catch(Exception e) {
			System.out.println(e);
		}

		
		Sheet sheet = null;
		int countColumn = 0; 
		
		
		if (sheetName == null) sheet = workbook.getSheetAt(0);
		else sheet = workbook.getSheet(sheetName);
		
		lastRow = sheet.getLastRowNum();
		
		if (lastRow > limitRow) { lastRow = limitRow-1; bLimitRow = true; }
		
		
		String[][] result = new String[lastRow+1][];
		
		int countReadColumn = 0;
		DecimalFormat df = new DecimalFormat("#");

		for (int i = 0; i <= lastRow; i++) {
			
			countReadColumn = 0;
			Row row = sheet.getRow(i);

			if(row == null) {				
				result[i] = new String[0];
				continue;
			}
			countColumn = row.getLastCellNum();
			if(countColumn < 0) countColumn = 0;
			if (countColumn > limitColumn) { countColumn = limitColumn-1; bLimitColumn = true;}
			result[i] = new String[countColumn];
			
			//for (int j = row.getFirstCellNum(); j < countColumn; j++) {
			for (int j = 0 ; j < countColumn; j++) {
				countReadColumn++;
				
				
				Cell cell = row.getCell(j);

				if (cell == null)
					continue;

				int cellType = cell.getCellType();
				
				// cell Ÿ�Կ� �� ��; ��½�Ų��
				switch (cellType) {
					case Cell.CELL_TYPE_NUMERIC:		// 0
						double d = cell.getNumericCellValue();
						// ����Ʈ Ÿ�Կ��θ� üũ�Ѵ�.
						if (DateUtil.isCellDateFormatted(cell)) {
							// format in form of YYYYMMDD
							SimpleDateFormat formatter =
								new SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
							String cellText = formatter.format(DateUtil.getJavaDate(d));
							result[i][j] = cellText;
						} else {
							result[i][j] = df.format(d);
						}
						
						break;
					case Cell.CELL_TYPE_STRING:			// 1
						result[i][j] = cell.getRichStringCellValue().getString();
						break;
					case Cell.CELL_TYPE_FORMULA:		// 2
						result[i][j] = cell.getCellFormula();
						break;
					case Cell.CELL_TYPE_BLANK:			// 3
							result[i][j] = "";
						break;
					case Cell.CELL_TYPE_BOOLEAN:		// 4
						result[i][j] = String.valueOf(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_ERROR:			// 5
						result[i][j] = String.valueOf(cell.getErrorCellValue());
						break;
					default:
						result[i][j] = "";
						break;
				}
			}
			if (countColumn > this.maxCountColumn) this.maxCountColumn = countReadColumn;
		}
		return result;

	}
	
	public void WriteAndDownLoad(HttpServletResponse response , String fileName , String[][] list) throws Exception {
		
		if ( this.sheetName == null ) this.sheetName = "Sheet1";
			
		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0 , this.sheetName );
		
		HSSFCellStyle style = setCellStyle(workbook);
		HSSFCellStyle styleTitle = setTitleCellStyle(workbook);
		
		
		HSSFRow row = sheet.createRow(0);
		
		HSSFRichTextString string = null;
		
		int title = 0;
		if (this.arrTitle != null) {
			
			row = sheet.createRow(title);

			for (int t = 0 ; t < this.arrTitle.length; t++){
				HSSFCell cell = row.createCell(t);
				cell.setCellStyle(styleTitle);
				sheet.setColumnWidth(t, this.arrTitle[t].length() * 12 * 100 );
				string = new HSSFRichTextString(this.arrTitle[t]);
				cell.setCellValue(string);
			}
			title++;
		}
		
		int rowCount = list.length;
		for ( int i = 0; i < rowCount; i++){
			
			row = sheet.createRow(i+title);

			for (int j = 0 ; j < list[i].length; j++){
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style);
				string = new HSSFRichTextString(list[i][j]);
				cell.setCellValue(string);
			}
		}

		OutputStream outs  = null;

		try {
			// 쿠키 등의 정보를 지우므로 이전에 세팅을 해서는 안된다.
			response.reset();
			
			response.setContentType("application/smnet");
			response.setHeader("Content-Disposition","attachment; filename="+ fileName+".xls" +";");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");
			
			outs = response.getOutputStream();
			workbook.write(outs);
		}
		finally {
			
				try{
					if(outs != null) { outs.close(); }
				}catch(Exception e){}
		}
	}
	
	private HSSFCellStyle setCellStyle(HSSFWorkbook workbook){
		
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setWrapText(false);
		
		return style;
	}
	
	private HSSFCellStyle setTitleCellStyle(HSSFWorkbook workbook){
		
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setFillBackgroundColor(HSSFColor.AQUA.index);
		style.setWrapText(false);
		//style.setFillPattern(HSSFCellStyle.LESS_DOTS);
		
		return style;
	}
}
