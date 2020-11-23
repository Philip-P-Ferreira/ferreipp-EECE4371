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
    String token, username;
    TextView errorText;
    ArrayList<Email> emails;
    EmailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        // get current intent and username header view
        Intent curIntent = getIntent();
        TextView userNameHeader = findViewById(R.id.listViewNameHeader);

        // set header and get token extra
        username = curIntent.getStringExtra(USERNAME);
        token = curIntent.getStringExtra(TOKEN);
        userNameHeader.setText(username);

        // find editable field
        errorText = findViewById(R.id.errorText);

        // get first email list
        emails = new ArrayList<Email>();
        getEmailListResponse();

        // set up email adapter and Recycler View
        RecyclerView rvEmails = findViewById(R.id.emailListView);
        adapter = new EmailAdapter(emails);
        rvEmails.setAdapter(adapter);
        rvEmails.setLayoutManager(new LinearLayoutManager(this));
    }

    public void logOutClick(View view) throws InterruptedException {

        errorText.setVisibility(View.INVISIBLE);

        // access server
        HashMap<String,String> argMap = new HashMap<>();
        argMap.put(EmailProtocol.TOKEN_KEY, token);
        HashMap<String,String> responseMap = NetworkActions.getServerResponse(argMap, EmailProtocol.LOG_OUT);

        // always go back to login on good connection
        if (!responseMap.isEmpty()) {

            // no need to validate, either we logout, or the session is over anyway
            Intent nextIntent = new Intent(this, LoginActivity.class);
            startActivity(nextIntent);
        } else {
            // on bad connect, simply display some error text
            errorText.setText(R.string.bad_connection);
            errorText.setVisibility(View.VISIBLE);
        }
    }

    public void refreshClick(View view) {

        // get new email list while keeping track of size
        int prevSize = emails.size();
        getEmailListResponse();
        int curSize = emails.size();

        // if the size has changed, notify adapter
        if (prevSize != curSize) {
            adapter.notifyItemRangeChanged(prevSize, curSize - prevSize);
        }
    }

    public void composeClick(View view) {

        // set next intent, put extras
        Intent nextIntent = new Intent(this, ComposeActivity.class);
        nextIntent.putExtra(USERNAME, username);
        nextIntent.putExtra(TOKEN, token);

        startActivity(nextIntent);
    }

    private void getEmailListResponse() {

        // define action for successful network request
        NetworkActions.OnOkStatus listAction = new NetworkActions.OnOkStatus() {
            @Override
            public void callback(HashMap<String, String> res) {

                // parse out response for individual emails
                ArrayList<Email> newList = new ArrayList<>();
                for (final String emailStr : res.get(EmailProtocol.EMAIL_LIST_KEY).split(EmailProtocol.EMAIL_DELIM)) {
                    newList.add(new Email(emailStr));
                }
                // update emails
                emails.clear();
                emails.addAll(newList);
            }
        };

        // method for networking and performing action
        NetworkActions.handleRequest(new HashMap<String, String>(), EmailProtocol.RETRIEVE_EMAILS, listAction, errorText, token);
    }
}
