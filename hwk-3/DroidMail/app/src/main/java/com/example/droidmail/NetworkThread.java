package com.example.droidmail;

import com.example.droidmail.emailutils.EmailProtocol;
import com.example.droidmail.emailutils.TcpStream;

import java.io.IOException;
import java.util.HashMap;

public class NetworkThread {

    private static HashMap<String,String> responseMap;

    public static HashMap<String,String> getNetworkResponse(final HashMap<String,String> argMap, final String commandType) {

        responseMap = new HashMap<>();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    // connect, write, then read response
                    TcpStream stream = new TcpStream(EmailProtocol.SERVER_ADDRESS, EmailProtocol.PORT);
                    EmailProtocol.sendProtocolMessage(stream, commandType, argMap);
                    responseMap = EmailProtocol.createProtocolMap(stream.read(), EmailProtocol.PAIR_DELIM,EmailProtocol.PAIR_SEPARATOR);

                    stream.close();
                } catch (IOException ignored){
                    // ignore because a bad connection will result in an empty map
                }

            }
        };

        // start thread then wait to complete
        // (no need to synchronize, this guarantees only one thread
        // will be accessing responseMap
        Thread actionThread = new Thread(task);
        actionThread.start();
        try {
            actionThread.join();
        } catch (InterruptedException ignored) {
        }

        return responseMap;
    }
}
