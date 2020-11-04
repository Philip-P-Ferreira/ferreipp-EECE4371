package com.example.droidmail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.droidmail.emailutils.Email;
import com.example.droidmail.emailutils.EmailProtocol;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    // member vars
    HashMap<String, String> argMap;
    EditText passwordField;
    EditText usernameField;
    TextView feedbackText;

    // Constants for setting/accessing tokens
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

        // initialize argMap
        argMap = new HashMap<>();
    }

    public void logUserIn(View view) {
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
            HashMap<String,String> responseMap = NetworkActions.getServerResponse(argMap, EmailProtocol.LOG_IN);

            // if server actually responded (no error was thrown)
            if (!responseMap.isEmpty()) {
                // get the status
                String status = responseMap.get(EmailProtocol.STATUS_KEY);

                // check status (null, ok, or failed)
                if (status == null || status.equals(EmailProtocol.STATUS_OK_VALUE)) {

                    // get token, set next intent
                    String token = responseMap.get(EmailProtocol.TOKEN_KEY);
                    Intent nextIntent = new Intent(this, ListViewActivity.class);

                    // put in extras for next activity (token and username)
                    nextIntent.putExtra(TOKEN, token);
                    nextIntent.putExtra(USERNAME, username);
                    startActivity(nextIntent);

                } else if (status.equals(EmailProtocol.STATUS_FAIL_VALUE)) {

                    feedbackText.setText(R.string.invalid_credentials);
                    passwordField.getText().clear();
                } else {
                    feedbackText.setText(R.string.unknown_error);
                }
            } else {
                feedbackText.setText(R.string.bad_connection);
            }
        } else {
            feedbackText.setText(R.string.empty_field);
        }
    }
}
