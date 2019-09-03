package com.fortunapix;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EditFile {
	 static File file = new File("C:\\Users\\manideepg\\Desktop\\textFile.txt");
	public static void main(String[] args) {

        try{
            
        	BufferedReader br = new BufferedReader(new FileReader(file));
              String st=null;
              while((st=br.readLine())!=null) {
            	  System.out.println(st);
            	  
              }
           
            


        }catch(IOException e){
        e.printStackTrace();
        }
    }

	
}