package com.fortunapix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelFInd {
public static void main(String[] args) throws IOException {
	File f=new File("/home/manideepg/convertion tool/sectionsName/CBSE Grade-7 Science.xls");
	InputStream ExcelFileToRead = new FileInputStream(f);

	HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
	HSSFWorkbook test = new HSSFWorkbook();
	HSSFSheet sheet = wb.getSheetAt(0);
	HSSFRow row;
	HSSFCell cell;
	Iterator rows = sheet.rowIterator();
	String topicIdTemp="";
	while (rows.hasNext()) {
		row = (HSSFRow) rows.next();
		try {
		if(row.getCell((short)0)!=null){
			if(row.getCell((short) 1).toString().trim().equalsIgnoreCase("15.2 REFLECTION OF LIGHT")){
				System.out.println(row.getCell((short) 0));
				break;
			}else if(row.getCell((short) 2).toString().trim().equalsIgnoreCase("15.2 REFLECTION OF LIGHT")) {
				topicIdTemp=row.getCell((short) 3).toString().trim();
				System.out.println(topicIdTemp);
				while(rows.hasNext()) {
					HSSFRow temprow =(HSSFRow) rows.next();
				//	System.out.println(temprow.getCell((short) 1)+topicIdTemp);
					//System.out.println(temprow.getCell((short) 0));
					if(temprow.getCell((short) 1).toString().trim().equalsIgnoreCase(topicIdTemp)) {
						System.out.println(temprow.getCell((short) 0));
						
					break;
					
					}
				}
				
			}
		}
		}catch(Exception e) {
			System.err.println("2nd one");
		}
	}
	


}
}
