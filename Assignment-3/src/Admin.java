public class Admin extends User {
    // Constructor
    public Admin(String username, String password, Order_Manager orderManager) {
        super(username, "XYZ", orderManager);
    }

    public boolean logIn(String username, String password) {
        if (validateCredentials(username, password)) {
            setLoggedin(true);
            System.out.println("Admin logged in successfully.");
            return true;
        } else {
            // MAKE AN EXCEPTION HANDLER?
            System.out.println("Invalid credentials for Admin.");
            return false;
        }
    }

    public void logOut() {
        System.out.println("Admin logged out successfully.");
        setLoggedin(false);
    }
}
