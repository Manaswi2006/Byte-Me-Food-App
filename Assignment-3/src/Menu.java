import java.util.*;

// Class representing the menu
public class Menu implements MenuManager {
    // Attributes
    private Scanner scanner = new Scanner(System.in);
    private Order_Manager orderManager;
    private TreeMap<Item, String> menuItems; // Item as key, String as value (unique)

    // Constructor
    public Menu(Order_Manager orderManager) {
        this.orderManager = orderManager;
        this.menuItems = new TreeMap<>(new PriceComparator()); // Sorted by price
    }

    public Order_Manager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(Order_Manager orderManager) {
        this.orderManager = orderManager;
    }

    public void setMenuItems(TreeMap<Item, String> menuItems) {
        this.menuItems = menuItems;
    }

    public TreeMap<Item, String> getMenuItems() {
        return menuItems;
    }
    // Add a new item to the menu
    @Override
    public void addItem(Item item) {
        menuItems.put(item, item.getName());  // Store the item with its name as the value
    }

    // Remove an item from the menu
    @Override
    public void removeItem(String name) throws ItemDoesntExistException {
        // Find the item to remove by its name
        Item itemToRemove = searchItem(name);
        if (itemToRemove != null) {
            menuItems.remove(itemToRemove);
            getOrderManager().denyOrders(itemToRemove);
        } else {
            throw new ItemDoesntExistException("The item \"" + name + "\" does not exist in the menu.");
        }
    }


    // Search for an item by name
    @Override
    public Item searchItem(String keyword) {
        for (Item item : menuItems.keySet()) {
            if (item.getName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println("Found: " + item);
                return item; // Return the found item
            }
        }

        System.out.println("No items found containing the keyword: " + keyword);
        return null; // Return null if not found
    }


    // Search for items by keyword (partial search)
    @Override
    public void searchItemsByKeyword(String keyword) {
        boolean found = false;
        for (Item item : menuItems.keySet()) {
            if (item.getName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println("Found: " + item);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items found containing the keyword: " + keyword);
        }
    }

    @Override
    public void updateItem() throws InvalidSwitchChoice, ItemDoesntExistException {
        System.out.println("Which Item do you want to update: ");
        String name = scanner.nextLine();
        Item item = searchItem(name);

        if (item == null) {
            throw new ItemDoesntExistException("The item \"" + name + "\" does not exist.");
        }
        chooseUpdateOption(item);
    }

    // Method to choose the update option
    private void chooseUpdateOption(Item item) throws InvalidSwitchChoice {
        System.out.println("What do you wish to change: ");
        System.out.println("Enter \n 1. Update Category\n 2. Update Price\n 3. Update Availability");
        int choice = scanner.nextInt();
        scanner.nextLine();
        performUpdate(item, choice);
    }

    // Method to perform the actual update
    private void performUpdate(Item item, int choice) throws InvalidSwitchChoice {
        switch (choice) {
            case 1:
                System.out.println("What is the new category you want to put the item in: ");
                String category = scanner.nextLine();
                item.setCategory(category);
                break;
            case 2:
                System.out.println("What is the new price: ");
                double price = scanner.nextDouble();
                item.setPrice(price);
                break;
            case 3:
                System.out.println("What is the new availability: ");
                boolean availability = scanner.nextBoolean();
                item.setAvailable(availability);
                if (!item.isAvailable()) {
                    orderManager.denyOrders(item);
                }
                break;
            default:
                throw new InvalidSwitchChoice("Please choose a number between 1 and 3.");
        }
    }



    @Override
    public String chooseCategory() throws InvalidSwitchChoice {
        String category = null;
        System.out.println("Available Categories:");
        System.out.println("1. Beverages");
        System.out.println("2. Snacks");
        System.out.println("3. Meals");
        System.out.println("4. Desserts");
        System.out.print("Please choose a category (1-4): ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                category = "Beverages";
                break;
            case 2:
                category = "Snacks";
                break;
            case 3:
                category = "Meals";
                break;
            case 4:
                category = "Desserts";
                break;
            default:
                throw new InvalidSwitchChoice("Please choose a valid option.");
        }

        return category;
    }

    // Filter items by category
    @Override
    public void filterItemByCategory() {
        String category = null;
        boolean validChoice = false;

        // Loop until a valid choice is made
        while (!validChoice) {
            try {
                category = chooseCategory();
                validChoice = true; // Exit loop if no exception was thrown
            } catch (InvalidSwitchChoice e) {
                System.out.println(e.getMessage());
            } finally {
                scanner.nextLine(); // Consume the newline to prevent input issues
            }
        }

        displayItemsInCategory(category);
    }


    // Method to display items in a specific category
    private void displayItemsInCategory(String category) {
        boolean found = false;
        System.out.println("Items in category: " + category);
        for (Item item : menuItems.keySet()) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                System.out.println(item.toString());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items found in the " + category + " category.");
        }
    }

    private int chooseAscendingOrDescending(){
        System.out.println("How would you like to sort the items by price?");
        System.out.println("1. Ascending");
        System.out.println("2. Descending");
        System.out.print("Please choose an option (1 or 2): ");
        int choice = scanner.nextInt();
        return choice;
    }

    // Method to sort items by price
    @Override
    public void displaySortedMenuByPrice() {
        try {
            int choice = chooseAscendingOrDescending();
            System.out.println("Menu Items Sorted by Price:");
            if (choice == 1) {
                System.out.println(this.toString());
            } else if (choice == 2) {
                menuItems.descendingMap().forEach((key, value) -> System.out.println(value));
            } else {
                throw new InvalidSwitchChoice("Invalid choice. Please choose 1 for ascending or 2 for descending.");
            }
        } catch (InvalidSwitchChoice e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public String toString() {
        StringBuilder menuDetails = new StringBuilder("Menu:\n");
        for (Map.Entry<Item, String> entry : menuItems.entrySet()) {
            menuDetails.append(entry.getValue()).append(" (Price: ").append(entry.getKey().getPrice()).append(")\n");
        }
        return menuDetails.toString();
    }
}
