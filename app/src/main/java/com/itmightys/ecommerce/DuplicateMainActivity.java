package com.itmightys.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DuplicateMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ViewPager viewpager;
    ViewPager viewpager2;
    RecyclerView recyclerView;
    FeaturedProductsAdapter featuredProductsAdapter;
    Context mContext;
    public VolleySingleton volleySingleton;
    RequestQueue mRequestQueue;
    public String names[] = new String[50];
    public int actual_price[] = new int[50] ;
    public int id[] = new int[50];
    public int total;
    public String featured_categories[] = new String[50];
    public String category_images[] = new String[50];
    public String category_names[] = new String[50];
    public int category_id[] = new int[50];
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        CustomAdapter customAdapter = new CustomAdapter(this,category_images,4);
        viewpager.setAdapter(customAdapter);
        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewpager);
        String url = "http://nikhilvatwani.esy.es/products.php";
        volleySingleton = VolleySingleton.getInstance();
        mRequestQueue = volleySingleton.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("testing","success");
                        parseJsonResponse(response);
                        recyclerView = (RecyclerView)findViewById(R.id.drawerList);
                        for(int i=0;i<2;i++){
                            Log.d("insideloop",actual_price[i]+"");
                        }
                        featuredProductsAdapter = new FeaturedProductsAdapter(mContext,names,actual_price,id,names,total);
                        recyclerView.setAdapter(featuredProductsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("testing","failure");
                Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);

        JsonObjectRequest jsonForCategories = new JsonObjectRequest(Request.Method.GET, "http://nikhilvatwani.esy.es/getFeaturedCategories.php?from=2", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseFeaturedCategories(response);
                for (int i = 0; i <= 3; i++) {
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
                        case 3:
                            setImageLoader(3);
                            TextView textView4 = (TextView) findViewById(R.id.textview_category4);
                            textView4.setText(category_names[i]);
                            break;
                    }
                }
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
        ImageView image = (ImageView)findViewById(R.id.image_view);
        image.setImageResource(R.drawable.mobiles);
        Button viewAllProducts = (Button)findViewById(R.id.button1);
        viewAllProducts.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DuplicateMainActivity.this,AllProducts.class);
                startActivity(intent);
            }
        });
        Button b = (Button)findViewById(R.id.button6);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DuplicateMainActivity.this,AllCategory.class);
                startActivity(intent);
            }
        });
        TextView t2= (TextView)findViewById(R.id.textview2);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DuplicateMainActivity.this,ContactUs.class);
                startActivity(intent);
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        /*String url = "http://nikhilvatwani.esy.es/products.php";
        RequestQueue queue = VolleySingleton.getsInstance().getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response","success");
                Toast.makeText(mContext,response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response","failure");
                Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);*/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }
    public void parseJsonResponse(JSONObject response){
        Log.d("inside","parsejson");
        if(response == null|| response.length() == 0){
            return;
        }
        try{
            JSONArray jsonArray = response.getJSONArray("products");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                names[i]=jsonObject.getString("name");
                Log.d("testing",names[i]);
                actual_price[i] = Integer.parseInt(jsonObject.getString("actual_price"));
                id[i]= Integer.parseInt(jsonObject.getString("id"));
                Log.d("testingid",id[i]+"");
            }
            total = jsonArray.length();
        }catch(JSONException e){

        }

    }
    public void setImageLoader(int i){
        final String str = new String(""+i+"");
        final String id = new String(""+this.id[i]+"");
        ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
        imageLoader.get(category_images[i], new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    Log.d("iminside_category","onresponse");
                    switch(str){
                        case "0":ImageView imageView1 = (ImageView)findViewById(R.id.image1);
                            imageView1.setImageBitmap(response.getBitmap());
                            imageView1.setTag(id);
                            Log.d("goingin","subcategoryfrommain");
                            imageView1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mContext,SubCategory.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("<id>",view.getTag()+"");;
                                    mContext.startActivity(intent);
                                }
                            });

                            break;
                        case "1":ImageView imageView2= (ImageView)findViewById(R.id.image2);
                            imageView2.setImageBitmap(response.getBitmap());
                            imageView2.setTag(id);
                            Log.d("goingin","subcategoryfrommain");
                            imageView2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mContext,SubCategory.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("<id>",view.getTag()+"");;
                                    mContext.startActivity(intent);
                                }
                            });
                            break;
                        case "2":ImageView imageView3 = (ImageView)findViewById(R.id.image3);
                            imageView3.setImageBitmap(response.getBitmap());
                            imageView3.setTag(id);
                            Log.d("goingin","subcategoryfrommain");
                            imageView3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mContext,SubCategory.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("<id>",view.getTag()+"");;
                                    mContext.startActivity(intent);
                                }
                            });
                            break;
                        case "3":ImageView imageView4 = (ImageView)findViewById(R.id.image4);
                            imageView4.setImageBitmap(response.getBitmap());
                            imageView4.setTag(id);
                            Log.d("goingin","subcategoryfrommain");
                            imageView4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mContext,SubCategory.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("<id>",view.getTag()+"");;
                                    mContext.startActivity(intent);
                                }
                            });
                            break;
                    }

                }
            }
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
    public void parseFeaturedCategories(JSONObject response){
        try{
            JSONArray jsonArray = response.getJSONArray("categories");
            for(int i=0;i<=3;i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                category_id[i] = Integer.parseInt(jsonObject.getString("id"));
                category_names[i] = jsonObject.getString("name");
                category_images[i] = jsonObject.getString("img_src");
            }
        }catch(JSONException e){

        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.toString().equals("Wishlist")){
            Intent intent = new Intent(DuplicateMainActivity.this,Wishlist.class);
            startActivity(intent);
        }else if(item.toString().equals("Profile")){
            Intent intent = new Intent(DuplicateMainActivity.this,Profile.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

}
