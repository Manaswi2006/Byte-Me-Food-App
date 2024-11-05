import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

// Class representing a customer's cart
public class Cart implements CartInterface{
    private Scanner scanner = new Scanner(System.in);
    private Map<Item, Integer> order; // Map of item and quantity
    private Customer customer; // Customer identifier
    private String status;
    private Double total_amount = 0.0; // Initializing total amount to avoid null issues
    private boolean payment_done = false;
    private Order_Manager orderManager;
    private String Description;
    private Date checkoutTime; // New attribute to track checkout time
    private boolean is_VIP;

    // Constructor
    public Cart(Customer customer, Order_Manager orderManager, boolean is_VIP) {
        this.order = new HashMap<>();
        this.customer = customer;
        this.status = "pre-checkout";
        this.orderManager = orderManager;
        this.checkoutTime = null; // Initialized to null initially
        this.is_VIP = is_VIP;
    }

    public boolean isIs_VIP() {
        return is_VIP;
    }

    public void setIs_VIP(boolean is_VIP) {
        this.is_VIP = is_VIP;
        //System.out.println("Reached here");
        //System.out.println(is_VIP);
    }

    public Map<Item, Integer> getOrder() {
        return order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getStatus() {
        return status;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setOrder(Map<Item, Integer> order) {
        this.order = order;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }

    public boolean isPayment_done() {
        return payment_done;
    }

    public void setPayment_done(boolean payment_done) {
        this.payment_done = payment_done;
    }

    public Order_Manager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(Order_Manager orderManager) {
        this.orderManager = orderManager;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public Date getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(Date checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    // Add an item to the cart
    @Override
    public void addItem(Item item, int quantity) {
        Double prev_price = this.getTotal_amount();
        this.setTotal_amount(prev_price + (item.getPrice() * quantity));
        getOrder().put(item, order.getOrDefault(item, 0) + quantity);
    }

    // Remove an item from the cart
    @Override
    public void removeItem(Item item) {
        int quantity = getOrder().get(item);
        double price = getTotalPrice();
        setTotal_amount(price - (quantity*item.getPrice()));
        getOrder().remove(item);
    }

    @Override
    public void removeAllItems(){
        setOrder(new HashMap<>());
        setTotal_amount(0.0);
    }

    // Update the quantity of an item in the cart
    @Override
    public void updateItemQuantity(Item item, int quantity) {
        if (getOrder().containsKey(item)) {
            if (quantity <= 0) {
                removeItem(item);
            } else {
                getOrder().put(item, quantity);
            }
        }
    }

    // Get total number of items in the cart
    @Override
    public int getTotalItems() {
        return getOrder().values().stream().mapToInt(Integer::intValue).sum();
    }

    // Get total price of the items in the cart
    @Override
    public double getTotalPrice() {
        return this.getTotal_amount();
    }

    public void payment() {
        setPayment_done(true);
    }

    @Override
    public void process_refund() {
        setPayment_done(false);
    }

    @Override
    public void checkout() {
        handleSpecialRequest();
        processPayment();
    }

    // Method to handle any special requests
    private void handleSpecialRequest() {
        System.out.println("Do you have any special request regarding your order: \nEnter 1 for yes, 2 for no");
        int choice_1 = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice_1) {
            case 1:
                System.out.println("Enter your description.");
                String specialRequest = scanner.nextLine();
                this.setDescription(specialRequest);
                System.out.println("Description added successfully");
                break;
            case 2:
                break;
            default:
                System.out.println("Please choose a valid option");
        }
    }

    private void processPayment() {
        System.out.print("Please pay amount: ");
        System.out.println(this.getTotalPrice());
        System.out.println("Choose 1 for payment, 2 to go back");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                payment();
                setStatus("order received");
                this.setCheckoutTime(new Date());
                UUID orderId = orderManager.createOrder(this, isIs_VIP());
                //System.out.println("isIs_VIP()"+is_VIP);
                // Change if it is VIP
                System.out.println("Order placed successfully. Your Order ID is: " + orderId);
                break;
            case 2:
                break;
            default:
                System.out.println("Please choose a valid option");
        }
    }

    @Override
    public String toString() {
        StringBuilder cartDetails = new StringBuilder();

        cartDetails.append("=================================\n");
        cartDetails.append("          Customer Cart          \n");
        cartDetails.append("=================================\n");
        cartDetails.append("Customer: ").append(customer.getUsername()).append("\n");

        if (getDescription() != null && !getDescription() .isEmpty()) {
            cartDetails.append("Description: ").append(getDescription() ).append("\n");
        }

        cartDetails.append("\nItems in Cart:\n");

        for (Map.Entry<Item, Integer> entry : order.entrySet()) {
            cartDetails.append(" - ")
                    .append(entry.getKey().getName())
                    .append(" (Quantity: ")
                    .append(entry.getValue())
                    .append(")\n");
        }

        cartDetails.append("\n---------------------------------\n");
        cartDetails.append("Total Items: ").append(getTotalItems()).append("\n");
        cartDetails.append("Total Price: ").append(getTotalPrice()).append("\n");

        if (checkoutTime != null) {
            cartDetails.append("Checkout Time: ").append(checkoutTime).append("\n");
        }

        cartDetails.append("=================================\n");
        return cartDetails.toString();
    }

}
