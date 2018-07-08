package com.example.tanon.mybru;

/**
 * Created by MelodyHacker on 11/1/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MelodyHacker on 10/30/2017.
 */

public class MarkerOff extends AppCompatActivity {

    ProgressDialog mProgressDialog;
    RequestQueue requestQueue;
    Url url = new Url();
    String[] ar_marker_off, ar_marker_event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        mProgressDialog = new ProgressDialog(MarkerOff.this);
        mProgressDialog.setMessage(getString(R.string.welcome));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();

        if (isNetworkConnected() == true) {
            sendToken();
            loadMarkerOff();
            loadMarkerEvent();
        } else {
            Intent intent = new Intent(MarkerOff.this, NotInterNet.class);
            startActivity(intent);
        }

    }

    private void loadMarkerEvent() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.jsonevent,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("events");
                            int x = 0;
                            ar_marker_event = new String[jsonArray.length() * 3];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject MarkObject = jsonArray.getJSONObject(i);
                                ar_marker_event[x] = MarkObject.getString("event_name");
                                ar_marker_event[x + 1] = MarkObject.getString("lat_location");
                                ar_marker_event[x + 2] = MarkObject.getString("long_location");
                                x = x + 3;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (ar_marker_event == null|ar_marker_off==null) {
                            Intent intent = new Intent(MarkerOff.this, NotInterNet.class);
                            startActivity(intent);
                        } else {
                            mProgressDialog.dismiss();
                            Intent intent = new Intent(MarkerOff.this, MainActivity.class);
                            intent.putExtra("arrayMarkerPoriline", ar_marker_off);
                            intent.putExtra("arrayMarkerEvent", ar_marker_event);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), getString(R.string.wrong) + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplication());
        requestQueue.add(stringRequest);
    }

    private void loadMarkerOff() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.jsonporiline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("poriline");
                            int x = 0;
                            ar_marker_off = new String[jsonArray.length() * 3];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject MarkObject = jsonArray.getJSONObject(i);
                                ar_marker_off[x] = MarkObject.getString("poriline_name");
                                ar_marker_off[x + 1] = MarkObject.getString("poriline_lat");
                                ar_marker_off[x + 2] = MarkObject.getString("poriline_long");
                                x = x + 3;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (ar_marker_off == null) {
                            Intent intent = new Intent(MarkerOff.this, NotInterNet.class);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), getString(R.string.wrong) + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplication());
        requestQueue.add(stringRequest);

    }

    private void sendToken() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AbstractMethodError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("token", FirebaseInstanceId.getInstance().getToken());
                return parameters;
            }
        };
        requestQueue.add(stringRequest);

    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
