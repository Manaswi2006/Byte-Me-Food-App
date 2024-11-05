
public class User {

    // Attributes
    private final String username;
    private String password;
    private boolean loggedin = false;
    private Order_Manager orderManager;

    // Constructor
    public User(String username, String password, Order_Manager orderManager) {
        this.username = username;
        this.password = password;
        this.orderManager = orderManager;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    protected void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public void setOrderManager(Order_Manager orderManager) {
        this.orderManager = orderManager;
    }

    public Order_Manager getOrderManager() {
        return orderManager;
    }

    public void reset_password(String password) throws ChangeWithoutLogin{
        if (isLoggedin()){
            this.setPassword(password);
        }
        else{
            throw new ChangeWithoutLogin("Please login first.");
        }
    }

    // Abstract methods for login and logout functionality
//    public abstract boolean logIn(String username, String password);
//    public abstract void logOut();

    // Method to validate user credentials
    protected boolean validateCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}
