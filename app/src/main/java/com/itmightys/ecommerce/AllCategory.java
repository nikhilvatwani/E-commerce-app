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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllCategory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ListAdapter adapter;
    private AsymmetricGridView listView;
    int[] images = {
            R.drawable.abc1,
            R.drawable.abc2,
            R.drawable.abc3,
            R.drawable.abc4,
            R.drawable.abc5,
            R.drawable.abc6
    };
    String[] categories = {
            "Appliances","Electronics","Watches","Computers","Cosmetics","School Products"
    };
    public String category_images[] = new String[50];
    public String category_names[] = new String[50];
    public int category_id[] = new int[50];
    Context mContext;
    RequestQueue mRequestQueue;
    public int total = 0;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         mContext =this;
        //GridView gridView = (GridView)findViewById(R.id.gridview);
        //gridView.setAdapter(new CategoriesAdapter(this));
         mRequestQueue = VolleySingleton.getInstance().getRequestQueue();
         JsonObjectRequest jsonForCategories = new JsonObjectRequest(Request.Method.GET, "http://nikhilvatwani.esy.es/getFeaturedCategories.php", new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {
                 parseFeaturedCategories(response);
                 /*for (int i = 0; i < response.length(); i++) {
                     switch (i) {
                         case 0:
                             setImageLoader(0);
                             TextView textView1 = (TextView) findViewById(R.id.textview_category1);
                             textView1.setText(category_names[i]);
                             break;
                         case 1:
                             setImageLoader(1);
                             TextView textView2 = (TextView) findViewById(R.id.textview_category2);
                             textView2.setText(category_names[i]);
                             break;
                         case 2:
                             setImageLoader(2);
                             TextView textView3 = (TextView) findViewById(R.id.textview_category3);
                             textView3.setText(category_names[i]);
                             break;
                     }
                 }*/
                 listView = (AsymmetricGridView) findViewById(R.id.all_categories);
                 listView.setAllowReordering(true);
                 final List<DemoItem> items = new ArrayList<>();
                 int flag =1;
                 for (int i=0;i<total;i++){
                     if(flag==1){
                         items.add(new DemoItem(2,1,0,category_names[i],category_images[i],category_id[i]));
                         flag = 0;
                         continue;
                     }
                     items.add(new DemoItem(1,1,0,category_names[i],category_images[i],category_id[i]));
                     if(i<(categories.length-1))
                         items.add(new DemoItem(1,1,0,category_names[i+1],category_images[++i],category_id[i]));
                     flag = 1;
                 }
                 adapter = new CategoriesAdapter(mContext, items);
                 listView.setAdapter(new AsymmetricGridViewAdapter(mContext, listView, adapter));
             }
         }
                 ,new Response.ErrorListener(){
             @Override
             public void onErrorResponse(VolleyError error){
                 Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
             }
         }
         );
         jsonForCategories.setRetryPolicy(new DefaultRetryPolicy(
                 5000,
                 DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         mRequestQueue.add(jsonForCategories);

         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                 this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
         drawer.setDrawerListener(toggle);
         toggle.syncState();

         NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);

    }
    public void parseFeaturedCategories(JSONObject response){
        try{
            JSONArray jsonArray = response.getJSONArray("categories");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                category_id[i] = Integer.parseInt(jsonObject.getString("id"));
                category_names[i] = jsonObject.getString("name");
                category_images[i] = jsonObject.getString("img_src");
                ++total;
            }
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


}
