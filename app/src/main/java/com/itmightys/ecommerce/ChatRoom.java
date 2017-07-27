package com.itmightys.ecommerce;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.itmightys.ecommerce.R.id.fab;
import static com.itmightys.ecommerce.R.id.right;

public class ChatRoom extends AppCompatActivity {

    private TextView textview;
    private EditText editText;
    private Button send;
    private DatabaseReference root;
    private String temp_key;
    String user_name;
    String room_name;
    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> messages = new ArrayList<String>();
    MessagesAdapter messagesAdapter;
    ListView listView;
    int inc = 0;
    int count;
    int flag = 0;
    //private LayoutInflater inflater = LayoutInflater.from(this);
    //View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);findViewById(R.id.message);

        //textview = (TextView)findViewById(R.id.textview);
        editText = (EditText)findViewById(R.id.message);
        send = (Button)findViewById(R.id.button_send);

        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        count = Integer.parseInt(getIntent().getExtras().get("count").toString());
        setTitle("Room - "+room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);
        Log.d("root",root.toString());

        Log.d("testingchatroom","1");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("msg",editText.getText().toString());

                message_root.updateChildren(map2);
                editText.setText("");

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("testingchatroom","2");
                Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("testingchatroom","3");
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d("testingchatroom","6");

    }

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();
        Log.d("testingchatroom","4");

        while(i.hasNext()){
            Log.d("testingchatroom","5");
            String chat_msg = ((DataSnapshot)i.next()).getValue().toString();
            String chat_user_name = ((DataSnapshot)i.next()).getValue().toString();
            usernames.add(chat_user_name);
            messages.add(chat_msg);
            inc++;
            if(flag == 1){
                messagesAdapter.notifyDataSetChanged();
            }
            if(inc == count){
                Log.d("count",inc+"");
                for(int j = 0;j<count;j++)
                    Log.d("count",messages.get(j).toString());
                listView = (ListView)findViewById(R.id.chat_listview);
                messagesAdapter = new MessagesAdapter(this,usernames,messages,user_name);
                listView.setAdapter(messagesAdapter);
                flag = 1;
            }

           /* RelativeLayout inner_layout = (RelativeLayout) findViewById(R.id.inner_layout);

            LinearLayout linearLayout = new LinearLayout(this);
            //linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackgroundResource(R.drawable.shadow);
           /* if(user_name.equals(chat_user_name)){
                Log.d("in","right");
                params.gravity = Gravity.RIGHT;
            }else{
                Log.d("in","left");
                params.gravity = Gravity.LEFT;
            }*/
           /* linearLayout.setLayoutParams(params);

            TextView name = new TextView(this);
            name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            name.setText(chat_user_name+"   ");
            name.setTextColor(getResources().getColor(R.color.red));
            linearLayout.addView(name);

            TextView msg = new TextView(this);
            msg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            msg.setText(chat_msg);
            msg.setTextColor(getResources().getColor(R.color.wallet_holo_blue_light));
            linearLayout.addView(msg);

            inner_layout.addView(linearLayout);*/

            /*view = inflater.inflate(R.layout.actual_message,null,false);

            TextView name = (TextView)view.findViewById(R.id.name);
            name.setText(chat_user_name);

            TextView msg = (TextView)view.findViewById(R.id.msg);
            msg.setText(chat_msg);*/

            //textview.append(chat_user_name+" : "+chat_msg+" \n");

        }

    }

}
