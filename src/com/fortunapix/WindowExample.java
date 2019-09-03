package com.fortunapix;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;  
import java.awt.event.WindowListener;  
public class WindowExample extends Frame{  
    WindowExample(){  
        addWindowListener(new WindowAdapter(){  
            public void windowClosing(WindowEvent e) {  
                dispose();  
            }  
        });  
        setSize(400,400);  
        setLayout(null);  
        setVisible(true);  
    }  
public static void main(String[] args) {  
    new WindowExample();  
} }