import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
	
//ATTRIBUTES
private int accountBalance;
private Lock access;
private Condition fundsAvailable;

//Bank Accoount CONSTRUCTOR
public BankAccount() {
access = new ReentrantLock();
fundsAvailable = access.newCondition();
accountBalance = 0;
}
//OPERATIONS:
//DEPOSIT:Locking System
public void deposit(String name) throws InterruptedException{
	
	int deposit = Transaction.depositing();
	access.lock();//locks thread
	
	
	try {
		accountBalance = accountBalance + deposit;
		System.out.printf("Thread %s deposits $%-3d\t\t\t\t\t\t(+) Balance is $%-3d\n", name, deposit, accountBalance);
		fundsAvailable.signalAll();
	} finally {
	
    access.unlock();//unlocks thread 

	}
}

//WITHDRAWAL: Locking System
public void withdrawl(String name) throws InterruptedException {
	int withdraw = Transaction.withdrawing();
	access.lock();
	
	try {
		// If funds, perform transactions.
		if (accountBalance > withdraw) {
			accountBalance = accountBalance - withdraw;
			System.out.printf("\t\t\t\tThread %s withdraws $%-3d\t(-) Balance is $%-3d\n", name, withdraw, accountBalance);   
	}
  
	// If insufficient funds, wait until deposit to try again
	else {
		while (accountBalance < withdraw){
			System.out.printf("\t\t\t\tThread %s withdraws $%-3d\t(******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!!\n", name, withdraw);
			fundsAvailable.await();
			}
		}
		
		}catch (InterruptedException e){
			e.printStackTrace();
		} finally {
			access.unlock();
		}
	}
}

