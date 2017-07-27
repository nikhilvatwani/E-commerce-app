package com.itmightys.ecommerce;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActualProduct extends AppCompatActivity implements MyDialog.Communicator, NavigationView.OnNavigationItemSelectedListener {
    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String PREFS_KEY = "AOP_PREFS_String";
    SharedPreferences sharedpreferences;
    ViewPager viewpager;
    Spinner spinner;
    public String current_pos;
    public ImageView heart;
    public VolleySingleton volleySingleton;
    RequestQueue mRequestQueue;
    public Context mContext;
    public String details;
    public String sku_code;
    public String product_id;
    public String sizes_available;
    public int discounted_price;
    public int actual_price;
    public String name;
    public String url;
    public int id;
    int flag = 0;
    String images[] = new String[20];
    public int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        current_pos = getIntent().getStringExtra("<StringName>");
        url = "http://nikhilvatwani.esy.es/actual_product.php?id="+current_pos;
        volleySingleton = VolleySingleton.getInstance();
        mRequestQueue = volleySingleton.getRequestQueue();
        String url1 = "http://nikhilvatwani.esy.es/getImages.php?id=" + current_pos;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("images");
                    for (counter = 0; counter < jsonArray.length(); counter++) {
                        images[counter] = jsonArray.getString(counter);
                        Log.d("imageload",images[counter]);
                    }
                    viewpager = (ViewPager)findViewById(R.id.view_pager);
                    viewpager.setAdapter(new ProductAdapter(mContext,counter,images));
                    CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator123);
                    circleIndicator.setViewPager(viewpager);
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        //Image URL - This can point to any image file supported by Android

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(jsonObjectRequest);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("testing","success");
                        parseJsonResponse(response);
                        TextView name1 = (TextView)findViewById(R.id.name);
                        name1.setText(name);
                        getSupportActionBar().setTitle(name);
                        TextView actual_price1 = (TextView)findViewById(R.id.actualprice);
                        actual_price1.setPaintFlags(actual_price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        actual_price1.setText("₹"+actual_price);
                        TextView discounted_price1 = (TextView)findViewById(R.id.discountedprice);
                        discounted_price1.setText("₹"+discounted_price);
                        Log.d("actual",""+actual_price);
                        Log.d("discount",""+(((actual_price-discounted_price)*100)/actual_price));
                        int discount = (((actual_price-discounted_price)*100)/actual_price);

                        TextView discount1 = (TextView)findViewById(R.id.discount);
                        discount1.setText(discount+"%");

                        TextView product_id1 = (TextView)findViewById(R.id.productid_value);
                        product_id1.setText(product_id);
                        TextView details1 = (TextView)findViewById(R.id.details_value);
                        details1.setText(details);
                        TextView sku_code1 = (TextView)findViewById(R.id.skucode_value);
                        sku_code1.setText(sku_code);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("testing",url);
                Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request);


        /*spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.sizes,R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/
        Log.d("Insideactual",getIntent().getStringExtra("<StringName>"));

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        heart = (ImageView)findViewById(R.id.heart);
        if(sharedpreferences.contains(PREFS_KEY+current_pos)){
            heart.setImageResource(R.drawable.index2);
        }else{
            heart.setImageResource(R.drawable.index);
        }
        mContext = this;
        heart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(sharedpreferences.contains(PREFS_KEY+current_pos)){
                    editor.remove(PREFS_KEY+current_pos);
                    heart.setImageResource(R.drawable.index);
                    Toast.makeText(getApplicationContext(),"Removed from wishlist",Toast.LENGTH_SHORT).show();
                }else{
                    editor.putInt(PREFS_KEY+current_pos,Integer.parseInt(current_pos));
                    heart.setImageResource(R.drawable.index2);
                    Toast.makeText(getApplicationContext(),"Added to wishlist",Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    public void parseJsonResponse(JSONObject response){
        if(response == null|| response.length() == 0){
            return;
        }
        try{
                name = response.getString("name");
                actual_price = Integer.parseInt(response.getString("actual_price"));
                discounted_price = Integer.parseInt(response.getString("discounted_price"));
                product_id = response.getString("product_id");
                details = response.getString("details");
                sku_code = response.getString("sku_code");
                id = Integer.parseInt(response.getString("id"));

        }catch(JSONException e){

        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showDialog(View v){
        FragmentManager manager = getFragmentManager();
        MyDialog myDialog = new MyDialog();
        myDialog.show(manager,"MyDialog");
    }

    @Override
    public void OnDialogMessage(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
    public String getURL(){
        return "http://nikhilvatwani.esy.es/products.php";
    }
}
