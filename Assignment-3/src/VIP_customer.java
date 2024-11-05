import java.util.UUID;

public class VIP_customer extends Customer {
    public VIP_customer(String username, String password, Order_Manager orderManager) {
        super(username, password, orderManager);
        this.getCart().setIs_VIP(true);
    }


    @Override
    public void view_status(UUID orderID) {
        // Check in completed orders first
        if (getOrderManager().getCompleted_orders().containsKey(orderID)) {
            System.out.println("Order Status: " + getOrderManager().getCompleted_orders().get(orderID).getStatus());
        }
        // Check in VIP orders
        else if (getOrderManager().getVIP_orders().containsValue(orderID)) {
            // Retrieve the cart associated with the order ID
            Cart cart = getOrderManager().getOrder(orderID);
            if (cart != null) {
                System.out.println("Order Status: " + cart.getStatus());
            } else {
                System.out.println("Order ID " + orderID + " not found in VIP orders.");
            }
        } else {
            System.out.println("Order ID " + orderID + " not found in either completed or pending orders.");
        }
    }
}
