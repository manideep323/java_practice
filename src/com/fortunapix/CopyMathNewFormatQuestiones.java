package com.fortunapix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Source;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CopyMathNewFormatQuestiones {
	
	static JSONObject questionsJson = null;
	static JSONObject mainJson = null;
	static File file = new File("/home/manideepg/01DocxToJson/Quiz_For Nikhil");
	static File excelFile = new File("/home/manideepg/01DocxToJson/test/mathGrade7final.xls");
	
	static String[] keysArray = { "AUTHOR EMAIL ID","CHAPTER CODE","TOPIC CODE", "QUIZ TYPE", "GRADE", "DIFFICULTY", "BLOOMS", "QUESTION TYPE", "VIDEO",
			"Text", "Optiona", "Optionb", "Optionc", "Optiond", "ANSWER", "HINTS", "EXPLANATION","category_id","HINT1","HINT2"};
	static String[] replaceableElements = {"</font>", "</span>", "<colgroup>", "<font>", "<span>", "<span >", "</col>",
			"<p>", "</p>", "<col>", "</colgroup>", "&nbsp;", "<br>", "<a></a><a></a>","<a></a>","<td>","</td>","<td bgcolor=\\\"#ffffff\\\">" };
	static Map<String,String> categoryMap=new HashMap<String,String>();  
	static ArrayList<String> htmlFilesList=new ArrayList<String>();
	static String imagePrefix="<img src= \"https://s3-us-west-2.amazonaws.com/fpixquestionbank/images/";
	public static void main(String[] args) throws JSONException, IOException {
		listOfHtmlFiles();
		readExcelFile();
		JSONObject convertHtmlToJson = convertHtmlToJson();
	//System.out.println(convertHtmlToJson);
}

	
	
	
private static void listOfHtmlFiles() {
	File[]htmlFiles= file.listFiles();
	for(File htmls:htmlFiles) {
		if(htmls.isFile()&&htmls.getPath().endsWith(".html")) {
			htmls.renameTo(new File(htmls.toString().replaceAll("[\\, ,\\-]", "_").replace("__", "_")));
			htmlFilesList.add(htmls.getName().replaceAll("[\\, ,\\-]", "_").replace("__", "_"));
			//System.out.println("::::::"+htmls);
		}
		}
	}








private static void readExcelFile() throws IOException {
		
	InputStream ExcelFileToRead = new FileInputStream(excelFile);

	HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
	HSSFWorkbook test = new HSSFWorkbook();
	HSSFSheet sheet = wb.getSheetAt(0);
	HSSFRow row;
	HSSFCell cell;
	Iterator rows = sheet.rowIterator();
	while (rows.hasNext()) {
		row = (HSSFRow) rows.next();
		if (row.getCell(0) != null) {
			categoryMap.put(row.getCell(1).toString().trim(),row.getCell(0).toString().trim());
		}
	}
	  for(Map.Entry m:categoryMap.entrySet()){  
		  // System.out.println(m.getKey()+" "+m.getValue());  
		  }  
	}

private static JSONObject convertHtmlToJson() throws JSONException  {
	for(String htmls:htmlFilesList) {
	int rowCount = 0;int optionCount=0;	int i=1;
	Document doc=null;
	JSONObject JSONObj = null;
	String tempQuestionNumber="";
	mainJson=new JSONObject();
	JSONObject allQuestionesJson=new JSONObject();
	
		ArrayList imagesInFloder=findExtentionInImagesFloder(htmls);
		
		//if(!imagesInFloder.isEmpty()) {System.out.println("hiiiiii");}
	//System.out.println("ssssssssss"+imagesInFloder);
	
	
	
	
String attributeElements[]= {"font face","class","style","colspan","face","size","lang","align","name","cellpadding","cellspacing","valign","color","width","height","font","span"};
			try {
				
					System.out.println("filenameeeee"+file+"/"+htmls);
				
				
		doc = Jsoup.parse(new File(file+"/"+htmls), "utf-8");
	
	Element elements = doc.body();
	Elements copyelement = elements.select("body > center > table");
	for (Element ele : copyelement) {
		Elements trs = ele.select("tr");
	}
	Elements element = elements.select("table");
	for (Element ele : element) {
		System.out.println("sizeeeeee"+element.size());
		
		Elements trs = ele.select("tr");	
		boolean questionFinished=false;
		for (Element tr : trs) {
			
			if(tr.text().startsWith("S. No")) {
				rowCount = 0;
				questionsJson=new JSONObject();
			}
			rowCount++;
			if (rowCount == 2 || rowCount == 4 || rowCount == 6 || rowCount == 8 || rowCount == 10 || rowCount == 11 || rowCount == 13 || rowCount == 14) {
				continue;
			}
			if(rowCount==1) {								
				//System.out.println(tr.child(1).text());
				questionsJson.put(keysArray[0], tr.child(3).text().trim());
				 tempQuestionNumber=tr.child(1).text();
			}
			//comment:mapping categoy_id and chapter or topic code
			
			else if(rowCount==3) {
				String categoryMapingValue="";
				if(!tr.child(1).text().isEmpty()) {
					questionsJson.put(keysArray[1], tr.child(1).text().trim());
					questionsJson.put(keysArray[2], tr.child(3).text().trim());
					categoryMapingValue=tr.child(1).text().trim();
				}else {
					questionsJson.put(keysArray[1], tr.child(1).text().trim());
					questionsJson.put(keysArray[2], tr.child(3).text().trim());
					categoryMapingValue=tr.child(3).text().trim();
				}
				
			if(!categoryMapingValue.isEmpty()) {
				 String category_id="";  
				for(Map.Entry m:categoryMap.entrySet()){  
					  if(categoryMapingValue.equalsIgnoreCase(m.getKey().toString())) {
						  category_id=(String) m.getValue();
					  }
					  
				  }  
				if(!category_id.isEmpty())questionsJson.put(keysArray[17], category_id.trim());
				else { System.err.println("category_id not found in excel "+categoryMapingValue);
				}
				  
			}
			else System.err.println("chapter code&topic code not found in docx question no is "+tempQuestionNumber);
			}
			else if(rowCount==5) {
				questionsJson.put(keysArray[3], tr.child(1).text().trim());
				questionsJson.put(keysArray[4], tr.child(3).text().trim());
			}
			else if(rowCount==7) {
				questionsJson.put(keysArray[5], tr.child(1).text().trim());
				questionsJson.put(keysArray[6], tr.child(3).text().trim());
			}
			else if(rowCount==9) {
				questionsJson.put(keysArray[7], tr.child(1).text().trim());
				questionsJson.put(keysArray[8], tr.child(3).text().trim());
			}
			
			//comment:question and option image name
			else if(rowCount==12) {
				//System.out.println("::::::"+tr.child(0).text());
				String text="";
				String imageName="";
				
				for (int q = 0; q < attributeElements.length; q++) {
					//tr.child(0).removeAttr(attributeElements[q]);
					//tr.child(1).removeAttr(attributeElements[q]);
					tr.child(0).getElementsByAttribute(attributeElements[q]).removeAttr(attributeElements[q]).html();
				}
				text=tr.child(0).toString().trim();
				imageName=tr.child(1).text().trim();
				for (int q = 0; q < replaceableElements.length; q++) {
					text=text.replace(replaceableElements[q], "");
				}
				//System.out.println("::::::::::::"+text+imageName);
				
				if(!tr.child(1).text().isEmpty()) {
					if(!imagesInFloder.isEmpty()) {//images adding to it
						for(Object imagesExt:imagesInFloder) {
							if(imageName.trim().equalsIgnoreCase(((String) imagesExt).replaceAll("\\.(.*)", "").trim())) {
								imageName=(String) imagesExt;
							}
						}
					questionsJson.put(keysArray[9], text+ imagePrefix+questionsJson.get(keysArray[17])+"/"+imageName+"\""+">");
					}else {
						questionsJson.put(keysArray[9], text);
						System.err.println("images floder not in path or make sure same name of directory and html file");
					}
				}
				else { questionsJson.put(keysArray[9], text);}
			}
			//comment:for optiones
			
			else if(rowCount==15||rowCount==16||rowCount==17||rowCount==18) {
				String option="";
				String optionImage="";
				if(!tr.child(1).text().isEmpty()) {
				for (int q = 0; q < attributeElements.length; q++) {
					//tr.child(1).removeAttr(attributeElements[q]);
					//tr.child(2).removeAttr(attributeElements[q]);
					tr.child(1).getElementsByAttribute(attributeElements[q]).removeAttr(attributeElements[q]).html();
					if(!tr.child(2).text().isEmpty())tr.child(2).getElementsByAttribute(attributeElements[q]).removeAttr(attributeElements[q]).html();
				}
				option=tr.child(1).html();
				optionImage=tr.child(2).html();
				
				for (int q = 0; q < replaceableElements.length; q++) {
					option=option.replace(replaceableElements[q], "");
					if(!tr.child(2).text().isEmpty())optionImage=optionImage.replace(replaceableElements[q], "");
				}
				if(!tr.child(2).text().isEmpty()) {
					if(!imagesInFloder.isEmpty()) {//images adding to it
						for(Object imagesExt:imagesInFloder) {
							if(optionImage.trim().equalsIgnoreCase(((String) imagesExt).replaceAll("\\.(.*)", ""))) {
								optionImage=(String) imagesExt;
							}
						}
				if(rowCount==15)questionsJson.put(keysArray[10], option+ imagePrefix+questionsJson.get(keysArray[17])+"/"+optionImage+"\""+">");
				if(rowCount==16)questionsJson.put(keysArray[11], option+ imagePrefix+questionsJson.get(keysArray[17])+"/"+optionImage+"\""+">");
				if(rowCount==17)questionsJson.put(keysArray[12], option+ imagePrefix+questionsJson.get(keysArray[17])+"/"+optionImage+"\""+">");
				if(rowCount==18)questionsJson.put(keysArray[13], option+ imagePrefix+questionsJson.get(keysArray[17])+"/"+optionImage+"\""+">");
					}else {
						System.err.println("images floder not in path or make sure same name of directory and html file");
					}
					}
				else {
					if(rowCount==15)questionsJson.put(keysArray[10], option.trim());
					if(rowCount==16)questionsJson.put(keysArray[11], option.trim());
					if(rowCount==17)questionsJson.put(keysArray[12], option.trim());
					if(rowCount==18)questionsJson.put(keysArray[13], option.trim());
				}
				
				if(tr.child(3).text().equalsIgnoreCase("True")) {
				if(rowCount==15)questionsJson.append(keysArray[14],"a");
				else if(rowCount==16)questionsJson.append(keysArray[14],"b");
				else if(rowCount==17)questionsJson.append(keysArray[14],"c");
				else if(rowCount==18)questionsJson.append(keysArray[14],"d");
				}
			
			}
				}
			
			else if(rowCount==20||rowCount==21) {
				String hints="";
				if(!tr.child(1).text().isEmpty()) {
					for (int q = 0; q < attributeElements.length; q++) {
						//tr.child(1).removeAttr(attributeElements[q]);
						tr.child(1).getElementsByAttribute(attributeElements[q]).removeAttr(attributeElements[q]).html();
					}
					hints=tr.child(1).html();
					for (int q = 0; q < replaceableElements.length; q++) {
						hints=hints.replace(replaceableElements[q], "");
					}
					
				//questionsJson.append(keysArray[15],hints);
				}
				if(rowCount==20) {
					questionsJson.put(keysArray[18],hints.trim());	
				}
				else if(rowCount==21) {
					questionsJson.put(keysArray[19],hints.trim());	
				}
				//System.out.println(questionsJson);
			}
			else if(rowCount==23||rowCount==24||rowCount==25||rowCount==26||rowCount==27||rowCount==28||rowCount==29||rowCount==30||rowCount==31||rowCount==32||rowCount==33||rowCount==34||rowCount==35||rowCount==36||rowCount==37) {
				System.out.println(":::::"+tr.child(1).text());
				String solutions="";
				if(!tr.child(1).text().isEmpty()) {
					for (int q = 0; q < attributeElements.length; q++) {
						//tr.child(1).removeAttr(attributeElements[q]);
						tr.child(1).getElementsByAttribute(attributeElements[q]).removeAttr(attributeElements[q]).html();
					}
					solutions=tr.child(1).html();
					for (int q = 0; q < replaceableElements.length; q++) {
						solutions=solutions.replace(replaceableElements[q], "");
					}
			
				questionsJson.append(keysArray[16],solutions);
				//questionsJson.put(keysArray[17], "hiiiiiiii");
				}
				//System.out.println(i);
				if(rowCount==32) {questionFinished=true;
				questionsJson=finalThings(questionsJson,tempQuestionNumber);
				allQuestionesJson.put("question"+i++, questionsJson);
				
				}
				
			}
			
			
		}
		
		//questionsJson=finalThings(questionsJson,tempQuestionNumber);
		//System.out.println(questionsJson);
		//allQuestionesJson.put("question"+i++, questionsJson);
		
		
		
		
		
		
		
		
		//comment:making folders and adding images into it
		//System.out.println(questionsJson);
		Iterator<String> keys = questionsJson.keys();

		while(keys.hasNext()) {
		    String key = keys.next();
		    String values=questionsJson.get(key).toString();
		    //System.out.println(values);
		    //System.out.println(imagePrefix);
		    
		    if(values.contains("<img src")) {
		    	
		    	//System.out.println(values);
		    	
		    	Pattern imagePattern = Pattern.compile(imagePrefix+ "(.*?)" + "\\\">");
				Matcher imageMatcher = imagePattern.matcher(values);
				while (imageMatcher.find()) {
					
					File sourceFile=new File(file+"/"+htmls.replace(".html", "")+imageMatcher.group(1).replaceAll("([0-9,a-z]){24}", ""));
					File destinationFile=new File(file+"/"+imageMatcher.group(0).trim().replace(imagePrefix, "").substring(0, 24)+"/");
					
					if (!destinationFile.exists()) destinationFile.mkdirs();		
					//File categoryFolder=new File(file+"/"+imageMatcher.group(0).trim());
			
			//System.out.println("c"+categoryFolder);
			
			
			//System.out.println("$$$$$$$"+sourceFile);
			//System.out.println("dddddd"+destinationFile);
			try{
				FileUtils.copyFileToDirectory(sourceFile, destinationFile);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			//FileUtils.copyFile(sourceFile, destinationFile);
			
			
				
				}
		    }
		    
		    
		    
		    
		    
		}
		
		
		
		
	}
		
		mainJson.put("QuestionsJSON", allQuestionesJson);
		imagesPushIntoS3();
//System.out.println();
		
		File outputJsonWriter=new File(file+"/"+htmls.replace(".html",".json"));
		//System.out.println("jjjjjjj"+file+htmls.replace(".html",".json"));
		//System.out.println("77777"+outputJsonWriter);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputJsonWriter), "UTF8"));

		bw.write(mainJson.toString());
		bw.flush();
		bw.close();
	
		
		
	//System.out.println(mainJson);
				
	} catch (IOException e) {
	e.printStackTrace();
}
//return mainJson;
}
	return mainJson;
}

		public static void imagesPushIntoS3() {
			
			
		}


private static JSONObject finalThings(JSONObject questionsJson2, String tempQuestionNumber) throws JSONException {
		
	if(questionsJson2.get(keysArray[2]).toString().equals("")) {
		//questionsJson2.put("TOPIC CODE", "integers"); 
	}
	if(questionsJson2.get(keysArray[7]).toString().equalsIgnoreCase("MCQ")) {
		questionsJson2.put(keysArray[7], "Multiple Choice");
	}
	if(questionsJson2.get(keysArray[7]).toString().equalsIgnoreCase("FIB")) {
		questionsJson2.put(keysArray[7], "Fill In The Blanks");
	}
	if(questionsJson2.get(keysArray[7]).toString().equalsIgnoreCase("TF")) {
		questionsJson2.put(keysArray[7], "True Or False");
	}
	if(questionsJson2.get(keysArray[5]).toString().equalsIgnoreCase("Hard")) {
		questionsJson2.put(keysArray[5], "Difficult");
	}
	/*if(!questionsJson2.has(keysArray[16])) {
		questionsJson2.append(keysArray[16], "");
	}*/
	if(!questionsJson2.has(keysArray[17])) {
		questionsJson2.put(keysArray[17], "5b9b83c14a3ea84481d12a53");
	}
		
	if(!(questionsJson2.length()==19)){
		if(questionsJson2.length()==17&&(questionsJson2.get(keysArray[7]).toString().equalsIgnoreCase("True Or False"))) {
			
		}
		else {
			System.err.println("missing some keys question no is "+tempQuestionNumber);
		}
	}
	
	
	//System.out.println("jsonLengthhhhhh"+questionsJson2.length());
	return questionsJson2;
}




private static ArrayList findExtentionInImagesFloder(String htmls) {
	//System.out.println(htmls);
	ArrayList<String> imagesInFloder=new ArrayList<String>();
	File a[]=file.listFiles();
	for(File b:a) {
		if(b.isDirectory()) {
		if(htmls.replace(".html", "").equals(b.getName())) {
			File c[]=b.listFiles();
			for(File d:c) {
			if(d.isFile()) imagesInFloder.add(d.getName());
			}
			}
			}
		}
	//System.out.println(imagesInFloder);
	return imagesInFloder;
	
	
	
}






}
