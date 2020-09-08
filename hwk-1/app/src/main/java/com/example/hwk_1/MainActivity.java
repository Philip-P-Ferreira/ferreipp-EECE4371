package com.example.hwk_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("LIFECYCLE", "onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LIFECYCLE", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LIFECYCLE", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LIFECYCLE", "onPause()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("LIFECYCLE", "onRestart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LIFECYCLE", "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LIFECYCLE", "onDestroy()");
    }
}