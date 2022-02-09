import java.util.Random;

// Running loop for Deposit Thread
//Class of a runnable object so that we can use Threads 
public class depositThread implements Runnable { 
private BankAccount bankAccount;

private String ThreadName;

// Constructor to link thread to main shared account
public depositThread (BankAccount mainAccount, String name) {
bankAccount = mainAccount;
ThreadName = name;
}

Random rand = new Random();

// Generate random integers in range 0 to 1999
int rand_int = rand.nextInt(5000);

public void run() {
	try {
// Infinite Boolean Loop. More efficient than a for math loop
		while (true){
		
			bankAccount.deposit(ThreadName);
			Thread.sleep(rand_int);
			Thread.yield();
  }

}catch (InterruptedException e) {
	      //Throws Exception 
          e.printStackTrace();
    }

  }
}


