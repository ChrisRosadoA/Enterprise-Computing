/*Name: Christian D. Rosado Arroyo
 Course: CNT 4714 – Fall 2021
 Assignment title: Project 2 – An Application Employing Synchronized/Cooperating Multiple 
 Threads In Java Using Locks – A Banking Simulator
 Date: Sunday October 3, 2021
*/
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Bank {
	public class Main {
		public static void main(String[] args) {
		  	
		//Steps For Thread Pool
		/*1. Create a task(Runnable Object) to execute
		  2. Create Executor Pool using Executors
		  3. Pass tasks to Executor Pool
		  4. Shutdown the Executor Pool*/
			
		//If all threads are being currently run by the executor then the pending tasks are placed in a queue and are executed when a thread becomes idle. (when it sleeps)
		ExecutorService pool = Executors.newFixedThreadPool(15);
		
		// Create Bank Account Object to be used by Depositing/Withdrawing Threads/Methods
		BankAccount sharedAccount = new BankAccount();
		
		//Run Threads
		try {
		
		//Initializing/Starting Threads 
		System.out.printf("Deposit Threads\t\t\tWithdrawl Threads\t\tBalance\t\t\t\n");
		System.out.printf("---------------\t\t\t-----------------\t\t---------------\t\t\t\n");
				
		//15 Threads: 9 Withdrawal Threads & 6 Depositor Threads
		pool.execute(new withdrawalThread(sharedAccount, "W6"));
		pool.execute(new depositThread(sharedAccount, "D4"));
		pool.execute(new withdrawalThread(sharedAccount, "W1"));
		pool.execute(new depositThread(sharedAccount, "D1"));
		pool.execute(new withdrawalThread(sharedAccount, "W2"));
		pool.execute(new depositThread(sharedAccount, "D3"));
		pool.execute(new withdrawalThread(sharedAccount, "W4"));
		pool.execute(new depositThread(sharedAccount, "D5"));
		pool.execute(new withdrawalThread(sharedAccount, "W5"));
		pool.execute(new withdrawalThread(sharedAccount, "W7"));
		pool.execute(new depositThread(sharedAccount, "D6"));
		pool.execute(new withdrawalThread(sharedAccount, "W8"));
		pool.execute(new withdrawalThread(sharedAccount, "W3"));
		pool.execute(new depositThread(sharedAccount, "D2"));
		pool.execute(new withdrawalThread(sharedAccount, "W9"));
		}
		
		catch(Exception exception) {
			exception.printStackTrace();
		}
				
		pool.shutdown();
				
	   }
	}
}
