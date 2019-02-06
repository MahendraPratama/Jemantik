package com.jentiknyamuk.mpdev.jentiknyamuk;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DigitalClock;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class KaderFragment extends Fragment {
private View mMainView;
private ImageButton add,view;
private DigitalClock dg;

    public KaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_kader, container, false);

        dg = (DigitalClock) mMainView.findViewById(R.id.clockwidget_kader);
        add = (ImageButton) mMainView.findViewById(R.id.btn_tambahkader);
        view = (ImageButton) mMainView.findViewById(R.id.btn_datakader);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(getActivity(), TambahKaderActivity.class);
                startActivity(addIntent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(getActivity(), DataKaderActivity.class);
                startActivity(viewIntent);
            }
        });
        return mMainView;
    }

}
