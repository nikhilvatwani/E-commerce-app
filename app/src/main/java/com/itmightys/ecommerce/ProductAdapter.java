package com.itmightys.ecommerce;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL on 20/02/2017.
 */

public class ProductAdapter extends PagerAdapter {
    private  Context context;
    private LayoutInflater inflater;
    int[] mResource = {
            R.drawable.abc1,
            R.drawable.abc2,
            R.drawable.abc3,
            R.drawable.abc4,
            R.drawable.abc5,
            R.drawable.abc6
    };
    public String details;
    public String sku_code;
    public String product_id;
    public String sizes_available;
    public int discounted_price;
    public int actual_price;
    public String name;
    ImageLoader mImageLoader;
    NetworkImageView mNetworkImageView;
    int id;
    int counter;
    String images[];
    public ProductAdapter(Context context,int counter,String[] images){
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.counter = counter;
        this.images = images;
    }
    @Override
    public int getCount() {
        return counter;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
    public Object instantiateItem(ViewGroup container, int position){
        View view = inflater.inflate(R.layout.view_pager1,container,false);
        /*ImageView imageView= (ImageView)view.findViewById(R.id.imageView2);
        imageView.setImageResource(mResource[position]);*/
        mNetworkImageView = (NetworkImageView) view.findViewById(R.id
                .networkImageView);
        String url = images[position];
        Log.d("imageloader",url);
    mImageLoader = VolleySingleton.getInstance().getImageLoader();
        mImageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView,
                0, android.R.drawable
                        .ic_dialog_alert));
        mNetworkImageView.setImageUrl(url, mImageLoader);
        container.addView(view);
        return view;
    }
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
