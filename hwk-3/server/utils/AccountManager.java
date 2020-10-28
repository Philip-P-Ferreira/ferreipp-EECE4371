package server.utils;

import java.util.HashMap;

public class AccountManager {
    
    HashMap<String,String> passwordMap;

    public AccountManager() {
        passwordMap = new HashMap<>();
    }

    public void addUser(String username, String password) {
        if (userExists(username)) {
            throw new IllegalArgumentException("Attempted to overwrite existing user and password");
        }

        passwordMap.put(username, password);
    }

    public boolean passwordMatches(String username, String password) {
        return userExists(username) && passwordMap.get(username).equals(password);
    }

    public boolean userExists(String username) {
        return passwordMap.containsKey(username);
    }
}
