 package com.mycompany.coffemocha12;
import java.io.*;
import java.util.*;


public class Coffemocha12 {
    public static final String MENU_FILE = "menu.txt";
    public static final String ORDERS_FILE = "orders.txt";
    public static final String USERS_FILE = "users.txt";
    public static final String BILL_FILE="bill.txt";
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayWelcomePage();
        if (!authenticateUser()) {// Authenticate the user before granting access to the system
            System.out.println("Invalid login credentials. Exiting...");
            return;
        }
        mainMenu();
         System.out.println("\n -------------------------------------------");
    }

    private static void displayWelcomePage() {
    System.out.println("WELCOME TO CAFE MOCHA!!");
   
    System.out.println("\n -------------------------------------------");
}

//user authentication before entering to the system
    private static boolean authenticateUser() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        File usersFile = new File(USERS_FILE);
        if (!usersFile.exists()) {
            System.out.println("Users file not found. Please ensure the file exists.");
            return false;
        }
 
        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file.");
        }
         System.out.println("\n -------------------------------------------");
        return false;
       
    }
    
// Displays the main menu and handles user choices
    private static void mainMenu() {
        int choice;
        do {
            System.out.println("\n -------------------------------------------");
            System.out.println("\nMain Menu:");
            System.out.println("\n--- Main menu---");
            System.out.println("1. Add New Customer Order");
            System.out.println("2. Update Customer Order");
            System.out.println("3. Delete Customer Order");
            System.out.println("4. Display Order Details");
            System.out.println("5. Calculate and Print Bill");
            System.out.println("6. Add New Menu Item");
            System.out.println("7. Update Menu Item");
            System.out.println("8. Delete Menu Item");
            System.out.println("9. View Menu");
            System.out.println("10. Help");
            System.out.println("11. Exit");
            System.out.print("Select an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
 System.out.println("\n -------------------------------------------");
            switch (choice) {//calls methods 
                case 1:
                    addNewOrder();
                    break;
                case 2:
                    updateOrder();
                    break;
                case 3:
                    deleteOrder();
                    break;
                case 4:
                    displayOrderDetails();
                    break;
                case 5:
                    calculateAndPrintBill();
                    break;
                case 6:
                    addNewMenuItem();
                    break;
                case 7:
                    updateMenuItem();
                    break;
                case 8:
                    deleteMenuItem();
                    break;
            
                case 9:
                    viewMenu();
                    break;
                case 10:
                    displayHelp();
                    break;
                case 11:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 11);
         System.out.println("\n -------------------------------------------");
    }

      private static void addNewOrder() {
    System.out.println("\n--- Add New Order ---");
    
    System.out.print("Enter Order Number: ");
    String orderNumber = scanner.nextLine();
    System.out.print("Enter Customer Name: ");
    String name = scanner.nextLine();
    System.out.print("Enter Address: ");
    String address = scanner.nextLine();
    String phone;
    
    while (true) {
        System.out.print("Enter Telephone Number (10 digits): ");
        phone = scanner.nextLine();
        if (isValidPhoneNumber(phone)) {
            break;
        } else {
            System.out.println("Invalid phone number. It must be exactly 10 digits.");
        }
    }
    
    System.out.print("Enter Item ID: ");
    String itemId = scanner.nextLine();
    System.out.print("Enter Order Details (items separated by ';'): ");
    String orderDetails = scanner.nextLine();

    // Construct order data and write it to the file
    String data = orderNumber + "," + name + "," + address + "," + phone + "," + itemId + "," + orderDetails;
    create(ORDERS_FILE, data);
}
private static boolean isValidPhoneNumber(String phone) {
    // Check if the phone number is exactly 10 digits long and contains only digits
    return phone.matches("\\d{10}");
}


    private static void updateOrder() {
        System.out.println("\n -------------------------------------------");
        System.out.print("Enter Order Number to Update: ");
        String orderNumber = scanner.nextLine();
        System.out.print("Enter New Customer Name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter New Address: ");
        String newAddress = scanner.nextLine();
        String newPhone;
    
    while (true) {
        System.out.print("Enter Telephone Number (10 digits): ");
        newPhone = scanner.nextLine();
        if (isValidPhoneNumber(newPhone)) {
            break;
        } else {
            System.out.println("Invalid phone number. It must be exactly 10 digits.");
        }
    }
    
        System.out.print("Enter New Order Details (items separated by ';'): ");
        String newOrderDetails = scanner.nextLine();

        String newData = orderNumber + "," + newName + "," + newAddress + "," + newPhone + "," + newOrderDetails;
        update(ORDERS_FILE, orderNumber, newData);
         System.out.println("\n -------------------------------------------");
    }
   

    private static void deleteOrder() {
        System.out.println("\n--- Delete Order ---");
        System.out.print("Enter Order Number to Delete: ");
        String orderNumber = scanner.nextLine();
        delete(ORDERS_FILE, orderNumber);
         System.out.println("\n -------------------------------------------");
    }

    private static void displayOrderDetails() {
        System.out.println("\n--- Display Orders Details ---");
        read(ORDERS_FILE);
        System.out.println("\n -------------------------------------------");
    }
 // Calculates and prints the bill for a specific order
private static void calculateAndPrintBill() {
    System.out.println("\n--- Calculate and Print Bill ---");
    System.out.print("Enter Order Number to Calculate Bill: ");
    String orderNumber = scanner.nextLine();

    // Read order details
    String orderDetails = getOrderDetails(orderNumber);
    if (orderDetails == null) {
        System.out.println("Order not found.");
        return;
    }

    // Parse order details
    String[] detailsParts = orderDetails.split(",");
    if (detailsParts.length < 5) {
        System.out.println("Invalid order format.");
        return;
    }
    
    String itemIds = detailsParts[4];
    String[] itemIdArray = itemIds.split(";");

    double totalBill = 0;
    StringBuilder billDetails = new StringBuilder();
    billDetails.append("Order Number: ").append(orderNumber).append("\n");

    // Read menu items
    try (BufferedReader menuReader = new BufferedReader(new FileReader(MENU_FILE))) {
        String line;
        Map<String, Double> menuItems = new HashMap<>();
        while ((line = menuReader.readLine()) != null) {
            String[] menuParts = line.split(",");
            if (menuParts.length == 4) {
                String id = menuParts[0];
                double price = Double.parseDouble(menuParts[3]);
                menuItems.put(id, price);
            }
        }

        // Calculate bill
        for (String itemId : itemIdArray) {
            Double itemPrice = menuItems.get(itemId);
            if (itemPrice != null) {
                totalBill += itemPrice;
                billDetails.append("Item ID: ").append(itemId)
                           .append(", Price: ").append(itemPrice).append("\n");
            } else {
                billDetails.append("Item ID: ").append(itemId)
                           .append(", Price: Not found\n");
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading menu file: " + e.getMessage());
        return;
    }

    billDetails.append("Total Bill:Rs. ").append(totalBill).append("\n");

    // Write bill to file
    try (BufferedWriter billWriter = new BufferedWriter(new FileWriter(BILL_FILE, true))) {
        billWriter.write(billDetails.toString());
        billWriter.newLine();
        System.out.println("Bill calculated and saved successfully.");
        System.out.println(billDetails.toString());
    } catch (IOException e) {
        System.out.println("Error writing to bill file: " + e.getMessage());
    }
}

private static String getOrderDetails(String orderNumber) {
    try (BufferedReader orderReader = new BufferedReader(new FileReader(ORDERS_FILE))) {
        String line;
        while ((line = orderReader.readLine()) != null) {
            String[] orderParts = line.split(",");
            if (orderParts.length > 4 && orderParts[0].equals(orderNumber)) {
                return line;
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading order file: " + e.getMessage());
    }
    return null;
}



    private static void addNewMenuItem() {
        System.out.println("\n -------------------------------------------");
        System.out.println("\n--- add new menu item ---");
        System.out.print("Enter Item ID: ");
        String Id = scanner.nextLine();
        System.out.print("Enter Item Name: ");
        String name=scanner.nextLine();
        System.out.print("Enter Item Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Item Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        String data = Id +","+ name + "," + description + "," + price;
        create(MENU_FILE, data);
    }

  private static void updateMenuItem() {
    System.out.println("\n -------------------------------------------");
    System.out.println("\n--- Update Menu Item ---");
    System.out.print("Enter Item ID to Update: ");
    String id = scanner.nextLine(); // Change variable name to 'id'
    System.out.print("Enter New Item Name: ");
    String name = scanner.nextLine();
    System.out.print("Enter New Description: ");
    String newDescription = scanner.nextLine();
    System.out.print("Enter New Price: ");
    double newPrice = scanner.nextDouble();
    scanner.nextLine(); // Consume newline

    // Construct a new data string formatted with the item's ID, name, new description, and new price
    String newData = id + "," + name + "," + newDescription + "," + newPrice;
    
    // Call the update method with 'id' as the search string
    update(MENU_FILE, id, newData);
}

    private static void deleteMenuItem() {
        System.out.println("\n -------------------------------------------");
        System.out.println("\n--- delete menu item ---");
        System.out.print("Enter Item ID to Delete: ");
        String Id = scanner.nextLine();
        delete(MENU_FILE, Id);
    }

   
    private static void viewMenu() {
        System.out.println("\n -------------------------------------------");
        read(MENU_FILE);
    }

    // File operations

    private static void create(String fileName, String data) {
        System.out.println("\n -------------------------------------------");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(data);
            writer.newLine();
            System.out.println(" added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
// Method to update a file with new data, replacing lines that match a search string
     private static void update(String fileName, String searchString, String newData) {
         System.out.println("\n -------------------------------------------");
         System.out.println("\n--- Update ---");
         
        List<String> lines = new ArrayList<>();// List to hold lines from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {// Try-with-resources to ensure BufferedReader is closed after use
            String line;
            while ((line = reader.readLine()) != null) {// If the line starts with the search string followed by a comma, replace it with newData
                if (line.startsWith(searchString + ",")) {
                    lines.add(newData);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        //buffer reader used to write text to a file efficiently.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("  updated successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void delete(String fileName, String searchString) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(searchString + ",")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println(" deleted successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void read(String fileName) {
        System.out.println("\n -------------------------------------------");
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println(lineNumber + ": " + line);
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    private static void displayHelp() {
        System.out.println("\n -------------------------------------------");
    System.out.println("\n--- Help Manual ---");
    System.out.println(">> Add New Customer Order:-->>Allows you to add a new order with details.");
    System.out.println("\n -------------------------------------------");
    System.out.println(">> Update Customer Order:-->> Modify existing orders based on order number.");
    System.out.println("\n -------------------------------------------");
    System.out.println(">> Delete Customer Order:-->> Remove an order from the system using order number.");
    System.out.println("\n -------------------------------------------");
    System.out.println(">> Display Order Details:-->> View details of all orders.");
    System.out.println("\n -------------------------------------------");
    System.out.println(">> Calculate and Print Bill:-->> Calculate the total bill for a specific order.");
    System.out.println("\n -------------------------------------------");
    System.out.println(">> Add New Menu Item:-->> Add a new item to the menu with name, description, and price.");
    System.out.println("\n -------------------------------------------");
    System.out.println(">> Update Menu Item:-->> Update the details of an existing menu item.");
    System.out.println("\n -------------------------------------------");
    System.out.println(">> Delete Menu Item:-->> Remove an item from the menu using the item name.");
    System.out.println("\n -------------------------------------------");
    System.out.println(">> View All Orders:-->> Display a list of all orders.");
    System.out.println("\n -------------------------------------------");
    System.out.println(">> View Menu:-->> Show the current menu with items and prices.");
    System.out.println("\n -------------------------------------------");
    System.out.println(">> Help:-->> Display this help manual.");
    System.out.println("\n -------------------------------------------");
    System.out.println(">> Exit:-->> Exit the application.");
    System.out.println("\n -------------------------------------------");
}

}