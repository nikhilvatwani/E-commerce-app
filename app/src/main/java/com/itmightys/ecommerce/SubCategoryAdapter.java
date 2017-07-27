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

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.zip.Inflater;

/**
 * Created by Niknom on 3/24/2017.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    int total = 0;
    String images[] = new String[50];
    String names[] = new String[50];
    int id[] = new int[50];
    SubCategoryAdapter(Context context,String[] images, String[] names,int[] id,int total){
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.images = images;
        this.names = names;
        this.id = id;
        this.total = total;
    }
    @Override
    public SubCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.categorieslayout,parent,false);
        SubCategoryAdapter.MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        final MyViewHolder temp_holder = holder;
        holder.imageView.setTag(id[position]+"");
        ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
        imageLoader.get(images[position], new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                temp_holder.imageView.setImageBitmap(response.getBitmap());
            }
            public void onErrorResponse(VolleyError error) {

            }
        });
        final String name = new String(names[position]);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("goingin","subsubcategory");
                Intent intent = new Intent(mContext,SubCategory.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("<id>",view.getTag()+"");
                intent.putExtra("<name>",name);;
                mContext.startActivity(intent);
            }
        });
        holder.textView.setText(names[position]);
    }

    @Override
    public int getItemCount() {
        return total;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public MyViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.flag);
            textView = (TextView)itemView.findViewById(R.id.textview);
        }


    }
}

