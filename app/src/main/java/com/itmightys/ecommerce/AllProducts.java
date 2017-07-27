package com.itmightys.ecommerce;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AllProducts extends AppCompatActivity {
    RecyclerView productsView;
    public int id[] = new int[50];
    public int discounted_price[] = new int[50];
    public String names[] = new String[50];
    public String product_id[] = new String[50];
    public int total;
    public String[][] images_url = new String[20][50];
    public Context context;
    public RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String url = "http://nikhilvatwani.esy.es/products.php";
        context = this;
        Log.d("2132017","0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("2132017","1");
                parseJsonResponse(response);
                Log.d("2132017","6");
                productsView = (RecyclerView)findViewById(R.id.productsview);
                AllProductsAdapter adapter = new AllProductsAdapter(context,total,names,id, discounted_price,images_url,product_id);
                Log.d("2132017","7");
                productsView.setAdapter(adapter);
                productsView.setLayoutManager(new LinearLayoutManager(context));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();
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
    }
    public void parseJsonResponse(JSONObject response){
        Log.d("inside","parsejson1");
        if(response == null|| response.length() == 0){
            return;
        }
        try{
            Log.d("2132017","2");
            JSONArray jsonArrayProducts = response.getJSONArray("products");
            for(int i=0;i<jsonArrayProducts.length();i++){
                Log.d("2132017","3");
                JSONObject jsonObject = jsonArrayProducts.getJSONObject(i);
                names[i]=jsonObject.getString("name");
                Log.d("testing",names[i]);
                discounted_price[i] = Integer.parseInt(jsonObject.getString("discounted_price"));
                id[i]= Integer.parseInt(jsonObject.getString("id"));
                product_id[i] = jsonObject.getString("product_id");
                Log.d("testingid",id[i]+"");
            }
            JSONArray jsonArrayIds = response.getJSONArray("ids");
            for(int i=0;i<jsonArrayIds.length();i++){
                Log.d("2132017","4");
                JSONObject jsonObject1 = jsonArrayIds.getJSONObject(i);
                Log.d("2132017","411");
                JSONArray jsonArrayIdsImages = jsonObject1.getJSONArray("images");
                Log.d("2132017",jsonArrayIdsImages.length()+"");
                //int id = Integer.parseInt(jsonObject.getString("id"));
                for(int j=0;j<jsonArrayIdsImages.length();j++){
                    Log.d("2132017","413");
                    images_url[i][j] = jsonArrayIdsImages.getString(j);
                }
            }
            total = jsonArrayProducts.length();
            Log.d("2132017","5");
        }catch(JSONException e){

        }
    }

}
