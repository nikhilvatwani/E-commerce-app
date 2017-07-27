package com.itmightys.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

/**
 * Created by Niknom on 3/2/2017.
 */

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyViewHolder> {
   public static final String PREFS_NAME = "AOP_PREFS";
    public static final String PREFS_KEY = "AOP_PREFS_String";
    public SharedPreferences sharedPreferences;
    private LayoutInflater inflater;
    private Context mContext;
    public int[] images = {
            R.drawable.abc1,
            R.drawable.abc2,
            R.drawable.abc3,
            R.drawable.abc4,
            R.drawable.abc5,
            R.drawable.abc6
    };
    public String[] names = {
            "Product 1","Product 2","Product 3","Product 4","Product 5","Product 6"
    };
    public int[] prices = {
            1000,2000,3000,4000,5000,6000
    };
    String[] ids = {
            "WEQ1243EWQ","FSDA134DF","FADD123DA","WEQ1243EWQ","FSDA134DF","FADD123DA"
    };
   public int i = 0;
    public String[] pos = new String[20];
    public Map<String,?> keys;
    public WishlistAdapter(Context context){
        mContext = context;
        inflater = LayoutInflater.from(context);
        sharedPreferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        keys = sharedPreferences.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("Key",entry.getKey().toString());
           pos[i++] = entry.getValue().toString();
        }

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Toast.makeText(mContext,"create",Toast.LENGTH_SHORT);
        View view = inflater.inflate(R.layout.productslayout,parent,false);
        //Log.d("Inside",new Integer(i).toString());
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WishlistAdapter.MyViewHolder holder, int position) {

        //Toast.makeText(mContext,"here",Toast.LENGTH_SHORT);

        //Log.d("InsideBind",new Integer(pos[position]).toString());
            //Toast.makeText(mContext,"bind",Toast.LENGTH_SHORT);
          holder.imageView.setImageResource(images[Integer.parseInt(pos[position])]);
        //Log.d("here",Integer.parseInt(pos[position])+"");
        holder.v.setTag(Integer.parseInt(pos[position]));
            //Toast.makeText(mContext,entry.getValue().toString(),Toast.LENGTH_SHORT);
            holder.textView.setText(names[Integer.parseInt(pos[position])]);
            holder.textView2.setText("₹"+prices[Integer.parseInt(pos[position])]);
        holder.textView3.setText("Id : "+ids[position]);

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ActualProduct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("<StringName>",view.getTag()+"");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        //Toast.makeText(mContext,"getItemCount",Toast.LENGTH_SHORT);
        return i;
    }
    public class  MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView,heartImage;
        TextView textView,textView2,textView3;
        View v;
        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            imageView = (ImageView)itemView.findViewById(R.id.imageView6);
            textView = (TextView)itemView.findViewById(R.id.textView11);
            textView2 = (TextView)itemView.findViewById(R.id.textView12);
            textView3 = (TextView)itemView.findViewById(R.id.pid);
            //itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext,ActualProduct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        }*/
    }
}
