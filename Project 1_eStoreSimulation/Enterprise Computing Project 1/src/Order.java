import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Order { 
	//ATTRIBUTES 
	private int totalQuantityOfItems = 0;
	private double orderSubtotal = 0;
	
	private int totalItems = 0;
	private int maxItems = -1;
	//private String inStock;
	
	private double currentItemSubtotal = 0;
	private double orderTotal = 0;
	private StringBuilder viewOrder = new StringBuilder();
	private StringBuilder finishOrder = new StringBuilder();
	private String filename = "transactions.txt";
	private ArrayList<String> items = new ArrayList<>();
	
	
	File file = new File(filename);
	String[] itemInfo = new String[6];
	
	
	//*****METHODS*****
	public String getFinishOrder() {
		return this.finishOrder.toString();
	}
	
	
	public void setFinishOrder(String date, String time) {
		this.setOrderTotal();
		
		
		//Date
		this.finishOrder.append("Date: " + date + " " + time);
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		
		//Num of items...
		this.finishOrder.append("Number of line items: " + this.getTotalItems());
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		
		//Item/Id/...
		this.finishOrder.append("Item# / ID / Title/Price / Qty / Disc %/ Subtotal");
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		
		//ViewOrder
		this.finishOrder.append(this.getViewOrder());
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		
		//Order subtotal...
		this.finishOrder.append("Order subtotal:   $" + new DecimalFormat("#0.00").format(this.getOrderSubtotal()));
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		
		//Tax rate...
		this.finishOrder.append("Tax rate:     6%");
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		
		//Tax amount...
		this.finishOrder.append("Tax amount: $" + new DecimalFormat("#0.00").format(this.getOrderSubtotal() * 0.06)); 
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		
		//Order total....
		this.finishOrder.append("Order total:      $" + new DecimalFormat("#0.00").format(this.getOrderTotal()));
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		
		//Goodbye Message
		this.finishOrder.append("Thanks Kupo!");


	}
	
	
	public String[] getItemInfo() {
		return itemInfo;
	}

	
	public String getViewOrder() {
		return this.viewOrder.toString();
	}
	
	public void itemInfoViewOrder(String order) {
		
		//Prints item # on list form
		viewOrder.append(this.getTotalItems() + ". " + order);
		viewOrder.append(System.getProperty("line.separator"));
	}
	
	public void setItemInfo(String itemID, String name, String price, String quantityOfItem, String discountPercentage, String totalDiscount) {
		
		itemInfo[0] = itemID;
		itemInfo[1] = name;
		itemInfo[2] = price;
		
		itemInfo[3] = quantityOfItem;
		itemInfo[4] = discountPercentage;
		itemInfo[5] = totalDiscount;
		
	}
    
	//Applying Discount to Subtotal 
	public double getTotalDiscount(int quantity, double itemPrice) {
		
		if(quantity >= 1 && quantity <= 4 )
			return (quantity * itemPrice); //0% discount
		if(quantity >= 5 && quantity <= 9)
			return .10 * (quantity * itemPrice); //10% discount
		if(quantity >= 10 && quantity <= 14)
			return .15 * (quantity * itemPrice); //15% discount
		if(quantity >= 15)
			return .20 * (quantity * itemPrice); //20% discount
	
		else return 0;
	}
	
	//Discount Percentage to Print
	public int getDiscountPercentage(int quantity) {
		if(quantity >= 1 && quantity <= 4 )
			return 0; //0% discount
		if(quantity >= 5 && quantity <= 9)
			return 10;//10% discount
		if(quantity >= 10 && quantity <= 14)
			return 15; //15% discount
		if(quantity >= 15)
			return 20; //20% discount
		return 0;
	}
	
	public String viewOrder() {
		return filename;
		
	}
	
	public void transactionTxt() {
		String lineItem = new String();
		for(int i = 0; i< this.itemInfo.length; i++){
			lineItem += this.itemInfo[i] + ", "; 
		}
		items.add(lineItem);
	}
	
	public void printTransactions() throws IOException {
		
		Calendar createCalendar= Calendar.getInstance();
		Date date = createCalendar.getTime();
		
        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a z");
		
		SimpleDateFormat dateFormatting = new SimpleDateFormat("MM/dd/yy");	
				
		SimpleDateFormat permutation = new SimpleDateFormat("ddMMyyyyHHmm");
		
		
		//Finish Order String
		this.setFinishOrder(dateFormatting.format(date), time.format(date));
		
		//Creating file 
		if(file.exists() == false) {
			file.createNewFile();
		}
		
		//Then append data if the file exists
		PrintWriter outputStream = new PrintWriter(new FileWriter(filename, true));
			
		
		for(int i = 0; i< this.items.size(); i++){
			outputStream.append(permutation.format(date) + ", ");
			String lineItem = this.items.get(i);
			outputStream.append(lineItem);
			outputStream.append(dateFormatting.format(date) + ", ");
			outputStream.append(time.format(date));
			outputStream.println();
		}	
		
		outputStream.flush();
		outputStream.close();
	}
	
	//******Getters/Setters*******
	public int getMaxItems() {
		return maxItems;
	}
	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}
	
	public int gettotalQuantityOfItems() {
		return totalQuantityOfItems;
	}
	public void settotalQuantityOfItems(int totalQuantityOfItems) {
		this.totalQuantityOfItems = this.totalQuantityOfItems + totalQuantityOfItems;
	}
	
	
	public double getCurrentItemSubtotal() {
		
		return currentItemSubtotal;
	}
	
	
	public double getOrderSubtotal() {
		return orderSubtotal;
	}
	
	//Calculates Order Subtotal with Qualifying Discount
	public void setOrderSubtotal(int quantity, double itemPrice) {
		
		currentItemSubtotal = 0; //Needed to reset subtotal so that it doesnt add up with previous item subtotals                 
		currentItemSubtotal = currentItemSubtotal + ( (quantity * itemPrice) - this.getTotalDiscount(quantity, itemPrice));  
		
		
		orderSubtotal = orderSubtotal + ( (quantity * itemPrice) - this.getTotalDiscount(quantity, itemPrice) );  
	}
	
	//Calculate order Subtotal with NO discount
	public void setOrderSubtotalNoDiscount(int quantity, double itemPrice) {
	
	currentItemSubtotal = 0;
	currentItemSubtotal = (currentItemSubtotal + (quantity * itemPrice) );	
	
	orderSubtotal = (orderSubtotal + ( quantity * itemPrice));
	}
	
	
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal() {
		this.orderTotal = this.orderSubtotal + (.06 * this.orderSubtotal);
	}
	
	
	
}
