package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;

public  class BurimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {

    private List<BurimeTask> burimeTaskList;
    private String user_id;
    private String op_user_id;
    private String op_user;
    private String user;
    private Boolean checker_on_flag;
    public ListUpdate listUpdate;

    private BurimeAdapter one = this;

    public BurimeAdapter(List<BurimeTask> burimeTaskList, String op_user, String user, String op_user_id, String user_id) {
        this.burimeTaskList = burimeTaskList;
        this.op_user = op_user;
        this.op_user_id = op_user_id;
        this.user = user;
        this.user_id = user_id;
        checker_on_flag = false;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (burimeTaskList.get(position).getSolve() == 1)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType)
        {
            case 0 :
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item_1, viewGroup, false);
                return new BurimeViewHolderSolveType(view);
            case 1 :
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item_2, viewGroup, false);
                return new BurimeViewHolderTaskType(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        System.out.println("Bind holder position: " + String.valueOf(i));
        BurimeTask cur = burimeTaskList.get(i);
        switch (getItemViewType(i))
        {
            case 0 :
                BurimeViewHolderSolveType solve_holder = (BurimeViewHolderSolveType) holder;
                solve_holder.setViews(cur);
                break;
            case 1 :
                BurimeViewHolderTaskType task_holder = (BurimeViewHolderTaskType) holder;
                task_holder.setViews(cur);
                setOnClickListeners(task_holder, i);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return burimeTaskList.size();
    }

    @SuppressLint("ResourceAsColor")
    private void setOnClickListeners(BurimeViewHolderTaskType holder, int position) {

        holder.solve_btn.setOnClickListener(v -> {
            if (holder.line_edit1.getText().toString().endsWith(holder.getLine_end1().getText().toString()) &&
                    holder.line_edit2.getText().toString().endsWith(holder.getLine_end2().getText().toString()) &&
                    holder.line_edit3.getText().toString().endsWith(holder.getLine_end3().getText().toString()) &&
                    holder.line_edit4.getText().toString().endsWith(holder.getLine_end4().getText().toString())) {

                Toast.makeText(v.getContext(), "Good", Toast.LENGTH_LONG).show();

                burimeTaskList.get(position).updateInDB(new String[]{holder.line_edit1.getText().toString(),
                                                                       holder.line_edit2.getText().toString(),
                                                                       holder.line_edit3.getText().toString(),
                                                                       holder.line_edit4.getText().toString()});
                reset_list();

            } else {
                Toast.makeText(v.getContext(), "Bad rhyme", Toast.LENGTH_LONG).show();
            }

        });

        holder.line_edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().endsWith(holder.getLine_end1().getText().toString())) {
//                    holder.getLine_end1().setTextColor(R.color.colorSuccess);
//                    System.out.println("green");
                    //holder.line_end1.setText("✔");
                    holder.line_end1.setTextColor(Color.rgb(30, 250, 0));
                } else {
                    holder.line_end1.setTextColor(Color.rgb(200, 0, 10));
//                    System.out.println("red");
//                    holder.line_end1.setTextColor(R.color.colorAccent);
                }
            }
        });

        holder.line_edit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().endsWith(holder.getLine_end2().getText().toString())) {
                    holder.line_end2.setTextColor(Color.rgb(30, 250, 0));
                } else {
                    holder.line_end2.setTextColor(Color.rgb(200, 0, 10));
                }
            }
        });

        holder.line_edit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().endsWith(holder.getLine_end3().getText().toString())) {
//                    holder.getLine_end1().setTextColor(R.color.colorSuccess);
//                    System.out.println("green");
                    holder.line_end3.setTextColor(Color.rgb(30, 250, 0));
                } else {
                    holder.line_end3.setTextColor(Color.rgb(200, 0, 10));
//                    System.out.println("red");
//                    holder.line_end1.setTextColor(R.color.colorAccent);
                }
            }
        });

        holder.line_edit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().endsWith(holder.getLine_end4().getText().toString())) {
//                    holder.getLine_end1().setTextColor(R.color.colorSuccess);
//                    System.out.println("green");
                    holder.line_end4.setTextColor(Color.rgb(30, 250, 0));
                } else {
                    holder.line_end4.setTextColor(Color.rgb(200, 0, 10));
//                    System.out.println("red");
//                    holder.line_end1.setTextColor(R.color.colorAccent);
                }
            }
        });
    }

    public void reset_list() {
        burimeTaskList.clear();
        try {
            burimeTaskList = init_new_list();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.notifyDataSetChanged();
    }

    public void checker() {
        listUpdate = new ListUpdate();
        listUpdate.execute(user_id, op_user_id);

}

    private List<BurimeTask> init_new_list() throws JSONException, ParseException {
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

        assert json != null;
        for (int i = 0; i < json.getInt("success"); i++) {
            if ((json.getJSONObject(String.valueOf(i))).getString("from_id").equals(user_id))
                creator = user;
            else creator = op_user;
            tasks.add(new BurimeTask(json.getJSONObject(String.valueOf(i)), creator, creator.equals(op_user)));
            System.out.println(tasks.get(i).toString());
        }
        Collections.sort(tasks, BurimeTask.sort_dy_date);
        return tasks;
    }

    class BurimeViewHolderTaskType extends RecyclerView.ViewHolder {

        private TextView head;
        private TextView line_end1;
        private TextView line_end2;
        private TextView line_end3;
        private TextView line_end4;
        private TextView line_edit1;
        private TextView line_edit2;
        private TextView line_edit3;
        private TextView line_edit4;
        private TextView username;
        private TextView date;
        private ImageView userpic;
        private Button solve_btn;

        public BurimeViewHolderTaskType(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.head);
            line_end1 = itemView.findViewById(R.id.line_end1);
            line_end2 = itemView.findViewById(R.id.line_end2);
            line_end3 = itemView.findViewById(R.id.line_end3);
            line_end4 = itemView.findViewById(R.id.line_end4);
            line_edit1 = itemView.findViewById(R.id.line_edit1);
            line_edit2 = itemView.findViewById(R.id.line_edit2);
            line_edit3 = itemView.findViewById(R.id.line_edit3);
            line_edit4 = itemView.findViewById(R.id.line_edit4);
            username = itemView.findViewById(R.id.from_username_list);
            date = itemView.findViewById(R.id.date);
            userpic = itemView.findViewById(R.id.userpic);
            solve_btn = itemView.findViewById(R.id.solve_btn);
        }

        public void setViews(BurimeTask task) {
                head.setText(task.getHead());
                line_end1.setText(task.getLine1());
                line_end2.setText(task.getLine2());
                line_end3.setText(task.getLine3());
                line_end4.setText(task.getLine4());
                username.setText(task.getOp_username());
                date.setText(task.getDate().toString());
                userpic.setImageResource(R.drawable.ic_launcher_foreground);

                if (!task.getCan_solve()) {
                    solve_btn.setVisibility(View.GONE);
                    System.out.println("! can solve,   head:" + task.getHead());
                }
            //userpic.setImageURI(Uri.parse("@tools:sample/avatars"));
            //userpic.setImage();
        }

        public TextView getHead() {
            return head;
        }

        public void setHead(TextView head) {
            this.head = head;
        }

        public TextView getLine_end1() {
            return line_end1;
        }

        public void setLine_end1(TextView line_end1) {
            this.line_end1 = line_end1;
        }

        public TextView getLine_end2() {
            return line_end2;
        }

        public void setLine_end2(TextView line_end2) {
            this.line_end2 = line_end2;
        }

        public TextView getLine_end3() {
            return line_end3;
        }

        public void setLine_end3(TextView line_end3) {
            this.line_end3 = line_end3;
        }

        public TextView getLine_end4() {
            return line_end4;
        }

        public void setLine_end4(TextView line_end4) {
            this.line_end4 = line_end4;
        }

        public TextView getLine_edit1() {
            return line_edit1;
        }

        public void setLine_edit1(TextView line_edit1) {
            this.line_edit1 = line_edit1;
        }

        public TextView getLine_edit2() {
            return line_edit2;
        }

        public void setLine_edit2(TextView line_edit2) {
            this.line_edit2 = line_edit2;
        }

        public TextView getLine_edit3() {
            return line_edit3;
        }

        public void setLine_edit3(TextView line_edit3) {
            this.line_edit3 = line_edit3;
        }

        public TextView getLine_edit4() {
            return line_edit4;
        }

        public void setLine_edit4(TextView line_edit4) {
            this.line_edit4 = line_edit4;
        }

        public TextView getUsername() {
            return username;
        }

        public void setUsername(TextView username) {
            this.username = username;
        }

        public TextView getDate() {
            return date;
        }

        public void setDate(TextView date) {
            this.date = date;
        }

        public ImageView getUserpic() {
            return userpic;
        }

        public void setUserpic(ImageView userpic) {
            this.userpic = userpic;
        }

        public Button getSolve_btn() {
            return solve_btn;
        }

        public void setSolve_btn(Button solve_btn) {
            this.solve_btn = solve_btn;
        }
    }

    class BurimeViewHolderSolveType extends RecyclerView.ViewHolder {

        private TextView head;
        private TextView line1;
        private TextView line2;
        private TextView line3;
        private TextView line4;
        private TextView username;
        private TextView date;
        private ImageView userpic;

        public BurimeViewHolderSolveType(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.head);
            line1 = itemView.findViewById(R.id.line1);
            line2 = itemView.findViewById(R.id.line2);
            line3 = itemView.findViewById(R.id.line3);
            line4 = itemView.findViewById(R.id.line4);
            username = itemView.findViewById(R.id.from_username_list);
            date = itemView.findViewById(R.id.date);
            userpic = itemView.findViewById(R.id.userpic);
        }

        public void setViews(BurimeTask task) {
                head.setText(task.getHead());
                line1.setText(task.getLine1());
                line2.setText(task.getLine2());
                line3.setText(task.getLine3());
                line4.setText(task.getLine4());
                username.setText(task.getOp_username());
                date.setText(task.getDate().toString());
                userpic.setImageResource(R.drawable.ic_launcher_foreground);
            //userpic.setImageURI(Uri.parse("@tools:sample/avatars"));
            //userpic.setImage();
        }

        public TextView getHead() {
            return head;
        }

        public void setHead(TextView head) {
            this.head = head;
        }

        public TextView getLine1() {
            return line1;
        }

        public void setLine1(TextView line1) {
            this.line1 = line1;
        }

        public TextView getLine2() {
            return line2;
        }

        public void setLine2(TextView line2) {
            this.line2 = line2;
        }

        public TextView getLine3() {
            return line3;
        }

        public void setLine3(TextView line3) {
            this.line3 = line3;
        }

        public TextView getLine4() {
            return line4;
        }

        public void setLine4(TextView line4) {
            this.line4 = line4;
        }

        public TextView getUsername() {
            return username;
        }

        public void setUsername(TextView username) {
            this.username = username;
        }

        public TextView getDate() {
            return date;
        }

        public void setDate(TextView date) {
            this.date = date;
        }

        public ImageView getUserpic() {
            return userpic;
        }

        public void setUserpic(ImageView userpic) {
            this.userpic = userpic;
        }
    }

    class ListUpdate extends AsyncTask<String, Void, Void> {

        private String url;
        private String s1;
        private String s2;
        private List<BurimeTask> new_tasks;


        @Override
        protected Void doInBackground(String... strings) {
            s1 = strings[0];
            s2 = strings[1];
            url = "get_all_task_burime_4.php" + "?id_1=" + strings[0] + "&id_2=" + strings[1];
            System.out.println("hello" + url);
            while (true) {
                try {
                    if (update_waiter()) {
                        System.out.println("Changes found");
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isCancelled()) {
                    break;
                }
                System.out.println("Waiting for changes");
            }
            return null;
        }

        @SuppressLint("SimpleDateFormat")
        private Boolean update_waiter() throws JSONException, ParseException {
            JSONObject json = null;
            new_tasks = new ArrayList<>();
            String creator;

            DB db = new DB();
            json = db.getJson(url);

            if (json.getInt("success") != burimeTaskList.size())
                return true;

            assert json != null;
            for (int i = 0; i < json.getInt("success"); i++) {
                if ((json.getJSONObject(String.valueOf(i))).getString("from_id").equals(user_id))
                    creator = user;
                else creator = op_user;
                new_tasks.add(new BurimeTask(json.getJSONObject(String.valueOf(i)), creator, creator.equals(op_user)));
            }
            Collections.sort(new_tasks, BurimeTask.sort_dy_date);

            for (int i = 0; i < new_tasks.size(); i++) {
                if (new_tasks.get(i).getDate().after(burimeTaskList.get(i).getDate())) {
                    //System.out.println("Pos: " + i + "  Date: " + burimeTaskList.get(i).getDate() + "  New Date" + new_tasks.get(i).getDate());
                    return true;
                }
            }
            return false;
        }

        public List<BurimeTask> new_list() throws JSONException, ParseException {
            List<BurimeTask> tasks = new ArrayList<>();
            String creator;
            JSONObject json = null;

            DB db = new DB();
            db.execute(url);
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
            return tasks;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isCancelled()) return;
            burimeTaskList.clear();
            try {
                new_tasks = new_list();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(new_tasks.toString());
            burimeTaskList = new_tasks;
            notifyDataSetChanged();
            one.checker();
        }
    }
}

