package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;

/**
 * Activity post berita baru, hanya digunakan oleh admin
 */

public class AddBeritaActivity extends AppCompatActivity implements View.OnClickListener{
private EditText judulberita, isiberita;
private Button addimage, bagikan, cancelfoto;
private ImageView fotoberita;
Bitmap bitmap;
int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_berita);


        judulberita = (EditText) findViewById(R.id.judulBerita);
        isiberita = (EditText) findViewById(R.id.isiBerita);
        fotoberita = (ImageView) findViewById(R.id.view_image);
        cancelfoto = (Button) findViewById(R.id.btn_delImage);
        addimage = (Button) findViewById(R.id.btn_addimage);
        bagikan = (Button) findViewById(R.id.btn_submitBerita);

        //hide button close pada saat awal create
        cancelfoto.setVisibility(View.INVISIBLE);

        //menjalankan fungsi showFileChooser
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        cancelfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotoberita.setImageBitmap(null);
                addimage.setVisibility(View.VISIBLE);
                cancelfoto.setVisibility(View.INVISIBLE);
            }
        });
        bagikan.setOnClickListener(this);
    }

    // fungsi untuk menampilkan layar untuk memilih gambar
    private void showFileChooser()
    {
        Intent localIntent = new Intent();
        localIntent.setType("image/*");
        localIntent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(localIntent, "Select Picture"), this.PICK_IMAGE_REQUEST);
    }

    // fungsi untuk menampung hasil dari memilih gambar
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Uri localUri = null;
        if ((requestCode == this.PICK_IMAGE_REQUEST) && (resultCode == -1) && (data != null) && (data.getData() != null)) {
            localUri = data.getData();
        }
        try
        {
            // menampung gambar yg telah dipilih ke dalam variable bitmap
            this.bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), localUri);
            // memasukkan gambar yg dipilih ke imageview
            fotoberita.setImageBitmap(this.bitmap);
            fotoberita.setScaleType(ImageView.ScaleType.CENTER_CROP);
            addimage.setVisibility(View.INVISIBLE);
            cancelfoto.setVisibility(View.VISIBLE);

            return;
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
    }

    // fungsi untuk konversi bitmap menjadi string (agar bisa diupload ke server)
    public String getStringImage(Bitmap paramBitmap)
    {
        if (paramBitmap == null){
            return "";
        }else {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
            // mengembalikan gambar dalam bentuk string yg telah di encode enggunakan Base64 encode
            return Base64.encodeToString(localByteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        }
    }

    // fungsi untuk post berita
    private void postBerita(){
        final String judul   = judulberita.getText().toString().trim();
        final String isi  = isiberita.getText().toString().trim();

        class postBerita extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddBeritaActivity.this
                        ,"Posting Berita...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                // response messagge dr server ditampilkan ke layar
                toast(s);
                Intent intentProfile = new Intent(AddBeritaActivity.this, MenuAdminActivity.class);
                startActivity(intentProfile);
                AddBeritaActivity.this.finish();
            }

            @Override
            protected String doInBackground(Void... params) {
                String generatedID = randomAlphaNumeric(7);

                HashMap<String,String> hashMap = new HashMap<>();
                // menampung parameter yang akan dikirimkan kedalam hashmap
                hashMap.put("id",generatedID);
                hashMap.put("judul",judul);
                hashMap.put("isi",isi);
                hashMap.put("image",getStringImage(bitmap));


                RequestHandler rh = new RequestHandler();
                //mengirimkan request ke url dengan post parameter
                String s = rh.sendPostRequst("http://mpdev.000webhostapp.com/postBerita.php",hashMap);
                //s adalah string messagge yg didapatkan dari response pada server
                //kemudian dikembalikan ke onPostExecute
                return s;
            }
        }

        postBerita po = new postBerita();
        po.execute();
    }

    @Override
    public void onClick(View view) {
        if(view == bagikan){
            if(judulberita.length()==0){
                toast("Tulis Judul Berita dahulu");
            }else if(isiberita.length()==0){
                toast("Tulis Isi Berita dahulu");
            }else
            {
                postBerita();
            }
        }
    }
    private void toast(String message){
        Toast.makeText(AddBeritaActivity.this,message,Toast.LENGTH_LONG).show();
    }

    // fungsi untuk membuat id dari string secara random
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
