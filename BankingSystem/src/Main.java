//CLI banking system

import java.util.*;
import java.io.*;

class Customer {
    String name;
    String password;
    double balance;

    Customer(String name, String password, double balance) {
        this.name = name;
        this.password = password;
        this.balance = balance;
    }
}

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static HashMap<String, Customer> customerMap = new HashMap<>();
    static Customer loggedInCustomer = null;
    static final String DATA_FILE = "bank_data.txt";

    public static void main(String[] args) {
        loadCustomers();
        printHeader();
        while (true) {
            if (loggedInCustomer == null) showWelcomeMenu();
            else showBankMenu();
        }
    }

    static void printHeader() {
        System.out.println("\n====================================");
        System.out.println("        CLI Banking System");
        System.out.println("        Made with Love in Java");
        System.out.println("====================================\n");
    }

    static void showWelcomeMenu() {
        System.out.println("1. Create New Account");
        System.out.println("2. Login to Your Account");
        System.out.println("3. Exit");
        System.out.print("\nChoose an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1 -> register();
            case 2 -> login();
            case 3 -> {
                System.out.println("\nThank you for using CLI Banking. Goodbye!\n");
                System.exit(0);
            }
            default -> System.out.println("Invalid option. Try again.\n");
        }
    }

    static void register() {
        System.out.print("\nEnter a unique username: ");
        String name = scanner.nextLine();
        if (customerMap.containsKey(name)) {
            System.out.println("Username already exists! Please try a different one.\n");
            return;
        }
        System.out.print("Set a password: ");
        String password = scanner.nextLine();
        customerMap.put(name, new Customer(name, password, 0.0));
        saveCustomers();
        System.out.println("Account created successfully! You can now login.\n");
    }

    static void login() {
        System.out.print("\nUsername: ");
        String name = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Customer customer = customerMap.get(name);
        if (customer != null && customer.password.equals(password)) {
            loggedInCustomer = customer;
            System.out.println("\nLogin successful! Welcome back, " + loggedInCustomer.name + "!\n");
        } else {
            System.out.println("Incorrect username or password.\n");
        }
    }

    static void showBankMenu() {
        System.out.println("Hello, " + loggedInCustomer.name + "!");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Logout");
        System.out.print("\nChoose an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1 -> System.out.println("\nYour current balance is: ₹" + loggedInCustomer.balance + "\n");
            case 2 -> deposit();
            case 3 -> withdraw();
            case 4 -> {
                System.out.println("\nLogged out successfully.\n");
                loggedInCustomer = null;
            }
            default -> System.out.println("Invalid choice. Please try again.\n");
        }
    }

    static void deposit() {
        System.out.print("\nEnter amount to deposit: ₹");
        double amount = scanner.nextDouble();
        if (amount > 0) {
            loggedInCustomer.balance += amount;
            saveCustomers();
            System.out.println("₹" + amount + " deposited successfully!\n");
        } else {
            System.out.println("Please enter a valid amount.\n");
        }
    }

    static void withdraw() {
        System.out.print("\nEnter amount to withdraw: ₹");
        double amount = scanner.nextDouble();
        if (amount > 0 && loggedInCustomer.balance >= amount) {
            loggedInCustomer.balance -= amount;
            saveCustomers();
            System.out.println("₹" + amount + " withdrawn successfully!\n");
        } else {
            System.out.println("Insufficient balance or invalid amount.\n");
        }
    }

    static void loadCustomers() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) return;
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                if (data.length == 3) {
                    String name = data[0];
                    String password = data[1];
                    double balance = Double.parseDouble(data[2]);
                    customerMap.put(name, new Customer(name, password, balance));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading customer data.");
        }
    }

    static void saveCustomers() {
        try {
            FileWriter writer = new FileWriter(DATA_FILE);
            for (Customer customer : customerMap.values()) {
                writer.write(customer.name + "," + customer.password + "," + customer.balance + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving customer data.");
        }
    }
}