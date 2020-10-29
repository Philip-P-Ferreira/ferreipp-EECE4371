package com.example.droidmail;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void logUserIn(View view) {

       InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
       imm.hideSoftInputFromWindow(view.getWindowToken(),0);

       String userName = ((EditText)findViewById(R.id.usernameField)).getText().toString();
       String passWord =  ((EditText)findViewById(R.id.passwordField)).getText().toString();

       TextView viewer = (TextView)findViewById(R.id.feedbackText);
       viewer.setVisibility(View.VISIBLE);
       viewer.setText(userName + " " + passWord);
    }
}
