package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class HomeFragment extends Fragment {
    private View mMainView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainView =  inflater.inflate(R.layout.fragment_home, container, false);
        //setContentView(R.layout.activity_data_kader);

        return mMainView;
    }
}
