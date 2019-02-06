package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuUserActivity extends AppCompatActivity {
private ImageButton lihatMap, inputData, logout, tambahKK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);

        lihatMap = (ImageButton) findViewById(R.id.lihat_peta_jentik);
        inputData = (ImageButton) findViewById(R.id.input_data_pemantauan);
        logout = (ImageButton) findViewById(R.id.btn_logout_user);
        tambahKK = (ImageButton) findViewById(R.id.tambah_data_kk);

        tambahKK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuUserActivity.this, TambahDataKKActivity.class));
            }
        });

        inputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuUserActivity.this, CariKKInputActivity.class));
            }
        });

        lihatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuUserActivity.this, MapsActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                startActivity(new Intent(MenuUserActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
