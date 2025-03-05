import java.io.*;
import java.util.HashMap;

public class BankManager {
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private HashMap<String, Account> accounts;
    private Account currentAccount;

    public BankManager() {
        accounts = new HashMap<>();
        loadAccounts();
    }

    public boolean createAccount(String name, String password) {
        if(name.isEmpty() || password.isEmpty() || accounts.containsKey(name)) {
            return false;
        }
        accounts.put(name, new Account(name, password));
        saveAccounts();
        Logger.log("Account created: " + name);
        return true;
    }

    public Account login(String name, String password) {
        Account account = accounts.get(name);
        if(account != null && account.checkPassword(password)) {
            currentAccount = account;
            Logger.log("Login successful: " + name);
            return account;
        }
        return null;
    }

    public void logout() {
        if(currentAccount != null) {
            Logger.log("Logout: " + currentAccount.getAccountName());
            currentAccount = null;
        }
    }

    public boolean deposit(String accountName, double amount) {
        Account account = accounts.get(accountName);
        if(account != null) {
            account.deposit(amount);
            saveAccounts();
            Logger.log("Deposit: " + amount + " to " + accountName);
            return true;
        }
        return false;
    }

    public boolean withdraw(String accountName, double amount) {
        Account account = accounts.get(accountName);
        if(account != null && account.withdraw(amount)) {
            saveAccounts();
            Logger.log("Withdrawal: " + amount + " from " + accountName);
            return true;
        }
        return false;
    }

    public double getBalance(String accountName) {
        Account account = accounts.get(accountName);
        return account != null ? account.getBalance() : -1;
    }

    private void saveAccounts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (Account account : accounts.values()) {
                writer.println(account.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    private void loadAccounts() {
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Account account = Account.fromString(line);
                accounts.put(account.getAccountName(), account);
            }
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }
}
