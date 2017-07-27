package com.itmightys.ecommerce;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * Created by DELL on 15/02/2017.
 */

public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    public ButtonAdapter(Context c){
        mContext = c;
    }
    public String mResources[] = {"Electronics","Appliances","AutoMobile","Comupters"};

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        Log.d("called for position",String.valueOf(position));
        if(convertView == null){
            button = new Button(mContext);
            button.setTextColor(Color.parseColor("#ffffff"));
            button.setBackgroundResource(R.drawable.mybutton);
        }else{
            button = (Button)convertView;
        }
        button.setText(mResources[position]);
        return button;
    }
}
