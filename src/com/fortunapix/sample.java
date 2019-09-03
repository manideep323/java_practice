package com.fortunapix;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sample {
public static void main(String[] args) {
	String t="kjdsk<img srckjhdgk";
	System.out.println(t.contains("<img src"));
	
	String h="shdfk^78";
	Pattern p = Pattern.compile("\\^[0-9]{1,9}");//. represents single character  
	Matcher m = p.matcher("shdfk^78");  
	if(m.find()) {
		System.out.println("::::"+m.group().replace("^",""));
	}
	//System.out.println(m.group(1));
	//System.out.println(h.replaceAll("\\^[0-9]{1,9}", ""));
	
	for(int i=1;i<=5;i++) {
//hi();
	
	}
	
	/*for(int j=5;j>0;j--) {
		//System.out.print(j+" ");
	}*/
}

private static void hi() {
	//System.out.println("hi");
	
}
	
	
	
}
