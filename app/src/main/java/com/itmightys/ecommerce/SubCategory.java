package com.itmightys.ecommerce;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubCategory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public int current_id;
    public String category_images[] = new String[50];
    public String category_names[] = new String[50];
    public int category_id[] = new int[50];
    int total_categories;
    int total_products;
    public int id[] = new int[50];
    public int discounted_price[] = new int[50];
    public String product_names[] = new String[50];
    public String product_id[] = new String[50];
    public String product_images[][] = new String[50][50];
    public Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        current_id = Integer.parseInt(getIntent().getStringExtra("<id>"));

        getSupportActionBar().setTitle(getIntent().getStringExtra("<name>"));

        String url = "http://nikhilvatwani.esy.es/getSubCategoryOrProducts.php?parent="+current_id;

        RequestQueue mRequestQueue = VolleySingleton.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJson(response);
                RecyclerView categories_view = (RecyclerView)findViewById(R.id.categories);
                categories_view.setAdapter(new SubCategoryAdapter(mContext,category_images,category_names,category_id,total_categories));
                categories_view.setLayoutManager(new GridLayoutManager(mContext,2));
                RecyclerView products_view = (RecyclerView)findViewById(R.id.products);
                products_view.setAdapter(new AllProductsAdapter(mContext,total_products,product_names,id,discounted_price,product_images,product_id));
                products_view.setLayoutManager(new LinearLayoutManager(mContext));
                if(total_categories == 0){
                    Log.d("before","completion-categories");
                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.categories_linearlayout);
                    TextView no_categories = (TextView)findViewById(R.id.categories_heading);
                    RecyclerView recyclerView = (RecyclerView)findViewById(R.id.categories);
                    linearLayout.removeView(no_categories);
                    linearLayout.removeView(recyclerView);

                    //no_categories.setText("No Categories Found");
                }
                if(total_products == 0){
                    Log.d("before","completion-products");
                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.products_linearlayout);
                    //TextView no_categories = (TextView)findViewById(R.id.no_categories);
                    linearLayout.removeAllViews();
                    //TextView no_products = (TextView)findViewById(R.id.no_products);
                    //no_products.setText("No Products Found");
                }
                if(total_products == 0 && total_categories == 0){
                    //LinearLayout linearLayout = (LinearLayout)findViewById(R.id.categories_linearlayout);
                    TextView no_categories = (TextView)findViewById(R.id.no_categories);
                    //linearLayout.addView(no_categories);
                    no_categories.setText("No Results Found");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(jsonObjectRequest);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
    public void parseJson(JSONObject response){
        try {
            JSONArray categories_array = response.getJSONArray("categories");
            for(int i = 0;i < categories_array.length();i++){
                JSONObject jsonObject = categories_array.getJSONObject(i);
                category_id[i] = Integer.parseInt(jsonObject.getString("id"));
                category_names[i] = jsonObject.getString("name");
                category_images[i] = jsonObject.getString("img_src");
                ++total_categories;
            }

            JSONArray products_array = response.getJSONArray("products");
            Log.d("before","completion");
            for(int i = 0;i < products_array.length();i++){
                JSONObject jsonObject = products_array.getJSONObject(i);
                product_names[i]=jsonObject.getString("name");
                //Log.d("testing",names[i]);
                discounted_price[i] = Integer.parseInt(jsonObject.getString("discounted_price"));
                id[i]= Integer.parseInt(jsonObject.getString("id"));
                product_id[i] = jsonObject.getString("product_id");
                JSONArray images_array = jsonObject.getJSONArray("images");
                product_images[i][0] = images_array.getString(0);
                //Log.d("testingid",id[i]+"");

                ++total_products;
            }


        }catch (JSONException e){

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

}
