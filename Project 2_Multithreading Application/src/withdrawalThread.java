
import java.util.Random;

//Running loop for Withdrawal Thread
public class withdrawalThread implements Runnable {
private BankAccount bankAccount;

private String ThreadName;

// Constructor to link thread to main shared account
public withdrawalThread (BankAccount mainAccount, String name) {
bankAccount = mainAccount;
ThreadName = name;
}

Random rand = new Random();

//Generate random integers in range 0 to 299
int rand_int = rand.nextInt(300);

public void run() {
	try {
        // Infinite Loop
		while (true) {
			bankAccount.withdrawl(ThreadName);
			Thread.sleep(rand_int);
			Thread.yield();

		}
	} catch (InterruptedException e) {
		e.printStackTrace();
	  }

	}
}