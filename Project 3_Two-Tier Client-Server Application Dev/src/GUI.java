import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.sql.*;

public class GUI extends JFrame{
	//ATTRIBUTES
	//LABELS
	private JLabel jdbcDriver;
	private JLabel dataBaseURLLabel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JLabel connectionLabel;
	private JLabel title;
	private JLabel subTitle;
	
	private JLabel resultWindow;
	
	//TEXT FIELDS 
	private JTextField usernameField;
	private JPasswordField passwordField;
	
	//DROP DOWN OPTIONS
	private JComboBox driver;
	private JComboBox dataBaseURL;
	
	//BUTTONS 
    private JButton connectToDB;
	private JButton clearCommand;
	private JButton executeCommand;
	private JButton clearResults;
	
	//COMMAND TEXT BOX 
	private JTextArea commandTextBox;
	
	//RESULTS TABLE
	private JTable table;
	private ResultSetTableModel resultTable = null;
	
	//CHECKS CONNECTION STATUS
	private Connection connection;
	private boolean dataBaseConnection = false;
    
	//GUI 
	public GUI() throws ClassNotFoundException, SQLException, IOException{
		
		 //Creates Initial GUI
		 this.buildGUI();
		 	 
		 //Modifying GUI's look:
		 //Buttons(middle) Panel
	     JPanel midButtonPanel = new JPanel(new GridLayout(5,5));//change Button's Panel Size (5 "buttons")
	     connectionLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 20));
	     
	     //Order in which Buttons Appear
	     midButtonPanel.add(this.connectionLabel);//Connection Panel Label 
		 midButtonPanel.add(this.connectToDB);//button
		 midButtonPanel.add(this.clearCommand);//button
		 midButtonPanel.add(this.executeCommand);//button 
		 midButtonPanel.add(this.resultWindow);//label
		
		 //Panel for textfield labels 
		 JPanel textFieldLabel = new JPanel(new GridLayout(6,2,0,30)); 
		
		 //Enter info labe;
		 textFieldLabel.add(this.title);
		 textFieldLabel.add(this.subTitle);
		 
		 //Driver List Label/Field
		 textFieldLabel.add(this.jdbcDriver);
		 textFieldLabel.add(this.driver);
		 
		 //URL Label/Field
		 textFieldLabel.add(this.dataBaseURLLabel);
		 textFieldLabel.add(this.dataBaseURL);
		 
		 //Username Label/Field
		 textFieldLabel.add(this.usernameLabel);
		 textFieldLabel.add(this.usernameField);
		 
		 //Password Label/Field
		 textFieldLabel.add(this.passwordLabel);
		 textFieldLabel.add(this.passwordField);
		
				 	 
		 //Change Fonts of Labels
		 jdbcDriver.setFont(new Font("Showcard Gothic", Font.PLAIN,16));
		 dataBaseURLLabel.setFont(new Font("Showcard Gothic", Font.PLAIN,16));
		 usernameLabel.setFont(new Font("Showcard Gothic", Font.PLAIN,16));
		 passwordLabel.setFont(new Font("Showcard Gothic", Font.PLAIN,16));
		 
		 //Button Fonts
		 connectToDB.setFont(new Font("Showcard Gothic",Font.PLAIN, 16)); 
		 clearCommand.setFont(new Font("Showcard Gothic",Font.PLAIN, 16)); 
		 executeCommand.setFont(new Font("Showcard Gothic",Font.PLAIN, 16)); 
		 resultWindow.setFont(new Font("Showcard Gothic",Font.PLAIN, 20)); 
		 
		 //Button Colors
		 Color purple = new Color(102, 0, 204);
		 Color orange = new Color(255, 153, 51);
		 
		 connectToDB.setBackground(purple);
		 connectToDB.setForeground(Color.green);
		 
		 clearCommand.setBackground(Color.white);
		 clearCommand.setForeground(Color.red);
		 
		 executeCommand.setBackground(purple);
		 executeCommand.setForeground(orange);
		 
		 resultWindow.setForeground(Color.red);
		 		
		 //GUI's Top Panel 
		 JPanel top = new JPanel(new GridLayout(1, 2));
		 top.add(textFieldLabel);
		 
		//Commands Text Area
		 top.add(commandTextBox);
	 	 			
		 //Results Bottom Panel 
		 JPanel bottom = new JPanel();
		 
		//Bottom Panel Size/Layout
		 bottom.setLayout(new BorderLayout(1,0));
         
		 //Results Table Display Area
		 bottom.add(new JScrollPane(table), BorderLayout.NORTH);
		 bottom.setBackground(Color.white);
		 
		 //Size of Clear Results Window Button
		 bottom.add(clearResults, BorderLayout.SOUTH);
		 clearResults.setPreferredSize(new Dimension(0,60));	 
		
	     //Sets Panels to the Frame
		 add(top, BorderLayout.NORTH);
		 add(midButtonPanel, BorderLayout.CENTER); 
		 add(bottom, BorderLayout.SOUTH);  
		 					      
	     //ACTION LISTENERS:
		//CONNECT TO DATABASE BUTTON ACTION
		connectToDB.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent arg0){
				//Loads jdbc driver
				try
				{
					Class.forName(String.valueOf(driver.getSelectedItem())); 
				} catch (ClassNotFoundException e) {	
				
					e.printStackTrace();
					
					 //Clean Table from results
					 table.setModel(new DefaultTableModel());
					 resultTable = null;
				}
				
				
				//Connect to DB & Check
				try 
				{    
					//If already connected then
					if(dataBaseConnection == true)
					{
						connection.close();
				     
						//Initially Connection status is set to False
						dataBaseConnection = false;
						
						//Clean Table from results 
						table.setModel(new DefaultTableModel());
						resultTable = null;
					}
				 
					//Otherwise:
					connection = DriverManager.getConnection(String.valueOf(dataBaseURL.getSelectedItem()), usernameField.getText(), passwordField.getText());
					
					//Change connection status color to green after connecting 
					connectionLabel.setText("Connected to " + String.valueOf(dataBaseURL.getSelectedItem()));
					connectionLabel.setForeground(Color.GREEN);
					
					//Connection Status is True
					dataBaseConnection = true;
				} catch (SQLException e){
					
					connectionLabel.setForeground(Color.RED);
					connectionLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 30));
					
					//Clean table results
					table.setModel(new DefaultTableModel());
					resultTable = null;
					e.printStackTrace();
				}
				
			}
			
		});
		//****END OF CONNECT TO DATABASE*****
		
		//EXECUTE COMMAND BUTTON & ACTION
		executeCommand.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				
				//create an instance of abstract table model is we never had one before
				if(resultTable == null && dataBaseConnection == true)
				{
					try 
					{
						//Read Query
						resultTable = new ResultSetTableModel(commandTextBox.getText(), connection);
						table.setModel(resultTable);
						
					} catch (ClassNotFoundException | SQLException | IOException e)
					
					{
						//Clear Results
						 table.setModel(new DefaultTableModel());
						 resultTable = null;
						 
						//Warning Error:
						 JOptionPane.showMessageDialog( null, 
			                        e.getMessage(), "Database error", 
			                        JOptionPane.ERROR_MESSAGE );
						e.printStackTrace();
					}
				}
				
				else
					if(resultTable != null && dataBaseConnection == true){
					//CHECK if its a Query
					String query = commandTextBox.getText(); 
					if(query.contains("select")){
						try 
						{
							resultTable.setQuery(query);
									
						}catch (IllegalStateException | SQLException e) {
						
							 table.setModel(new DefaultTableModel());
							 resultTable = null;
							 
							//Warning Error:
							 JOptionPane.showMessageDialog( null, 
				                        e.getMessage(), "Database error", 
				                        JOptionPane.ERROR_MESSAGE );
						
							e.printStackTrace();
						}
					}
					
					//Check if its an Update
					else
					{
						try 
						{
							resultTable.setUpdate(query);
							//clear table
							 table.setModel(new DefaultTableModel());
							 resultTable = null;
						} catch (IllegalStateException | SQLException e) 
						{
							//clear table
							 table.setModel(new DefaultTableModel());
							 resultTable = null;
							//display warning
							 JOptionPane.showMessageDialog( null, 
				                        e.getMessage(), "Database error", 
				                        JOptionPane.ERROR_MESSAGE );
							 
							e.printStackTrace();
						}
					}
				}
															
			}
			
		});
		
		
		//CLEAR COMMAND BUTTON ACTION
		this.clearCommand.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				commandTextBox.setText("");
			}
			
		});
		
		//Clear results window 
		this.clearResults.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				
				table.setModel(new DefaultTableModel()); //clears results 
				resultTable = null;
				
			}
			
		});
	      
	      //Disconnects From DataBase when User Closer the Program
	      addWindowListener(new WindowAdapter(){
	            public void windowClosed( WindowEvent event ){
	            	try 
	            	{
						if(!connection.isClosed()) {
							connection.close();
						}
					}catch (SQLException e){
		
						e.printStackTrace();
					}
	            } 
	         });   
				
	}
	
	//BUILD GUI METHOD
	public void buildGUI() throws ClassNotFoundException, SQLException, IOException{
		
		String[] driverStr1 = { "com.mysql.cj.jdbc.Driver"};
		
		//DataBase Address 
		String[] urlString = { "jdbc:mysql://localhost:3306/project3"};  
		String driverStr2 = ("jdbc:mysql://localhost:3306/bikedb");
	    
		Color purple = new Color(102, 0, 204);
		
		//Labels
		this.title = new JLabel("Enter DataBase Information:");
		title.setFont(new Font("Showcard Gothic", Font.PLAIN, 20));
		title.setForeground(Color.red);
		
		this.subTitle = new JLabel("");
		
		this.jdbcDriver = new JLabel("JDBC Driver:");
		this.dataBaseURLLabel = new JLabel("Database URL:");
		this.usernameLabel = new JLabel("Username:");
		this.passwordLabel = new JLabel("Password:");
		this.connectionLabel = new JLabel(" No Connection!");	
		this.connectionLabel.setForeground(Color.RED);
		
		this.resultWindow = new JLabel("SQL Execution Result Window:");
				
		//Create Drop Down Menus
		this.driver = new JComboBox(driverStr1);
				
		this.driver.setSelectedIndex(0);
		this.driver.setBackground(Color.white);
		
		this.dataBaseURL = new JComboBox(urlString);
		dataBaseURL.addItem(driverStr2);
		this.dataBaseURL.setBackground(Color.white);

		//Create Text Fields
		this.usernameField = new JTextField();
		this.passwordField = new JPasswordField();

		//Create Text Area
		this.commandTextBox = new JTextArea(4, 50);
		commandTextBox.setBackground(Color.lightGray);
		this.commandTextBox.setWrapStyleWord(true);
		this.commandTextBox.setLineWrap(true);

		//Create Buttons & Set Font & Color
		this.connectToDB = new JButton("Connect to Database");
		this.connectToDB.setPreferredSize(new Dimension(2,40));	
	
		this.clearCommand = new JButton("Clear SQL Command");
		this.executeCommand = new JButton("Execute SQL Command");
		this.clearResults = new JButton("Clear Result Window");
		
		clearResults.setFont(new Font("Showcard Gothic", Font.PLAIN, 20));
			
		clearResults.setBackground(Color.white); //set color of button 
		clearResults.setForeground(Color.red);
		
		//Create the Table
		this.table = new JTable();
	}
}
