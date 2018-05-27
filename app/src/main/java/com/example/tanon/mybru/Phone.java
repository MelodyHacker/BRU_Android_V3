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
 * Created by MelodyHacker on 11/1/2017.
 */

public class Phone extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    ProgressDialog mProgressDialog;
    ListView lv;
    Url url = new Url();
    String[] arrayseach, ar;
    AutoCompleteTextView autoCompleteTextView = null;
    ArrayAdapter<String> adapterseach;
    String contact,ck;
    ArrayList<GetSetPhone> arrayList;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Phone.this, MarkerOff.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_all);
        lv = (ListView) findViewById(R.id.json_Listview);
        arrayList = new ArrayList<>();
        mProgressDialog = new ProgressDialog(Phone.this);
        mProgressDialog.setMessage("กำลังโหลดข้อมูลติดต่อ........");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        loadNumberPhone();

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

                for (int c = 0; c <= arrayseach.length-1; c++) {
                    ck=arrayseach[c];
                    if (ck.equals(sc)) {
                        getCall(c);
                        break;
                    }
                }
            }
        });
    }


    private void getCall(final int c) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.jsonphone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("phones");
                            JSONObject PhoneObject = jsonArray.getJSONObject(c);
                            String contact = PhoneObject.getString("phone_number");
                            Intent intent = new Intent(Phone.this, CallPhone.class);
                            intent.putExtra("Position", c);
                            intent.putExtra("Contact", contact);
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


    private void loadNumberPhone() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.jsonphone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("phones");
                            int x = 0;
                            ar = new String[jsonArray.length() * 3];
                            arrayseach = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject PhoneObject = jsonArray.getJSONObject(i);
                                contact = PhoneObject.getString("phone_number");
                                arrayList.add(new GetSetPhone(
                                        arrayseach[i] = PhoneObject.getString("phone_name"),
                                        PhoneObject.getString("phone_number"),
                                        PhoneObject.getString("image_phone")
                                ));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final AdapterPhone adapter = new AdapterPhone(
                                getApplicationContext(), R.layout.item_events_view_all, arrayList
                        );
                        lv.setAdapter(adapter);
                        seach();
                        mProgressDialog.dismiss();
                        Toast.makeText(Phone.this, "โหลดข้อมูลติดต่อเรียบร้อย",
                                Toast.LENGTH_LONG).show();
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(Phone.this, CallPhone.class);
                                intent.putExtra("Position", position);
                                intent.putExtra("Contact", contact);
                                startActivity(intent);

                            }
                        });


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
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.edit_text_event);
        adapterseach = new ArrayAdapter<String>(Phone.this, R.layout.item_seach, arrayseach);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapterseach);
        autoCompleteTextView.setOnItemClickListener(this);
        autoCompleteTextView.setOnItemSelectedListener(this);
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
