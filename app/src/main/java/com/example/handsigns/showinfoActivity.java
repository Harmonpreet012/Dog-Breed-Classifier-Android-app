package com.example.handsigns;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class showinfoActivity extends AppCompatActivity{

    TextView name, height, origin, life_span, weight, temperament, breed_group, bred_for;
    String url;
    JSONObject jsonObject = new JSONObject();
    ProgressDialog pg;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showinfo);
        Intent intent = getIntent();
        final String breedName = intent.getStringExtra("dogBreed");
        url ="https://api.thedogapi.com/v1/breeds?attach_breed="+breedName+"&limit=1" ;
        pg=new ProgressDialog(this);
        pg.setMessage("Loading");

        pg.show();

        name=findViewById(R.id.textViewName);
        height=findViewById(R.id.textViewHeight);
        weight=findViewById(R.id.textViewWeight);
        bred_for=findViewById(R.id.textViewBred_for);
        life_span=findViewById(R.id.textViewLife);
        temperament =findViewById(R.id.textViewtemp);
        runThread();
    }

    private void runThread()
    {
        new Thread()
        {
            public void run()
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                                Request.Method.GET,
                                url,
                                null,
                                new Response.Listener<JSONArray>() {

                                    @Override
                                    public void onResponse(JSONArray response) {
                                        try {
                                            jsonObject=response.getJSONObject(0);


                                            Log.e("CL: ", jsonObject.toString());
                                            pg.dismiss();
                                            try {
                                                Thread.sleep(2000);
                                            }
                                            catch(InterruptedException e)
                                            {
                                                e.printStackTrace();
                                            }
                                            life_span.setText(jsonObject.get("life_span").toString());
                                            weight.setText(jsonObject.get("weight").toString());
                                            height.setText(jsonObject.get("height").toString());
                                            temperament.setText(jsonObject.get("temperament").toString());
                                            origin.setText(jsonObject.get("origin").toString());
                                        }
                                        catch (JSONException  e) {
                                            e.printStackTrace();
                                        }
                                    }

                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Custom Error :", error.toString());
                                    }
                                }
                        );
                        requestQueue.add(jsonArrayRequest);
                    }
                });
            }
        }.start();
    }

}
