package server.utils;

import java.util.HashMap;
import java.util.Random;

public class TokenManager {
    
    // private member vars
    HashMap<String,String> tokenMap;
    Random tokenGen;
    
    // used in token generation
    static int TOKEN_LEN = 20;
    static String ALPHABET = "QWERTYUIOPASDFGHJKLZXCVBNM";
    static String NUMBERS = "123456789";
    static String ALPHANUMERIC = ALPHABET + ALPHABET.toLowerCase() + NUMBERS;

    // Constructor
    public TokenManager() {
        tokenMap = new HashMap<>();
        tokenGen =new Random(System.currentTimeMillis()); // use current time as seed
    }

    // 
    public String newToken(String userName) {
        
        String token = "";
        for (int i = 0; i < TOKEN_LEN; ++i) {
            token += ALPHANUMERIC.charAt(tokenGen.nextInt(ALPHANUMERIC.length()));
        }
        tokenMap.put(token, userName);

        return token;
    }

    public void destroyToken(String token) {
        tokenMap.remove(token);
    }

    public String getUser(String token) {
        return tokenMap.get(token);
    }
}
