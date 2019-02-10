package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class AccountFragment extends Fragment {
    public View mMainView;
    private LinearLayout profil, logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_account, container, false);
        profil = (LinearLayout) mMainView.findViewById(R.id.menu_profil);
        logout = (LinearLayout) mMainView.findViewById(R.id.menu_logout);
        final SharedPrefManager sharedPrefManager =  SharedPrefManager.getInstance(getContext());
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = null;
                i = new Intent(getActivity(), TambahKaderActivity.class);
                i.putExtra("nama", sharedPrefManager.getNama());
                i.putExtra("username",sharedPrefManager.getUsername());
                i.putExtra("password",sharedPrefManager.getPassword());
                i.putExtra("alamat",sharedPrefManager.getAlamat());
                i.putExtra("no_telp",sharedPrefManager.getNoTelp());
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getContext()).logout();
                startActivity(new Intent(getActivity(), BerandaActivity.class));
                getActivity().finish();
            }
        });
        return mMainView;
    }
}
