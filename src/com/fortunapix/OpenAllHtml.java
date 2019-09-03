package com.fortunapix;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class OpenAllHtml {
	//static String path = "D:\\My project\\Primary topics";
	static String path = "\\192.168.1.25\\Xampp\\htdocs\\Math\\";

	public static void main(String[] args) {
		try {
			File file = new File(path);
			File[] a = file.listFiles();
			for (File b : a) {
				if (b.isDirectory()) {
					File[] c = b.listFiles();
					for (File d : c) {
						if (d.isDirectory()) {
							// System.out.println(d);
							
								File[] e = d.listFiles();
								for (File f : e) {
									if (f.getCanonicalPath().endsWith("index.html")) {	
									 
										 try {
												Desktop desktop = Desktop.getDesktop();
												if (f.exists()) {
													desktop.open(f);
												}
											} catch (Exception excep) {
												System.err.println("open ex");
												excep.printStackTrace();
											}	
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("end");
			e.printStackTrace();
		}
	}

}
