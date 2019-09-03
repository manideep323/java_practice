package com.fortunapix;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.*;    
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.*;

public class XlToActivityJson {
	static ArrayList<String> imagesArray = new ArrayList<String>();
	static Workbook workbook = null;
	static String outputLocation = null;
	static FileInputStream inputStream;
	static FileReader reader;
	static String inputFile;
	static Properties properties;
	static String fileName;
	static JSONObject mainJson;
	static JSONObject mainJsonReturned;
	static JSONObject propertiesJson;
	static JSONObject propertiesFieldsJson;
	static JSONObject sourceObject;
	static JSONArray sourceArray;
	static JSONObject sourceFieldObject;
	//static String templatesPath ="\\\\192.168.1.20\\production\\templatized-dlos\\activities\\";
	static String templatesPath = "/run/user/1000/gvfs/smb-share:server=192.168.1.20,share=production/templatized-dlos/activities/";
	//static String appLocation =  new File("").getAbsolutePath()+"/";
	static String appLocation;
	static String selectedTemplate;
	
	static boolean secondOccurance;
	static boolean secondOccuranceSheetThree;
	private static JSONArray targetArray;
	private static JSONObject targetFieldObject;
	private static File[] htmlFiles;
	static Logger log = Logger.getLogger(XlToActivityJson.class.getName());
	static String jInputFile;
	static JFrame f;
	static String errorMessage = "template mismatch";
	//static String sucessMessage = "json write into a file and pasted into appropriate directory...!";
	static String sucessMessage = "created sucessfully...!";
	static String jOutputLocaion;
	static String prefix;
	static String templateNameInExcel;
	
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		gui();
	}

	 static void gui() {
        f = new JFrame("fortunapix"); 
        JLabel textMessageLabel = new JLabel("please select the type of template");
        textMessageLabel.setBounds(50, 70,400,20);
        final JLabel label = new JLabel();          
        label.setHorizontalAlignment(JLabel.CENTER);  
        label.setSize(400,100);  
        JButton b=new JButton("choose excel File");
        JButton buttonCreate=new JButton("Create");
        //JButton openButton=new JButton("filechoose");
        b.setBounds(50,300,300,20);//left,top,width,height
        buttonCreate.setBounds(500,200,199,20);
        File[] listFloderFiles = readDirectories();
        ArrayList<String> listOfArray = new ArrayList<>();
        for (File file : listFloderFiles) {
        	listOfArray.add(file.getName());
		}
		String[] array = listOfArray.stream().toArray(String[]::new);
        JComboBox<String> dropDown = new JComboBox<>(array);    
        dropDown.setBounds(50, 100,250,20);    
        f.add(dropDown); f.add(label); f.add(b); f.add(textMessageLabel); f.add(buttonCreate);
        f.setLayout(null);    
        f.setSize(800,500);    
        f.setVisible(true);       
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buttonCreate.setVisible(false);
        b.addActionListener(new ActionListener() {//choosing excel  
            public void actionPerformed(ActionEvent e) {
            	jInputFile = jFileChosser();//asks path
            	
            	File f = new File(jInputFile);
            	b.setText(f.getName());
            	if(jInputFile!=null) {
            		buttonCreate.setVisible(true);
            	}
            }  
        });  
     
        buttonCreate.addActionListener(new ActionListener() {//creating template
			public void actionPerformed(ActionEvent e) {

				selectedTemplate = dropDown.getItemAt(dropDown.getSelectedIndex());
				//System.out.println("::::::"+jInputFile);
            	
				if(jInputFile != null) {
					String inputFileValidation = jInputFile; 
					File f = new File(jInputFile);
					appLocation = f.getParent();
					//System.out.println(FilenameUtils.removeExtension(f.getName()));
					//System.out.println(selectedTemplate);
					try {
						inputFileValidation = inputFileValidation.replace("%20", " ").replace("XLSX", "xlsx");
					inputStream = new FileInputStream(inputFileValidation);
					 	if (inputFileValidation.endsWith(".xlsx")) {
					 		workbook = new XSSFWorkbook(inputStream);
					 	}
					    else if(inputFileValidation.endsWith(".xls")){
					    	workbook = new HSSFWorkbook(inputStream);	    	
					    }
					Sheet sheet = workbook.getSheetAt(0);//reading sheet one
					templateNameInExcel = sheet.getRow(0).getCell(3).toString();
				}catch(Exception err) {
					err.printStackTrace();
					log.error(err);
				}
					
					if(templateNameInExcel.equalsIgnoreCase(selectedTemplate)) {
						File templatePahtSpecific = new File(templatesPath+selectedTemplate);
		            	File appLocationFile = new File(appLocation);
		            	try {
							FileUtils.copyDirectoryToDirectory(templatePahtSpecific, appLocationFile);
							} catch (IOException e1) {
							log.error(e1);
						}
		            	
		            	
		            	if(jInputFile != null) {
		            		

		            		
		            		if(selectedTemplate.equalsIgnoreCase("Drag and drop with text")||(selectedTemplate.equalsIgnoreCase("Drag and drop with images"))) {
		            			jOutputLocaion =  appLocationFile+"/"+selectedTemplate+"/data/config.js";
			            		mainJsonReturned = readExcelDragAndDrop(jInputFile);
			            		prefix = "var json = ";
		            		}
		            		else if (selectedTemplate.equalsIgnoreCase("Keywords")) {
		            			jOutputLocaion =  appLocationFile+"/"+selectedTemplate+"/js/data.js";
		            			mainJsonReturned = readExcelKeyword(jInputFile);
		            			prefix = "var json = ";
							}	
		            		else if(selectedTemplate.equalsIgnoreCase("Clickable")){
		            			jOutputLocaion =  appLocationFile+"/"+selectedTemplate+"/data/config.js";		            			
		            			mainJsonReturned = readExcelClickable(jInputFile);
		            			prefix = "var json = ";
		            		}else if(selectedTemplate.equalsIgnoreCase("Fill in the blanks with 2 options")||selectedTemplate.equalsIgnoreCase("Fill in the blanks with 3 options")) {
		            			jOutputLocaion =  appLocationFile+"/"+selectedTemplate+"/data/config.js";		            			
		            			mainJsonReturned =  readExcelfillInTheBlanksWithTwoOptions(jInputFile);
		            			prefix = "var json = ";
		            		}
		            		else if(selectedTemplate.equalsIgnoreCase("Matching")) {
		            			jOutputLocaion =  appLocationFile+"/"+selectedTemplate+"/data/config.js";		            			
		            			mainJsonReturned = readExcelMatching(jInputFile);
		            			prefix = "var json = ";
		            		}
		            		else if(selectedTemplate.equalsIgnoreCase("Sequence")) {
		            			jOutputLocaion =  appLocationFile+"/"+selectedTemplate+"/data/config.js";		            			
		            			mainJsonReturned = readExcelSequence(jInputFile);
		            			prefix = "var json = ";
		            		}
		            	}		            		
		            		
		            		
		            		
		            			
		            		
		            		
        try {
        	
        	
        	BufferedWriter bw =	new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jOutputLocaion), "UTF8"));
        	if(mainJsonReturned != null) {
        	bw.write(prefix+mainJsonReturned.toString());
	   		bw.flush();
	   		bw.close();
	   		
	   		
	   		
	   		
	   		if(!imagesArray.isEmpty()) {
        		File imagesFloder = new File(appLocationFile+"/images"); 
        		if(imagesFloder.exists()) {
        			for (String arr : imagesArray) {
        				File srcFile = new File(appLocationFile+"/images/"+arr);
        				File destFile = new File(appLocationFile+"/"+selectedTemplate+"/images/");
        				if(srcFile.exists()) {
						System.out.println(imagesArray);
						FileUtils.copyFileToDirectory(srcFile, destFile);
        				}
        				else {
        					JOptionPane.showMessageDialog(new JFrame(), arr+"    images not found in images floder", "Dialog",
	            			        JOptionPane.INFORMATION_MESSAGE);	
        				}
					}
        		}
        		else {
        			JOptionPane.showMessageDialog(new JFrame(), "images floder id not exists", "Dialog",
        			        JOptionPane.INFORMATION_MESSAGE);	
        		}
        	}
	   		
	   		
	   		
	   			
	   		
	   		JOptionPane.showMessageDialog(new JFrame(), sucessMessage, "Dialog",
				        JOptionPane.INFORMATION_MESSAGE);
	   		log.info("json write into a file and pasted into appropriate directory...!");
        	}
        }
		            catch(Exception error) {
		            	error.printStackTrace();
		            			log.error(error);
		            }
		            		
		            		
		            	
					}else {
						 JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Dialog",
							        JOptionPane.ERROR_MESSAGE);
					}
						
            		
            	}
            	
            	
			}
		});
        
	}
	
	
	private static String jFileChosser() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Please choose excel file");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnValue = jfc.showSaveDialog(null);
		String path = jfc.getSelectedFile().toString();
		return path;
	}

	private static File[] readDirectories() {
		try {
			
			File templatesPathFile = new File(templatesPath);
			htmlFiles = templatesPathFile.listFiles();
			for (File file : htmlFiles) {
			//	System.out.println(file);	
			}
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return htmlFiles;
		
	}

	
	
	
	public static JSONObject readExcelKeyword(String inputFile) {
		imagesArray.clear();
		

		DataFormatter dataFormatter = new DataFormatter();
	  try {
		//inputFile = properties.getProperty("inputExcelFile");
		inputFile = inputFile.replace("%20", " ").replace("XLSX", "xlsx");
		String fileSplitArray[] = inputFile.split("/");
		outputLocation = inputFile.replace(fileSplitArray[fileSplitArray.length-1], "");
		fileName = fileSplitArray[fileSplitArray.length-1].replace(".xlsx", "");
		//System.exit(0);
		inputStream = new FileInputStream(inputFile);
		 	if (inputFile.endsWith(".xlsx")) {
		 		workbook = new XSSFWorkbook(inputStream);
		 	}
		    else if(inputFile.endsWith(".xls")){
		    	workbook = new HSSFWorkbook(inputStream);	    	
		    }
		mainJson = new JSONObject();
		JSONObject propertiesFieldsJson = new JSONObject();
	    
	    Sheet sheet = workbook.getSheetAt(0);//reading sheet one
	    int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
	    	for (int i = 1; i < rowCount+1; i++) {
		    	Row row = sheet.getRow(i);
		    	String propValue =  dataFormatter.formatCellValue(row.getCell(1)).trim().replace("FALSE()", "false").replace("TRUE()", "true");
		    	try {
			    	if(propValue.equalsIgnoreCase("true")||propValue.equalsIgnoreCase("false")) {
			    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Boolean.parseBoolean(propValue));
			    	}
			    	else {
			    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Integer.parseInt(propValue));
			    	}
		    	}catch (NumberFormatException e) {
		    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),propValue);
				}
		    	
		   }
	    	mainJson.put("properties", propertiesFieldsJson);
		    sourceArray = new JSONArray();
		    
		    
		 Sheet sheetTwo = workbook.getSheetAt(1);//reading sheet two
		 int rowCountSheetTwo = sheetTwo.getLastRowNum() - sheetTwo.getFirstRowNum();
		 
		 JSONObject sourceFieldObject = new JSONObject();
		    for (int i = 1; i < rowCountSheetTwo+1; i++) {
		    	Row row = sheetTwo.getRow(i);

	    		if(dataFormatter.formatCellValue(row.getCell(1)).trim() != "") {
	    			imagesArray.add(dataFormatter.formatCellValue(row.getCell(1)).trim());
	    		}
		    	//System.out.println(mainJson);
		    	sourceFieldObject.put("title", dataFormatter.formatCellValue(row.getCell(0)).trim());
		    	sourceFieldObject.put("image", dataFormatter.formatCellValue(row.getCell(1)).trim().replace("\\/", "/"));
		    	sourceFieldObject.put("Explanation", dataFormatter.formatCellValue(row.getCell(2)).trim());
		    	sourceArray.add(sourceFieldObject);
		    	sourceFieldObject = new JSONObject();
		    	
		    }
		    mainJson.put("data", sourceArray);// pushing into mainJson
			  		
		    Set<String> listWithoutDuplicates = new LinkedHashSet<String>(imagesArray);
			  imagesArray.clear();
			  imagesArray.addAll(listWithoutDuplicates);
		    log.info("sucessfully created");
		 
	  }
	  catch (Exception e) {
		  e.printStackTrace();
		  log.error(e);
		  JOptionPane.showMessageDialog(new JFrame(), "template file structure is not correct", "Dialog",
			        JOptionPane.ERROR_MESSAGE);
		
	  }
		
	//System.out.println(mainJson);
		return mainJson;
		
		
	}
	
	
	public static JSONObject readExcelClickable(String inputFile) {

		imagesArray.clear();
			
			DataFormatter dataFormatter = new DataFormatter();
		  try {
			//inputFile = properties.getProperty("inputExcelFile");
			inputFile = inputFile.replace("%20", " ").replace("XLSX", "xlsx");
			String fileSplitArray[] = inputFile.split("/");
			outputLocation = inputFile.replace(fileSplitArray[fileSplitArray.length-1], "");
			fileName = fileSplitArray[fileSplitArray.length-1].replace(".xlsx", "");
			//System.exit(0);
			inputStream = new FileInputStream(inputFile);
			 	if (inputFile.endsWith(".xlsx")) {
			 		workbook = new XSSFWorkbook(inputStream);
			 	}
			    else if(inputFile.endsWith(".xls")){
			    	workbook = new HSSFWorkbook(inputStream);	    	
			    }
			 	 mainJson = new JSONObject();
			 	JSONObject propertiesFieldsJson = new JSONObject();
		    
		    Sheet sheet = workbook.getSheetAt(0);//reading sheet one
		    int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		    	for (int i = 1; i < rowCount+1; i++) {
			    	Row row = sheet.getRow(i);
			    	String propValue =  dataFormatter.formatCellValue(row.getCell(1)).trim().replace("FALSE()", "false").replace("TRUE()", "true");
			    	try {
				    	if(propValue.equalsIgnoreCase("true")||propValue.equalsIgnoreCase("false")) {
				    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Boolean.parseBoolean(propValue));
				    	}
				    	else {
				    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Integer.parseInt(propValue));
				    	}
			    	}catch (NumberFormatException e) {
			    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),propValue);
					}
			    	
			   }
		    	mainJson.put("properties", propertiesFieldsJson);
		    	JSONArray sourceArray = new JSONArray();
		    	JSONObject sourceFieldObject = new JSONObject();
			    
			 Sheet sheetTwo = workbook.getSheetAt(1);//reading sheet two
			 int rowCountSheetTwo = sheetTwo.getLastRowNum() - sheetTwo.getFirstRowNum();
			 secondOccurance = true;
			 ArrayList content = new ArrayList<>();
			 String id = "";
			 String image = "";
			 String answer = "";
			    for (int i = 1; i < rowCountSheetTwo+1; i++) {
			    	Row row = sheetTwo.getRow(i);
			    	
			    	for(int j=0;j < 4; j++) {
			    		if(dataFormatter.formatCellValue(row.getCell(1)).trim() != "") {
			    			
			    			imagesArray.add(dataFormatter.formatCellValue(row.getCell(1)).trim());
			    		}
			    		
			    		
			    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			if(!secondOccurance) {
			    			sourceFieldObject.put("id", Integer.parseInt(id));
			    			sourceFieldObject.put("image", image);
			    			sourceFieldObject.put("answer", Boolean.parseBoolean(answer.replace("FALSE()", "false").replace("TRUE()", "true")));
			    			sourceFieldObject.put("content", content);
			    			sourceArray.add(sourceFieldObject);
			    			content = new ArrayList();
			    			sourceFieldObject = new JSONObject();
			    			image = ""; 
			    			answer = "";
			    			
			    			content.clear();
			    			}
			    		}
			    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
							id = dataFormatter.formatCellValue(row.getCell(j)).trim();
						}
			    		if(j==1 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			image =  dataFormatter.formatCellValue(row.getCell(j)).trim();
			    		}
						if(j==2 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
						  content.add(dataFormatter.formatCellValue(row.getCell(j)).trim()); 
						}
						if(j==3 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			answer =  dataFormatter.formatCellValue(row.getCell(j)).trim();
			    		}
						secondOccurance = false;
			    		}
			    	if(i==rowCountSheetTwo) {
						sourceFieldObject.put("id", Integer.parseInt(id));
		    			sourceFieldObject.put("image", image);
		    			sourceFieldObject.put("answer", Boolean.parseBoolean(answer.replace("FALSE()", "false").replace("TRUE()", "true")));
		    			sourceFieldObject.put("content", content);
		    			sourceArray.add(sourceFieldObject);
		    			content = new ArrayList();
		    			sourceFieldObject = new JSONObject();
					}
			    }
			    mainJson.put("source", sourceArray);// pushing into mainJson
			    
			    
			    
			    
			    
		   			if(mainJson.isEmpty()) {
		   				log.error("please check the excel sheet format");
		   			}else {
		   				log.info("json created sucessful");
		   			}
		   		 
		   			System.out.println(mainJson);
		   		}
		  catch (Exception e) {
			  e.printStackTrace();
			  log.error(e);
			  
			
		  }
		  Set<String> listWithoutDuplicates = new LinkedHashSet<String>(imagesArray);
		  imagesArray.clear();
		  imagesArray.addAll(listWithoutDuplicates);
		  System.out.println(imagesArray);
		return mainJson;
			
		
	
		
	}
	
	
	
	public static JSONObject readExcelfillInTheBlanksWithTwoOptions(String inputFile) {

		imagesArray.clear();
		DataFormatter dataFormatter = new DataFormatter();
	  try {
		//inputFile = properties.getProperty("inputExcelFile");
		inputFile = inputFile.replace("%20", " ").replace("XLSX", "xlsx");
		String fileSplitArray[] = inputFile.split("/");
		outputLocation = inputFile.replace(fileSplitArray[fileSplitArray.length-1], "");
		fileName = fileSplitArray[fileSplitArray.length-1].replace(".xlsx", "");
		//System.exit(0);
		inputStream = new FileInputStream(inputFile);
		 	if (inputFile.endsWith(".xlsx")) {
		 		workbook = new XSSFWorkbook(inputStream);
		 	}
		    else if(inputFile.endsWith(".xls")){
		    	workbook = new HSSFWorkbook(inputStream);	    	
		    }
		mainJson = new JSONObject();
	    propertiesFieldsJson = new JSONObject();
	    
	    Sheet sheet = workbook.getSheetAt(0);//reading sheet one
	    int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
	    	for (int i = 1; i < rowCount+1; i++) {
		    	Row row = sheet.getRow(i);
		    	String propValue =  dataFormatter.formatCellValue(row.getCell(1)).trim().replace("FALSE()", "false").replace("TRUE()", "true");
		    	try {
			    	if(propValue.equalsIgnoreCase("true")||propValue.equalsIgnoreCase("false")) {
			    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Boolean.parseBoolean(propValue));
			    	}
			    	else {
			    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Integer.parseInt(propValue));
			    	}
		    	}catch (NumberFormatException e) {
		    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),propValue);
				}
		    	
		   }
	    	mainJson.put("properties", propertiesFieldsJson);
		    
	
		    sourceArray = new JSONArray();
		    sourceFieldObject = new JSONObject();
		    Sheet sheetThree = workbook.getSheetAt(1);//reading sheet two
			 int rowCountSheetThree = sheetThree.getLastRowNum() - sheetThree.getFirstRowNum();
			 secondOccuranceSheetThree = true;
			 ArrayList contentSheetThree = new ArrayList<>();
			 ArrayList options = new ArrayList<>();
			 String idSheetThree = "";
			 String answerSheetThree = "";
			 String imageSheetThree = "";
			    for (int i = 1; i < rowCountSheetThree+1; i++) {
			    	Row row = sheetThree.getRow(i);
			    	if(dataFormatter.formatCellValue(row.getCell(1)).trim() != "") {
		    			imagesArray.add(dataFormatter.formatCellValue(row.getCell(1)).trim());
		    		}
			    	for(int j=0;j < 5; j++) {
			    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			if(!secondOccuranceSheetThree) {
			    				sourceFieldObject.put("id", Integer.parseInt(idSheetThree));
			    				sourceFieldObject.put("answer", Integer.parseInt(answerSheetThree));
			    				sourceFieldObject.put("image", imageSheetThree);
			    				sourceFieldObject.put("content", contentSheetThree);
			    				sourceFieldObject.put("options", options);
			    				sourceArray.add(sourceFieldObject);
			    				contentSheetThree = new ArrayList();
			    				options = new ArrayList();
			    			sourceFieldObject = new JSONObject();
			    			imageSheetThree = ""; 
			    			options.clear();
			    			contentSheetThree.clear();
			    			}
			    		}
			    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			idSheetThree = dataFormatter.formatCellValue(row.getCell(j)).trim();
						}
			    		if(j==1 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			imageSheetThree =  dataFormatter.formatCellValue(row.getCell(j)).trim();
			    		}
						if(j==2 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
							contentSheetThree.add(dataFormatter.formatCellValue(row.getCell(j)).trim()); 
						}
						if(j==3 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
							options.add(dataFormatter.formatCellValue(row.getCell(j)).trim());
			    		}
						if(j==4 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
							answerSheetThree = dataFormatter.formatCellValue(row.getCell(j)).trim();
						}
						secondOccuranceSheetThree = false;
			    		}
			    	if(i==rowCountSheetThree) {
			    		sourceFieldObject.put("id", Integer.parseInt(idSheetThree));
			    		sourceFieldObject.put("id", Integer.parseInt(answerSheetThree));
			    		sourceFieldObject.put("image", imageSheetThree);
			    		sourceFieldObject.put("content", contentSheetThree);
			    		sourceFieldObject.put("options", options);
			    		sourceArray.add(sourceFieldObject);
			    		contentSheetThree = new ArrayList();
			    		options = new ArrayList();
			    		sourceFieldObject = new JSONObject();
					}
			    }
			
			
			    mainJson.put("source", sourceArray);
			    //System.out.println(mainJson);
			     //System.out.println(jOutputLocaion+"config.js");
	   			if(mainJson.isEmpty()) {
	   				log.error("please check the excel sheet format");
	   			}else {
	   				log.info("json created sucessful");
	   			}
	   		 
	   			System.out.println(mainJson);
	   		 Set<String> listWithoutDuplicates = new LinkedHashSet<String>(imagesArray);
	   	  imagesArray.clear();
	   	  imagesArray.addAll(listWithoutDuplicates);
	   		}
	  catch (Exception e) {
		  log.error(e);
		  JOptionPane.showMessageDialog(new JFrame(), "template file structure is not correct", "Dialog",
			        JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	  }
	 	return mainJson;
		
	
	
	}
	public static JSONObject readExcelMatching (String inputFile) {

		imagesArray.clear();
		DataFormatter dataFormatter = new DataFormatter();
		
				
	  try {
		//inputFile = properties.getProperty("inputExcelFile");
		inputFile = inputFile.replace("%20", " ").replace("XLSX", "xlsx");
		String fileSplitArray[] = inputFile.split("/");
		outputLocation = inputFile.replace(fileSplitArray[fileSplitArray.length-1], "");
		fileName = fileSplitArray[fileSplitArray.length-1].replace(".xlsx", "");
		//System.exit(0);
		inputStream = new FileInputStream(inputFile);
		 	if (inputFile.endsWith(".xlsx")) {
		 		workbook = new XSSFWorkbook(inputStream);
		 	}
		    else if(inputFile.endsWith(".xls")){
		    	workbook = new HSSFWorkbook(inputStream);	    	
		    }
		mainJson = new JSONObject();
	    propertiesFieldsJson = new JSONObject();
	    
	    Sheet sheet = workbook.getSheetAt(0);//reading sheet one
	    int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
	    	for (int i = 1; i < rowCount+1; i++) {
		    	Row row = sheet.getRow(i);
		    	String propValue =  dataFormatter.formatCellValue(row.getCell(1)).trim().replace("FALSE()", "false").replace("TRUE()", "true");
		    	try {
			    	if(propValue.equalsIgnoreCase("true")||propValue.equalsIgnoreCase("false")) {
			    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Boolean.parseBoolean(propValue));
			    	}
			    	else {
			    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Integer.parseInt(propValue));
			    	}
		    	}catch (NumberFormatException e) {
		    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),propValue);
				}
		    	
		   }
	    	mainJson.put("properties", propertiesFieldsJson);
	    	targetArray = new JSONArray();
		    targetFieldObject = new JSONObject();
		    
		 Sheet sheetTwo = workbook.getSheetAt(2);//reading sheet two
		 int rowCountSheetTwo = sheetTwo.getLastRowNum() - sheetTwo.getFirstRowNum();
		 secondOccurance = true;
		 ArrayList content = new ArrayList<>();
		 String id = "";
		 String image = "";
		    for (int i = 1; i < rowCountSheetTwo+1; i++) {
		    	Row row = sheetTwo.getRow(i);
		    	if(dataFormatter.formatCellValue(row.getCell(1)).trim() != "") {
	    			imagesArray.add(dataFormatter.formatCellValue(row.getCell(1)).trim());
	    		}
		    	
		    	
		    	for(int j=0;j < 3; j++) {
		    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
		    			if(!secondOccurance) {
		    			targetFieldObject.put("id", Integer.parseInt(id));
		    			targetFieldObject.put("image", image);
		    			targetFieldObject.put("content", content);
		    			targetArray.add(targetFieldObject);
		    			content = new ArrayList();
		    			targetFieldObject = new JSONObject();
		    			image = ""; 
		    			content.clear();
		    			}
		    		}
		    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
						id = dataFormatter.formatCellValue(row.getCell(j)).trim();
					}
		    		if(j==1 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
		    			image =  dataFormatter.formatCellValue(row.getCell(j)).trim();
		    		}
					if(j==2 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
					    //System.out.println(dataFormatter.formatCellValue(row.getCell(j)).trim());
					  content.add(dataFormatter.formatCellValue(row.getCell(j)).trim()); 
					}
					secondOccurance = false;
		    		}
		    	if(i==rowCountSheetTwo) {
					targetFieldObject.put("id", Integer.parseInt(id));
	    			targetFieldObject.put("image", image);
	    			targetFieldObject.put("content", content);
	    			targetArray.add(targetFieldObject);
	    			content = new ArrayList();
	    			targetFieldObject = new JSONObject();
				}
		    }
		    mainJson.put("target", targetArray);// pushing into mainJson
		    
		    sourceArray = new JSONArray();
		    sourceFieldObject = new JSONObject();
		    Sheet sheetThree = workbook.getSheetAt(1);//reading sheet three
			 int rowCountSheetThree = sheetThree.getLastRowNum() - sheetThree.getFirstRowNum();
			 secondOccuranceSheetThree = true;
			 ArrayList contentSheetThree = new ArrayList<>();
			 ArrayList sourceAns = new ArrayList<>();
			 String idSheetThree = "";
			 String imageSheetThree = "";
			    for (int i = 1; i < rowCountSheetThree+1; i++) {
			    	Row row = sheetThree.getRow(i);
			    	
			    	if(dataFormatter.formatCellValue(row.getCell(1)).trim() != "") {
		    			imagesArray.add(dataFormatter.formatCellValue(row.getCell(1)).trim());
		    		}
			    	for(int j=0;j < 4; j++) {
			    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			if(!secondOccuranceSheetThree) {
			    				sourceFieldObject.put("id", Integer.parseInt(idSheetThree));
			    				sourceFieldObject.put("image", imageSheetThree);
			    				sourceFieldObject.put("content", contentSheetThree);
			    				sourceFieldObject.put("answer", sourceAns);
			    				sourceArray.add(sourceFieldObject);
			    				contentSheetThree = new ArrayList();
			    				sourceAns = new ArrayList();
			    			sourceFieldObject = new JSONObject();
			    			imageSheetThree = ""; 
			    			sourceAns.clear();
			    			contentSheetThree.clear();
			    			}
			    		}
			    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			idSheetThree = dataFormatter.formatCellValue(row.getCell(j)).trim();
						}
			    		if(j==1 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			imageSheetThree =  dataFormatter.formatCellValue(row.getCell(j)).trim();
			    		}
						if(j==2 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
							contentSheetThree.add(dataFormatter.formatCellValue(row.getCell(j)).trim()); 
						}
						
						if(j==3 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
							sourceAns.add(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(j)).trim()));
			    		}
						secondOccuranceSheetThree = false;
			    		}
			    	if(i==rowCountSheetThree) {
			    		sourceFieldObject.put("id", Integer.parseInt(idSheetThree));
			    		sourceFieldObject.put("image", imageSheetThree);
			    		sourceFieldObject.put("content", contentSheetThree);
			    		sourceFieldObject.put("answer", sourceAns);
			    		sourceArray.add(sourceFieldObject);
			    		contentSheetThree = new ArrayList();
			    		sourceAns = new ArrayList();
			    		sourceFieldObject = new JSONObject();
					}
			    }
			
			
			    mainJson.put("source", sourceArray);
			    //System.out.println(mainJson);
			     //System.out.println(jOutputLocaion+"config.js");
	   			if(mainJson.isEmpty()) {
	   				log.error("please check the excel sheet format");
	   			}else {
	   				log.info("json created sucessful");
	   			}
	   		 
	   			//System.out.println(mainJson);
	   		 Set<String> listWithoutDuplicates = new LinkedHashSet<String>(imagesArray);
	   	  imagesArray.clear();
	   	  imagesArray.addAll(listWithoutDuplicates);
	   		}
	  catch (Exception e) {
		  log.error(e);
		  JOptionPane.showMessageDialog(new JFrame(), "template file structure is not correct", "Dialog",
			        JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	  }
	 	return mainJson;
		
	
	}
	
	
	public static JSONObject readExcelSequence(String inputFile) {

		imagesArray.clear();
		DataFormatter dataFormatter = new DataFormatter();
		
				
	  try {
		//inputFile = properties.getProperty("inputExcelFile");
		inputFile = inputFile.replace("%20", " ").replace("XLSX", "xlsx");
		String fileSplitArray[] = inputFile.split("/");
		outputLocation = inputFile.replace(fileSplitArray[fileSplitArray.length-1], "");
		fileName = fileSplitArray[fileSplitArray.length-1].replace(".xlsx", "");
		//System.exit(0);
		inputStream = new FileInputStream(inputFile);
		 	if (inputFile.endsWith(".xlsx")) {
		 		workbook = new XSSFWorkbook(inputStream);
		 	}
		    else if(inputFile.endsWith(".xls")){
		    	workbook = new HSSFWorkbook(inputStream);	    	
		    }
		mainJson = new JSONObject();
	    propertiesFieldsJson = new JSONObject();
	    
	    Sheet sheet = workbook.getSheetAt(0);//reading sheet one
	    int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
	    	for (int i = 1; i < rowCount+1; i++) {
		    	Row row = sheet.getRow(i);
		    	String propValue =  dataFormatter.formatCellValue(row.getCell(1)).trim().replace("FALSE()", "false").replace("TRUE()", "true");
		    	try {
			    	if(propValue.equalsIgnoreCase("true")||propValue.equalsIgnoreCase("false")) {
			    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Boolean.parseBoolean(propValue));
			    	}
			    	else {
			    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Integer.parseInt(propValue));
			    	}
		    	}catch (NumberFormatException e) {
		    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),propValue);
				}
		    	
		   }
	    	mainJson.put("properties", propertiesFieldsJson);
		    sourceArray = new JSONArray();
		    sourceFieldObject = new JSONObject();
		    
		 Sheet sheetTwo = workbook.getSheetAt(1);//reading sheet two
		 int rowCountSheetTwo = sheetTwo.getLastRowNum() - sheetTwo.getFirstRowNum();
		 secondOccurance = true;
		 ArrayList content = new ArrayList<>();
		 String id = "";
		 String image = "";
		    for (int i = 1; i < rowCountSheetTwo+1; i++) {
		    	Row row = sheetTwo.getRow(i);
		    	if(dataFormatter.formatCellValue(row.getCell(1)).trim() != "") {
	    			imagesArray.add(dataFormatter.formatCellValue(row.getCell(1)).trim());
	    		}
		    	
		    	
		    	for(int j=0;j < 3; j++) {
		    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
		    			if(!secondOccurance) {
		    			sourceFieldObject.put("id", Integer.parseInt(id));
		    			sourceFieldObject.put("image", image);
		    			sourceFieldObject.put("content", content);
		    			sourceArray.add(sourceFieldObject);
		    			content = new ArrayList();
		    			sourceFieldObject = new JSONObject();
		    			image = ""; 
		    			content.clear();
		    			}
		    		}
		    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
						id = dataFormatter.formatCellValue(row.getCell(j)).trim();
					}
		    		if(j==1 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
		    			image =  dataFormatter.formatCellValue(row.getCell(j)).trim();
		    		}
					if(j==2 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
					    //System.out.println(dataFormatter.formatCellValue(row.getCell(j)).trim());
					  content.add(dataFormatter.formatCellValue(row.getCell(j)).trim()); 
					}
					secondOccurance = false;
		    		}
		    	if(i==rowCountSheetTwo) {
					sourceFieldObject.put("id", Integer.parseInt(id));
	    			sourceFieldObject.put("image", image);
	    			sourceFieldObject.put("content", content);
	    			sourceArray.add(sourceFieldObject);
	    			content = new ArrayList();
	    			sourceFieldObject = new JSONObject();
				}
		    }
		    mainJson.put("source", sourceArray);// pushing into mainJson
		    
				if(mainJson.isEmpty()) {
	   				log.error("please check the excel sheet format");
	   			}else {
	   				log.info("json created sucessful");
	   			}
	   		 
	   			//System.out.println(mainJson);
	   		 Set<String> listWithoutDuplicates = new LinkedHashSet<String>(imagesArray);
	   	  imagesArray.clear();
	   	  imagesArray.addAll(listWithoutDuplicates);
	   		}
	  catch (Exception e) {
		  log.error(e);
		  JOptionPane.showMessageDialog(new JFrame(), "template file structure is not correct", "Dialog",
			        JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	  }
	 	return mainJson;
		
	
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject readExcelDragAndDrop(String inputFile) {
		imagesArray.clear();
		DataFormatter dataFormatter = new DataFormatter();
		
				
	  try {
		//inputFile = properties.getProperty("inputExcelFile");
		inputFile = inputFile.replace("%20", " ").replace("XLSX", "xlsx");
		String fileSplitArray[] = inputFile.split("/");
		outputLocation = inputFile.replace(fileSplitArray[fileSplitArray.length-1], "");
		fileName = fileSplitArray[fileSplitArray.length-1].replace(".xlsx", "");
		//System.exit(0);
		inputStream = new FileInputStream(inputFile);
		 	if (inputFile.endsWith(".xlsx")) {
		 		workbook = new XSSFWorkbook(inputStream);
		 	}
		    else if(inputFile.endsWith(".xls")){
		    	workbook = new HSSFWorkbook(inputStream);	    	
		    }
		mainJson = new JSONObject();
	    propertiesFieldsJson = new JSONObject();
	    
	    Sheet sheet = workbook.getSheetAt(0);//reading sheet one
	    int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
	    	for (int i = 1; i < rowCount+1; i++) {
		    	Row row = sheet.getRow(i);
		    	String propValue =  dataFormatter.formatCellValue(row.getCell(1)).trim().replace("FALSE()", "false").replace("TRUE()", "true");
		    	try {
			    	if(propValue.equalsIgnoreCase("true")||propValue.equalsIgnoreCase("false")) {
			    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Boolean.parseBoolean(propValue));
			    	}
			    	else {
			    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),Integer.parseInt(propValue));
			    	}
		    	}catch (NumberFormatException e) {
		    		propertiesFieldsJson.put(dataFormatter.formatCellValue(row.getCell(0)).trim(),propValue);
				}
		    	
		   }
	    	mainJson.put("properties", propertiesFieldsJson);
		    sourceArray = new JSONArray();
		    sourceFieldObject = new JSONObject();
		    
		 Sheet sheetTwo = workbook.getSheetAt(1);//reading sheet two
		 int rowCountSheetTwo = sheetTwo.getLastRowNum() - sheetTwo.getFirstRowNum();
		 secondOccurance = true;
		 ArrayList content = new ArrayList<>();
		 String id = "";
		 String image = "";
		    for (int i = 1; i < rowCountSheetTwo+1; i++) {
		    	Row row = sheetTwo.getRow(i);
		    	if(dataFormatter.formatCellValue(row.getCell(1)).trim() != "") {
	    			imagesArray.add(dataFormatter.formatCellValue(row.getCell(1)).trim());
	    		}
		    	
		    	
		    	for(int j=0;j < 3; j++) {
		    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
		    			if(!secondOccurance) {
		    			sourceFieldObject.put("id", Integer.parseInt(id));
		    			sourceFieldObject.put("image", image);
		    			sourceFieldObject.put("content", content);
		    			sourceArray.add(sourceFieldObject);
		    			content = new ArrayList();
		    			sourceFieldObject = new JSONObject();
		    			image = ""; 
		    			content.clear();
		    			}
		    		}
		    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
						id = dataFormatter.formatCellValue(row.getCell(j)).trim();
					}
		    		if(j==1 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
		    			image =  dataFormatter.formatCellValue(row.getCell(j)).trim();
		    		}
					if(j==2 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
					    //System.out.println(dataFormatter.formatCellValue(row.getCell(j)).trim());
					  content.add(dataFormatter.formatCellValue(row.getCell(j)).trim()); 
					}
					secondOccurance = false;
		    		}
		    	if(i==rowCountSheetTwo) {
					sourceFieldObject.put("id", Integer.parseInt(id));
	    			sourceFieldObject.put("image", image);
	    			sourceFieldObject.put("content", content);
	    			sourceArray.add(sourceFieldObject);
	    			content = new ArrayList();
	    			sourceFieldObject = new JSONObject();
				}
		    }
		    mainJson.put("source", sourceArray);// pushing into mainJson
		    
		    targetArray = new JSONArray();
		    targetFieldObject = new JSONObject();
		    Sheet sheetThree = workbook.getSheetAt(2);//reading sheet three
			 int rowCountSheetThree = sheetThree.getLastRowNum() - sheetThree.getFirstRowNum();
			 secondOccuranceSheetThree = true;
			 ArrayList contentSheetThree = new ArrayList<>();
			 ArrayList sourceAns = new ArrayList<>();
			 String idSheetThree = "";
			 String imageSheetThree = "";
			    for (int i = 1; i < rowCountSheetThree+1; i++) {
			    	Row row = sheetThree.getRow(i);
			    	if(dataFormatter.formatCellValue(row.getCell(1)).trim() != "") {
		    			imagesArray.add(dataFormatter.formatCellValue(row.getCell(1)).trim());
		    		}
			    	for(int j=0;j < 4; j++) {
			    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			if(!secondOccuranceSheetThree) {
			    				targetFieldObject.put("id", Integer.parseInt(idSheetThree));
			    				targetFieldObject.put("image", imageSheetThree);
			    				targetFieldObject.put("content", contentSheetThree);
			    				targetFieldObject.put("source", sourceAns);
			    				targetArray.add(targetFieldObject);
			    				contentSheetThree = new ArrayList();
			    				sourceAns = new ArrayList();
			    			targetFieldObject = new JSONObject();
			    			imageSheetThree = ""; 
			    			sourceAns.clear();
			    			contentSheetThree.clear();
			    			}
			    		}
			    		if(j==0 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			idSheetThree = dataFormatter.formatCellValue(row.getCell(j)).trim();
						}
			    		if(j==1 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
			    			imageSheetThree =  dataFormatter.formatCellValue(row.getCell(j)).trim();
			    		}
						if(j==2 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
							contentSheetThree.add(dataFormatter.formatCellValue(row.getCell(j)).trim()); 
						}
						
						if(j==3 && dataFormatter.formatCellValue(row.getCell(j)).trim() != "") {
							sourceAns.add(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(j)).trim()));
			    		}
						secondOccuranceSheetThree = false;
			    		}
			    	if(i==rowCountSheetThree) {
			    		targetFieldObject.put("id", Integer.parseInt(idSheetThree));
			    		targetFieldObject.put("image", imageSheetThree);
			    		targetFieldObject.put("content", contentSheetThree);
			    		targetFieldObject.put("source", sourceAns);
			    		targetArray.add(targetFieldObject);
			    		contentSheetThree = new ArrayList();
			    		sourceAns = new ArrayList();
			    		targetFieldObject = new JSONObject();
					}
			    }
			
			
			    mainJson.put("target", targetArray);
			    //System.out.println(mainJson);
			     //System.out.println(jOutputLocaion+"config.js");
	   			if(mainJson.isEmpty()) {
	   				log.error("please check the excel sheet format");
	   			}else {
	   				log.info("json created sucessful");
	   			}
	   		 
	   			//System.out.println(mainJson);
	   		 Set<String> listWithoutDuplicates = new LinkedHashSet<String>(imagesArray);
	   	  imagesArray.clear();
	   	  imagesArray.addAll(listWithoutDuplicates);
	   		}
	  catch (Exception e) {
		  log.error(e);
		  JOptionPane.showMessageDialog(new JFrame(), "template file structure is not correct", "Dialog",
			        JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	  }
	 	return mainJson;
		
	}
}
	