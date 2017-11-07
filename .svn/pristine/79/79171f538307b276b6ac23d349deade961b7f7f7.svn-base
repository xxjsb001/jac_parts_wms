package com.vtradex.base;

import java.io.FileOutputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * 
 * @author <a href="mailto:brofe@163.com">潘宁波</a>
 * @since Feb 14, 2014 12:02:47 PM
 */
public class POITestCase {
	public static void main(String[] args) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sheet1");
		HSSFRow row = sheet.createRow((short) 0);
		
		// 文本
		HSSFCellStyle textStyle = workbook.createCellStyle();
		HSSFDataFormat textFormat = workbook.createDataFormat();
		textStyle.setDataFormat(textFormat.getFormat("@"));
		HSSFCell cell0 = row.createCell(0);
		cell0.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell0.setCellValue(new HSSFRichTextString("brofe"));
		cell0.setCellStyle(textStyle);
		
		// 数字
		HSSFCell cell1 = row.createCell(1);
		cell1.setCellValue(2.4D);
		cell1.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		HSSFCellStyle doubleStyle = workbook.createCellStyle();
		doubleStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		cell1.setCellStyle(doubleStyle);
		
		// 日期
		HSSFCell cell2 = row.createCell(2);
		cell2.setCellValue(new Date());
		HSSFCellStyle dateStyle = workbook.createCellStyle();
		HSSFDataFormat format = workbook.createDataFormat();
		dateStyle.setDataFormat(format.getFormat("yyyy年m月d日"));
		cell2.setCellStyle(dateStyle);
		
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream("brofe.xls");
			workbook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
