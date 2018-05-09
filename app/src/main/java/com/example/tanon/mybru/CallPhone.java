package com.example.tanon.mybru;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by MelodyHacker on 10/18/2017.
 */

public class CallPhone extends AppCompatActivity {
    ProgressDialog mProgressDialog;
    String call;
    int position;
    String[] ar;
    Url url = new Url();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("Position");
        ar = bundle.getStringArray("Contact");
        call = ar[position].toString();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", call, null));
        startActivity(intent);
    }
}


