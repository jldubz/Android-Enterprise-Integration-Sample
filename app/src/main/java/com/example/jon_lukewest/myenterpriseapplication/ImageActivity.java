package com.example.jon_lukewest.myenterpriseapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.RestrictionsManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageActivity extends AppCompatActivity {

    String imageUrl = "http://wisdompets.com/wp-content/uploads/2015/10/cat-scratching-post-97572046-262x300.jpg";

    ImageView fullScreenImage;

    RestrictionsManager myRestrictionsManager;
    BroadcastReceiver restrictionsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        fullScreenImage = findViewById(R.id.fullScreenImage);

        myRestrictionsManager = (RestrictionsManager) getSystemService(Context.RESTRICTIONS_SERVICE);

        IntentFilter restrictionsFilter = new IntentFilter(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED);

        restrictionsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle appRestrictions = myRestrictionsManager.getApplicationRestrictions();

                if (appRestrictions.size() > 0) {

                    imageUrl = appRestrictions.getString("imageUrl");
                    if (URLUtil.isValidUrl(imageUrl)) {

                        fullScreenImage.post(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(getApplicationContext())
                                        .load(imageUrl)
                                        .centerCrop()
                                        .into(fullScreenImage);
                            }
                        });
                    }
                }

            }
        };

        registerReceiver(restrictionsReceiver, restrictionsFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle appRestrictions = myRestrictionsManager.getApplicationRestrictions();

        if (appRestrictions.size() > 0) {

            imageUrl = appRestrictions.getString("imageUrl");
            if (URLUtil.isValidUrl(imageUrl)) {

                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .centerCrop()
                        .into(fullScreenImage);
            }
        }

        startLockTask();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(restrictionsReceiver);

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }
}
