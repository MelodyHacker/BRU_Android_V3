package com.example.tanon.mybru;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import me.toptas.fancyshowcase.FancyShowCaseView;

public class Into extends AppCompatActivity implements OnMapReadyCallback {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapmain);
        mapFragment.getMapAsync(this);
        final ImageView imgskip = (ImageView) findViewById(R.id.into);
        final ImageView imgmap_show = (ImageView) findViewById(R.id.hide_map);
        imgmap_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim(imgmap_show,  getString(R.string.into_care_full));
            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                anim(imgskip, getString(R.string.into_back));
            }
        }, 1000);
        imgskip.setImageResource(R.drawable.arrow);
        imgskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Into.this, MarkerOff.class);
                startActivity(intent);
            }
        });
        final ImageView imgreport = (ImageView) findViewById(R.id.report);
        imgreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim(imgreport, getString(R.string.into_report));
            }
        });
        final ImageView imgevent = (ImageView) findViewById(R.id.event);
        imgevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim(imgevent, getString(R.string.into_event));
            }
        });
        final ImageView imgmap = (ImageView) findViewById(R.id.mapimg);
        imgmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim(imgmap, getString(R.string.into_place));
            }
        });
        final ImageView imgtoilet = (ImageView) findViewById(R.id.toilet);
        imgtoilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim(imgtoilet, getString(R.string.into_toilet));
            }
        });
        final ImageView imgphone = (ImageView) findViewById(R.id.callphone);
        imgphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim(imgphone, getString(R.string.into_phone));
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Double lat = 14.990395303361007, lng = 103.10022532939911;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 18));
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
