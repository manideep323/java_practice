package com.fortunapix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UrlChangeNo {
public static void main(String[] args) {
	int rightNo[] = {5};

	

	
	/*for(int i=1;i<=rightNo.length;i++) {
		
		for(int j=1;j<=rightNo[i-1];j++) {
			System.out.println("urltext"+i+"_"+j+".html");
		}
	}*/
	int flag=0;int count=0;
	ArrayList listContainingDuplicates=new ArrayList<Integer>();
	listContainingDuplicates.add(1);
	listContainingDuplicates.add(1);
	listContainingDuplicates.add(1);
	listContainingDuplicates.add(2);
	listContainingDuplicates.add(2);
	listContainingDuplicates.add(3);
	listContainingDuplicates.add(4);
	
	
	
	Object[]obj =listContainingDuplicates.toArray();
	
	
	int count1=0;
	for(int i=0;i<obj.length;i++) {
		count1=0;
		for(int j=0;j<obj.length;j++) {
			if(obj[i]==obj[j]) {
				count1++;
			}
			
		}
	if(count1==1) {
		System.out.println(":::::::::::"+obj[i]);
	}
	
	
	
	}
	
	
	
	
	
	/*for(int i = 0; i < obj.length; i++)
     {
         for(int j = 0; j < obj.length; j++)
         {
             if(i != j)
             {
                 if(obj[i] != obj[j])
                 {
                     flag = 1;
                 }
                 else
                 {
                     flag = 0;
                     break;
                 }
             }
         }
         if(flag == 1)
         {
             count++;
             
             
             System.out.print(obj[i]+" ");
         }
     }
	
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}}
