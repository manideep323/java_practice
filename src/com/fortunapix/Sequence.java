package com.fortunapix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;














public class Sequence
{
  static String filePath = new File("").getAbsolutePath() + "\\";
  static BufferedWriter bw = null;
  static FileWriter fw = null;
  static String fileDestinationPath = filePath + "sequenceGeneratedFiles\\";
  static String templateCopingPath = filePath + "needtocopy";
  static ArrayList listContainingDuplicates = new ArrayList();
  


  static String filePath1 = new File("").getAbsolutePath() + "\\";
  static BufferedWriter bw1 = null;
  static FileWriter fw1 = null;
  static String fileDestinationPath1 = filePath1 + "sequenceGeneratedFiles\\";
  static String templateCopingPath1 = filePath1 + "needtocopy1";
  

  public Sequence() {}
  

  public static void main(String[] args)
    throws IOException
  {
    JFrame frame = new JFrame("Button Example");
    JButton button = new JButton("type1 slides generate");
    JButton button2 = new JButton("type2 slides generate");
    button.setBounds(150, 100, 200, 40);
    button2.setBounds(150, 200, 200, 40);
    frame.add(button);
    frame.add(button2);
    frame.setBounds(700, 350, 500, 400);
    frame.setLayout(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(3);
    
    //button.addActionListener(new Sequence.1());
    
























































































































































































































































































































    //button2.addActionListener(new Sequence.2());
  }
  









































































































  private static ArrayList uniqueValues(ArrayList listContainingDuplicates)
  {
    int flag = 0;int count = 0;
    ArrayList nonDuplicateValues = new ArrayList();
    Object[] obj = listContainingDuplicates.toArray();
    int count1 = 0;
    for (int i = 0; i < obj.length; i++) {
      count1 = 0;
      for (int j = 0; j < obj.length; j++) {
        if (obj[i].equals(obj[j])) {
          count1++;
        }
      }
      
      if (count1 == 1)
      {
        nonDuplicateValues.add("Activity" + obj[i]);
      }
    }
    
    return nonDuplicateValues;
  }
  


  private static Set<Object> findDuplicateActivites(ArrayList<Object> listContainingDuplicates)
  {
    Set<Object> setToReturn = new HashSet();
    Set<Object> set1 = new HashSet();
    
    for (Object yourInt : listContainingDuplicates)
    {

      if (!set1.add(yourInt))
      {

        setToReturn.add(yourInt);
      }
    }
    return setToReturn;
  }
}
