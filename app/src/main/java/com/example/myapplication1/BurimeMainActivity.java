package com.example.myapplication1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BurimeMainActivity extends AppCompatActivity {

    String user;
    String user_id;
    String op_user;
    String op_user_id;
    Date last_modify;
    BurimeAdapter burimeAdapter;

    BurimeTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burime_main);

        user = (String) getIntent().getSerializableExtra("USER");
        user_id = (String) getIntent().getSerializableExtra("USER_ID");
        op_user = (String) getIntent().getSerializableExtra("OP_USER");
        op_user_id = (String) getIntent().getSerializableExtra("OP_USER_ID");
        Toast.makeText(getApplicationContext(), user, Toast.LENGTH_SHORT).show();
        TextView op_username = (TextView) findViewById(R.id.op_username);
        op_username.setText(op_user);

        try {
            initlistview();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
         checker_on();
    }


    public void initlistview() throws JSONException, ParseException {
        List<BurimeTask> burimeTaskList;
        //List<BurimeTask> burimeTaskList = init_fake_list();
        burimeTaskList = init_list(user_id, op_user_id);
        burimeAdapter = new BurimeAdapter(burimeTaskList, op_user, user, op_user_id, user_id);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(burimeAdapter);
        recyclerView.scrollToPosition(burimeTaskList.size() - 1);
    }

    public List<BurimeTask> init_list(String user_id, String op_user_id) throws JSONException, ParseException {
        List<BurimeTask> tasks = new ArrayList<>();
        String creator;
        JSONObject json = null;

        DB db = new DB();
        db.execute("get_all_task_burime_4.php" + "?id_1=" + user_id + "&id_2=" + op_user_id);
        try {
            json = db.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < json.getInt("success"); i++) {
            if ((json.getJSONObject(String.valueOf(i))).getString("from_id").equals(user_id))
                creator = user;
            else creator = op_user;
            tasks.add(new BurimeTask(json.getJSONObject(String.valueOf(i)), creator, creator == op_user));
            System.out.println(tasks.get(i).toString());
        }
        Collections.sort(tasks, BurimeTask.sort_dy_date);
//        System.out.println(tasks.toString());
        return tasks;
    }

    public List<BurimeTask> init_fake_list() {
        List<BurimeTask> burimeTaskList;
        burimeTaskList = new ArrayList<>(5);
        burimeTaskList.add(new BurimeTask("Fake #1", "line", "line2", "line3", "line4", op_user, "01/01/1990", "NULL"));
        burimeTaskList.add(new BurimeTask("Fake #2", "line", "line2", "line3", "line4", op_user, "01/01/1990", "NULL"));
        burimeTaskList.add(new BurimeTask("Fake #3", "line", "line2", "line3", "line4", op_user, "01/01/1990", "NULL"));
        burimeTaskList.add(new BurimeTask("Fake #4", "line", "line2", "line3", "line4", op_user, "01/01/1990", "NULL"));
        burimeTaskList.add(new BurimeTask("Fake #5", "line", "line2", "line3", "line4", op_user, "01/01/1990", "NULL"));
        return burimeTaskList;
    }

    public void newTask(View view) throws InterruptedException {
        task = new BurimeTask();
        task.setFrom_id(user_id);
        task.setTo_id(op_user_id);
        dialogType(task);
    }

    public void dialogType(BurimeTask task) {
        CharSequence[] types = {"AABB", "ABAB", "ABBA", "?"};
        System.out.println(types);
        AlertDialog.Builder typeDialogBuilder = new AlertDialog.Builder(BurimeMainActivity.this);
        typeDialogBuilder.setTitle("Choice\nType of rhyme")
                            .setItems(types, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    task.setType((String) types[which]);
//                                    Toast.makeText(getApplicationContext(), task.getType(), Toast.LENGTH_SHORT).show();
                                    dialogContent(task);
                                }
                            });
        AlertDialog typeDialog = typeDialogBuilder.create();
        typeDialog.show();
    }

    public void dialogContent(BurimeTask task) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(BurimeMainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_set_burime_task, null);

        builder.setView(view)
                .setPositiveButton("apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText header =  view.findViewById(R.id.d_header);
                        EditText line1 = view.findViewById(R.id.d_line1);
                        EditText line2 = view.findViewById(R.id.d_line2);
                        EditText line3 = view.findViewById(R.id.d_line3);
                        EditText line4 = view.findViewById(R.id.d_line4);
                        task.setHead(header.getText().toString());
                        task.setLine1(line1.getText().toString());
                        task.setLine2(line2.getText().toString());
                        task.setLine3(line3.getText().toString());
                        task.setLine4(line4.getText().toString());
                        Toast.makeText(getApplicationContext(), task.toString(), Toast.LENGTH_LONG).show();
                        task.setToDB();
                        try {
                            initlistview();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    public void dialogApply(View view) {
        EditText header = findViewById(R.id.d_header);
        EditText line1 = findViewById(R.id.d_line1);
        EditText line2 = findViewById(R.id.d_line2);
        EditText line3 = findViewById(R.id.d_line3);
        EditText line4 = findViewById(R.id.d_line4);
        task.setHead(header.getText().toString());
        task.setLine1(line1.getText().toString());
        task.setLine2(line2.getText().toString());
        task.setLine3(line3.getText().toString());
        task.setLine4(line4.getText().toString());
        Toast.makeText(getApplicationContext(), task.toString(), Toast.LENGTH_LONG).show();
    }

    public void checkUpdate() {

    }

    public void update_recyclerView(View view) {
        burimeAdapter.reset_list();
    }

    public void checker_on() {
        burimeAdapter.checker();
    }

    public void checker_stop_btn(View view) {
        burimeAdapter.listUpdate.cancel(false);
    }

    public void checker_stop() {
        burimeAdapter.listUpdate.cancel(false);
    }

    @Override
    public void onBackPressed() {
        checker_stop();
        super.onBackPressed();
    }
}
