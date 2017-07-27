package com.itmightys.ecommerce;

import android.content.Context;
import android.content.Intent;
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

/**
 * Created by DELL on 13/02/2017.
 */

public class FeaturedProductsAdapter extends RecyclerView.Adapter<FeaturedProductsAdapter.MyViewHolder> implements View.OnClickListener{
    private LayoutInflater inflater;
    private Context mContext;
    public int current_pos;
    static int temp = 0;
    int[] mResource = {
            R.drawable.abc1,
            R.drawable.abc2,
            R.drawable.abc3,
            R.drawable.abc4
    };
    /*{
            "Product 1(New)","Product 2(Old)","Product 3(Old)","Product 4(New)","Product 5(Old)","Product 6(New)"
    }*/
    /*{
            1000,2000,3000,4000,5000,6000
    };*/
    String names[];
    int prices[];
    int id[];
    int total;
    String[] product_images;
    public FeaturedProductsAdapter(Context context,String[] name,int[] actual_price,int[] ids,String[] product_images,int total){
        inflater = LayoutInflater.from(context);
        mContext = context;
        names = name;
        prices = actual_price;
        id = ids;
        this.total = total;
        this.product_images = product_images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.featured_products,parent,false);
        MyViewHolder holder = new MyViewHolder(view,mContext);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
           // position+=temp;

       // current_pos = position;
        //Toast.makeText(mContext,current_pos+"",Toast.LENGTH_SHORT).show();
        for(int i=0;i<2;i++){
            Log.d("forloop",id[i]+"");
        }
            //holder.imageview3.setImageResource(mResource[position]);
            holder.imageview3.setTag(id[position]);
            final ImageView imageView = holder.imageview3;
            Log.d("testing",holder.imageview3.getTag()+"");
            holder.textView.setText(names[position]);
            holder.textView2.setText("₹"+prices[position]);
            ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
            Log.d("checking",position+"---"+product_images[position]);
            imageLoader.get(product_images[position], new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    imageView.setImageBitmap(response.getBitmap());
                }
                public void onErrorResponse(VolleyError error) {

                }
            });
            holder.imageview3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,ActualProduct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("<StringName>",view.getTag()+"");
                    mContext.startActivity(intent);
                }
            });
            //holder.imageview4.setImageResource(mResource[position + 1]);
            //holder.imageview5.setImageResource(mResource[position + 2]);
            //temp+=2;

    }

    @Override
   /* public int getItemCount() {
        return (mResource.length/3);
    }*/
     public int getItemCount() {
        return total;
    }

    @Override
    public void onClick(View view) {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageview3;
        //ImageView imageview3,imageview4,imageview5;
        TextView textView,textView2;
        Context mContext;
        public MyViewHolder(View itemView,Context context) {
            super(itemView);
            imageview3 = (ImageView)itemView.findViewById(R.id.imageView3);
            textView = (TextView)itemView.findViewById(R.id.name);
            textView2 = (TextView)itemView.findViewById(R.id.price);
            //imageview4 = (ImageView)itemView.findViewById(R.id.imageView4);
            //imageview5= (ImageView)itemView.findViewById(R.id.imageView5);
            mContext = context;
            //imageview3.setOnClickListener(this);
            //imageview4.setOnClickListener(this);
            //imageview5.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,ActualProduct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Toast.makeText(mContext,getItemId()+"",Toast.LENGTH_SHORT).show();
            mContext.startActivity(intent);
        }*/
    }
}
