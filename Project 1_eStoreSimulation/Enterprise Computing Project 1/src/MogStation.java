/*Name: Christian D. Rosado Arroyo
 Course: CNT 4714 – Fall 2021
 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday September 12, 2021
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import java.util.*;

public class MogStation extends JFrame{
	
	
	//ATTRIBUTES
	ArrayList<Item> inventory;
	Order order = new Order();
	
	//Labels For  Text Fields
	JLabel NumItems = new JLabel("Enter number of items in this order:");
	JLabel itemID = new JLabel("Enter Item ID for Item #1:");
	JLabel Quantity = new JLabel("Enter Quantity for Item #1:");
	JLabel ItemInfo = new JLabel("Item #1 Info:");
	JLabel Subtotal = new JLabel("Order subtotal for 0 item(s):");
	
	//Text Fields For User Input 
	JTextField textNumItems = new JTextField();
	JTextField textItemID = new JTextField();
	JTextField textQuantity = new JTextField();
	JTextField textItemInfo = new JTextField();
	JTextField textTotalItems = new JTextField();

	//User Buttons
	JButton ProcessItemButton = new JButton("Process Item #1");
	JButton ConfirmItemButton = new JButton("Confirm Item #1");
	JButton ViewOrderButton = new JButton("View Order");
	JButton FinishOrderButton = new JButton("Finish Order");
	JButton NewOrderButton = new JButton("New Order");
	JButton ExitButton = new JButton("Exit");

	//MOGSTATION CONSTRUCTOR                                    
	public MogStation() throws FileNotFoundException{
		
		//Load Store Inventory into an ArrayList 
		getInventoryFromFile();
		
		//Change Font Size of Labels 
	    NumItems.setFont(new Font("", Font.BOLD,18));
		itemID.setFont(new Font("", Font.BOLD,18));
		Quantity.setFont(new Font("", Font.BOLD,18));
		ItemInfo.setFont(new Font("", Font.BOLD,18));
		Subtotal.setFont(new Font("", Font.BOLD,18));
		
		
		//textNumItems.setBackground(Color.DARK_GRAY);
		//textItemID.setBackground(Color.DARK_GRAY);
		//textQuantity.setBackground(Color.DARK_GRAY);
		textItemInfo.setBackground(Color.DARK_GRAY);
		textTotalItems.setBackground(Color.DARK_GRAY);
		
		//Grid Layout: Panel 1 Holds Labels & TextFields.
		//Space between labels & fields: Height: 20
		JPanel panel1 = new JPanel(new GridLayout(5,2,2,20));   
		
		//Number of Items Label & Text Field
		panel1.add(NumItems);
		panel1.add(textNumItems);
		
		//ItemID Label & Text Field
		panel1.add(itemID); 
		panel1.add(textItemID); 
		
		//Quantity Label & Text Field
		panel1.add(Quantity);
		panel1.add(textQuantity);
		
		//ItemInfo Label & Text Field
		panel1.add(ItemInfo);
		panel1.add(textItemInfo);
		
		//Subtotal Label & TextField
		panel1.add(Subtotal);
		panel1.add(textTotalItems);
		
	
		//Add Image to Panel
		BufferedImage storeLogo;
		try{
			storeLogo = ImageIO.read(new File("Hello Kupo!.png"));
		} catch (IOException e2){
			
			e2.printStackTrace();
			
		}
		
		JLabel logoLabel = new JLabel(new ImageIcon("Hello Kupo!.png"));
		add(logoLabel);
	
			
		//Panel 2 Holds Buttons                                    
		JPanel panel2 = new JPanel(new GridLayout(1,1,1,10));
		
		panel2.add(ProcessItemButton);          
		panel2.add(ConfirmItemButton);
		panel2.add(ViewOrderButton);
		
		panel2.add(FinishOrderButton);
		panel2.add(NewOrderButton);
		panel2.add(ExitButton);
		
		//deactivate buttons
		this.ConfirmItemButton.setEnabled(false);
		this.ViewOrderButton.setEnabled(false);
		this.FinishOrderButton.setEnabled(false);
		
		//deactivate textfields
		this.textTotalItems.setEnabled(false);
		this.textItemInfo.setEnabled(false);
		
		//add the panels to the frame
		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.SOUTH);
		
		
		//Buttons Action Listeners:
		//PROCESS ITEM BUTTON LISTENER
		ProcessItemButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				
				//Get text from text field and parse into int 
				int numOfItemsInOrder = Integer.parseInt(textNumItems.getText().trim()); 
				int quantityOfItem = Integer.parseInt(textQuantity.getText().trim());
				int itemID = Integer.parseInt(textItemID.getText().trim()); 
									
				//Set Total Num of items & Disable Button 
				if(order.getMaxItems() == -1 && numOfItemsInOrder > 0) {
					order.setMaxItems(numOfItemsInOrder);
					textNumItems.setEnabled(false);
				}
				
				//Search for the item
				int itemElementIndex = linearSearch(itemID); //if found returns 1
				
				//If item is found then:
				if(itemElementIndex != -1) 
				{
																				
					//Grab the item's info from Inventory ArrayList
					Item checkForItem = inventory.get(itemElementIndex);
					
					//Check if item is in Stock 
					if(checkForItem.getStockStatus().equals("false")) { 
					
					//Panels Message Title
					JOptionPane.showMessageDialog(null, "Sorry... that item is out of stock, please try another item", "MogStation - ERROR", JOptionPane.INFORMATION_MESSAGE);	
					
					textQuantity.setText("");
					textItemID.setText("");
					
					return;
					}
					
										
					//For Order with no Discount
					if(order.getDiscountPercentage(quantityOfItem) == 0){ 
					
						order.setOrderSubtotalNoDiscount(quantityOfItem, checkForItem.getPrice());
					}
					
					//For Order with Discount
					else{
						order.setOrderSubtotal(quantityOfItem, checkForItem.getPrice());   
					}
					
					
					//Prints on Transaction Text File
					order.setItemInfo(checkForItem.getItemID() + "", checkForItem.getName(), checkForItem.getPrice() + "", quantityOfItem 
					+ "", new DecimalFormat("#0.0").format(order.getDiscountPercentage(quantityOfItem)) + "", new DecimalFormat("$#0.00").format( order.getCurrentItemSubtotal()) + "");

					
					//Prints Info on View Order and Final Receipt 
					String itemInfo = (checkForItem.getItemID() + checkForItem.getName() +  " $" + checkForItem.getPrice() + " " + quantityOfItem + " " + 
					order.getDiscountPercentage(quantityOfItem) + "% " +"$" + new DecimalFormat("#0.00").format( order.getCurrentItemSubtotal())); 

					//Disable/Enable Needed buttons and fields																	
					textItemInfo.setText(itemInfo);		
					ConfirmItemButton.setEnabled(true);
					textItemInfo.setEnabled(false);
					textTotalItems.setEnabled(false);
					ProcessItemButton.setEnabled(false);						
				}
				//Item not found display error
				else 
				{
					JOptionPane.showMessageDialog(null, "Item ID " + itemID + " not in file.", "MogStation - ERROR", JOptionPane.INFORMATION_MESSAGE);
				}
			}
					
		});
		
		
		//CONFIRM ITEM BUTTON LISTENER
		ConfirmItemButton.addActionListener(new ActionListener(){		
			@Override
			public void actionPerformed(ActionEvent e) 
			{
										
				int quantityOfItem = Integer.parseInt(textQuantity.getText().trim());
											
				//Keeps count of Total Processed items vs maxItems to be processed
				order.settotalQuantityOfItems(quantityOfItem); 
					
				//Count of total items
				order.setTotalItems(order.getTotalItems() + 1);
				
				
				JOptionPane.showMessageDialog(null, "Item #" + order.getTotalItems() + " accepted. Added to your cart.", "MogStation - Item Confirmed", JOptionPane.INFORMATION_MESSAGE);
				
				//Creating transaction txt file
				order.transactionTxt();
				
				//Adds item to View Order Pop up Window
				order.itemInfoViewOrder(textItemInfo.getText().trim());
				
		
				//Enable/disable necessary buttons & text field
				ProcessItemButton.setEnabled(true);
			    ViewOrderButton.setEnabled(true);
				FinishOrderButton.setEnabled(true);
				ConfirmItemButton.setEnabled(false);
				textNumItems.setEnabled(false);
				
				//Update Button's text only if it isnt the last item
				if(order.getTotalItems() != order.getMaxItems()) { 
				
				ProcessItemButton.setText("Process Item #" + (order.getTotalItems() + 1));
				ConfirmItemButton.setText("Confirm Item #" + (order.getTotalItems() + 1));
				
				}
				
				//Removes # from button after last item is processed 
				else {
				ProcessItemButton.setText("Process Item");
				ConfirmItemButton.setText("Confirm Item");	
					
					
				}
				
				//Clear Text Fields for next Items User Input 
				textQuantity.setText("");
				textItemID.setText("");
				
				textTotalItems.setText("$" +  new DecimalFormat("#0.00").format(order.getOrderSubtotal()));
				
				//Increment items # on j Labels 
				Subtotal.setText("Order subtotal for " + order.gettotalQuantityOfItems() + " item(s)");
				itemID.setText("Enter ID for Item #" + (order.getTotalItems() + 1) + ":");
				Quantity.setText("Enter quantity for Item #" + (order.getTotalItems() + 1) + ":");
				
		
				//Do not update Label # to next item if we are already at the last item
				if(order.getTotalItems() != order.getMaxItems()) { 
				ItemInfo.setText("Item #" + (order.getTotalItems() + 1) + " info:");
				}
				
				//If on Last Order Item then:
				if(order.getTotalItems() == order.getMaxItems()) { 
									
				    itemID.setVisible(false);
					Quantity.setVisible(false);
					ProcessItemButton.setEnabled(false);
					ConfirmItemButton.setEnabled(false);
					textQuantity.setEnabled(false);
					textItemID.setEnabled(false);
				}
							
			}
		
		
		});
		
		
		//VIEW ORDER BUTTON ACTION
		ViewOrderButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, order.getViewOrder(), "MogStation - Current Shopping Cart Status", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		
		//FINISH ORDER BUTTON ACTION
		FinishOrderButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Print items on transactions txt
				try {
					order.printTransactions();
					
					//Final Invoice Pop up Message
					JOptionPane.showMessageDialog(null, order.getFinishOrder(), "MogStation - Final Invoice", JOptionPane.INFORMATION_MESSAGE);

				} catch (IOException e1) {
					e1.printStackTrace();
				}
				MogStation.super.dispose(); 
			}
		});
		
		
		//FULLY DISPOSE OF WINDOW
		ExitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MogStation.super.dispose(); //dispose frame
			}
		});
		
		//FULLY DISPOSE OF WINDOW 
		NewOrderButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				MogStation.super.dispose(); 
				
				try {
					MogStation.main(null);
				} catch (FileNotFoundException e1) {
					
					e1.printStackTrace();
				}
			}
		});
			
	}
	
	//***** END OF CONSTRUCTOR ******

    //METHODS
	//GET INVENTORY INFO FROM FILE
	public void getInventoryFromFile() throws FileNotFoundException {
		
		//Load items into an ArrayList
		this.inventory = new ArrayList<Item>();
		File file = new File("inventory.txt");
		Scanner scanTextFile = new Scanner(file);
		

		//Inventory to ArrayList
		while (scanTextFile.hasNextLine()) {
								
			// Takes next inventory line and parse into 4 strings with item id, title, item price and stockStatus
			String inventoryData = scanTextFile.nextLine();
			String[] itemInfo = inventoryData.split(",");
												
			//CREATING ITEM OBJECT 	
			Item currentItem = new Item();  
			
			currentItem.setItemID(Integer.parseInt(itemInfo[0]));

			currentItem.setName(itemInfo[1]);

			currentItem.setPrice(Double.parseDouble(itemInfo[3]));
			
			currentItem.setStockStatus(itemInfo[2].trim()); 
			  												
			//Adds item to ArrayList
			inventory.add(currentItem);
			
		}
		
		//CLose scanner
		scanTextFile.close();
	}

	//Getter
	public ArrayList<Item> getInventory() {
		return inventory;
	}
    
	//Setter
	public void setInventory(ArrayList<Item> inventory) {
		this.inventory = inventory;
	}
	
	//LINEAR SEARCH
	public int linearSearch(int ItemID) {
		
		for(int i = 0; i < this.inventory.size(); i++) {
			
			Item currentItem = inventory.get(i);
			if(currentItem.getItemID() == ItemID)
				return i;
		}
		
		return -1;
	}
	
	
	//*****MAIN*****
	public static void main(String[] args) throws FileNotFoundException {
		
		MogStation frame = new MogStation();
		
		//Sets size and Location of Window
		frame.pack(); // fit windows for screen
		frame.setSize(1000,500);
		frame.setLocationRelativeTo(null); // center windows
		
		//Program Title
		frame.setTitle("Mog Station");
		
		//Fully Close Window
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		
		//Display Window
		frame.setVisible(true); 
		
		//Not Resizable
		frame.setResizable(false);
		
		//Modify Window's Color
		frame.getContentPane().setBackground(Color.white);
		
		//Program Icon: Loads Image
		ImageIcon image = new ImageIcon("MogStore.jpg");
		
		//Set New Image Icon
		frame.setIconImage(image.getImage());
		
		
		
	}
}