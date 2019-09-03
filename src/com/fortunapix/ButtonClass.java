package com.fortunapix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ButtonClass {
	static boolean t=false;
	static void ButtonExample(){    
		
		JFrame f=new JFrame("Button Example");            
		JButton b=new JButton("hi");
		b.setBounds(100,100,100, 40);    
		f.add(b);    
		f.setSize(300,400);    
		f.setLayout(null);    
		f.setVisible(true);    
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		System.out.println(b.isSelected());
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				t=true;
				System.out.println("hii");
				JFrame f=new JFrame("Button Example");            
				JButton b=new JButton("ok");
				b.setBounds(100,100,100, 40);    
				f.add(b);    
				f.setSize(300,400);    
				f.setLayout(null);    
				f.setVisible(true);    
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
				b.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
					}
				});
			}
		});
		System.out.println(t);
	if(t) {
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	}         
	
		public static void main(String[] args) {    
		    ButtonExample();    
		}    
}
