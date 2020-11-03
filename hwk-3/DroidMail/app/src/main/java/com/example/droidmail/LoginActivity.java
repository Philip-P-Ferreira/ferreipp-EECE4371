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

    HashMap<String, String> argMap, responseMap;
    EditText passwordField;
    EditText usernameField;
    TextView feedbackText;

    public static final String TOKEN = "com.exmaple.droidmail.TOKEN";
    public static final String USERNAME = "com.exmaple.droidmai.USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get feedback text view
        feedbackText = findViewById(R.id.feedbackText);
        usernameField = findViewById((R.id.usernameField));
        passwordField = findViewById(R.id.passwordField);

        argMap = new HashMap<>();
    }

    public void logUserIn(View view) throws InterruptedException {
        // hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

        // set valid response flag, get username and password
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        // create argument map that will hold username and password
        argMap.put(EmailProtocol.USERNAME_KEY, username);
        argMap.put(EmailProtocol.PASSWORD_KEY, password);

        // if there is an empty field, skip
        if (!username.isEmpty() && !password.isEmpty()) {

            // send args to server, get response
            responseMap = NetworkThread.getNetworkResponse(argMap, EmailProtocol.LOG_IN);

            // if server actually responded (no error was thrown)
            if (!responseMap.isEmpty()) {
                // get the status
                String status = responseMap.get(EmailProtocol.STATUS_KEY);

                // check status (null, ok, or failed)
                if (status == null) {
                    feedbackText.setText(R.string.unknown_error);
                } else if (status.equals(EmailProtocol.STATUS_OK_VALUE)) {
                    String token = responseMap.get(EmailProtocol.TOKEN_KEY);
                    Intent nextIntent = new Intent(this, ListViewActivity.class);

                    // put in extras for next activity (token and username)
                    nextIntent.putExtra(TOKEN, token);
                    nextIntent.putExtra(USERNAME, username);
                    startActivity(nextIntent);
                } else {
                    feedbackText.setText(R.string.invalid_credentials);
                    passwordField.getText().clear();
                }
            } else {
                feedbackText.setText(R.string.bad_connection);
            }
        } else {
            feedbackText.setText(R.string.empty_field);
        }


    }
}
