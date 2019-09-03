package com.fortunapix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class fileRename {
public static void main(String[] args) {
String path = "/home/manideepg/01DocxToJson/";
File a = new File(path);
String[] b =a.list();
for(String c:b) {
	File d = new File(a+"/"+c);
	//String name = c.replace(" ", "_");
	//name = c.replace("-", "_");
	File e = new File(a+"/"+c.replace(" ", "_").replace("-", "_").replace("__", ""));
 	d.renameTo(e);
	System.out.println(d+"::::::::::"+e);
	
}





}
}
