package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainMenuActivity extends AppCompatActivity {
    String username = new String("My username");
    String id;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView hello = findViewById(R.id.Hello);

        String user = (String) getIntent().getSerializableExtra("USER");
        user = user.replaceAll("^\\[|\\]$", "");

        JSONObject userJSON = null;

        try {
            userJSON = new JSONObject(user);
            username = userJSON.getString("login");
            hello.setText("Hello, " + userJSON.getString("login") + "!");

            id = userJSON.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startBurime(View view) {
        System.out.println("Click!");
        Intent intent = new Intent(this, FindFriendActivity.class);
        intent.putExtra("USER", username);
        intent.putExtra("USER_ID", id);


        AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
        builder.setTitle("Choose");
        builder.setCancelable(true);
        builder.setNegativeButton("Random",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("Friend",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(intent);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
