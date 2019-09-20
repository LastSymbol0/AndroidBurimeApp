package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;


public class RegActivity extends AppCompatActivity {
    int login = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        EditText log = findViewById(R.id.login);
        log.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                final JSONObject[] loginJSON = {null};

                final CountDownLatch latch = new CountDownLatch(1);

                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        DB db = new DB();
                        loginJSON[0] = db.getJson("check_same_login.php" + "?login=" + s.toString());
                        latch.countDown();
                    }
                }).start();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    login = loginJSON[0].getInt("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TextView logAl = findViewById(R.id.loginAlert);
                try {
                    logAl.setText(loginJSON[0].getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (login == 1) {
                    logAl.setTextColor(R.color.colorPrimaryDark);
                }
                else {
                    logAl.setTextColor(R.color.colorAccent);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void registr(View view) throws IOException {
        if (!((EditText) findViewById(R.id.pass)).getText().toString().equals(((EditText) findViewById(R.id.passVerif)).getText().toString())) {
            Toast.makeText(getApplicationContext(), "Pass verification invalid", Toast.LENGTH_SHORT).show();
        }
        else if (login == 1) {
            new Thread((Runnable) // PUT is another valid option
                    () -> {
                        try {
                            run();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
            back();
        }
    }

    private void run() throws IOException {
        String post = "login=" + ((EditText) findViewById(R.id.login)).getText().toString() + "&" +
                "password=" + ((EditText) findViewById(R.id.pass)).getText().toString() + "&" +
                "email=" + ((EditText) findViewById(R.id.email)).getText().toString() + "&" +
                "name=" + ((EditText) findViewById(R.id.name)).getText().toString() + "&" +
                "lastname=" + ((EditText) findViewById(R.id.lastname)).getText().toString();

        String url = "https://resenddb2.000webhostapp.com/set_user.php?" + post;
        System.out.println("Url to get: " + url);

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

    }

    public void back() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void back_btn(View view) {
        back();
    }
}

