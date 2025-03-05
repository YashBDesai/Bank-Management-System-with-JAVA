import java.awt.*;
import javax.swing.*;

public class BankGUI extends JFrame {
    private BankManager bankManager;
    private JTextField accountField, passwordField, amountField;
    private JTextArea displayArea;
    private JPanel loginPanel, operationsPanel;
    private String currentUser;

    // Constants for UI design
    private static final Color PRIMARY_COLOR = new Color(63, 81, 181);
    private static final Color ACCENT_COLOR = new Color(92, 107, 192);
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final int PADDING = 5;
    private static final int CORNER_RADIUS = 10;

    public BankGUI() {
        bankManager = new BankManager();
        setupGUI();
        displayArea.setText("Welcome to Bank Management System");
    }

    private void createAccount() {
        String name = accountField.getText();
        String password = passwordField.getText();
        
        if(bankManager.createAccount(name, password)) {
            JOptionPane.showMessageDialog(this, 
                "Account created successfully!\nAccount Name: " + name, 
                "Account Created", 
                JOptionPane.INFORMATION_MESSAGE);
            accountField.setText("");
            passwordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, 
                "Could not create account!", 
                "Creation Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void login() {
        String name = accountField.getText();
        String password = passwordField.getText();
        
        Account account = bankManager.login(name, password);
        if(account != null) {
            currentUser = name;
            switchPanels(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid credentials!", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if(bankManager.deposit(currentUser, amount)) {
                displayArea.setText("Deposited: Rupees " + amount);
            } else {
                displayArea.setText("Deposit failed!");
            }
        } catch(NumberFormatException e) {
            displayArea.setText("Please enter a valid amount!");
        }
    }

    private void withdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if(bankManager.withdraw(currentUser, amount)) {
                displayArea.setText("Withdrawn: Rupees " + amount);
            } else {
                displayArea.setText("Withdrawal failed! Insufficient funds.");
            }
            amountField.setText("");
        } catch(NumberFormatException e) {
            displayArea.setText("Please enter a valid amount!");
        }
    }

    private void checkBalance() {
        double balance = bankManager.getBalance(currentUser);
        displayArea.setText("Current balance: Rupees " + balance);
    }

    private void logout() {
        bankManager.logout();
        currentUser = null;
        switchPanels(false);
        displayArea.setText("Logged out successfully!");
    }

    private void setupGUI() {
        setTitle("Modern Bank Management System");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Create components
        createInputPanel(mainPanel);
        createDisplayArea(mainPanel);
        createLoginPanel();
        createOperationsPanel();

        // Center panel for buttons
        JPanel centerPanel = new JPanel(new CardLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(loginPanel, "login");
        centerPanel.add(operationsPanel, "operations");
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Initial state
        switchPanels(false);
        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private void createInputPanel(JPanel mainPanel) {
        JPanel loginFieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JPanel amountFieldPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        loginFieldsPanel.setBackground(BACKGROUND_COLOR);
        amountFieldPanel.setBackground(BACKGROUND_COLOR);

        accountField = createStyledTextField();
        passwordField = createStyledTextField();
        amountField = createStyledTextField();

        addLabelAndField(loginFieldsPanel, "Account Name:", accountField);
        addLabelAndField(loginFieldsPanel, "Password:", passwordField);
        addLabelAndField(amountFieldPanel, "Amount:", amountField);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.add(loginFieldsPanel, BorderLayout.NORTH);
        inputPanel.add(amountFieldPanel, BorderLayout.CENTER);

        amountFieldPanel.setVisible(false);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, PADDING, PADDING));
        loginPanel.setBackground(BACKGROUND_COLOR);
        addStyledButton(loginPanel, "Create Account", e -> createAccount());
        addStyledButton(loginPanel, "Login", e -> login());
    }

    private void createOperationsPanel() {
        operationsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, PADDING, PADDING));
        operationsPanel.setBackground(BACKGROUND_COLOR);
        addStyledButton(operationsPanel, "Deposit", e -> deposit());
        addStyledButton(operationsPanel, "Withdraw", e -> withdraw());
        addStyledButton(operationsPanel, "Check Balance", e -> checkBalance());
        addStyledButton(operationsPanel, "Logout", e -> logout());
    }

    // Add these methods
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(15) {
            @Override 
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, CORNER_RADIUS, CORNER_RADIUS);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void addLabelAndField(JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        panel.add(label);
        panel.add(field);
    }

    private void addStyledButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        button.addActionListener(listener);
        panel.add(button);
    }

    private void switchPanels(boolean isLoggedIn) {
        loginPanel.setVisible(!isLoggedIn);
        operationsPanel.setVisible(isLoggedIn);
        
        // Toggle amount field visibility
        Component[] components = ((JPanel)accountField.getParent().getParent()).getComponents();
        for (Component c : components) {
            if (c instanceof JPanel && c != accountField.getParent()) {
                c.setVisible(isLoggedIn);
            }
        }
        
        accountField.setEnabled(!isLoggedIn);
        passwordField.setEnabled(!isLoggedIn);
        amountField.setEnabled(isLoggedIn);

        if (!isLoggedIn) {
            accountField.setText("");
            passwordField.setText("");
            amountField.setText("");
        }

        revalidate();
        repaint();
    }

    private void createDisplayArea(JPanel mainPanel) {
        displayArea = new JTextArea(8, 35);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        displayArea.setMargin(new Insets(PADDING, PADDING, PADDING, PADDING));
        displayArea.setBackground(Color.WHITE);
        displayArea.setForeground(TEXT_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true));
        scrollPane.setBackground(BACKGROUND_COLOR);
        
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new BankGUI().setVisible(true);
        });
    }
}
