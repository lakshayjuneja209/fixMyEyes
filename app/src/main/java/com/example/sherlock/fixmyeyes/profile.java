package com.example.sherlock.fixmyeyes;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class profile extends Fragment {
    CircularImageView dp;
    TextView name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("mySharedPreferences",MODE_PRIVATE);
        dp = view.findViewById(R.id.user_profile_photo_hello);
        name = view.findViewById(R.id.user_profile_name);
        name.setText(mSharedPreferences.getString("USER_NAME",""));
        Picasso.with(getActivity()).load(mSharedPreferences.getString("PROFILE_PIC_URL",""))
                .placeholder(R.drawable.userdp)
                .error(R.drawable.userdp).into(dp);
        return view;
    }
}
