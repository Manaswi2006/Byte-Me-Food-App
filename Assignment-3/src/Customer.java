import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class Customer extends User {
    private TreeMap<Cart, UUID> orderHistory;
    private Cart cart = new Cart(this,getOrderManager(),false);


    // Constructor
    public Customer(String username, String password, Order_Manager orderManager) {
        super(username, password,orderManager);
        this.orderHistory = new TreeMap<>(new CartCheckoutTimeComparator());
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public TreeMap<Cart, UUID> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(TreeMap<Cart, UUID> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public void addToHistory(UUID orderID, Cart cart) {
        orderHistory.put(cart, orderID);
        System.out.println("Order ID " + orderID + " has been added to the order history.");
    }

    public void viewHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("No orders found in history.");
            return;
        }

        System.out.println("=================================");
        System.out.println("         Order History           ");
        System.out.println("=================================");

        for (Map.Entry<Cart, UUID> entry : orderHistory.entrySet()) {
            Cart cart = entry.getKey();
            UUID orderID = entry.getValue();

            // Print Order ID
            System.out.printf("Order ID: %s%n", orderID.toString());
            System.out.println("Items in this order:");

            // Print Cart Details using the toString method
            System.out.println(cart.toString());

            // Print a separator line for better readability
            System.out.println("---------------------------------");
        }

        System.out.println("End of Order History");
        System.out.println("=================================");
    }

    public boolean logIn(String username, String password) {
        if (validateCredentials(username, password)) {
            setLoggedin(true);
            System.out.println("Customer logged in successfully.");
            return true;
        } else {
            System.out.println("Invalid credentials for Customer.");
            return false;
        }
    }

    public VIP_customer turnVIP() {
        // Create a new VIP_customer instance
        VIP_customer vipCustomer = new VIP_customer(this.getUsername(), this.getPassword(), this.getOrderManager());

        // Copy fields from the existing Customer
        vipCustomer.setOrderHistory(this.getOrderHistory());
        vipCustomer.setCart(this.getCart());
        vipCustomer.getCart().setIs_VIP(true);
        System.out.println("Customer has been upgraded to VIP.");
        return vipCustomer;
    }


    public void logOut() {
        setLoggedin(false);
        System.out.println("Customer logged out successfully.");
    }

    public void view_status(UUID orderID) {
        // Check in completed orders first
        if (getOrderManager().getCompleted_orders().containsKey(orderID)) {
            System.out.println("Order Status: " + getOrderManager().getCompleted_orders().get(orderID).getStatus());
        }
        // Check in regular orders
        else if (getOrderManager().getOrders().containsValue(orderID)) {
            // Retrieve the cart associated with the order ID
            Cart cart = getOrderManager().getOrder(orderID);
            if (cart != null) {
                System.out.println("Order Status: " + cart.getStatus());
            } else {
                System.out.println("Order ID " + orderID + " not found in regular orders.");
            }
        } else {
            System.out.println("Order ID " + orderID + " not found in either completed or pending orders.");
        }
    }

}
