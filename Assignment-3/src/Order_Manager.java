import java.util.*;
import java.text.SimpleDateFormat;

// Class representing an order manager
public class Order_Manager {
    private TreeMap<Cart, UUID> VIP_orders;
    private TreeMap<Cart, UUID> orders; // Map of Cart and corresponding order ID
    private Map<UUID, Cart> completed_orders;
    private Map<UUID, Cart> denied_orders;

    // Constructor with custom comparator to sort by checkout time
    public Order_Manager() {
        this.VIP_orders = new TreeMap<>(new CartCheckoutTimeComparator());
        this.orders = new TreeMap<>(new CartCheckoutTimeComparator());
        this.completed_orders = new HashMap<>();
        this.denied_orders = new HashMap<>();
    }

    // Getter and setter methods
    public void setOrders(TreeMap<Cart, UUID> orders) {
        this.orders = orders;
    }

    public TreeMap<Cart, UUID> getOrders() {
        return orders;
    }

    public Map<UUID, Cart> getCompleted_orders() {
        return completed_orders;
    }

    public Map<UUID, Cart> getDenied_orders() {
        return denied_orders;
    }

    public TreeMap<Cart, UUID> getVIP_orders() {
        return VIP_orders;
    }

    public void setCompleted_orders(Map<UUID, Cart> completed_orders) {
        this.completed_orders = completed_orders;
    }

    public void setDenied_orders(Map<UUID, Cart> denied_orders) {
        this.denied_orders = denied_orders;
    }

    public void setVIP_orders(TreeMap<Cart, UUID> VIP_orders) {
        this.VIP_orders = VIP_orders;
    }

    // Create a new order
    public UUID createOrder(Cart cart, boolean isVIP) {
        UUID orderId = UUID.randomUUID(); // Generate a new order ID
        cart.setCheckoutTime(new Date()); // Set the checkout time of the cart

        if (isVIP) {
            VIP_orders.put(cart, orderId);
        } else {
            orders.put(cart, orderId);
        }

        return orderId;
    }

    // Get the cart associated with an order ID
    public Cart getOrder(UUID orderId) {
        for (Map.Entry<Cart, UUID> entry : VIP_orders.entrySet()) {
            if (entry.getValue().equals(orderId)) {
                return entry.getKey();
            }
        }
        for (Map.Entry<Cart, UUID> entry : orders.entrySet()) {
            if (entry.getValue().equals(orderId)) {
                return entry.getKey();
            }
        }
        return null; // If not found, return null
    }

    // Remove an order by ID
    public void removeOrder(UUID orderId) {
        // Variable to hold the cart to refund
        Cart cartToRefund = null;

        // Check VIP_orders map for the order ID
        for (Map.Entry<Cart, UUID> entry : VIP_orders.entrySet()) {
            if (entry.getValue().equals(orderId)) {
                cartToRefund = entry.getKey();
                break; // Exit the loop once the cart is found
            }
        }

        // If the cart was found in VIP_orders, process the refund
        if (cartToRefund != null) {
            cartToRefund.process_refund(); // Process refund for the found cart
            System.out.println("Refund processed for VIP order ID: " + orderId);
            VIP_orders.remove(cartToRefund); // Remove the order from VIP_orders
        } else {
            // If not found in VIP_orders, check in the regular orders map
            for (Map.Entry<Cart, UUID> entry : orders.entrySet()) {
                if (entry.getValue().equals(orderId)) {
                    cartToRefund = entry.getKey();
                    break; // Exit the loop once the cart is found
                }
            }

            // If the cart was found in regular orders, process the refund
            if (cartToRefund != null) {
                cartToRefund.process_refund(); // Process refund for the found cart
                System.out.println("Refund processed for regular order ID: " + orderId);
                orders.remove(cartToRefund); // Remove the order from regular orders
            } else {
                // If not found in either map, print a message
                System.out.println("Order ID not found in any orders.");
            }
        }
    }


    // Get total number of orders
    public int getTotalOrders() {
        return orders.size() + VIP_orders.size();
    }

    public void complete_order(UUID orderID) {
        // Check if the orderID is in VIP_orders
        if (VIP_orders.containsValue(orderID)) {
            // Find the cart associated with the orderID
            Cart cart = null;
            for (Map.Entry<Cart, UUID> entry : VIP_orders.entrySet()) {
                if (entry.getValue().equals(orderID)) {
                    cart = entry.getKey();
                    break;
                }
            }
            if (cart != null) {
                cart.setStatus("completed"); // Update cart status
                completed_orders.put(orderID, cart); // Move to completed orders
                VIP_orders.remove(cart); // Remove from VIP orders
                cart.getCustomer().addToHistory(orderID,cart);
                System.out.println("Order ID " + orderID + " has been completed and moved to completed orders.");
            }
        }
        // Check if the orderID is in orders
        else if (orders.containsValue(orderID)) {
            // Find the cart associated with the orderID
            Cart cart = null;
            for (Map.Entry<Cart, UUID> entry : orders.entrySet()) {
                if (entry.getValue().equals(orderID)) {
                    cart = entry.getKey();
                    break;
                }
            }
            if (cart != null) {
                cart.setStatus("completed"); // Update cart status
                completed_orders.put(orderID, cart); // Move to completed orders
                orders.remove(cart); // Remove from orders
                cart.getCustomer().addToHistory(orderID,cart);
                System.out.println("Order ID " + orderID + " has been completed and moved to completed orders.");
            }
        } else {
            System.out.println("Order ID " + orderID + " not found in either VIP or regular orders.");
        }
    }

    public void process_refund(UUID orderId) {
        // Variable to hold the found cart
        Cart cartToRefund = null;

        // Iterate through the VIP_orders map to find the cart associated with the given orderId
        for (Map.Entry<Cart, UUID> entry : getVIP_orders().entrySet()) {
            if (entry.getValue().equals(orderId)) {
                cartToRefund = entry.getKey();
                break; // Exit the loop once the cart is found
            }
        }

        for (Map.Entry<Cart, UUID> entry : getOrders().entrySet()) {
            if (entry.getValue().equals(orderId)) {
                cartToRefund = entry.getKey();
                break; // Exit the loop once the cart is found
            }
        }

        if (cartToRefund != null) {
            // Process refund for the found cart
            cartToRefund.process_refund();
            System.out.println("Refund processed for order ID: " + orderId);
        } else {
            System.out.println("Order ID not found");
        }
    }



    public void denyOrders(Item item) {
        TreeMap<Cart, UUID> ordersToDeny = new TreeMap<>(new CartCheckoutTimeComparator());
        TreeMap<Cart, UUID> VIP_ordersToDeny = new TreeMap<>(new CartCheckoutTimeComparator());

        for (Cart cart : orders.keySet()) {
            if (cart.getOrder().containsKey(item)) {
                cart.setStatus("denied");
                cart.process_refund();
                ordersToDeny.put(cart, orders.get(cart));
            }
        }

        for (Cart cart : VIP_orders.keySet()) {
            if (cart.getOrder().containsKey(item)) {
                cart.setStatus("denied");
                process_refund(getVIP_orders().get(cart));
                VIP_ordersToDeny.put(cart, VIP_orders.get(cart));
            }
        }

        for (Cart cart : ordersToDeny.keySet()) {
            UUID orderId = orders.remove(cart);
            denied_orders.put(orderId, cart);
        }
        for (Cart cart : VIP_ordersToDeny.keySet()) {
            UUID orderId = VIP_orders.remove(cart);
            denied_orders.put(orderId, cart);
        }
        System.out.println("All current orders with this item have been denied and refunds have been processed.");
    }

    public String View_Pending_Orders() {
        StringBuilder orderDetails = new StringBuilder();

        orderDetails.append("=================================\n");
        orderDetails.append("          Pending Orders         \n");
        orderDetails.append("=================================\n");

        // Check for VIP orders first
        if (!VIP_orders.isEmpty()) {
            orderDetails.append("VIP Orders:\n");
            for (Map.Entry<Cart, UUID> entry : VIP_orders.entrySet()) {
                orderDetails.append(" - VIP Order ID: ")
                        .append(entry.getValue())
                        .append("\n")
                        .append(entry.getKey())
                        .append("\n");
            }
        } else {
            orderDetails.append("No VIP orders found.\n");
        }

        // Check for regular orders
        if (!orders.isEmpty()) {
            orderDetails.append("\nRegular Orders:\n");
            for (Map.Entry<Cart, UUID> entry : orders.entrySet()) {
                orderDetails.append(" - Order ID: ")
                        .append(entry.getValue())
                        .append("\n")
                        .append(entry.getKey())
                        .append("\n");
            }
        } else {
            orderDetails.append("No regular orders found.\n");
        }

        orderDetails.append("=================================\n");
        return orderDetails.toString();
    }

    // Add this method to the Order_Manager class
    public String generateReport(Date date) {
        StringBuilder report = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String targetDate = sdf.format(date);

        report.append("=================================\n");
        report.append("        Completed Orders         \n");
        report.append("          for ").append(targetDate).append("\n");
        report.append("=================================\n");

        boolean hasOrders = false;
        for (Map.Entry<UUID, Cart> entry : completed_orders.entrySet()) {
            Cart cart = entry.getValue();
            Date checkoutDate = cart.getCheckoutTime(); // Assuming getCheckoutTime() returns the checkout date

            // Format the checkout date to compare with the target date
            String orderDate = sdf.format(checkoutDate);
            if (orderDate.equals(targetDate)) {
                report.append(" - Order ID: ").append(entry.getKey())
                        .append("\n").append(cart)
                        .append("\n");
                hasOrders = true;
            }
        }

        if (!hasOrders) {
            report.append("No completed orders found for this date.\n");
        }

        report.append("=================================\n");
        return report.toString();
    }


}