package com.fortunapix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelDivisionErrorFiles {
public static void main(String[] args) throws IOException {
	File f=new File("/home/manideepg/Untitled 2.xls");
	File jsonFind= new File("/home/manideepg/D-drive/test/Grade_7");
	File exceptionFiles= new File("/home/manideepg/D-drive/test/Grade_7/needToCheck");
	ArrayList<Object> row1 =new ArrayList<>();
	ArrayList<Object> listJsonList =new ArrayList<>();
	
	File[] listJson=jsonFind.listFiles();
	for (File file : listJson) {
		if(file.exists()&&file.getName().endsWith(".json")) {
		listJsonList.add(file.getName().replaceAll(".json",""));
		}
	}
	
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
			if(row.getCell((short) 0).toString().trim()!=null)
			row1.add(row.getCell((short) 0).toString().trim());
		}catch(Exception e) {
			//System.err.println("2nd one");
		}
	}
	
	

	
	
	for(int i=0;i<listJsonList.size();i++) {
		for(int j=0;j<row1.size();j++) {
			//System.out.println(row1.size());
			if(listJsonList.get(i).equals(row1.get(j))) {
				if(!exceptionFiles.exists())exceptionFiles.mkdirs();
				System.out.println(exceptionFiles);
				System.out.println(Paths.get(jsonFind+"/"+listJsonList.get(i)));
				System.out.println(Paths.get(exceptionFiles.toString()));
				System.out.println(Files.move(Paths.get(jsonFind+"/"+listJsonList.get(i)+".json"),Paths.get(exceptionFiles.toString()), StandardCopyOption.REPLACE_EXISTING));
			}
			
		}
		
		//System.out.println(row1.get(i));
		
	}
	
	
	
	
	
	
	
	


}



}
