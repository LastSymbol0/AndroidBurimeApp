package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class LogInActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }



//    @RequiresApi(api = Build.VERSION_CODES.N)
    public void logIn(View view) throws JSONException, InterruptedException {

        EditText login = (EditText) findViewById(R.id.login);
        final String log = login.getText().toString();

        EditText password = (EditText) findViewById(R.id.pass);
        final String pass = password.getText().toString();

        TextView alert = findViewById(R.id.alert);

        JSONObject userJSON = null;

        DB db = new DB();
        db.execute("get_user.php" + "?login=" + log+ "&pass=" + pass);
        try {
            userJSON = db.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if (userJSON != null) {
            if (userJSON.getInt("success") == 0) {
                alert.setText(userJSON.getString("message"));
            }
            else {
                alert.setText("Welcome");
                System.out.println("Done");
                Intent intent = new Intent(this, MainMenuActivity.class);
                intent.putExtra("USER", userJSON.getString("user").toString());
                startActivity(intent);
            }
        }
    }

    public void back() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void back_btn(View view) {
        back();
    }
}
