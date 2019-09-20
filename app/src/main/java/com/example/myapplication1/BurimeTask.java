package com.example.myapplication1;

import android.annotation.SuppressLint;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class BurimeTask {

    private String id;
    private String head;
    private String line1;
    private String line2;
    private String line3;
    private String line4;
    private String type;
    private String username;
    private String from_id;
    private String to_id;
    private Date date;
    private int solve;
    private int view_type;
    private Boolean can_solve;

    @Override
    public String toString() {
        return "BurimeTask{" +
                "id='" + id + '\'' +
                ", head='" + head + '\'' +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", line3='" + line3 + '\'' +
                ", line4='" + line4 + '\'' +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", from_id='" + from_id + '\'' +
                ", to_id='" + to_id + '\'' +
                ", date=" + date +
                ", solve=" + solve +
                ", view_type=" + view_type +
                ", userpic='" + userpic + '\'' +
                ", can_solve='" + can_solve + '\'' +
                '}';
    }

    private String userpic;

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public void setLine4(String line4) {
        this.line4 = line4;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BurimeTask() {

    }

    public String getId() { return id; }

    public String getHead() {
        return head;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine3() {
        return line3;
    }

    public String getLine4() {
        return line4;
    }

    public String getOp_username() {
        return username;
    }

    public Date getDate() {
        return date;
    }

    public String getUserpic() {
        return userpic;
    }

    public int getSolve() {
        return solve;
    }

    public BurimeTask(String head, String line1, String line2, String line3, String line4, String username, String date, String userpic) {
        this.head = head;
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line4 = line4;
        this.username = username;
        this.date = new Date(date);
        this.userpic = userpic;
    }



    @SuppressLint("SimpleDateFormat")
    public BurimeTask(JSONObject json, String from_username, Boolean can_solve) throws JSONException, ParseException {
        this.id = json.getString("id");
        this.head = json.getString("header");
        this.line1 = json.getString("line1");
        this.line2 = json.getString("line2");
        this.line3 = json.getString("line3");
        this.from_id = json.getString("from_id");
        this.to_id = json.getString("to_id");
        this.line4 =  json.getString("line4");
        this.solve = json.getInt("solve");
        this.username =  from_username;
        this.can_solve = can_solve;
        this.date =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(json.getString("modify_date"));
        this.userpic = "userpic";
    }

    public void setToDB() {
        JSONObject json = null;

        DB db = new DB();
        db.execute("set_task_burime_4.php" +
                "?type=" + type + "&from_id="+ from_id +
                "&to_id=" + to_id + "&header=" + head +
                "&line1=" + line1 + "&line2=" + line2 +
                "&line3=" + line3 + "&line4=" + line4);
//        try {
//            json = db.get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void updateInDB(String lines[]) {
        JSONObject json = null;

        DB db = new DB();
        db.execute("update_task_burime_4.php" + "?id=" + id +
                "&line1=" + lines[0] + "&line2=" + lines[1] +
                "&line3=" + lines[2] + "&line4=" + lines[3]);
//        try {
//            json = db.get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(json.toString());
    }

    final public static Comparator<BurimeTask> sort_dy_date = new Comparator<BurimeTask>() {
        @Override
        public int compare(BurimeTask o1, BurimeTask o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    };

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public Boolean getCan_solve() {
        return can_solve;
    }

    public void setCan_solve(Boolean can_solve) {
        this.can_solve = can_solve;
    }
}
