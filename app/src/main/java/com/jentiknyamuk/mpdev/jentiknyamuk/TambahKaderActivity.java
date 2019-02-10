package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahKaderActivity extends AppCompatActivity {
private EditText username, password, nama, alamat, notelp;
private Button btn_add_kader;
private ProgressDialog progressDialog;
private Toolbar toolbar;
String url = "http://mpdev.000webhostapp.com/add_user.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kader);

        username = (EditText) findViewById(R.id.tambah_username);
        password = (EditText) findViewById(R.id.tambah_password);
        nama = (EditText) findViewById(R.id.tambah_nama);
        alamat = (EditText) findViewById(R.id.tambah_alamat);
        notelp = (EditText) findViewById(R.id.tambah_notelp);
        btn_add_kader = (Button) findViewById(R.id.button_tambah_kader);
        progressDialog = new ProgressDialog(this);

        toolbar = (Toolbar) findViewById(R.id.tambahkader_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Kader");

        if(getIntent().hasExtra("nama")){
            btn_add_kader.setText("Update Data");
            username.setText(getIntent().getStringExtra("username"));
            password.setText(getIntent().getStringExtra("password"));
            nama.setText(getIntent().getStringExtra("nama"));
            alamat.setText(getIntent().getStringExtra("alamat"));
            notelp.setText(getIntent().getStringExtra("no_telp"));
            getSupportActionBar().setTitle("Edit Kader");
            username.setEnabled(false);
            url = "https://mpdev.000webhostapp.com/update_user.php";
        }

        btn_add_kader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert_data();
            }
        });
    }

    private void insert_data() {
        final String usrname = username.getText().toString().trim();
        final String pwd = password.getText().toString().trim();
        final String almt = alamat.getText().toString().trim();
        final String noHP = notelp.getText().toString().trim();
        final String name = nama.getText().toString().trim();

        progressDialog.setMessage("Menyimpan Data");
        progressDialog.show();

        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(TambahKaderActivity.this,DataKaderActivity.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", usrname);
                params.put("password", pwd);
                params.put("nama", name);
                params.put("alamat", almt);
                params.put("notelp", noHP);

                return params;
            }
        };
        if(url.contains("update")){
            SharedPrefManager.getInstance(getApplicationContext())
                    .update(name,almt,noHP,pwd);
        }
        UserRequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
