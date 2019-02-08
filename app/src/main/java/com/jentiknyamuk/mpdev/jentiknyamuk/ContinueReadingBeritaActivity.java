package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ContinueReadingBeritaActivity extends AppCompatActivity {
private String judul,isi,gambar;
private TextView detJdl,detIsi;
private ImageView gb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_reading_berita);

        Intent o = getIntent();
        judul   = o.getStringExtra("judul");
        isi     = o.getStringExtra("isi");
        gambar  = o.getStringExtra("gambar");

        Log.d("jdl",judul);
        detIsi = (TextView) findViewById(R.id.detail_isiBerita);
        detJdl = (TextView) findViewById(R.id.detail_judulberita);
        gb = (ImageView) findViewById(R.id.detail_gambarberita);

        detJdl.setText(judul);
        detIsi.setText(isi);
        Glide.with(ContinueReadingBeritaActivity.this)
                .load(gambar)
                .placeholder(R.drawable.placeholder_berita_jemantik)
                .into(gb);
    }
}
