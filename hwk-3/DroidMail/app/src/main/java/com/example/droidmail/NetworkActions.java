package com.example.droidmail;

import android.view.View;
import android.widget.TextView;

import com.example.droidmail.emailutils.EmailProtocol;
import com.example.droidmail.emailutils.TcpStream;

import java.io.IOException;
import java.util.HashMap;

public class NetworkActions {

    // holds response so thread can access
    private static HashMap<String,String> responseMap;

    public static HashMap<String,String> getServerResponse(final HashMap<String,String> argMap, final String commandType) {

        responseMap = new HashMap<>();

        //define a network task for a new thread
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

    /**
     * resolveNetworkRequest -
     * Static method for accessing the server, processing the response, and performing the necessary action
     *
     * @param argMap - holds key-value pair arguments to send to server. Can be empty
     * @param commandType - type of command to send to server
     * @param action - Callback function to execute if the status comes back as "ok"
     * @param feedback - TextView to view error messages should some part of the request fail
     * @param token - String of unqiue token for a current user's session
     */
    public static void handleRequest(HashMap<String, String> argMap, final String commandType, final OnOkStatus action, final TextView feedback, final String token) {

        // set error text to invisible and put in the token
        feedback.setVisibility(View.INVISIBLE);
        argMap.put(EmailProtocol.TOKEN_KEY, token);

        // get response from server
        HashMap<String, String> responseMap = NetworkActions.getServerResponse(argMap, commandType);

        // go through all possible errors
        boolean displayError = true;
        if (!responseMap.isEmpty()) {
            String status = responseMap.get(EmailProtocol.STATUS_KEY);
            if (status == null || status.equals(EmailProtocol.STATUS_OK_VALUE)) {

                // what to do if network succeeds
                displayError = false;
                action.callback(responseMap);

            } else if (status.equals(EmailProtocol.INVALID_TOKEN_VALUE)) {
                feedback.setText(R.string.invalid_token);
            } else {
                feedback.setText(R.string.unknown_error);
            }
        } else {
            feedback.setText(R.string.bad_connection);
        }
        if (displayError) {
            feedback.setVisibility(View.VISIBLE);
        }
    }

    // interface for defining a callback
    public interface OnOkStatus {
        // function that's called should the network succeed
        void callback(final HashMap<String,String> res);
    }
}
