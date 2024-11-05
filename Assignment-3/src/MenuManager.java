public interface MenuManager {
    void addItem(Item item);
    void removeItem(String name) throws ItemDoesntExistException;
    Item searchItem(String keyword);
    void searchItemsByKeyword(String keyword);
    void updateItem() throws InvalidSwitchChoice, ItemDoesntExistException;
    void filterItemByCategory();
    void displaySortedMenuByPrice();
    String chooseCategory() throws InvalidSwitchChoice;
}
