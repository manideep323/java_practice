package com.fortunapix;

import java.io.File;
import java.io.IOException;


public class ReadSpecificFilesFromJava {

	static String EXCEL_OUTPUT_FILE_LOCATION = "D:\\temp1\\excel_sucess.xls";

	public static void main(String[] args) {
		try {
		 String path ="C:\\Users\\manideepg\\Desktop\\to html\\scripts_for_customisation\\";

	File mainPath=new File(path);
		String []files1 = mainPath.list();
		 for (String filesarr : files1) {
			 File path2=new File(path+"\\"+filesarr);
			 
			 File [] files2=path2.listFiles();
			
			try{ 
			 for (File filesarr2 : files2) {
				//System.out.println(filesarr2);
			
				 File path3=new File(filesarr2+"\\");
				 
				
				 
				 File [] files3=path3.listFiles();
				try { 
				 for (File filesarr3 : files3) {
				//	System.out.println(filesarr3.toString());
				 String all=filesarr3.toString();
				 /*if(all.substring(all.length()-3,all.length()).equals("xls")){*/
				 if(all.substring(all.length()-8,all.length()).equals("quiz.xls")){	 
				 System.out.println(filesarr3);
				 }
			 
			 }}catch(Exception e) {}
		 }
		 
			
			
			
			
			
			
			
			
			
			
			}
			catch(Exception e) {
				System.err.println();
				e.printStackTrace();
			}
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 /*	File path1 = new File(path);
		String[] path2 = path1.list();
		for(String a: path2){
		//	System.out.println(a);
			File b = new File(path1+"\\"+a);
			//System.out.println(b);
			String[] c = b.list();
		
			for(String d: c){
				File e = new File(b+"\\"+d);
				//System.out.println(e);
				File quiz = new File(e+"\\quiz.xls");
				if(quiz.exists()) {
					
					//System.out.println(quiz);
					
				}
			}
		}*/
	}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.err.println();
		}
	}
	
}
