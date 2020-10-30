package com.example.droidmail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.droidmail.emailutils.EmailProtocol;
import com.example.droidmail.emailutils.TcpStream;
import java.io.IOException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    String token;
    boolean validInput;
    Thread logInThread;
    TextView feedbackText;
    HashMap<String, String> argMap;

    public static final String TOKEN = "com.exmaple.droidmail.TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void logUserIn(View view) throws InterruptedException {
        // hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

        // set valid input flag, get username and password
        validInput = false;
        String username = ((EditText)findViewById(R.id.usernameField)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordField)).getText().toString();

        // create argument map that will hold username and password
        argMap = new HashMap<>();
        argMap.put(EmailProtocol.USERNAME_KEY, username);
        argMap.put(EmailProtocol.PASSWORD_KEY, password);

        // get feedback text view
        feedbackText = (TextView) findViewById(R.id.feedbackText);

        // set thread to null
        logInThread = null;

        // if there is an empty field, skip
        if (!username.isEmpty() && !password.isEmpty()) {

            // define method to run in new thread
            Runnable logInRequest = new Runnable() {
                @Override
                public void run() {
                    try {
                        // connect, write request, read response
                        TcpStream clientStream = new TcpStream(EmailProtocol.SERVER_ADDRESS, EmailProtocol.PORT); // connect
                        EmailProtocol.sendProtocolMessage(clientStream, EmailProtocol.LOG_IN, argMap); // write
                        HashMap<String,String> responseMap = EmailProtocol.createProtocolMap(clientStream.read(), EmailProtocol.PAIR_DELIM,EmailProtocol.PAIR_SEPARATOR); // read

                        // check if credentials were valid
                        String status = responseMap.get(EmailProtocol.STATUS_KEY);
                        if (status != null && status.equals(EmailProtocol.STATUS_OK_VALUE)) {
                            validInput = true;
                            token = responseMap.get(EmailProtocol.TOKEN_KEY);
                        } else {
                            feedbackText.setText(R.string.invalid_credentials);
                        }
                        clientStream.close();

                    } catch (IOException e) {
                        // print feedback if couldn't connect to server
                        feedbackText.setText(R.string.bad_connection);
                    }
                }
            };
            // create and start new thread
            logInThread = new Thread(logInRequest);
            logInThread.start();
        } else {
            feedbackText.setText(R.string.empty_field);
        }

        // if thread isn't null, wait to finish
        if (logInThread != null) {

            logInThread.join();
            // do something if input was overall valid
            if (validInput) {
                Intent intent = new Intent(this, ListViewActivity.class);
                intent.putExtra(TOKEN, token);

                startActivity(intent);
            }
        }
    }
}
