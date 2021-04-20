import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {

    private ConcurrentHashMap<String, Account> accounts;
    private final Random random = new Random();
    private final String accErr = "Doesn't consist acc.";
    private final String balanceErr = "Not have enough money.";
    private final String accBlocked = "Account is blocked.";

    public Bank(ConcurrentHashMap<String, Account> accounts) {
        this.accounts = accounts;
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        if (amount >= 50000) return false;
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
     * усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Account fromAccount = accounts.get(fromAccountNum);
        Account toAccount = accounts.get(toAccountNum);

        if (fromAccount == null || toAccount == null) {
            System.out.println(accErr);
            return;
        }

        if (fromAccount.getMoney() < amount) {
            System.out.println(balanceErr + " AccountNum = " + fromAccountNum);
            return;
        }

        if (!isFraud(fromAccountNum, toAccountNum, amount)) {
            System.out.println(accBlocked + " " + fromAccountNum + " " + toAccountNum + " accounts are blocked");
            return;
        }

        int fromId = Integer.parseInt(fromAccount.getAccNumber());
        int toId = Integer.parseInt(toAccount.getAccNumber());
        if (fromId < toId) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    fromAccount.setMoney(fromAccount.getMoney() - amount);
                    toAccount.setMoney(toAccount.getMoney() + amount);
                }
            }
        } else {
            synchronized (toAccount) {
                synchronized (fromAccount) {
                    fromAccount.setMoney(fromAccount.getMoney() - amount);
                    toAccount.setMoney(toAccount.getMoney() + amount);
                }
            }
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {

        return accounts.get(accountNum).getMoney();
    }

    public long getSumAllAccounts() {
        return 0;
    }
}
