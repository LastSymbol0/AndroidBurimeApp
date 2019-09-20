//package com.example.myapplication1;
//
//import android.os.AsyncTask;
//import android.support.v7.widget.RecyclerView;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Date;
//import java.util.concurrent.ExecutionException;
//
//public class ListUpdate extends AsyncTask<String, Void, JSONObject> {
//
//    private String url;
//    private Date last_modify;
//    private JSONObject json;
//
//    @Override
//    protected JSONObject doInBackground(String... strings) {
//        url = "check_upd_task_burime_4.php?" + "id1=" + strings[0] + "&id2=" + strings[1];
//        while (true) {
//            if (update_waiter()) {
//                break;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(JSONObject object) {
//        super.onPostExecute(object);
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//    }
//
//    private Boolean update_waiter() {
//        JSONObject json = null;
//
//        DB db = new DB();
//        db.execute(url);
//        try {
//            json = db.get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        try {
//            return json.getInt("Success") == 1;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}