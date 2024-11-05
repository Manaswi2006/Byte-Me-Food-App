import java.util.HashSet;
import java.util.Set;

public class User_Manager {
    private Set<User> userList; // Changed ArrayList to Set

    // Constructor
    public User_Manager() {
        userList = new HashSet<>(); // Use HashSet for no duplicates
    }

    // Getters and Setters
    public Set<User> getUserList() {
        return userList;
    }

    public void setUserList(Set<User> userList) {
        this.userList = userList;
    }

    // Method to add a user
    public void addUser(User user) {
        userList.add(user); // Will not add if the user already exists
    }

    public void removeUser(User user) { userList.remove(user);}
    // Method to find a user by name
    public User findUserByName(String name) {
        for (User user : userList) { // Iterate over the set
            if (user.getUsername().equals(name)) { // Check username
                return user; // Return the user if found
            }
        }
        return null; // Return null if user not found
    }
}
