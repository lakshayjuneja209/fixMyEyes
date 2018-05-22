package com.example.sherlock.fixmyeyes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class home extends Fragment {

    CardView crop;
    CardView add_text;
    CardView frame;
    CardView mirror;
    CardView resize;
    CardView collage;
    CardView misc;
    CardView effects;
    CardView cec;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        crop = view.findViewById(R.id.cv1);
        add_text = view.findViewById(R.id.cv2);
        frame = view.findViewById(R.id.cv3);
        mirror = view.findViewById(R.id.cv4);
        resize = view.findViewById(R.id.cv5);
        collage = view.findViewById(R.id.cv6);
        misc = view.findViewById(R.id.cv7);
        effects = view.findViewById(R.id.cv8);
        cec = view.findViewById(R.id.cv9);
        cec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), fmi.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
