package com.example.tanon.mybru;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import me.toptas.fancyshowcase.FancyShowCaseView;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    ProgressDialog mProgressDialog;
    JSONArray array;
    ViewFlipper viewFlipper;
    Url url = new Url();
    String img1, img2, img3;
    int zoom = 15;
    Double lat = 14.990395303361007, lng = 103.10022532939911;
    LatLng position = new LatLng(lat, lng);
    GoogleMap mMap;
    String[] ar_marker_off, ar_marker_event;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, MarkerOff.class);
        startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage(getString(R.string.welcome));
        mProgressDialog.setIndeterminate(false);
        final ImageView into = (ImageView) findViewById(R.id.into);
        into.setImageResource(R.drawable.question);
        into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Into.class);
               startActivity(intent);
            }
        });

        ImageView imgreport = (ImageView) findViewById(R.id.report);
        imgreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Report.class);
                startActivity(intent);
            }
        });
        ImageView imgphone = (ImageView) findViewById(R.id.callphone);
        imgphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Phone.class);
                startActivity(intent);
            }
        });
        ImageView imgmap = (ImageView) findViewById(R.id.mapimg);
        imgmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MarkerPlaces.class);
                startActivity(intent);
            }
        });
        ImageView imgevent = (ImageView) findViewById(R.id.event);
        imgevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventItem.class);
                startActivity(intent);
            }
        });
        ImageView imgtoilet = (ImageView) findViewById(R.id.toilet);
        imgtoilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MarkerToilets.class);
                startActivity(intent);
            }
        });
        viewFlipper = (ViewFlipper) findViewById(R.id.all_img_item);
        viewFlipper.startFlipping();
        viewFlipper.setFlipInterval(5000);
        loadImageNew();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapmain);
        mapFragment.getMapAsync(this);

    }

    private void loadImageNew() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.jsonnews,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("news");
                            array = jsonArray;
                            Random rand = new Random();
                            int n = rand.nextInt(jsonArray.length());
                            JSONObject productObject = jsonArray.getJSONObject(n);
                            img1 = productObject.getString("news_image1");
                            img2 = productObject.getString("news_image2");
                            img3 = productObject.getString("news_image3");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (img1 == null | img2 == null | img3 == null) {
                            Intent intent = new Intent(MainActivity.this, NotInterNet.class);
                            startActivity(intent);
                        }
                        ImageView imageView1 = (ImageView) findViewById(R.id.new11);
                        Picasso.with(getApplicationContext()).load(url.imgnew + img1).into(imageView1);
                        ImageView imageView2 = (ImageView) findViewById(R.id.new12);
                        Picasso.with(getApplicationContext()).load(url.imgnew + img2).into(imageView2);
                        ImageView imageView3 = (ImageView) findViewById(R.id.new13);
                        Picasso.with(getApplicationContext()).load(url.imgnew + img3).into(imageView3);
                        mProgressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), getString(R.string.wrong)+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle bundle = getIntent().getExtras();
        ar_marker_off = bundle.getStringArray("arrayMarkerPoriline");
        ar_marker_event = bundle.getStringArray("arrayMarkerEvent");
        final ImageView imgmap_show = (ImageView) findViewById(R.id.hide_map);
        if (ar_marker_off == null | ar_marker_event == null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    anim(imgmap_show, getString(R.string.closs));
                }
            }, 1000);
/////////////////////////////////////////////////////////////////////
            for (int x = 0; x < ar_marker_off.length; x++) {
                String name = ar_marker_off[x];
                Double lat = Double.parseDouble(ar_marker_off[x + 1]);
                Double lng = Double.parseDouble(ar_marker_off[x + 2]);
                x = x + 2;
                LatLng position = new LatLng(lat, lng);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.cancel);
                MarkerOptions markerOptions = new MarkerOptions().position(position)
                        .title(name)
                        .icon(icon);

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
//                mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).title(name));
            }
            for (int x = 0; x < ar_marker_event.length; x++) {
                String name = ar_marker_event[x];
                Double lat = Double.parseDouble(ar_marker_event[x + 1]);
                Double lng = Double.parseDouble(ar_marker_event[x + 2]);
                x = x + 2;
                LatLng position = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.location);
                MarkerOptions markerOptions = new MarkerOptions().position(position)
                        .title(name)
                        .icon(icon);

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
//                mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(name));
            }
/////////////////////////////////////////////////////////////////////
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    public void anim(final View v, String s) {
        Animation exitAnimation = AnimationUtils.loadAnimation(this, R.anim.side_top);
        FancyShowCaseView fancy;
        fancy = new FancyShowCaseView.Builder(this)
                .focusOn(v)
                .title(s).enterAnimation(exitAnimation)
                .enterAnimation(exitAnimation)
                .build();
        fancy.show();
//        .enterAnimation(exitAnimation)
    }
}
