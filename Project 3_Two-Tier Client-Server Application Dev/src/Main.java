/*Name: Christian D. Rosado Arroyo
 Course: CNT 4714 – Fall 2021
 
 Assignment title: Project 3 - Project Three: Two-Tier Client-Server 
 Application Development With MySQL and JDBC
 
 Date: Sunday October 28, 2021
*/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Main{
	
	
	//MAIN
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException 
	{
		
		//Create GUI and Display it 
		//GUI extends JFrame
						
		GUI frame = new GUI();
		
		frame.setVisible(true);
		frame.pack();
		frame.setLayout(new BorderLayout(2,0));
		
		frame.setLocationRelativeTo(null); 
		frame.setTitle("Project Three: Two-Tier Client-Server Application Development With MySQL and JDBC");
	
		//Not Resizable
		frame.setResizable(false);
		
		//Fully Close Window
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
				
		//Program Icon: Loads Image
		ImageIcon image = new ImageIcon("src/jack.png");
					
		//Set New Image Icon
		frame.setIconImage(image.getImage());
							
	}	
	
}

