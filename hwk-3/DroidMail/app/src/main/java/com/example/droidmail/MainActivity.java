package com.example.droidmail;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // simply just go to login activity
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
  }
}