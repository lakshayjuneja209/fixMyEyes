package com.example.sherlock.fixmyeyes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;


public class splash_screen extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.getStackTrace();
                } finally {
                    SharedPreferences mSharedPreferences = getSharedPreferences("mySharedPreferences", MODE_PRIVATE);
                    if (!mSharedPreferences.contains("LOGGED_IN")) {
                            startActivity(new Intent(splash_screen.this, signIn.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(splash_screen.this, Drawer.class));
                    }
                }
            }
        };
        mThread.start();
    }
}

