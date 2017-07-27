package com.itmightys.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Niknom on 3/3/2017.
 */

public class CategoriesAdapter extends ArrayAdapter<DemoItem> {

    private final LayoutInflater layoutInflater;
    Context mContext;
    public CategoriesAdapter(Context context, List<DemoItem> items) {
        super(context, 0, items);
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public CategoriesAdapter(Context context) {
        super(context, 0);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DemoItem item = getItem(position);
        View  v = layoutInflater.inflate( R.layout.categorieslayout, parent, false);
        ImageView imageView = (ImageView)v.findViewById(R.id.flag);
        final ImageView temporary = imageView;
        ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
        imageLoader.get(item.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                temporary.setImageBitmap(response.getBitmap());
            }
            public void onErrorResponse(VolleyError error) {

            }
        });
        final String name = new String(item.getText());
        imageView.setTag(item.getId()+"");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("goingin","subcategory");
                Intent intent = new Intent(mContext,SubCategory.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("<id>",view.getTag()+"");
                intent.putExtra("<name>",name);
                mContext.startActivity(intent);
            }
        });
        TextView textView = (TextView) v.findViewById(R.id.textview);
        textView.setText(item.getText());
        return v;
    }

    @Override public int getViewTypeCount() {
        return 2;
    }

    @Override public int getItemViewType(int position) {
        return position % 2 == 0 ? 1 : 0;
    }

    public void appendItems(List<DemoItem> newItems) {
        addAll(newItems);
        notifyDataSetChanged();
    }

    public void setItems(List<DemoItem> moreItems) {
        clear();
        appendItems(moreItems);
    }
    /*public void setImageLoader(int i){
        final String str = new String(""+i+"");
        ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
        imageLoader.get(category_images[i], new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                ImageView imageView = (ImageView)v.findViewById(R.id.flag);
                imageView.setImageBitmap(response.getBitmap());
            }
            public void onErrorResponse(VolleyError error) {

            }
        });
    }*/
}
