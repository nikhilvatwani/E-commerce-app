package com.itmightys.ecommerce;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

/**
 * Created by Niknom on 3/31/2017.
 */

public class MessagesAdapter extends BaseAdapter {

    ArrayList<String> usernames;
    ArrayList<String> messages;
    Context mContext;
    String user_name;
    public MessagesAdapter(Context mContext,ArrayList<String> usernames,ArrayList<String> messages,String user_name){
        this.usernames = usernames;
        this.messages = messages;
        this.mContext = mContext;
        this.user_name = user_name;
        Log.d("countof",usernames.size()+"");
    }
    @Override
    public int getCount() {
        return usernames.size();
    }

    @Override
    public Object getItem(int i) {
        return usernames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Log.d("i",i+"");
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.messages,viewGroup,false);
        RelativeLayout relativeLayout = (RelativeLayout)v.findViewById(R.id.relativelayout);
        LinearLayout linearLayoutLeft = (LinearLayout)v.findViewById(R.id.left);
        LinearLayout linearLayoutRight = (LinearLayout)v.findViewById(R.id.right);
        if(usernames.get(i).equals(user_name)){
            Log.d("right",i+"");
            relativeLayout.removeView(linearLayoutLeft);
            TextView name = (TextView)v.findViewById(R.id.name2);
            name.setText(usernames.get(i));
            TextView msg = (TextView)v.findViewById(R.id.msg2);
            msg.setText(messages.get(i));
        }
        else{
            Log.d("left",i+"");
            relativeLayout.removeView(linearLayoutRight);
            TextView name = (TextView)v.findViewById(R.id.name1);
            name.setText(usernames.get(i));
            TextView msg = (TextView)v.findViewById(R.id.msg1);
            msg.setText(messages.get(i));
        }
       /* TextView name = (TextView)v.findViewById(R.id.name1);
        name.setText(usernames.get(i));
        TextView msg = (TextView)v.findViewById(R.id.msg1);
        msg.setText(messages.get(i));*/
        /*RelativeLayout relativeLayout = (RelativeLayout)v.findViewById(R.id.relativelayout);

        LinearLayout linearLayout = new LinearLayout(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        if(usernames.get(i).equals(user_name)){
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.shadow);

        TextView name = new TextView(mContext);
        name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        name.setText(usernames.get(i));

        linearLayout.addView(name);

        TextView msg = new TextView(mContext);
        msg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        msg.setText(messages.get(i));
        linearLayout.addView(msg);

        relativeLayout.addView(linearLayout);*/

        return v;
    }
}
