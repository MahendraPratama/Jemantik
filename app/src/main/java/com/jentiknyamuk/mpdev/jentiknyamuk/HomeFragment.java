package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class HomeFragment extends Fragment {
    private View mMainView;
    private ImageButton addberita;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainView =  inflater.inflate(R.layout.fragment_home, container, false);
        //setContentView(R.layout.activity_data_kader);

        addberita = (ImageButton) mMainView.findViewById(R.id.btn_addberita);
        addberita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddBeritaActivity.class));
            }
        });
        return mMainView;
    }
}
