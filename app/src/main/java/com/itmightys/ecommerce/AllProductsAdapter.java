package com.itmightys.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by DELL on 21/02/2017.
 */

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String PREFS_KEY = "AOP_PREFS_String";
    public int current_pos;
    public AllProductsAdapter.MyViewHolder temp_holder;
    SharedPreferences sharedpreferences;
    int[] images = {
            R.drawable.abc1,
            R.drawable.abc2,
            R.drawable.abc3,
            R.drawable.abc4,
            R.drawable.abc5,
            R.drawable.abc6
    };
    String[] names;
    int[] prices;
    int[] ids;
    String[][] images_url;
    String[] product_id;
    int total;
    public AllProductsAdapter(Context context,int total,String[] names,int[] id, int[] discounted_price,String[][] images_url,String[] product_id){
        mContext = context;
        inflater = LayoutInflater.from(context);
        sharedpreferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.names = names;
        this.ids = id;
        this.prices = discounted_price;
        this.images_url = images_url;
        this.product_id = product_id;
        this.total = total;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.productslayout,parent,false);
        Log.d("Inside","oncreate");
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
        String url = images_url[position][0];
        Log.d("image_url_position",images_url[position][0]+"");
        holder.heartImage.setTag(ids[position]);
        temp_holder = holder;
        current_pos = ids[position];
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    Log.d("iminside","onresponse");
                    temp_holder.imageView.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        //holder.imageView.setImageResource(images_url[position]);
        holder.v.setTag(ids[position]);
        Log.d("Image",position+"");
        holder.textView.setText(names[position]);
        holder.textView2.setText("₹"+prices[position]);
        holder.textView3.setText("Id : "+product_id[position]);
        if(sharedpreferences.contains(PREFS_KEY+position)){
            holder.heartImage.setImageResource(R.drawable.index2);
        }else{
            holder.heartImage.setImageResource(R.drawable.index);
        }

       /* holder.heartImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Toast.makeText(mContext.getApplicationContext(),)
                if(sharedpreferences.contains(PREFS_KEY+view.getTag())){
                    editor.remove(PREFS_KEY+view.getTag());
                    temp_holder.heartImage.setImageResource(R.drawable.index);
                    Toast.makeText(mContext.getApplicationContext(),"Removed from wishlist",Toast.LENGTH_SHORT).show();
                }else{
                    editor.putInt(PREFS_KEY+current_pos,Integer.parseInt(view.getTag()+""));
                    temp_holder.heartImage.setImageResource(R.drawable.index2);
                    Toast.makeText(mContext.getApplicationContext(),"Added to wishlist",Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            }
        });*/
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
        return total;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView,heartImage;
        TextView textView,textView2,textView3;
        View v;
        public MyViewHolder(View itemView){
            super(itemView);
            v = itemView;
            imageView = (ImageView)itemView.findViewById(R.id.imageView6);
            textView = (TextView)itemView.findViewById(R.id.textView11);
            textView2 = (TextView)itemView.findViewById(R.id.textView12);
            textView3 = (TextView)itemView.findViewById(R.id.pid);
            heartImage = (ImageView)itemView.findViewById(R.id.heart);
            heartImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            Log.d("onclick",position+"");
            if(sharedpreferences.contains(PREFS_KEY+position)){
                editor.remove(PREFS_KEY+position);
                heartImage.setImageResource(R.drawable.index);
                Toast.makeText(mContext.getApplicationContext(),"Removed from wishlist",Toast.LENGTH_SHORT).show();
            }else{
                editor.putInt(PREFS_KEY+position,Integer.parseInt(position+""));
                heartImage.setImageResource(R.drawable.index2);
                Toast.makeText(mContext.getApplicationContext(),"Added to wishlist",Toast.LENGTH_SHORT).show();
            }
            editor.commit();
        }

        /*@Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext,ActualProduct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        }*/
    }
}
