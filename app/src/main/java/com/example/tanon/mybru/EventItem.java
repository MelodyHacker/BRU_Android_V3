package com.example.tanon.mybru;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by MelodyHacker on 10/16/2017.
 */


public class EventItem extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    Url url = new Url();
    ArrayList<GetSetEvent> arrayListset;
    ArrayList<GetSetEvent> arrayListseach;
    ArrayList<GetSetItem> arrayListsend;
    ListView lv;
    ProgressDialog mProgressDialog;
    String[] arraysend;
    String[] arrayseach;
    String contenteq;
    AutoCompleteTextView autoCompleteTextView = null;
    ArrayAdapter<String> adapterseach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_all);
        lv=(ListView)findViewById(R.id.json_Listview) ;
        mProgressDialog = new ProgressDialog(EventItem.this);
        mProgressDialog.setMessage("กำลังโหลดงานที่จัดขึ้น.......");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        arrayListset = new ArrayList<>();
        arrayListsend = new ArrayList<>();
        arrayListseach = new ArrayList<>();
        loadListEvents();
    }


    public void getAllItemEvent(final int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.jsonevent,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("events");
                            JSONObject productObject = jsonArray.getJSONObject(position);
                            if (jsonArray == null) {
                                Intent intent = new Intent(EventItem.this, NotInterNet.class);
                                startActivity(intent);
                            }
                            arraysend = new String[9];
                            arraysend[0] = productObject.getString("event_image1");
                            arraysend[1] = productObject.getString("event_image2");
                            arraysend[2] = productObject.getString("event_image3");
                            arraysend[3] = productObject.getString("event_name");
                            arraysend[4] = productObject.getString("event_description");
                            arraysend[5] = productObject.getString("start_event");
                            arraysend[6] = productObject.getString("end_event");
                            arraysend[7] = productObject.getString("lat_location");
                            arraysend[8] = productObject.getString("long_location");
                            Intent intent = new Intent(EventItem.this, EventAll.class);
                            intent.putExtra("ArrayEventEdit", arraysend);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplication());
        requestQueue.add(stringRequest);
    }


    private void loadListEvents() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.jsonevent,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("events");
                            arrayseach = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject productObject = jsonArray.getJSONObject(i);
                                arrayListset.add(new GetSetEvent(
                                        productObject.getString("event_image1"),
                                        productObject.getString("event_name"),
                                        productObject.getString("start_event"),
                                        productObject.getString("end_event")
                                ));
                                arrayseach[i] = productObject.getString("event_name");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final AdapterEvents adapter = new AdapterEvents(
                                getApplicationContext(), R.layout.item_events_view_all, arrayListset
                        );
                        lv.setAdapter(adapter);
                        mProgressDialog.dismiss();

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                getAllItemEvent(position);
                            }
                        });

                        ImageView btn_search = (ImageView) findViewById(R.id.btn_search);
                        btn_search.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InputMethodManager inputManager = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                EditText editText = (EditText) findViewById(R.id.edit_text_event);
                                String sc = editText.getText().toString();
                                String ck;
                                for (int c = 0; c < arrayListset.size(); c++) {
                                    ck = arrayseach[c];
                                    ck.toString();
                                    if (ck.equals(sc)) {
                                        getAllItemEvent(c);
                                        break;
                                    } else if (c == arrayListseach.size() - 1) {
                                        Toast.makeText(EventItem.this, "ไม่พบข้อมูลที่ค้นหา", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        seach();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplication());
        requestQueue.add(stringRequest);
    }

    private void seach() {
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.edit_text_event);
        adapterseach = new ArrayAdapter<String>(EventItem.this, R.layout.item_seach, arrayseach);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapterseach);
        autoCompleteTextView.setOnItemClickListener(this);
        autoCompleteTextView.setOnItemSelectedListener(this);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}





