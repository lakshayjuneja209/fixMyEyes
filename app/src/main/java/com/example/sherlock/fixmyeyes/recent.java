package com.example.sherlock.fixmyeyes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;


public class recent extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recent);
        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new ContactPagerAdapter(this));

        // Bind the tabs to the ViewPager
        //PagerSlidingTabStrip tabs = findViewById(R.id.tabs);
        //tabs.setViewPager(pager);

    }


    public static class ContactPagerAdapter extends PagerAdapter implements PagerSlidingTabStrip.CustomTabProvider {

        private final String[] TITLES = {
                "FIRST",
                "SECOND",
                "THIRD",
                "FOURTH",
                "LAST"
        };

        private final Context mContext;

        public ContactPagerAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences("mySharedPreferences",MODE_PRIVATE);
            ImageView imageview = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.fragment_quickcontact, container, false);
            Picasso.with(mContext).load(mSharedPreferences.getString(Integer.toString(position),mSharedPreferences.getString("PROFILE_PIC_URL","")))
                    .placeholder(R.drawable.userdp)
                    .error(R.drawable.userdp).into(imageview);
            container.addView(imageview);
            return imageview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View v, Object o) {
            return v == o;
        }

        @Override
        public View getCustomTabView(ViewGroup parent, int position) {
            Log.e("hekl","I MA CALLED");
            View tab = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, parent, false);
            ((TextView)tab.findViewById(R.id.posts_tab_title)).setText(TITLES[position]);
            return tab;
        }

        @Override
        public void tabSelected(View tab) {
            //Callback with the tab on his selected state. It is up to the developer to change anything on it.
        }

        @Override
        public void tabUnselected(View tab) {
            //Callback with the tab on his unselected state. It is up to the developer to change anything on it.
        }
    }
}
