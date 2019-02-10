package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

public class ContinueReadingBeritaActivity extends AppCompatActivity {
private String judul,isi,gambar,tgl;
private TextView detJdl, tglberita;
private JustifiedTextView detIsi;
private ImageView gb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_reading_berita);

        Intent o = getIntent();
        judul   = o.getStringExtra("judul");
        isi     = o.getStringExtra("isi");
        gambar  = o.getStringExtra("gambar");
        tgl  = o.getStringExtra("tgl");

        tglberita = (TextView) findViewById(R.id.tglberita);
        detIsi = (JustifiedTextView) findViewById(R.id.detail_isiBerita);
        detJdl = (TextView) findViewById(R.id.detail_judulberita);
        gb = (ImageView) findViewById(R.id.detail_gambarberita);

        tglberita.setText("Posted by: Admin, "+tgl);
        detJdl.setText(judul);
        detIsi.setText(isi);
        Glide.with(ContinueReadingBeritaActivity.this)
                .load(gambar)
                .placeholder(R.drawable.placeholder_berita_jemantik)
                .into(gb);
    }
}
