package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static java.lang.String.valueOf;

public class FindFriendActivity extends AppCompatActivity {
//    String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText editText;
    String loginToSearch;
    int countPeople;
    TextView findCount;
    Intent intent;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        listView=(ListView)findViewById(R.id.listview);
        editText=(EditText)findViewById(R.id.searchtext);
        intent = new Intent(this, BurimeMainActivity.class);
        intent.putExtra("USER", getIntent().getSerializableExtra("USER"));
        intent.putExtra("USER_ID", getIntent().getSerializableExtra("USER_ID"));

        try {
            loginToSearch = "";
            initList();
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < loginToSearch.length()){
                    // reset listview
                    loginToSearch = "";
                    try {
                        initList();
                    } catch (InterruptedException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                   try {
                        resetList();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                String op_username = listItems.get(position);
                String op_id = "id";
                try {
                    op_id = get_id(op_username);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.print("Position: " + position + "\tItem text: " + op_username);
                Toast.makeText(getApplicationContext(), op_username, Toast.LENGTH_SHORT).show();
                intent.putExtra("OP_USER", op_username);
                intent.putExtra("OP_USER_ID", op_id);
                startActivity(intent);

            }
        });

    }

    public void searchItem(String textToSearch) {
        String textToSearch1 = textToSearch.toLowerCase();
        String item;
        for(int i = 0; i < listItems.size();  i++) {
            item = listItems.get(i).toString();
            if(!item.toLowerCase().contains(textToSearch1)){
                listItems.remove(item);
                i--;
            }
        }
        countPeople = listItems.size();
        adapter.notifyDataSetChanged();
    }

/*    public void clearList() {
        for(String item:items){
                listItems.remove(item);
            }
        adapter.notifyDataSetChanged();
    }
*/

    public void resetListButton(View view) throws InterruptedException, JSONException {
        resetList();
    }

    public void resetList() throws InterruptedException, JSONException {
        loginToSearch = valueOf(editText.getText());
        searchItem(loginToSearch);
        findCount.setText("Find: " + countPeople +" people");
//        initList();
    }

    public void initList() throws InterruptedException, JSONException {

        final JSONObject[] loginsJSON = {null};

        final CountDownLatch latch = new CountDownLatch(1);

        new Thread(new Runnable(){
            @Override
            public void run() {
                DB db = new DB();
                loginsJSON[0] = db.getJson("get_users.php" + "?login=" + loginToSearch);
                latch.countDown();
            }
        }).start();
        latch.await();

        countPeople =  loginsJSON[0].getInt("success");
        findCount = (TextView) findViewById(R.id.findCount);

        findCount.setText("Find: " + countPeople +" people");

        listItems=new ArrayList<String>();
        int i = 0;
        while (i < countPeople) {
            listItems.add(loginsJSON[0].getString(String.valueOf(i)));
            i++;
        }
        adapter=new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, listItems);
        listView.setAdapter(adapter);
    }

    public String get_id(String login) throws InterruptedException, JSONException {
        final JSONObject[] json = {null};

        final CountDownLatch latch = new CountDownLatch(1);

        new Thread(new Runnable(){
            @Override
            public void run() {
                DB db = new DB();
                json[0] = db.getJson("get_user_id.php" + "?login=" + login);
                latch.countDown();
            }
        }).start();
        latch.await();
        if (json[0].getInt("success") == 1) {
            return json[0].getString("id");
        }
        return "id";
    }
}
