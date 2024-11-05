import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static Order_Manager orderManager = new Order_Manager();
    private static User_Manager userManager = new User_Manager();
    private static Menu menu = new Menu(orderManager);

    private static void preFillMenu() {
        // Add some predefined items to the menu
        menu.addItem(new Item("Cheeseburger", 50, "Meals", true));
        menu.addItem(new Item("Aloo Bhujiya", 20, "Snacks", true));
        menu.addItem(new Item("Soda", 20, "Beverages", true));
        menu.addItem(new Item("French Fries", 40, "Snacks", true));
        menu.addItem(new Item("Grilled Chicken", 80, "Meals", true));
        menu.addItem(new Item("Ice Cream", 10, "Desserts", true));
        System.out.println("Menu pre-filled with initial items.");
    }

    public static void main(String[] args) {
        preFillMenu();
        while (true) {
            try {
                System.out.println("Welcome to the Order Management System");
                System.out.println("1. LOGIN");
                System.out.println("2. SIGNUP");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.println("1. Login as admin");
                        System.out.println("2. Login as Customer");
                        System.out.println("3. Go back");
                        int choice_1 = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice_1) {
                            case 1:
                                handle_admin_session();
                                break;
                            case 2:
                                System.out.println("1. Login as regular customer");
                                System.out.println("2. Login as VIP customer");
                                System.out.println("3. Go back");
                                int choice_2 = scanner.nextInt();
                                scanner.nextLine();

                                switch (choice_2) {
                                    case 1:
                                        handle_customer_session();
                                        break;
                                    case 2:
                                        handle_VIP_customer_session();
                                        break;
                                    case 3:
                                        break;
                                    default:
                                        throw new InvalidSwitchChoice("Please choose a valid option");
                                }
                                break;
                            case 3:
                                break;
                            default:
                                throw new InvalidSwitchChoice("Please choose a valid option");
                        }
                        break;
                    case 2:
                        System.out.println("1. Sign up as admin");
                        System.out.println("2. Sign up as Customer");
                        System.out.println("3. Go back");
                        int choice_3 = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice_3) {
                            case 1:
                                handle_admin_sign_up();
                                break;
                            case 2:
                                handle_customer_sign_up();
                                break;
                            case 3:
                                break;
                            default:
                                throw new InvalidSwitchChoice("Please choose a valid option");
                        }
                        break;
                    case 3:
                        System.out.println("Exiting the application.");
                        return;
                    default:
                        throw new InvalidSwitchChoice("Please choose a valid option");
                }
            } catch (InvalidSwitchChoice e) {
                System.out.println(e.getMessage()); // Display the custom error message
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input from the scanner
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }


    private static String promptForUniqueName(String type) throws NameAlreadyUsedException {
        System.out.print("Enter " + (type.equals("user") ? "username" : "item name") + ": ");
        String name = scanner.nextLine();

        if (type.equals("user")) {
            // Check for uniqueness in UserManager
            User existingUser = userManager.findUserByName(name);
            if (existingUser != null) {
                throw new NameAlreadyUsedException("An account for this username already exists. Please login or use another username.");
            }
        } else if (type.equals("menu")) {
            // Check for uniqueness in Menu
            if (menu.getMenuItems().containsValue(name)) {
                throw new NameAlreadyUsedException("Item with this name already exists, please enter a different name.");
            }
        }

        return name;
    }

    private static void handle_customer_sign_up() {
        String username = null;
        while (username == null) {
            try {
                username = promptForUniqueName("user");
            } catch (NameAlreadyUsedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        Customer customer = new Customer(username, password, orderManager);
        userManager.addUser(customer);
        System.out.println("Signed up!");
    }


    private static void handle_admin_sign_up() {
        String username = null;
        while (username == null) {
            try {
                username = promptForUniqueName("user");
            } catch (NameAlreadyUsedException e) {
                System.out.println(e.getMessage());
            }
        }
        Admin admin = new Admin(username, "XYZ", orderManager);
        userManager.addUser(admin);
        System.out.println("Signed up!");
    }

    private static void handle_admin_session() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        User existingUser = userManager.findUserByName(username);

        if (existingUser != null) {
            Admin admin = (Admin) existingUser; // Cast to existing Admin instance
            boolean loginSuccessful = false;

            while (!loginSuccessful) {
                try {
                    System.out.println("Enter password: ");
                    String password = scanner.nextLine();
                    if (admin.logIn(username, password)) {
                        loginSuccessful = true; // Exit loop on successful login
                        boolean session_active = true;

                        while (session_active) {
                            System.out.println("Admin Menu:");
                            System.out.println("1. Add New Item");
                            System.out.println("2. Update Existing Item");
                            System.out.println("3. Remove Item");
                            System.out.println("4. View Pending Orders");
                            System.out.println("5. Update Order Status");
                            System.out.println("6. Remove an order");
                            System.out.println("7. Complete an order");
                            System.out.println("8. Process Refunds");
                            System.out.println("9. View Menu");
                            System.out.println("10. Generate Report for the day");
                            System.out.println("11. Go Back to Main Menu");
                            System.out.print("Choose an option: ");
                            int choice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            switch (choice) {
                                case 1:
                                    addMenuItem();
                                    break;
                                case 2:
                                    updateItem();
                                    break;
                                case 3:
                                    removeItem();
                                    break;
                                case 4:
                                    viewPendingOrders();
                                    break;
                                case 5:
                                    updateOrderStatus();
                                    break;
                                case 6:
                                    removeOrder();
                                    break;
                                case 7:
                                    complete_order();
                                    break;
                                case 8:
                                    processRefund();
                                    break;
                                case 9:
                                    viewMenu();
                                    break;
                                case 10:
                                    // Add functionality for generating report here
                                    GenerateReport();
                                    break;
                                case 11:
                                    session_active = false;
                                    break;
                                default:
                                    System.out.println("Invalid option, please choose again.");
                            }
                        }
                    } else {
                        throw new IllegalLoginException("Wrong password added");
                    }
                } catch (IllegalLoginException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Please try logging in again.");
                }
            }
        } else {
            System.out.println("No account found for this username.");
        }
    }

    private static void addMenuItem() {
        String name = null;
        while (name == null) {
            try {
                name = promptForUniqueName("menu");
            } catch (NameAlreadyUsedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.print("Enter item price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        String category = menu.chooseCategory();
        System.out.print("Is the item available? (true/false): ");
        boolean available = scanner.nextBoolean();

        Item newItem = new Item(name, price, category, available);
        menu.addItem(newItem);
        System.out.println("Item added successfully.");
    }

    private static void updateItem() {
        try {
            menu.updateItem();
            System.out.println("Item successfully updated.");
        } catch (InvalidSwitchChoice e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removeItem() {
        System.out.print("Enter item name to remove: ");
        String name = scanner.nextLine();
        try {
            menu.removeItem(name);
            System.out.println("Item removed successfully.");
        } catch (ItemDoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void viewPendingOrders() {
        System.out.println(orderManager.View_Pending_Orders());
    }

    private static void updateOrderStatus() {
        // This method will require additional implementation to manage order statuses
        System.out.print("Enter order ID to update: ");
        UUID orderId = UUID.fromString(scanner.nextLine());
        // Here you would implement logic to update the order status
        System.out.println("Order status updated (dummy implementation).");
    }

    private static void processRefund() {
        System.out.print("Enter order ID for refund: ");
        UUID orderId = UUID.fromString(scanner.nextLine());
        orderManager.process_refund(orderId);
        //System.out.println("Refund processed.");
    }

    public static void complete_order() {
        System.out.print("Enter order ID to be completed: ");
        UUID orderId = UUID.fromString(scanner.nextLine());
        orderManager.complete_order(orderId);
    }

    public static void removeOrder() {
        System.out.print("Enter order ID for refund: ");
        UUID orderId = UUID.fromString(scanner.nextLine());
        orderManager.removeOrder(orderId);
    }

    public static void GenerateReport() {
        String report = orderManager.generateReport(new Date());
        System.out.println(report);
    }

    public static void viewMenu() {
        System.out.println(menu.toString());
    }

    private static void handle_customer_session() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        User existingUser = userManager.findUserByName(username);

        if (existingUser != null) {
            Customer customer = (Customer) existingUser; // Cast to existing Customer instance
            boolean loginSuccessful = false;

            while (!loginSuccessful) {
                try {
                    System.out.println("Enter password: ");
                    String password = scanner.nextLine();
                    if (customer.logIn(username, password)) {
                        loginSuccessful = true; // Exit login loop on successful login
                        boolean session_active = true;

                        while (session_active) { // Keep the session active until the user chooses to log out
                            System.out.println("Customer Menu:");
                            System.out.println("1. View Menu");
                            System.out.println("2. Search Item");
                            System.out.println("3. Filter Items by Category");
                            System.out.println("4. Sort Items by Price");
                            System.out.println("5. See order history");
                            System.out.println("6. Modify Cart");
                            System.out.println("7. Checkout");
                            System.out.println("8. View Order Status");
                            System.out.println("9. Upgrade to a VIP customer");
                            System.out.println("10. Go Back to Main Menu");
                            System.out.print("Choose an option: ");
                            int choice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            switch (choice) {
                                case 1:
                                    viewMenu();
                                    break;
                                case 2:
                                    searchItem();
                                    break;
                                case 3:
                                    filterItemsByCategory();
                                    break;
                                case 4:
                                    sortItemsByPrice();
                                    break;
                                case 5:
                                    viewOrderHistory(customer);
                                    break;
                                case 6:
                                    modifyCart(customer);
                                    break;
                                case 7:
                                    checkout(customer);
                                    break;
                                case 8:
                                    viewOrderStatus(customer);
                                    break;
                                case 9:
                                    VIP(customer);
                                    break;
                                case 10:
                                    session_active = false;
                                    break;
                                default:
                                    System.out.println("Invalid option, please choose again.");
                            }
                        }

                        customer.logOut(); // Log out after exiting the session
                    } else {
                        throw new IllegalLoginException("Wrong password added");
                    }
                } catch (IllegalLoginException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Please try logging in again.");
                }
            }
        } else {
            System.out.println("No account found for this username.");
        }
    }



    private static void handle_VIP_customer_session() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        User existingUser = userManager.findUserByName(username);
        VIP_customer customer;

        if (existingUser != null) {
            customer = (VIP_customer) existingUser; //If the user alr exists we dont want it to instantiate a new object that is empty but rather cast it to the existing one
            System.out.println("Enter password: ");
            String password = scanner.nextLine();
            if (customer.logIn(username, password)) {
                boolean session_active = true;
                while (session_active) {
                    System.out.println("Customer Menu:");
                    System.out.println("1. View Menu");
                    System.out.println("2. Search Item");
                    System.out.println("3. Filter Items by Category");
                    System.out.println("4. Sort Items by Price");
                    System.out.println("5. See order history");
                    System.out.println("6. Modify Cart");
                    System.out.println("7. Checkout");
                    System.out.println("8. View Order Status");
                    System.out.println("9. Go Back to Main Menu");
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    switch (choice) {
                        case 1:
                            viewMenu();
                            break;
                        case 2:
                            searchItem();
                            break;
                        case 3:
                            filterItemsByCategory();
                            break;
                        case 4:
                            sortItemsByPrice();
                            break;
                        case 5:
                            viewOrderHistory(customer);
                            break;
                        case 6:
                            modifyCart(customer);
                            break;
                        case 7:
                            checkout(customer);
                            break;
                        case 8:
                            viewOrderStatus(customer);
                            break;
                        case 9:
                            session_active = false;
                            break;
                        default:
                            System.out.println("Invalid option, please choose again.");
                    }
                }
            }
        }
    }

    private static void VIP(Customer customer){
        System.out.println("Payment of 200rs to become VIP member\nEnter y to confirm");
        String confirmation = scanner.nextLine();
        if (confirmation.equals("y")) {
            userManager.addUser(customer.turnVIP());
            System.out.println("Please log out and login again as VIP");
            userManager.removeUser(customer);
        }
        else{
            return;
        }
    }

    private static void searchItem() {
        System.out.print("Enter item name or keyword: ");
        String keyword = scanner.nextLine();
        menu.searchItemsByKeyword(keyword);
    }

    private static void filterItemsByCategory() {
        try {
            menu.filterItemByCategory();
        } catch (InvalidSwitchChoice e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sortItemsByPrice() {
        menu.displaySortedMenuByPrice();
    }

    private static void modifyCart(Customer customer) {
        try {
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addItemToCart(customer);
                    break;
                case 2:
                    removeItemFromCart(customer);
                    break;
                case 3:
                    removeAllItemsFromCart(customer);
                    break;
                default:
                    throw new InvalidSwitchChoice("Invalid choice. Please choose a valid option.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number.");
            scanner.nextLine();
        } catch (InvalidSwitchChoice | ItemDoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

    private static int getUserChoice() {
        System.out.println("Do you wish to \n1. Add items \n2. Remove items \n3. Remove all items");
        return scanner.nextInt();
    }

    private static void addItemToCart(Customer customer) throws ItemDoesntExistException {
        scanner.nextLine();
        System.out.println("What do you wish to add to your cart:");
        String name = scanner.nextLine();
        Item itemToAdd = null;
        System.out.println("Quantity you wish to add to your cart:");
        int quantity = scanner.nextInt();

        itemToAdd = menu.searchItem(name);

        if (itemToAdd != null) {
            customer.getCart().addItem(itemToAdd, quantity);
            System.out.println("Item added successfully.");
        } else {
            throw new ItemDoesntExistException("The item \"" + name + "\" does not exist in the menu.");
        }
    }

    private static void removeItemFromCart(Customer customer) throws ItemDoesntExistException {
        scanner.nextLine(); // Consume newline
        System.out.println("What do you wish to remove from your cart:");
        String itemName = scanner.nextLine();
        Item itemToRemove = null;

        itemToRemove = menu.searchItem(itemName);
        if (itemToRemove != null) {
            customer.getCart().removeItem(itemToRemove);
            System.out.println("Item removed successfully.");
        } else {
            throw new ItemDoesntExistException("The item \"" + itemName + "\" does not exist in the menu.");
        }
    }

    private static void removeAllItemsFromCart(Customer customer) {
        customer.getCart().removeAllItems();
        System.out.println("All items removed from your cart.");
    }


    private static void checkout(Customer customer) {
        customer.getCart().checkout();
    }

    private static void viewOrderStatus(Customer customer) {
        System.out.println(customer.getCart().getStatus());
    }

    private static void viewOrderHistory(Customer customer){
        customer.viewHistory();
    }
}
