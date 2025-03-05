public class Account {
    private String accountName;
    private String password;
    private double balance;

    public Account(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
        this.balance = 0.0;
    }

    // Getters and setters
    public String getAccountName() { return accountName; }
    public boolean checkPassword(String password) { return this.password.equals(password); }
    public double getBalance() { return balance; }
    
    public void deposit(double amount) {
        balance += amount;
    }
    
    public boolean withdraw(double amount) {
        if(amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    // Add toString method for saving to file
    @Override
    public String toString() {
        return accountName + "," + password + "," + balance;
    }

    // Add constructor for loading from file
    public static Account fromString(String line) {
        String[] parts = line.split(",");
        Account account = new Account(parts[0], parts[1]);
        account.balance = Double.parseDouble(parts[2]);
        return account;
    }
}
