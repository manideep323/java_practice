package com.fortunapix;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex{  

	public static void main(String args[]){  
String regex="[0-9]{1,99}/[0-9]{1,99}";


Pattern sectionNumberPattern = Pattern.compile(regex);
Matcher m = sectionNumberPattern.matcher("(23");
while (m.find()) {
    System.out.print(m.group());
}
boolean ans=Pattern.matches(regex,"mn");
//System.out.println(ans);
Pattern pattern1 = Pattern.compile("[0-9]");

String matchString="15.4 Transfer of Charge::::Science_Some-Natural-Phenomena.html";


/*for(int i=0;i<matchString.length();i++) {
	if(matchString.charAt(i)==) {
		
	}
}*/

//System.out.println(matchString.charAt(3));
//System.out.println(matchString.valueOf(matchString));


//System.out.println(Pattern.matches("[0-9]", "hfgsakj43"));
//System.out.println(Pattern.matches("", "matchString"));
//System.out.println(Pattern.matches("", "matchString"));

/*String string = "15.4 Transfer of Charge::::Science_Some-Natural-Phenomena.html";
System.out.println(string.replaceAll("[0-9](.*)\\.[0-9]",""));
Pattern pattern = Pattern.compile("[0-9](.*)\\.[0-9]");
Matcher matcher = pattern.matcher(string);
while (matcher.find()) {
    //System.out.print(matcher.group());
}*/




}}  