import java.util.Comparator;

// Class to compare Cart objects based on their checkout time
public class CartCheckoutTimeComparator implements Comparator<Cart> {
    @Override
    public int compare(Cart cart1, Cart cart2) {
        if (cart1.getCheckoutTime() == null || cart2.getCheckoutTime() == null) {
            throw new IllegalArgumentException("Cart checkout time cannot be null");
        }
        return cart1.getCheckoutTime().compareTo(cart2.getCheckoutTime());
    }
}
