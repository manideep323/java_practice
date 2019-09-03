package com.fortunapix;

public class Split {
static String input="<ol>h</ol><ol>2</ol><ol>1</ol><ol>3</ol>";
	public static void main(String[] args) {
		knowSplit();
	}

	
	
	
	private static void knowSplit() {
		String output=input.split("<ol>")[1].split("</ol>")[0];
		
		String text="songte)tere";
		System.out.println(text.replace("text()", "TEXT"));
		
		/*for (int i = 0; i < output.length; i++) {
			System.out.println(output[i]);
			
		}*/		
		
		
	}
	
	
}
