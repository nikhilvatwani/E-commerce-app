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
import android.view.Gravity;
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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.google.android.gms.vision.text.Line;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static int id1 = 0;
    ViewPager viewpager;
    ViewPager viewpager2;
    RecyclerView recyclerView;
    FeaturedProductsAdapter featuredProductsAdapter;
    Context mContext;
    public VolleySingleton volleySingleton;
    RequestQueue mRequestQueue;
    public String names[] = new String[50];
    public int actual_price[] = new int[50] ;
    public String product_images[] = new String[50];
    public int id[] = new int[50];
    public int total;
    public String featured_categories[] = new String[50];
    public String category_images[] = new String[50];
    public String category_names[] = new String[50];
    public int category_id[] = new int[50];
    public String slider_images[] = new String[50];
    public LinearLayout linearLayout1;
    private FirebaseAnalytics mFirebaseAnalytics;
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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();

        ScrollView sc = (ScrollView)findViewById(R.id.content_main);

        linearLayout1 = new LinearLayout(this);
        linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT
        ));
        linearLayout1.setId(getNextGeneratedId());
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setWeightSum(1);
        sc.addView(linearLayout1);

        /*ProgressBar progressBar = new ProgressBar(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        progressBar.setId(getNextGeneratedId());
        progressBar.setLayoutParams(layoutParams);
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_bar));
        linearLayout1.addView(progressBar);
        progressBar.setVisibility(View.GONE);*/

        String url = "http://nikhilvatwani.esy.es/home_data.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJson(response);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    public void parseJson(JSONObject response){
        try {
            JSONArray full_home_data = response.getJSONArray("full_home_data");
            for( int i = 0 ; i < full_home_data.length() ; i++ ){
                JSONObject jsonObject = full_home_data.getJSONObject(i);
                JSONObject module = jsonObject.getJSONObject("module");
                switch(Integer.parseInt(module.getString("type"))){
                    case 1 : JSONObject data = jsonObject.getJSONObject("data");
                             setStaticImage(data.getString("param1"),data.getString("param2"));
                    break;

                    case 2 : JSONArray dataArray = jsonObject.getJSONArray("data");
                             for(int j = 0 ; j < dataArray.length() ; j++ ){
                                 slider_images[j] = dataArray.getJSONObject(j).getString("param1");
                             }
                             setSlider(slider_images,dataArray.length());
                    break;

                    case 3 : JSONArray productArray = jsonObject.getJSONArray("data");
                             for(int j = 0 ; j < productArray.length() ; j++ ){
                                 product_images[j] = productArray.getJSONObject(j).getString("images");
                                 names[j] = productArray.getJSONObject(j).getString("name");
                                 actual_price[j] = Integer.parseInt(productArray.getJSONObject(j).getString("discounted_price"));
                                 id[j] = Integer.parseInt(productArray.getJSONObject(j).getString("id"));
                             }
                             setFeaturedProducts(productArray.length());
                    break;

                    case 4 : JSONArray categoriesArray = jsonObject.getJSONArray("data");
                             for(int j = 0 ; j < categoriesArray.length() ; j++ ){
                                category_images[j] = categoriesArray.getJSONObject(j).getString("img_src");
                                category_names[j] = categoriesArray.getJSONObject(j).getString("name");
                                category_id[j] = Integer.parseInt(categoriesArray.getJSONObject(j).getString("id"));
                             }
                             setFeaturedCategories();
                    break;

                    case 5 : setQuickTabs();
                    break;
                }
            }

        }catch(JSONException e){

        }
    }

    public int getNextGeneratedId(){
        return id1++;
    }

    public void setSlider(String[] slider_images, int total){
        RelativeLayout relativeLayout1 = new RelativeLayout(this);
        relativeLayout1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
        relativeLayout1.setId(getNextGeneratedId());
        relativeLayout1.setBackgroundResource(R.drawable.shadow);
        linearLayout1.addView(relativeLayout1);

        ViewPager viewPager = new ViewPager(this);
        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,500));
        viewPager.setId(getNextGeneratedId());
        relativeLayout1.addView(viewPager);
        CustomAdapter customAdapter = new CustomAdapter(this,slider_images,total);
        viewPager.setAdapter(customAdapter);
        CirclePageIndicator circleIndicator = new CirclePageIndicator(this);
        RelativeLayout.LayoutParams params10 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params10.setMargins(1,1,1,1);
        params10.addRule(RelativeLayout.ALIGN_BOTTOM,viewPager.getId());
        circleIndicator.setLayoutParams(params10);
        circleIndicator.setPadding(10,10,10,10);
        circleIndicator.setId(getNextGeneratedId());
        circleIndicator.setFillColor(R.color.red);
        circleIndicator.setStrokeColor(R.color.red);
        circleIndicator.setPageColor(R.color.red);
        circleIndicator.setStrokeWidth(1);
        relativeLayout1.addView(circleIndicator);
        circleIndicator.setViewPager(viewPager);

        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,2));
        view.setBackgroundResource(R.color.grey);
        linearLayout1.addView(view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddRoom.class);
                startActivity(intent);
            }
        });
    }

    public void setFeaturedProducts(int total){
        RelativeLayout relativeLayout_featuredProducts = new RelativeLayout(this);
        relativeLayout_featuredProducts.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
        RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)relativeLayout_featuredProducts.getLayoutParams();
        relativeParams.setMargins(0, 50, 0, 0);  // left, top, right, bottom
        relativeLayout_featuredProducts.setLayoutParams(relativeParams);
        linearLayout1.addView(relativeLayout_featuredProducts);

        TextView featuredproducts_textview = new TextView(this);
        featuredproducts_textview.setTextSize(15);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(5,0,0,0);
        featuredproducts_textview.setLayoutParams(params1);
        featuredproducts_textview.setGravity(Gravity.LEFT);
        featuredproducts_textview.setText("Featured Products");
        //featuredproducts_textview.setPadding(3,0,0,0);
        featuredproducts_textview.setTextColor(getResources().getColor(R.color.black));
        relativeLayout_featuredProducts.addView(featuredproducts_textview);

        Button featuredproducts_button = new Button(this);
        featuredproducts_button.setId(getNextGeneratedId());
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 40);
        featuredproducts_button.setTextColor(getResources().getColor(R.color.white));
        featuredproducts_button.setText("View All");
        featuredproducts_button.setBackgroundResource(R.drawable.mybutton);
        featuredproducts_button.setPadding(0,0,0,0);
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params2.addRule(RelativeLayout.ALIGN_PARENT_END);
        params2.addRule(RelativeLayout.ALIGN_BOTTOM,featuredproducts_textview.getId());
        params2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params2.setMargins(0,5,5,0);
        featuredproducts_button.setTextSize(12);
        //featuredproducts_button.setTextAppearance(R.style.TextAppearance_Material_Caption);
        featuredproducts_button.setLayoutParams(params2);
        relativeLayout_featuredProducts.addView(featuredproducts_button);
        featuredproducts_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AllProducts.class);
                startActivity(intent);
            }
        });

        RecyclerView featuredproducts_recyclerview = new RecyclerView(this);
        featuredproducts_recyclerview.setId(getNextGeneratedId());
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params3.addRule(RelativeLayout.ALIGN_PARENT_START);
        params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params3.setMargins(0,3,0,0);
        featuredproducts_recyclerview.setLayoutParams(params3);
        linearLayout1.addView(featuredproducts_recyclerview);

        featuredProductsAdapter = new FeaturedProductsAdapter(mContext,names,actual_price,id,product_images,total);
        featuredproducts_recyclerview.setAdapter(featuredProductsAdapter);
        featuredproducts_recyclerview.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));

    }

    public void setStaticImage(String img_src, String text){
        RelativeLayout staticimage_relativelayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        staticimage_relativelayout.setBackgroundResource(R.drawable.shadow);
        params4.setMargins(0,10,0,0);
        staticimage_relativelayout.setLayoutParams(params4);

        FrameLayout staticimage_framelayout = new FrameLayout(this);
        staticimage_framelayout.setId(getNextGeneratedId());
        staticimage_framelayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,350));

        final ImageView staticimage_imageview = new ImageView(this);
        staticimage_imageview.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,350));
        staticimage_imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
        imageLoader.get(img_src, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                staticimage_imageview.setImageBitmap(response.getBitmap());
            }
            public void onErrorResponse(VolleyError error) {

            }
        });
        staticimage_framelayout.addView(staticimage_imageview);

        FrameLayout staticimage_innerframe = new FrameLayout(this);
        staticimage_innerframe.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        staticimage_innerframe.setBackgroundResource(R.drawable.gradient_bg);
        staticimage_framelayout.addView(staticimage_innerframe);

        staticimage_relativelayout.addView(staticimage_framelayout);

        TextView staticimage_textview = new TextView(this);
        staticimage_textview.setId(getNextGeneratedId());
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params5.addRule(RelativeLayout.ALIGN_LEFT,staticimage_framelayout.getId());
        params5.addRule(RelativeLayout.ALIGN_RIGHT,staticimage_framelayout.getId());
        params5.addRule(RelativeLayout.ALIGN_TOP,staticimage_framelayout.getId());
        params5.addRule(RelativeLayout.ALIGN_BOTTOM,staticimage_framelayout.getId());
        params5.setMargins(1,1,1,1);
        staticimage_textview.setLayoutParams(params5);
        staticimage_textview.setGravity(Gravity.CENTER);
        staticimage_textview.setText(text);
        staticimage_textview.setTextColor(getResources().getColor(R.color.white));
        staticimage_relativelayout.addView(staticimage_textview);

        linearLayout1.addView(staticimage_relativelayout);
    }

    public void setFeaturedCategories(){
        RelativeLayout relativeLayout_featuredCategories = new RelativeLayout(this);
        relativeLayout_featuredCategories.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
        RelativeLayout.LayoutParams params6 = (RelativeLayout.LayoutParams)relativeLayout_featuredCategories.getLayoutParams();
        params6.setMargins(0, 30, 0, 0);  // left, top, right, bottom
        relativeLayout_featuredCategories.setLayoutParams(params6);
        linearLayout1.addView(relativeLayout_featuredCategories);

        TextView featuredcategories_textview = new TextView(this);
        featuredcategories_textview.setTextSize(15);
        RelativeLayout.LayoutParams params7 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params6.setMargins(5,0,0,0);
        featuredcategories_textview.setLayoutParams(params6);
        featuredcategories_textview.setGravity(Gravity.LEFT);
        featuredcategories_textview.setText("Featured Categories");
        //featuredproducts_textview.setPadding(3,0,0,0);
        featuredcategories_textview.setTextColor(getResources().getColor(R.color.black));
        relativeLayout_featuredCategories.addView(featuredcategories_textview);

        Button featuredcategories_button = new Button(this);
        featuredcategories_button.setId(getNextGeneratedId());
        RelativeLayout.LayoutParams params8 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 40);
        featuredcategories_button.setTextColor(getResources().getColor(R.color.white));
        featuredcategories_button.setText("View All");
        featuredcategories_button.setBackgroundResource(R.drawable.mybutton);
        featuredcategories_button.setPadding(0,0,0,0);
        params8.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params8.addRule(RelativeLayout.ALIGN_PARENT_END);
        params8.addRule(RelativeLayout.ALIGN_BOTTOM,featuredcategories_textview.getId());
        params8.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params8.setMargins(0,5,5,0);
        featuredcategories_button.setTextSize(12);
        featuredcategories_button.setLayoutParams(params8);
        relativeLayout_featuredCategories.addView(featuredcategories_button);
        featuredcategories_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AllCategory.class);
                startActivity(intent);
            }
        });

        LinearLayout featuredcategories_linearlayout = new LinearLayout(this);
        featuredcategories_linearlayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.54f));
        featuredcategories_linearlayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout featuredcategories_firstinner = new LinearLayout(this);
        featuredcategories_firstinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        featuredcategories_firstinner.setOrientation(LinearLayout.VERTICAL);

        setGradientImage(featuredcategories_firstinner,category_id[0],category_names[0],category_images[0]);
        setGradientImage(featuredcategories_firstinner,category_id[1],category_names[1],category_images[1]);

        featuredcategories_linearlayout.addView(featuredcategories_firstinner);

        LinearLayout featuredcategories_secondinner = new LinearLayout(this);
        featuredcategories_secondinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        featuredcategories_secondinner.setOrientation(LinearLayout.VERTICAL);

        setGradientImage(featuredcategories_secondinner,category_id[2],category_names[2],category_images[2]);
        setGradientImage(featuredcategories_secondinner,category_id[3],category_names[3],category_images[3]);

        featuredcategories_linearlayout.addView(featuredcategories_secondinner);

        linearLayout1.addView(featuredcategories_linearlayout);
    }

    public void setGradientImage(LinearLayout linearLayout, int id, String name, String img_src){
        RelativeLayout staticimage_relativelayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        staticimage_relativelayout.setBackgroundResource(R.drawable.shadow);
        params4.setMargins(50,50,50,50);
        staticimage_relativelayout.setLayoutParams(params4);
        staticimage_relativelayout.setPadding(10,10,10,10);

        FrameLayout staticimage_framelayout = new FrameLayout(this);
        staticimage_framelayout.setId(getNextGeneratedId());
        staticimage_framelayout.setLayoutParams(new FrameLayout.LayoutParams(340,250));

        final ImageView staticimage_imageview = new ImageView(this);
        RelativeLayout.LayoutParams params6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);

        staticimage_imageview.setLayoutParams(params6);
        staticimage_imageview.setScaleType(ImageView.ScaleType.FIT_XY);
        staticimage_framelayout.addView(staticimage_imageview);
        ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
        imageLoader.get(img_src, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                staticimage_imageview.setImageBitmap(response.getBitmap());
            }
            public void onErrorResponse(VolleyError error) {

            }
        });
        staticimage_imageview.setTag(id);
        final String temp_name = new String(name);
        staticimage_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,SubCategory.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("<id>",view.getTag()+"");;
                intent.putExtra("<name>",temp_name);
                mContext.startActivity(intent);
            }
        });

        FrameLayout staticimage_innerframe = new FrameLayout(this);
        staticimage_innerframe.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        staticimage_innerframe.setBackgroundResource(R.drawable.gradient_bg);
        staticimage_framelayout.addView(staticimage_innerframe);

        staticimage_relativelayout.addView(staticimage_framelayout);

        TextView staticimage_textview = new TextView(this);
        staticimage_textview.setId(getNextGeneratedId());
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params5.addRule(RelativeLayout.ALIGN_LEFT,staticimage_framelayout.getId());
        params5.addRule(RelativeLayout.ALIGN_RIGHT,staticimage_framelayout.getId());
        //params5.addRule(RelativeLayout.ALIGN_TOP,staticimage_framelayout.getId());
        params5.addRule(RelativeLayout.ALIGN_BOTTOM,staticimage_framelayout.getId());
        params5.setMargins(0,0,0,70);
        staticimage_textview.setLayoutParams(params5);
        staticimage_textview.setGravity(Gravity.CENTER);
        staticimage_textview.setTextColor(getResources().getColor(R.color.white));
        staticimage_relativelayout.addView(staticimage_textview);
        staticimage_textview.setText(name);

        linearLayout.addView(staticimage_relativelayout);
    }

    public void setQuickTabs(){
        LinearLayout quicktabs_linearlayout = new LinearLayout(this);
        quicktabs_linearlayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        quicktabs_linearlayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout quicktabs_firstinner = new LinearLayout(this);
        quicktabs_firstinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        quicktabs_firstinner.setOrientation(LinearLayout.HORIZONTAL);

        setQuickTabsTextView(quicktabs_firstinner,"Contact Us",1);
        setQuickTabsTextView(quicktabs_firstinner,"About Us",2);

        quicktabs_linearlayout.addView(quicktabs_firstinner);

        LinearLayout quicktabs_secondinner = new LinearLayout(this);
        quicktabs_secondinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        quicktabs_secondinner.setOrientation(LinearLayout.HORIZONTAL);

        setQuickTabsTextView(quicktabs_secondinner,"Cart",1);
        setQuickTabsTextView(quicktabs_secondinner,"Orders",2);

        quicktabs_linearlayout.addView(quicktabs_secondinner);

        linearLayout1.addView(quicktabs_linearlayout);
    }

    public void setQuickTabsTextView(LinearLayout linearLayout, String string, int gravity){
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1.74f);
        if(gravity == 1){
            textView.setGravity(Gravity.LEFT);
            params.setMargins(70,0,0,0);
        }else{
            textView.setGravity(Gravity.RIGHT);
            params.setMargins(0,0,70,0);
        }
        textView.setLayoutParams(params);
        textView.setText(string);
        textView.setTextSize(18);

        linearLayout.addView(textView);
    }
    /*public void parseJsonResponse(JSONObject response){
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

    }*/
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
            Intent intent = new Intent(MainActivity.this,Wishlist.class);
            startActivity(intent);
        }else if(item.toString().equals("Profile")){
            Intent intent = new Intent(MainActivity.this,Profile.class);
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
