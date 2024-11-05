import java.util.Comparator;

// Comparator class to compare Item objects based on price
public class PriceComparator implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
        return Double.compare(item1.getPrice(), item2.getPrice());  // Compare prices of two items
    }
}
