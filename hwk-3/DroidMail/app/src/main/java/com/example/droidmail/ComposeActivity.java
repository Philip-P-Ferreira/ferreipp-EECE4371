package com.example.droidmail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.droidmail.emailutils.Email;
import com.example.droidmail.emailutils.EmailProtocol;

import java.util.HashMap;

public class ComposeActivity extends AppCompatActivity {

    // member vars
    String token, username;
    EditText recipientInput, bodyInput;
    TextView feedbackText;

    // constants for changing feedback color text
    private static final String GREEN_HEX = "#44c96f";
    private static final String RED_HEX ="#FF0000";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // get extras
        Intent curIntent = getIntent();
        token = curIntent.getStringExtra(LoginActivity.TOKEN);
        username = curIntent.getStringExtra(LoginActivity.USERNAME);

        // set username header
        ((TextView)findViewById(R.id.composeNameHeader)).setText(username);

        //get input fields
        recipientInput = findViewById(R.id.recipientField);
        bodyInput = findViewById(R.id.bodyInputField);

        //get error text
        feedbackText = findViewById(R.id.composeErrorText);
    }

    public void onBack(View view) {

        // set next intent, add extras
        Intent nextIntent = new Intent(this, ListViewActivity.class);
        nextIntent.putExtra(LoginActivity.TOKEN, token);
        nextIntent.putExtra(LoginActivity.USERNAME, username);

        startActivity(nextIntent);
    }

    public void onSend(View view) {
        // hide keyboard
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(),0);

        // set text back to red
        feedbackText.setTextColor(Color.parseColor(RED_HEX));

        // get to and body fields, make an email
        String recipient = recipientInput.getText().toString();
        String body = bodyInput.getText().toString();
        Email mailToSend = new Email(recipient, username, body);

        // add email to hashmap
        HashMap<String,String> argMap = new HashMap<>();
        argMap.put(EmailProtocol.EMAIL_KEY, mailToSend.toString());

        // if there is a recipient, send the email
        if (!recipient.isEmpty()) {
            // define a callback for a successful network request
            NetworkActions.OnOkStatus sendAction = new NetworkActions.OnOkStatus() {
                @Override
                public void callback(HashMap<String, String> res) {
                    feedbackText.setTextColor(Color.parseColor(GREEN_HEX));
                    feedbackText.setText(R.string.email_success_message);
                    feedbackText.setVisibility(View.VISIBLE);

                    recipientInput.getText().clear();
                    bodyInput.getText().clear();
                }
            };

            // method for requesting and performing an action
            NetworkActions.handleRequest(argMap, EmailProtocol.SEND_EMAIL, sendAction, feedbackText, token);
        } else {
            feedbackText.setText(R.string.no_recipient_error);
            feedbackText.setVisibility(View.VISIBLE);
        }
    }
}
