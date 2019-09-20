package com.example.myapplication1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void logIn(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    public void Reg(View view) {
        Intent intent = new Intent(this, RegActivity.class);
        startActivity(intent);
    }
}
