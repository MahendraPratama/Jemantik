package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddBeritaActivity extends AppCompatActivity {
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
        cancelfoto.setVisibility(View.INVISIBLE);
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
            }
        });
    }
    private void showFileChooser()
    {
        Intent localIntent = new Intent();
        localIntent.setType("image/*");
        localIntent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(localIntent, "Select Picture"), this.PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Uri localUri = null;
        if ((requestCode == this.PICK_IMAGE_REQUEST) && (resultCode == -1) && (data != null) && (data.getData() != null)) {
            localUri = data.getData();
        }
        try
        {
            this.bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), localUri);
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

    public String getStringImage(Bitmap paramBitmap)
    {
        if (paramBitmap == null){
            return "";
        }else {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
            return Base64.encodeToString(localByteArrayOutputStream.toByteArray(), 0);
        }
    }
}
