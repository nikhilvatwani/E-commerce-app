package com.itmightys.ecommerce;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by DELL on 13/02/2017.
 */

public class CustomAdapter extends PagerAdapter {
    Context mContext;
    String[] slider_images;
    int total;
    LayoutInflater mLayoutInflator;
    int[] mResource = {
            R.drawable.abc1,
            R.drawable.abc2,
            R.drawable.abc3,
            R.drawable.abc4,
            R.drawable.abc5,
            R.drawable.abc6
    };
    public CustomAdapter(Context context,String[] slider_images,int total){
        mContext = context;
        mLayoutInflator = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.slider_images = slider_images;
        this.total = total;
        Log.d("inside","const");
    }
    public Object instantiateItem(ViewGroup container,int position){
        View view = mLayoutInflator.inflate(R.layout.view_pager,container,false);
        final ImageView imageView = (ImageView)view.findViewById(R.id.imageView2);
        ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
        Log.d("checking",position+"---"+slider_images[position]);
        imageLoader.get(slider_images[position], new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imageView.setImageBitmap(response.getBitmap());
            }
            public void onErrorResponse(VolleyError error) {

            }
        });
        //imageView.setImageResource(mResource[position]);
        container.addView(view);
        Log.d("inside","ii");
        return view;
    }
    @Override
    public int getCount() {
        Log.d("inside","gc");return total;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        Log.d("inside","ivfo");
        return view == ((LinearLayout) object);
    }
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("inside","di");
        container.removeView((LinearLayout) object);
        }
}
