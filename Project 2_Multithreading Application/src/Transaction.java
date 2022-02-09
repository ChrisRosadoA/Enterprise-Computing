import java.util.Random;

public class Transaction {
  
public static int depositing (){
  	
Random rand = new Random();	
	
int moneyAmmount = 0;

	//Depositing Money 
	//Random Amount of Money Range: 1 to 250 -> (0 to 249) +1
	moneyAmmount = rand.nextInt(250) + 1;
	
return moneyAmmount;

//Withdrawing Money if flag is false
} 

public static int withdrawing (){
  	
Random rand = new Random();	
	
int moneyAmmount = 0;

//Withdrawing Money if flag is false
	moneyAmmount = rand.nextInt(50) + 1;
	return moneyAmmount;
	}
}