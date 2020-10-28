package server.utils;

import java.util.HashMap;

public class AccountManager {
    
    // member var
    HashMap<String,String> passwordMap;

    // Constructor
    public AccountManager() {
        passwordMap = new HashMap<>();
    }

    /**
     * addUser -
     * Adds a username and password pair to memory. If user already exists, 
     * throws IllegalArgumentException. Is synchronized
     * 
     * @param username - String, username
     * @param password - String, passwrod
     */
    public synchronized void addUser(String username, String password) {
        if (userExists(username)) {
            throw new IllegalArgumentException("Attempted to overwrite existing user and password");
        }

        passwordMap.put(username, password);
    }

    /**
     * passWordMatches -
     * Checks if the given userName and password pair are valid. Will return false
     * if user doesn't not exist. Is synchronized
     * 
     * @param username - String, username
     * @param password - String, password
     * @return boolean, is password matches or not
     */
    public synchronized boolean passwordMatches(String username, String password) {
        return userExists(username) && passwordMap.get(username).equals(password);
    }

    /**
     * userExists - 
     * Checks if the given user exists. Is synchronized
     * 
     * @param username - String, username
     * @return - String
     */
    public synchronized boolean userExists(String username) {
        return passwordMap.containsKey(username);
    }
}
