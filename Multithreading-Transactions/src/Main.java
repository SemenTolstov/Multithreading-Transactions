import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        Account accountOne = new Account("1", 100000);
        Account accountTwo = new Account("2", 200000);
        Account accountThree = new Account("3", 0);

        ConcurrentHashMap<String, Account> accountMap = new ConcurrentHashMap<>();
        accountMap.put("1", accountOne);
        accountMap.put("2", accountTwo);
        accountMap.put("3", accountThree);


        Bank bank = new Bank(accountMap);

        // один поток 1 и 2 счета, 2 поток 2 и 3 счета, 3 поток 3 и 1 счет

        new Thread(() -> {
            try {
                bank.transfer("1","2", 10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread 1: " + "1 account " + bank.getBalance("1") + ", " + "2 account " + bank.getBalance("2"));
        }).start();
        new Thread(() -> {
            try {
                bank.transfer("2", "3", 20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread 2: " + "2 account " + bank.getBalance("2") + ", " + "3 account " + bank.getBalance("3"));

        }).start();
        new Thread(() -> {
            try {
                bank.transfer("3","1", 20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread 3: " + "3 account " + bank.getBalance("3") + ", " + "1 account " + bank.getBalance("1"));

        }).start();
    }
}
