package com.example.sherlock.fixmyeyes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment;
    CustomTabHelper mCustomTabHelper;
    Uri uri;
    String FME = "https://www.facebook.com";
    private static final String GOOGLE_PLAY_LINK = "https://play.google.com/store/apps/details?id=com.plumbum.aapu.household&hl=en";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCustomTabHelper = new CustomTabHelper();
        uri = Uri.parse(FME);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment = new home();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.logout) {

        }
        else if(id == R.id.share){
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.share_msg) + " " + GOOGLE_PLAY_LINK);
            startActivity(Intent.createChooser(shareIntent, "Share with others"));

        }
        else if(id == R.id.visit){
            if (mCustomTabHelper.getPackageName(this).size() != 0) {
                CustomTabsIntent customTabsIntent =
                        new CustomTabsIntent.Builder()
                                .build();
                customTabsIntent.intent.setPackage(mCustomTabHelper.getPackageName(this).get(0));
                customTabsIntent.launchUrl(this, uri);
            } else {
                Log.e("FFF","through browser");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FME)));
            }

        }

        else {
            displaySelectedScreen(id);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        fragment = null;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.home:
                //startActivity(new Intent(userDashBoard.this,getUserLocation.class));
                fragment = new home();
                break;
            case R.id.recent:
                startActivity(new Intent(Drawer.this,recent.class));
                break;
            case R.id.profile:
                Log.e("I MA PRESSING","PROFILE BUTTON");
                fragment = new profile();
                break;
        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }



}
