package com.jentiknyamuk.mpdev.jentiknyamuk;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;


public class DataKKFragment extends Fragment {
    private View mMainView;
    String JSON_STRING;
    private ImageButton add,view;
    private TextView textView;
    final ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    GridView gridView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_data_kk, container, false);

        add = (ImageButton) mMainView.findViewById(R.id.btn_addkk);
        view = (ImageButton) mMainView.findViewById(R.id.btn_datakk);
        textView = (TextView) mMainView.findViewById(R.id.textwelcome);

        if(SharedPrefManager.getInstance(getContext()).getKodeLevel() == 2){
            view.setImageResource(R.drawable.ic_listdata_hslpantau);
            String nama = SharedPrefManager.getInstance(getContext()).getNama();
            textView.setText("Halo "+nama);
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(getActivity(), TambahDataKKActivity.class);
                startActivity(addIntent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(getActivity(), CariKKInputActivity.class);
                startActivity(viewIntent);
            }
        });
        return mMainView;
    }
}
