package com.fortunapix;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExample {
public static void main(String[] args) {
	
String temp="Q2   ;  secondsecond";

	/*System.out.println(Pattern.compile("(Q|question|q|Question)[ ,:;]*[0-9][ ,:;]*").matcher(temp).group(0));
	System.out.println(Pattern.matches("(Q|question|q|Question)[ ,:;]*[0-9][ ,:;]*", temp));
	*/
	
	
	 // String to be scanned to find the pattern.
    //String line = "Q2   ;  secondsecond";
    String pattern = "(Q|question|q|Question)[ ,:;]*[0-9][ ,:;]*";
    System.out.println("hiiiii");

    // Create a Pattern object
    Pattern r = Pattern.compile(pattern);

    // Now create matcher object.
    Matcher m = r.matcher(temp);
    if (m.find( )) {
       System.out.println("Found value: " + m.group(0) );
       System.out.println("Found value: " + m.group(1) );
       
    }else {
       System.out.println("NO MATCH");
    }
}	
}
