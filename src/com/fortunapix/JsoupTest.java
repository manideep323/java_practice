package com.fortunapix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTest {
	
	
	static File file = new File("/home/manideepg/eclipse-workspace/vtt-questions-import/testing/Untitled 1.html");
public static void main(String[] args) {
	
	convertHtmlToJson();
	
}

private static void convertHtmlToJson() {
	int rowCount = 0;
	Document doc=null;
	try {
		doc = Jsoup.parse(file, "utf-8");
	} catch (IOException e) {
		e.printStackTrace();
	}

	Element elements = doc.body();
	Elements element = elements.select("table");
	
	for (Element ele : element) {
		Elements trs = ele.select("tr");	
		for (Element tr : trs) {
			if(tr.text().startsWith("S. No")) {
				rowCount = 0; 
			}
			rowCount++;
			if (rowCount == 2 || rowCount == 4 || rowCount == 6 || rowCount == 8 || rowCount == 10 || rowCount == 11 || rowCount == 13 || rowCount == 14) {
				continue;
			}
			if(rowCount==1) {								
				System.out.println(tr.child(1).text());
			}
			else if(rowCount==3) {
				
			}
			else if(rowCount==12) {
					System.out.println(tr.child(0).text());
			}

			}
			
			
			
	}
	
}
}
