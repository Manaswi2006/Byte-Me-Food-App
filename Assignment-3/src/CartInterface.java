public interface CartInterface {
    void addItem(Item item, int quantity);
    void removeItem(Item item);
    void removeAllItems();
    void updateItemQuantity(Item item, int quantity);
    int getTotalItems();
    double getTotalPrice();
    void checkout();
    void process_refund();
}
