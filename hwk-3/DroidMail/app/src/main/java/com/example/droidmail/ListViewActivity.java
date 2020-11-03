package com.example.droidmail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import static com.example.droidmail.LoginActivity.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.droidmail.emailutils.Email;
import com.example.droidmail.emailutils.EmailProtocol;

import java.util.*;

public class ListViewActivity extends AppCompatActivity {

    // member vars
    String token;
    TextView errorText;
    ArrayList<Email> emails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        // get current intent and username header view
        Intent curIntent = getIntent();
        TextView userNameHeader = findViewById(R.id.userNameHeader);

        // set header and get token extra
        userNameHeader.setText(curIntent.getStringExtra(USERNAME));
        token = curIntent.getStringExtra(TOKEN);

        // find editable field
        errorText = findViewById(R.id.errorText);

        // get first email list
        refreshClick(findViewById(R.id.refreshButton));

        // set up email adapter and Recycler View
        RecyclerView rvEmails = findViewById(R.id.emailListView);
        EmailAdapter adapter = new EmailAdapter(emails);
        rvEmails.setAdapter(adapter);
        rvEmails.setLayoutManager(new LinearLayoutManager(this));
    }

    public void logOutClick(View view) throws InterruptedException {

        // store instance of "this" to use in callback
        final ListViewActivity thisReference = this;

        // create callback for successful network operations
        ClickAction logOutAction = new ClickAction() {
            @Override
            public void callback(HashMap<String, String> res) {
                Intent nextIntent = new Intent(thisReference, LoginActivity.class);
                startActivity(nextIntent);
            }
        };

        // method for networking and performing action
        commonClickActions(new HashMap<String, String>(), EmailProtocol.LOG_OUT, logOutAction);
    }

    public void refreshClick(View view) {

        // reset email var
        emails = new ArrayList<>();

        // define action for successful network request
        ClickAction listAction = new ClickAction() {
            @Override
            public void callback(HashMap<String, String> res) {
                // parse out response for individual emails
                for (final String emailStr : res.get(EmailProtocol.EMAIL_LIST_KEY).split(EmailProtocol.EMAIL_DELIM)) {
                    emails.add(new Email(emailStr));
                }
            }
        };

        // method for networking and performing action
        commonClickActions(new HashMap<String, String>(), EmailProtocol.RETRIEVE_EMAILS, listAction);
    }

    public void composeClick(View view) {


    }

    public void commonClickActions(final HashMap<String, String> argMap, final String commandType, final ClickAction action) {

        // set error text to invisible and put in the token
        errorText.setVisibility(View.INVISIBLE);
        argMap.put(EmailProtocol.TOKEN_KEY, token);

        // get response from server
        HashMap<String,String> responseMap = NetworkThread.getNetworkResponse(argMap, commandType);

        // go through all possible errors
        boolean displayError = true;
        if (!responseMap.isEmpty()) {
            String status = responseMap.get(EmailProtocol.STATUS_KEY);
            if (status == null) {
                errorText.setText(R.string.unknown_error);
            } else if (status.equals(EmailProtocol.STATUS_OK_VALUE)) {
                displayError = false;
                action.callback(responseMap);
            } else {
                errorText.setText(R.string.invalid_token);
            }
        } else {
            errorText.setText(R.string.bad_connection);
        }
        if (displayError) {
            errorText.setVisibility(View.VISIBLE);
        }
    }

    // interface for defining a callback
    private interface ClickAction {
        void callback(final HashMap<String,String> res);
    }
}
