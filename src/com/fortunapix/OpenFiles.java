package com.fortunapix;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class OpenFiles {
	static String path = "D:\\for sample\\";

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
							if (d.getCanonicalPath().endsWith("CoolFacts")) {
								File[] e = d.listFiles();
								for (File f : e) {
									//opening files of index automation
									/* System.out.println(f);
									 if (f.getAbsolutePath().endsWith("index.html")) {
										 try {
												Desktop desktop = Desktop.getDesktop();
												if (f.exists()) {
													desktop.open(f);
												}
											} catch (Exception excep) {
												excep.printStackTrace();
											}
									 
									 }*/
									 
											
									if (f.isDirectory()) {

										File[] g = f.listFiles();
										for (File h : g) {
											//opening files of style.css automation
											if (h.getAbsolutePath().endsWith("style.css")) {
												System.out.println(h);

												try {
													Desktop desktop = Desktop.getDesktop();
													if (h.exists()) {
														desktop.open(h);
													}
												} catch (Exception excep) {
													excep.printStackTrace();
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
